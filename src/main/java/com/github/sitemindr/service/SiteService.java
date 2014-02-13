/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sitemindr.service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.github.sitemindr.entity.Site;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.QueryResultList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author moscac
 */
public class SiteService {

    private static final String KIND = "Site";

    public boolean save(Site siteEntity) {
        Entity site = new Entity(KIND, siteEntity.getFqdn());
        //site.setProperty(SiteEntity.PROP_FQDN, siteEntity.getFqdn());
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = datastore.put(site);
        boolean result = key != null;
        return result;
    }

    public static List<String> getSites() {
        List sites = new ArrayList<>(0);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(KIND);
        PreparedQuery pq = datastore.prepare(q);
        FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
        QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        for (Entity entity : results) {
            sites.add(entity.getKey().getName());
        }
        return sites;
    }

    public static void updateSite(String url, Boolean available) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter filter = new Query.FilterPredicate(Site.PROP_FQDN, Query.FilterOperator.EQUAL, url);
        Query q = new Query(KIND).setFilter(filter);
        PreparedQuery pq = datastore.prepare(q);
        FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
        QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        Entity site;
        Boolean wasAvailable = false;
        if (results.size() > 0) {
            site = results.get(0);
            wasAvailable = (Boolean) site.getProperty(Site.PROP_AVAILABLE);
        } else {
            site = new Entity(KIND, url);
        }
        site.setProperty(Site.PROP_AVAILABLE, available);
        if (available) {
            site.setProperty(Site.PROP_LAST_AVAILABLE, new Date());
        } else {
            if (wasAvailable) {

            }
            site.setProperty(Site.PROP_LAST_UNAVAILABLE, new Date());
        }
        site.setProperty(Site.PROP_AVAILABLE, available);
        datastore.put(site);
    }

}
