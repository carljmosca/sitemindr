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
public class Site implements Serializable {
    
    @Id
    private String fqdn;
    private boolean available;
    private Date lastAvailable;
    private Date lastUnavailable;
    private String fromEmail;
    private int waitTimeout;

    public Site() {
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

    public Date getLastAvailable() {
        return lastAvailable;
    }

    public void setLastAvailable(Date lastAvailable) {
        this.lastAvailable = lastAvailable;
    }

    public Date getLastUnavailable() {
        return lastUnavailable;
    }

    public void setLastUnavailable(Date lastUnavailable) {
        this.lastUnavailable = lastUnavailable;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public int getWaitTimeout() {
        return waitTimeout;
    }

    public void setWaitTimeout(int waitTimeout) {
        this.waitTimeout = waitTimeout;
    }

}
