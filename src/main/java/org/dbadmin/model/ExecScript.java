package org.dbadmin.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by henrynguyen on 6/22/16.
 */

@Entity
@Table(name = "scripts")
public class ExecScript extends BaseEntity {

    public ExecScript(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.state = ScriptState.STOPPED;
    }

    public ExecScript(String name) {
        this.name = name;
        this.state = ScriptState.STOPPED;
    }

    public ExecScript(String name, ScriptState state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScriptState getState() {
        return state;
    }

    public void setState(ScriptState state) {
        this.state = state;
    }

    public ScriptAction getAvailAction() {
        if (state == ScriptState.RUNNING) {
            return ScriptAction.TERMINATE;
        } else {
            return ScriptAction.START_AND_WAIT;
        }
    }

    public ScriptAction getUnblockAction() {
        if (state == ScriptState.RUNNING) {
            return ScriptAction.RESTART;
        } else {
            return ScriptAction.START;
        }
    }

    @Column(name = "name")
    @NotEmpty
    public String name;
    public ScriptState state;
}
