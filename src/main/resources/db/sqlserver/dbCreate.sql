-- Create Script for Application Database
-- This will RECREATE the database, be careful
--
-- Default information

-- database: development
-- login:    dev, sa
-- user:     dev
-- password: pass

IF EXISTS 
    (SELECT name  
     FROM master.sys.server_principals
     WHERE name = 'dev')
BEGIN
    DROP LOGIN dev
END
GO

IF NOT EXISTS 
    (SELECT name  
     FROM master.sys.server_principals
     WHERE name = 'dev')
BEGIN
    CREATE LOGIN dev  WITH PASSWORD = N'Pass12345'
END
GO


USE Master
GRANT CREATE ANY DATABASE TO dev
GO


WHILE EXISTS (select NULL from sys.databases where name='development')
BEGIN
    DECLARE @SQL varchar(max)
    SELECT @SQL = COALESCE(@SQL,'') + 'Kill ' + Convert(varchar, SPId) + ';'
    FROM MASTER..SysProcesses
    WHERE DBId = DB_ID(N'development') AND SPId <> @@SPId
    EXEC(@SQL)
    DROP DATABASE development
END
GO

CREATE DATABASE development
GO

USE development
GO

IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'dev')
BEGIN
    CREATE USER dev  FOR LOGIN dev
    -- for Server 2008 and up: 
    EXEC sp_addrolemember  N'db_owner',  N'dev'
END;
GO

-- SQL Server 2012 and Up
ALTER ROLE db_owner  ADD MEMBER dev
GO

USE development
GO

-- routes
IF OBJECT_ID (N'routes', N'U') IS NOT NULL DROP TABLE routes;
CREATE TABLE routes (
  id INT IDENTITY(1,1) NOT NULL,
  url VARCHAR(60)  ,
  access VARCHAR(60)  ,
  priority INT NOT NULL
);
GO


-- Users
IF OBJECT_ID (N'users', N'U') IS NOT NULL DROP TABLE users;
CREATE TABLE users (
  id INT IDENTITY(1,1) NOT NULL,
  username VARCHAR(60),
  password VARCHAR(60),
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (id)
);
GO

-- Roles
IF OBJECT_ID (N'roles', N'U') IS NOT NULL DROP TABLE roles;
CREATE TABLE roles (
  id INT IDENTITY(1,1) NOT NULL,
  role_name VARCHAR(60),
  PRIMARY KEY (id)
);
GO

-- User Roles
IF OBJECT_ID (N'users_roles', N'U') IS NOT NULL DROP TABLE users_roles;
CREATE TABLE users_roles (
  id INT IDENTITY(1,1) NOT NULL,
  user_id int NOT NULL,
  role_id int NOT NULL
);
GO

-- DBConnections
IF OBJECT_ID (N'dbconnections', N'U') IS NOT NULL DROP TABLE dbconnections; 
CREATE TABLE dbconnections (
id INT IDENTITY(1,1) NOT NULL
, connection_name VARCHAR(64)  
, database_jdbc_driver VARCHAR(196)  
, database_jdbc_url VARCHAR(240)  
, database_table_pattern VARCHAR(32)  
, database_passwd VARCHAR(128)  
, database_catalog VARCHAR(32)  
, database_user VARCHAR(32)  
, database_schema_pattern VARCHAR(32)  
, database_type VARCHAR(32)  
, database_protocol VARCHAR(32)  
, database_table_type VARCHAR(32)  
, database_column_pattern VARCHAR(32)  
, database_jpa_vendor_name VARCHAR(32)  
, PRIMARY KEY (id)
, CONSTRAINT dbconnections_ix1 UNIQUE (connection_name)
);
CREATE INDEX dbconnections_name_idx ON dbconnections (connection_name);
GO

-- User Connections
IF OBJECT_ID (N'users_connections', N'U') IS NOT NULL DROP TABLE users_connections; 
CREATE TABLE users_connections (
  id INT IDENTITY(1,1) NOT NULL,
  user_id INT NOT NULL,
  connection_id INT NOT NULL,
  PRIMARY KEY (id)
)
GO

