-- Insert sample data into database for testing purposes.
--
-- Default information
-- database: development
-- login:    dev
-- user:     dev
-- password: pass

USE development
GO

-- Users
INSERT INTO users(username,password,enabled) VALUES ('henry','$2a$10$yXjXWVa9mPmQYIJVoA8Gqex9.8ChGQfdlTU4UK02DKyH0KGblTfSa', 1);
INSERT INTO users(username,password,enabled) VALUES ('fred','$2a$10$yXjXWVa9mPmQYIJVoA8Gqex9.8ChGQfdlTU4UK02DKyH0KGblTfSa', 1);
INSERT INTO users(username,password,enabled) VALUES ('denys','$2a$10$yXjXWVa9mPmQYIJVoA8Gqex9.8ChGQfdlTU4UK02DKyH0KGblTfSa', 1);
INSERT INTO users(username,password,enabled) VALUES ('jake','$2a$10$yXjXWVa9mPmQYIJVoA8Gqex9.8ChGQfdlTU4UK02DKyH0KGblTfSa', 1);
INSERT INTO users(username,password,enabled) VALUES ('user','$2a$10$yXjXWVa9mPmQYIJVoA8Gqex9.8ChGQfdlTU4UK02DKyH0KGblTfSa', 1);
INSERT INTO users(username,password,enabled) VALUES ('admin','$2a$10$yXjXWVa9mPmQYIJVoA8Gqex9.8ChGQfdlTU4UK02DKyH0KGblTfSa', 1);
INSERT INTO users(username,password,enabled) VALUES ('9Yangm','$2a$10$yXjXWVa9mPmQYIJVoA8Gqex9.8ChGQfdlTU4UK02DKyH0KGblTfSa', 1);
GO

-- Roles
INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_USER');
INSERT INTO roles (role_name) VALUES ('ROLE_NA');
INSERT INTO roles (role_name) VALUES ('ROLE_SCRYLDAP');
GO

