package org.dbadmin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by henrynguyen on 7/6/16.
 */

@Entity
@Table(name = "scripts_logs")
public class ScriptLog extends BaseEntity{
    @Column(name = "script_name")
    private String script_name;

    @Column(name = "output")
    private String output;

    public String getScript_name() {
        return script_name;
    }

    public void setScript_name(String script_name) {
        this.script_name = script_name;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Timestamp getStarted_at() {
        return started_at;
    }

    public void setStarted_at(Timestamp started_at) {
        this.started_at = started_at;
    }

    public Timestamp getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(Timestamp completed_at) {
        this.completed_at = completed_at;
    }

    public String getStarted_by() {
        return started_by;
    }

    public void setStarted_by(String started_by) {
        this.started_by = started_by;
    }

    @Column(name = "started_at")

    private Timestamp started_at;

    @Column(name = "completed_at")
    private Timestamp completed_at;

    @Column(name = "started_by")
    private String started_by;


}