ALTER TABLE users_roles ADD FOREIGN KEY(user_id) REFERENCES users (id);
ALTER TABLE users_roles ADD FOREIGN KEY(role_id) REFERENCES roles (id);

GO
ALTER TABLE users_connections ADD FOREIGN KEY(user_id) REFERENCES users (id);
ALTER TABLE users_connections ADD FOREIGN KEY(connection_id) REFERENCES dbconnections (id);

GO

-- dqstats bounds
IF OBJECT_ID (N'dqs_bounds', N'U') IS NOT NULL DROP TABLE dqs_bounds; 
CREATE TABLE dqs_bounds (
  id INT IDENTITY(1,1) NOT NULL,
  table_name VARCHAR(100) NOT NULL,
  column_name VARCHAR(100) NOT NULL,
  min_val DOUBLE PRECISION NULL,
  max_val DOUBLE PRECISION NULL,
  PRIMARY KEY (id)
);
GO

-- NEW STRUCTURE 6/30/2016

-- jobs
IF OBJECT_ID (N'jobs', N'U') IS NOT NULL DROP TABLE jobs; 
CREATE TABLE jobs (
id INT IDENTITY(1,1) NOT NULL
, name VARCHAR(30)  
, description VARCHAR(255)  
, status INT NOT NULL
, level_id INT NOT NULL
, PRIMARY KEY (id)
);
--ALTER TABLE jobs ADD FOREIGN KEY (status) REFERENCES execution_status (status);
ALTER TABLE jobs  ADD FOREIGN KEY (level_id) REFERENCES roles (id);
GO
CREATE INDEX jobs_name_idx ON jobs (name);
GO

-- users_jobs
IF OBJECT_ID (N'users_jobs', N'U') IS NOT NULL DROP TABLE users_jobs; 
CREATE TABLE users_jobs(
	id INT IDENTITY(1,1) NOT NULL,
	user_id INT NOT NULL,
	job_id INT NOT NULL,
	active BIT NOT NULL,
	PRIMARY KEY (id)
); 
GO

ALTER TABLE users_jobs ADD FOREIGN KEY (job_id) REFERENCES jobs (id);
ALTER TABLE users_jobs ADD FOREIGN KEY (user_id) REFERENCES users (id);
GO

-- job_transfer_queue table
IF OBJECT_ID (N'job_transfer_queue', N'U') IS NOT NULL DROP TABLE job_transfer_queue;
CREATE TABLE job_transfer_queue(
	id INT IDENTITY(1,1) NOT NULL,
	job_id INT NOT NULL,
	user_job_id INT NOT NULL,
	transferer INT NOT NULL,
	transferee INT NOT NULL,
	description VARCHAR(100) NULL,
	active BIT NOT NULL,
	complete_timestamp DATETIME NULL DEFAULT GETDATE(),
	PRIMARY KEY (id)
);
GO

ALTER TABLE job_transfer_queue  ADD FOREIGN KEY (job_id) REFERENCES jobs (id);
ALTER TABLE job_transfer_queue  ADD FOREIGN KEY (user_job_id) REFERENCES users_jobs (id);
ALTER TABLE job_transfer_queue  ADD FOREIGN KEY (transferer) REFERENCES users (id);
ALTER TABLE job_transfer_queue  ADD FOREIGN KEY (transferee) REFERENCES users (id);
GO

-- trigger creation for time stamp (complete_timestamp) in job_transfer_queue table
IF EXISTS (select * from sys.triggers where name = 'trgAfterUpdateJobTransferQueue')
	BEGIN
		DROP TRIGGER trgAfterUpdateJobTransferQueue
	END
GO
CREATE TRIGGER trgAfterUpdateJobTransferQueue
ON job_transfer_queue
AFTER UPDATE 
AS BEGIN
   UPDATE job_transfer_queue
   SET complete_timestamp = GETDATE()
   FROM INSERTED i
   WHERE i.ID = job_transfer_queue.id
END
GO

