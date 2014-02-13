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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author moscac
 */
@Entity
public class Site implements Serializable {
    
    @Id
    private String fqdn;
    private boolean available;
    private Date lastAvaialable;
    private Date lastUnavailable;
    private List<Person> interestedParties;
    public static final String PROP_FQDN = "fqdn";
    public static final String PROP_AVAILABLE = "available";
    public static final String PROP_LAST_AVAILABLE = "lastAvailable";
    public static final String PROP_LAST_UNAVAILABLE = "lastUnavailable";

    public Site() {
        interestedParties = new ArrayList<>(0);
    }
    
    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getLastAvaialable() {
        return lastAvaialable;
    }

    public void setLastAvaialable(Date lastAvaialable) {
        this.lastAvaialable = lastAvaialable;
    }

    public Date getLastUnavailable() {
        return lastUnavailable;
    }

    public void setLastUnavailable(Date lastUnavailable) {
        this.lastUnavailable = lastUnavailable;
    }

    public List<Person> getInterestedParties() {
        return interestedParties;
    }

    public void setInterestedParties(List<Person> interestedParties) {
        this.interestedParties = interestedParties;
    }
  
}
