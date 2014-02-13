/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sitemindr.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author moscac
 */
@Entity
public class Person implements Serializable {

    @Id
    private String email;
    private Date updated;
    private PersonAuthority authority;
    public static final String PROP_EMAIL = "email";
    public static final String PROP_UPDATED = "updated";
    public static final String PROP_AUTHORITY = "authority";

    public Person() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setAuthority(PersonAuthority authority) {
        this.authority = authority;
    }

    public PersonAuthority getAuthority() {
        return authority;
    }

}