-- tasks
IF OBJECT_ID (N'tasks', N'U') IS NOT NULL DROP TABLE tasks;
CREATE TABLE tasks (
id INT IDENTITY(1,1) NOT NULL
, name VARCHAR(32)  
, description VARCHAR(255)  
, body VARCHAR(8000)  
, type_id INT NOT NULL
, source_db_conn_id INT NOT NULL
, target_db_conn_id INT NOT NULL
, status INT NOT NULL
, PRIMARY KEY (id)
);
ALTER TABLE tasks ADD FOREIGN KEY (source_db_conn_id) REFERENCES dbconnections (id);
ALTER TABLE tasks ADD FOREIGN KEY (target_db_conn_id) REFERENCES dbconnections (id);
GO

CREATE INDEX tasks_name_idx ON tasks (name);
GO

-- job2job_link
IF OBJECT_ID (N'job2job_link', N'U') IS NOT NULL DROP TABLE job2job_link;
CREATE TABLE job2job_link (
id INT IDENTITY(1,1) NOT NULL
, job_id INT NOT NULL
, related_job_id INT NOT NULL
, related_job_priority INT NOT NULL
, PRIMARY KEY (id)
);
ALTER TABLE job2job_link ADD FOREIGN KEY (related_job_id) REFERENCES jobs (id);
CREATE INDEX jj_link_job_id ON job2job_link (job_id);
CREATE INDEX jj_link_related_job_id ON job2job_link (related_job_id);
GO

-- task2job_link
IF OBJECT_ID (N'task2job_link', N'U') IS NOT NULL DROP TABLE task2job_link;
CREATE TABLE task2job_link (
id INT IDENTITY(1,1) NOT NULL
, job_id INT NOT NULL
, related_task_id INT NOT NULL
, related_task_priority INT NOT NULL
, task_start_condition_id INT NOT NULL
, PRIMARY KEY (id)
);
ALTER TABLE task2job_link ADD FOREIGN KEY (related_task_id) REFERENCES tasks (id);
CREATE INDEX jt_link_job_id ON task2job_link (job_id);
CREATE INDEX jt_link_related_task_id ON task2job_link (related_task_id);
GO


-- task2task_link
-- removed 08/08/2016

-- businessrules
IF OBJECT_ID (N'businessrules', N'U') IS NOT NULL DROP TABLE businessrules;  
CREATE TABLE businessrules (
id INT IDENTITY(1,1) NOT NULL
, name VARCHAR(64) NOT NULL
, description VARCHAR(8000)  
, body VARCHAR(8000)  
, restconnection VARCHAR(1000)
, config_id int
, PRIMARY KEY (id)
);
CREATE INDEX businessrules_name_idx ON businessrules (name);
GO

-- brule2task_link
IF OBJECT_ID (N'brule2task_link', N'U') IS NOT NULL DROP TABLE brule2task_link;
CREATE TABLE brule2task_link (
id INT IDENTITY(1,1) NOT NULL
, task_id INT NOT NULL
, related_brule_id INT NOT NULL
, related_brule_priority INT NOT NULL
, PRIMARY KEY (id)
);

ALTER TABLE brule2task_link ADD FOREIGN KEY (task_id) REFERENCES tasks (id);
ALTER TABLE brule2task_link ADD FOREIGN KEY (related_brule_id) REFERENCES businessrules (id);
GO

-- Roles
IF OBJECT_ID (N'scripts', N'U') IS NOT NULL DROP TABLE scripts;
CREATE TABLE scripts (
  id INT IDENTITY(1,1) NOT NULL,
  name VARCHAR(60),
  PRIMARY KEY (id)
);
GO

-- User Roles
IF OBJECT_ID (N'users_scripts', N'U') IS NOT NULL DROP TABLE users_scripts;
CREATE TABLE users_scripts (
  id INT IDENTITY(1,1) NOT NULL,
  user_id int NOT NULL,
  script_id int NOT NULL
);
GO

-- Foreign Keys
ALTER TABLE users_scripts ADD FOREIGN KEY(user_id) REFERENCES users (id);
ALTER TABLE users_scripts ADD FOREIGN KEY(script_id) REFERENCES scripts (id);
GO

-- Script Log
IF OBJECT_ID (N'scripts_logs', N'U') IS NOT NULL DROP TABLE scripts_logs;
CREATE TABLE scripts_logs (
  id INT IDENTITY(1,1) NOT NULL,
  script_name VARCHAR(60),
  output VARCHAR(60),
  started_at datetime,
  completed_at datetime,
  started_by VARCHAR(60)
);
GO

