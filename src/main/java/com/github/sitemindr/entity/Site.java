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
    private Date lastAvailable;
    private Date lastUnavailable;

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

}
