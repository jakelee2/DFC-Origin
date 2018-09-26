package org.dbadmin.model;

import org.dbadmin.util.DataQualitifiers;

/**
 * Created by henrynguyen on 8/31/16.
 * 
 * see also @link org.dbadmin.repository.jdbc.JdbcDqConfigRepositoryImpl.getDQRule(String)
 */
public class DQExecutor {
    public static Report run(DQConfig config) throws Exception {
        if (config.rule == DQRule.NULL_CHECK) {
            return runNullCheck(config);
        } else if (config.rule == DQRule.LENGTH_CHECK) {
            return runLengthCheck(config);
        } else if (config.rule == DQRule.SET_CHECK) {
            return runSetCheck(config);
        } else if (config.rule == DQRule.DATE_CHECK) {
            return runDateCheck(config);
        } else if (config.rule == DQRule.OUTLIER) {
            return runOutlierCheck(config);
        } else if (config.rule == DQRule.ORPHAN_DETECT) {
        	return runOrphanDetect(config);
        } else if (config.rule == DQRule.INVALID_PK_DETECT) {
        	return runInvalidPKDetect(config);
        } else if (config.rule == DQRule.DATA_TYPE_DETECT) {
        	return runDataTypeDetect(config);
        } else if (config.rule == DQRule.PHONE_CHECK) {
            return runTelephoneCheck(config);
        } else if (config.rule == DQRule.DUP_CHECK) {
            return runDuplicatedCheck(config); 
        } else if (config.rule == DQRule.ADDRESS_CHECK) {
            return runAddressCheck(config);  
        } else if (config.rule == DQRule.CANDIDATE_KEY_CHECK) {
            return runCandidateKeyCheck(config);              
        } else if (config.rule == DQRule.ORPHAN_DETECT) {
        	return runOrphanDetect(config);
        } else if (config.rule == DQRule.INVALID_PK_DETECT) {
        	return runInvalidPKDetect(config);
        } else if (config.rule == DQRule.DATA_TYPE_DETECT) {
        	return runDataTypeDetect(config);
        } else if (config.rule == DQRule.CLUSTER_DETECT) {
        	return runClusterDetect(config);   
        } else if (config.rule == DQRule.FITTING_DISTRIBUTION) {
        	return runFittingDistributionDetect(config);  
        } else if (config.rule == DQRule.ABNORMAL_DUP_CHECK) {
        	return runDuplicatedAbnormalVolumeDetect(config);   
        } else if (config.rule == DQRule.DATE_PARSABILITY) {
        	return runDateParsableReportDetect(config);         	
        } else if (config.rule == DQRule.NUMERIC_CONVERTION_DETECT) {
        	return runNullCheck(config);         	
        }
        
        throw new IllegalArgumentException("DQExecutor: invalid DQRule: "+config.toString());
    }

	public static Report runNullCheck(DQConfig config) {
        return DataQualitifiers.getNullReport(config.connection, config.tableName, config.columnName);
    }

    public static Report runOutlierCheck(DQConfig config) {
        return DataQualitifiers.getOutliersReport(config.connection, config.tableName, config.columnName);
    }

    public static Report runLengthCheck(DQConfig config) {
        return DataQualitifiers.getLengthCheckReport(config.connection, config.tableName, config.columnName, config.getDQOp(), config.getLength());
    }

    public static Report runSetCheck(DQConfig config) {
        return DataQualitifiers.getSetCheckReport(config.connection, config.tableName,
            config.columnName, config.getDQOp(), config.getSet());
    }

    public static Report runDateCheck(DQConfig config) {
        return DataQualitifiers.getDateCheckReport(config.connection, config.tableName,
            config.columnName, config.getDQOp(), config.getDate());
    }
    
    public static Report runTelephoneCheck(DQConfig config) {
        return DataQualitifiers.getTelephoneCheckReport(config.connection, config.tableName, config.columnName);
    }
    
    public static Report runDuplicatedCheck(DQConfig config) {
        return DataQualitifiers.getDuplicatedReport(config.connection, config.tableName, config.columnName);
    }
    
    public static Report runAddressCheck(DQConfig config) {
        return DataQualitifiers.getAddressCheckReport(config.connection, config.tableName, config.columnName);
    }
    
    public static Report runCandidateKeyCheck(DQConfig config) {
        return DataQualitifiers.getCandidateKeyReport(config.connection, config.tableName, config.columnName);
    }

    public static Report runOrphanDetect(DQConfig config) {
        return DataQualitifiers.getOrphanDetectReport(config.connection, config.tableName,
            config.columnName, config.getReferTable(), config.getDQOp());
    }
    
    private static Report runInvalidPKDetect(DQConfig config) {
    	return DataQualitifiers.getInvalidPKDetectReport(config.connection, config.tableName,
                config.columnName, config.getReferTable(), config.getDQOp());
	}
    
    private static Report runDataTypeDetect(DQConfig config) {
    	return DataQualitifiers.getDataTypeDetectReport(config.connection, config.tableName);
	}
    
    private static Report runClusterDetect(DQConfig config) {
		return DataQualitifiers.getClusterDetectionReport(config.connection, config.tableName,
                config.columnName);
	}
    
    private static Report runFittingDistributionDetect(DQConfig config) {
		return DataQualitifiers.getFittingDistributionReport(config.connection, config.tableName,
                config.columnName);
    }
    
    private static Report runDuplicatedAbnormalVolumeDetect(DQConfig config) {
		return DataQualitifiers.getDuplicatedAbnormalVolumeReport (config.connection, config.tableName,
		        config.columnName);	
	}
    
    private static Report runDateParsableReportDetect(DQConfig config) {
		return DataQualitifiers.getDateParsableReport(config.connection, config.tableName,
		        config.columnName);	
	}
    
}
