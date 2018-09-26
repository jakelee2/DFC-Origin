package org.dbadmin.model;

/**
 * Created by henrynguyen on 6/9/16.
 */
public class RowData {

    public RowData(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String value;


}
