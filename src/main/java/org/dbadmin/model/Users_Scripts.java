package org.dbadmin.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by henrynguyen on 6/28/16.
 */
@Entity
@Table(name = "users_scripts")
public class Users_Scripts  extends BaseEntity{

    @Column(name = "user_id")
    @NotEmpty
    private Integer user_id;

    public Integer getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Column(name = "script_id")
    private Integer script_id;

    public Integer getScript_id() {
        return this.script_id;
    }

    public void setScript_id(Integer script_id) {
        this.script_id = script_id;
    }

    /*
    Construction of relation between users and scripts
     */
    public Users_Scripts(Integer script_id, Integer user_id) {
        this.script_id = script_id;
        this.user_id = user_id;
    }
}
