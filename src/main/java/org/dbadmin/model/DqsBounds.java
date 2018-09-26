package org.dbadmin.model;

/**
 * Created by Jake Lee
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Domain object representing an DqsBounds.
 *
 */
@Entity
@Table(name = "dqs_bounds")
public class DqsBounds extends BaseEntity {

	@Column(name = "table_name")
    @NotEmpty
    private String tableName;

	@Column(name = "column_name")
    @NotEmpty
    private String columnName;

	@Column(name = "min_val")
    private Double minVal;

	@Column(name = "max_val")
    private Double maxVal;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Double getMinVal() {
		return minVal;
	}

	public void setMinVal(Double minVal) {
		this.minVal = minVal;
	}

	public Double getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(Double maxVal) {
		this.maxVal = maxVal;
	}
}