-- DB Tables
IF OBJECT_ID (N'dbtables', N'U') IS NOT NULL DROP TABLE dbtables;  
CREATE TABLE dbtables (
id INT IDENTITY(1,1) NOT NULL
, name VARCHAR(64) NOT NULL
, comments VARCHAR(8000) 
, dbconnection_id INT NOT NULL
, PRIMARY KEY (id)
);
ALTER TABLE dbtables ADD FOREIGN KEY (dbconnection_id) REFERENCES dbconnections (id);
CREATE INDEX dbtables_name_idx ON dbtables (name);
GO

-- DB Columns
IF OBJECT_ID (N'dbcolumns', N'U') IS NOT NULL DROP TABLE dbcolumns;  
CREATE TABLE dbcolumns (
id INT IDENTITY(1,1) NOT NULL
, name VARCHAR(64) NOT NULL
, comments VARCHAR(8000)  
, dbtable_id INT NOT NULL
, PRIMARY KEY (id)
);
GO

-- brule2tabs_columns_link
IF OBJECT_ID (N'brule2tabs_columns_link', N'U') IS NOT NULL DROP TABLE brule2tabs_columns_link;
CREATE TABLE brule2tabs_columns_link (
id INT IDENTITY(1,1) NOT NULL
, brule_id INT NOT NULL
, source_table_id INT NOT NULL
, source_table_column_id INT NOT NULL
, target_table_id INT
, target_table_column_id INT
, PRIMARY KEY (id)
--, CONSTRAINT brule2tabs_col_ix1 UNIQUE (brule_id)
);

ALTER TABLE brule2tabs_columns_link ADD FOREIGN KEY (source_table_id) REFERENCES dbtables (id);
--ALTER TABLE brule2tabs_columns_link ADD FOREIGN KEY (target_table_id) REFERENCES dbtables (id);
ALTER TABLE brule2tabs_columns_link ADD FOREIGN KEY (source_table_column_id) REFERENCES dbcolumns (id);
--ALTER TABLE brule2tabs_columns_link ADD FOREIGN KEY (target_table_column_id) REFERENCES dbcolumns (id);
GO


-- execution_output table
IF OBJECT_ID (N'execution_output', N'U') IS NOT NULL DROP TABLE execution_output;  
CREATE TABLE execution_output (
id INT IDENTITY(1,1) NOT NULL
, job_id INT
, task_id INT
, businessrule_id INT
, text_output VARCHAR(MAX) 
, list_output VARCHAR(MAX)
, error VARCHAR(MAX)
, output_status VARCHAR(32)
, comments VARCHAR(256)
, complete_timestamp DATETIME2 NULL DEFAULT GETDATE()
, PRIMARY KEY (id)
);
ALTER TABLE execution_output ADD FOREIGN KEY (job_id) REFERENCES jobs (id);
ALTER TABLE execution_output ADD FOREIGN KEY (task_id) REFERENCES tasks (id);
ALTER TABLE execution_output ADD FOREIGN KEY (businessrule_id) REFERENCES businessrules (id);
GO

-- parameters_map table
IF OBJECT_ID (N'parameters_map', N'U') IS NOT NULL DROP TABLE parameters_map;  
CREATE TABLE parameters_map (
id INT IDENTITY(1,1) NOT NULL
, map_id INT NOT NULL
, map_key VARCHAR(32) 
, map_value VARCHAR(MAX)
, comments VARCHAR(256)
, PRIMARY KEY (id)
);
GO

-- dq_config table
IF OBJECT_ID (N'dq_config', N'U') IS NOT NULL DROP TABLE dq_config;  
CREATE TABLE dq_config (
id INT IDENTITY(1,1) NOT NULL
, parameters_map_id INT
, dq_rule VARCHAR(32)
, comments VARCHAR(256)
, PRIMARY KEY (id)
);
--ALTER TABLE dq_config ADD FOREIGN KEY (paramaters_map_id) REFERENCES parameters_map (id);
GO