-- User Roles
INSERT INTO users_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (4, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (5, 3);
INSERT INTO users_roles (user_id, role_id) VALUES (6, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (6, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (7, 4);
GO

-- DBConnections
INSERT INTO dbconnections (connection_name, database_jdbc_driver, database_jdbc_url, database_passwd, database_user, database_jpa_vendor_name)
VALUES ('VBox SQLServer 2012 Dev', 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://192.168.56.101:1433;databaseName=development', 'pass', 'dev', 'SQL_SERVER');
INSERT INTO dbconnections (connection_name, database_jdbc_driver, database_jdbc_url, database_passwd, database_user, database_jpa_vendor_name)
VALUES ('VBox SQLServer 2012 Master', 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://192.168.56.101:1433;databaseName=master', 'pass', 'dev', 'SQL_SERVER');
INSERT INTO dbconnections (connection_name, database_jdbc_driver, database_jdbc_url, database_passwd, database_user, database_jpa_vendor_name)
VALUES ('Localhost SQLServer 2012 Dev', 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://127.0.0.1:1433;databaseName=development', 'Pass12345', 'dev', 'SQL_SERVER');
INSERT INTO dbconnections (connection_name, database_jdbc_driver, database_jdbc_url, database_passwd, database_user, database_jpa_vendor_name)
VALUES ('Localhost SQLServer 2012 Master', 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://127.0.0.1:1433;databaseName=master', 'Pass12345', 'dev', 'SQL_SERVER');
GO

-- User Connection
INSERT INTO users_connections (user_id, connection_id) VALUES (2, 1);
INSERT INTO users_connections (user_id, connection_id) VALUES (2, 2);
INSERT INTO users_connections (user_id, connection_id) VALUES (3, 1);
INSERT INTO users_connections (user_id, connection_id) VALUES (3, 2);
INSERT INTO users_connections (user_id, connection_id) VALUES (4, 3);
GO

-- routes
INSERT INTO routes(url,access,priority) VALUES ('/login**', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/logout**', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/signup**', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/registration', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/Access_Denied', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/favicon.ico', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/js/**', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/resources/**', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/vendors/**', 'permitAll', 1);
INSERT INTO routes(url,access,priority) VALUES ('/admin/**', 'ROLE_ADMIN', 1);
INSERT INTO routes(url,access,priority) VALUES ('/jobs', 'ROLE_USER', 1);
INSERT INTO routes(url,access,priority) VALUES ('/**', 'ROLE_USER', 1000);
GO

-- Jobs
INSERT INTO jobs (name, description, status, level_id) VALUES ('New_Job#001', 'New_Job#001 description', 1, 1);
INSERT INTO jobs (name, description, status, level_id) VALUES ('JOB002', 'Job#002 description here', 5, 2);
INSERT INTO jobs (name, description, status, level_id) VALUES ('New_Job#003', 'New_Job#003 and description', 10, 1);
INSERT INTO jobs (name, description, status, level_id) VALUES ('New_Job#004', 'description for New_Job#004 here', 20, 2);
INSERT INTO jobs (name, description, status, level_id) VALUES ('New_Job#005', 'New_Job#005 description', 100, 2);
INSERT INTO jobs (name, description, status, level_id) VALUES (null, 'New_Job#005 description', 100, 1);
INSERT INTO jobs (name, description, status, level_id) VALUES ('New_Job#007', 'New_Job#005 description', 100, 2);
INSERT INTO jobs (name, description, status, level_id) VALUES ('Job #1 for DQ checks', 'see associated tasks and businessrules', 1, 2);
GO

-- Users_jobs
INSERT INTO users_jobs (user_id, job_id, active) VALUES (2,1,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (4,2,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (4,3,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (3,3,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (3,1,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (3,4,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (3,8,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (2,8,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (1,8,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (4,8,1);
INSERT INTO users_jobs (user_id, job_id, active) VALUES (6,4,1);
GO

-- Business Rules
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('Total Outstanding Loans', 'Count of outstanding single payment loans.', 'body info', 'stats/collections/outstanding', 0)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('Total Delinquent Standings', 'Dollar amount of outstanding single payment loans.', 'here is body info', 'stats/collections/standings', 0)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('Total Accounts in Delinquency', 'Count of delinquent accounts for single payment loans.', 'body info', 'stats/collections/accounts', 0)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('Average Delinquent Amount', 'Average dollar amount in outstanding accounts.', 'body info', 'stats/collections/avgamount', 0)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('Total Delinquent Payments', 'Total dollar amount of delinquent payments.', 'body here', 'stats/collections/totalpayment', 0)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('Count of Checks in Collections', 'Count of checks returned from banking.', 'body info', 'stats/collections/checks', 0)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ NULL CHECK', 'DataQualitifiers Null tests descrption', 'optional body', 'optional rest connection', 1)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ LENGHT CHECK', 'DataQualitifiers Length tests descrption', 'optional body', 'optional rest connection', 2)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ OUTLIERS CHECK', 'DataQualitifiers outliers tests descrption', 'optional body', 'optional rest connection', 3)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ DATE CHECK', 'DataQualitifiers date tests descrption', 'optional body', 'optional rest connection', 4)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ SET CHECK', 'DataQualitifiers SET tests descrption', 'optional body', 'optional rest connection', 5)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ PHONE CHECK', 'DataQualitifiers PHONE tests descrption', 'optional body', 'optional rest connection', 6)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ DUPLICATE CHECK', 'DataQualitifiers DUPLICATE tests descrption', 'optional body', 'optional rest connection', 7)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ ADDRESS CHECK', 'DataQualitifiers ADDRESS tests descrption', 'optional body', 'optional rest connection', 8)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ CANDIDATE KEY', 'Candidate keys suggestions', 'optional body', 'optional rest connection', 9)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ ORPHAN DETECT', 'ORPHAN DETECT', 'optional body', 'optional rest connection', 10)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ DATA TYPE DETECT', 'Data type detect', 'optional body', 'optional rest connection', 11)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ CLUSTER DETECTION', 'Cluster detection', 'optional body', 'optional rest connection', 12)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ FITTING DISTRIBUTION', 'Distribution', 'optional body', 'optional rest connection', 13)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ INVALID PK CHECK', 'Invalid PK check', 'optional body', 'optional rest connection', 14)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ ABNORMAL DUPLICATES', 'Abn dups', 'optional body', 'optional rest connection', 15)
INSERT INTO businessrules (name, description, body, restconnection, config_id) VALUES ('DQ DATE PARSABILITY', 'Date parsability check', 'optional body', 'optional rest connection', 16)
GO

-- Tasks
INSERT INTO tasks (name, description, body, type_id, source_db_conn_id, target_db_conn_id, status) VALUES ('GetCurrentDateSQL', 'This ETL Task retrieves current server Date', 'SELECT CAST(GETDATE() AS DATE)', 1, 2, 1, 10);
INSERT INTO tasks (name, description, body, type_id, source_db_conn_id, target_db_conn_id, status) VALUES ('GetCurrentTimeSQL', 'This ETL Task retrieves current server Time', 'call {SELECT CAST(GETTIME() AS DATETIME)}', 3, 1, 3, 5);
INSERT INTO tasks (name, description, body, type_id, source_db_conn_id, target_db_conn_id, status) VALUES ('GetSomethingElse', 'This ETL Task is an example of Oracle DB dialect', 'select 1 from dual', 2, 2, 2, 100);
INSERT INTO tasks (name, description, body, type_id, source_db_conn_id, target_db_conn_id, status) VALUES ('DQ Task #4', 'DQ Test - see included Businessrules', 'optional', 10, 3, 1, 1);
INSERT INTO tasks (name, description, body, type_id, source_db_conn_id, target_db_conn_id, status) VALUES ('DQ Task #5', 'DQ Test - see included Businessrules', 'optional', 10, 3, 1, 1);
GO

-- Job to Job link
INSERT INTO job2job_link (job_id, related_job_id, related_job_priority) VALUES (5,1,37);
INSERT INTO job2job_link (job_id, related_job_id, related_job_priority) VALUES (1,2,17);
INSERT INTO job2job_link (job_id, related_job_id, related_job_priority) VALUES (5,3,25);
INSERT INTO job2job_link (job_id, related_job_id, related_job_priority) VALUES (4,2,30);
INSERT INTO job2job_link (job_id, related_job_id, related_job_priority) VALUES (2,5,31);
GO

-- Task to Task link
-- removed

-- Task to Job link
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (1,1,21,100);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (2,2,1,20);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (3,1,31,100);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (4,3,44,100);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (5,1,19,100);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (6,2,1,100);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (7,1,51,20);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (8,4,5,100);
INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (8,5,10,100);
GO

-- Businessrule to Task link
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (1,3,14);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (2,4,33);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (2,5,13);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,7,100);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (5,8,5);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (5,9,3);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,10,70);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,11,40);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (5,12,33);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (5,13,35);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (5,14,37);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,15,30);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,16,4);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,17,20);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,18,10);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,19,5);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,20,3);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,21,2);
INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (4,22,1);
GO

-- DB Tables
INSERT INTO dbtables (name, comments, dbconnection_id) VALUES ('jobs', 'optional comment', 1);
INSERT INTO dbtables (name, comments, dbconnection_id) VALUES ('users', 'optional comment', 2);
INSERT INTO dbtables (name, comments, dbconnection_id) VALUES ('execution_output', 'optional comment', 3);

-- DB Columns
INSERT INTO dbcolumns (name, comments, dbtable_id) VALUES ('name', 'optional comment', 1);
INSERT INTO dbcolumns (name, comments, dbtable_id) VALUES ('username', 'optional comment', 2);
INSERT INTO dbcolumns (name, comments, dbtable_id) VALUES ('complete_timestamp', 'optional comment', 3);
INSERT INTO dbcolumns (name, comments, dbtable_id) VALUES ('id', 'universal PK column name', 0);
INSERT INTO dbcolumns (name, comments, dbtable_id) VALUES ('status', 'optional comment', 1);

-- Businessrule to DB Tables and Columns link
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (1,1,1,2,2);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (3,1,1,2,2);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (7,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (8,2,2,2,2);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (9,1,4,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (10,3,3,2,2);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (11,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (12,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (13,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (14,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (15,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (16,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (17,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (18,1,5,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (19,1,5,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (20,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (21,1,1,1,1);
INSERT INTO brule2tabs_columns_link (brule_id, source_table_id, source_table_column_id, target_table_id, target_table_column_id) VALUES (22,3,3,1,1);
GO

--
ALTER TABLE roles ADD role_prefix VARCHAR(60), role_level INT
GO
UPDATE roles SET role_prefix = 'B' , role_level = 10 
GO
UPDATE roles SET role_prefix = '.*', role_level = 100 WHERE role_name = 'ROLE_ADMIN'
GO

-- dq_config
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'NULL_CHECK', 'Test for null check');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (2, 'LENGTH_CHECK', 'Test for lenth check');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (3, 'OUTLIER', 'Test for outliers');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (4, 'DATE_CHECK', 'Test for date');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (5, 'SET_CHECK', 'Set test');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'PHONE_CHECK', 'Test for phone number');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'DUP_CHECK', 'Duplicates');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'ADDRESS_CHECK', 'Address validation');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'CANDIDATE_KEY_CHECK', 'Candidate key');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (6, 'ORPHAN_DETECT', 'ORPHAN detection');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'DATA_TYPE_DETECT', 'Data type check');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'CLUSTER_DETECT', 'Cluster detection');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'FITTING_DISTRIBUTION', 'Distribution of the data');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (7, 'INVALID_PK_DETECT', 'PK detect against other table');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'ABNORMAL_DUP_CHECK', 'Abnormal duplicates check');
INSERT INTO dq_config (parameters_map_id, dq_rule, comments) VALUES (1, 'DATE_PARSABILITY', 'Date parsability test');

GO

-- parameters_map
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (1, null, null, 'no additional params is required');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (2, 'op', '=', 'param 1 for  LENGTH_CHECK');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (2, 'length', '4', 'param 2 for  LENGTH_CHECK');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (3, null, null, 'no additional params for OUTLIER is required');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (4, 'op', '>', 'param 1 for DATE_CHECK');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (4, 'date', '2016-09-01', 'param 2 for DATE_CHECK');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (5, 'op', 'IN', 'Set check param 1');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (5, 'set', 'JOB002|NEW', 'Set check param 2,3');

INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (6, 'op', 'IN', 'Orphan detection');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (6, 'referTable', 'jobs', 'Orphan detection on referTable = jobs');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (7, 'op', 'IN', 'PK check');
INSERT INTO parameters_map (map_id, map_key, map_value, comments) VALUES (7, 'referTable', 'jobs', 'PK check on referTable = jobs');


GO


