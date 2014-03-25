package com.github.sitemindr.service;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;

import com.github.sitemindr.entity.Site;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.SaveException;
import java.util.ArrayList;

@Api(name = "siteendpoint", namespace = @ApiNamespace(ownerDomain = "github.com",
        ownerName = "github.com", packagePath = "sitemindr.service"))
public class SiteEndpoint {

    /**
     * This method lists all the entities inserted in datastore. It uses HTTP
     * GET method and paging support.
     *
     * @return A CollectionResponse class containing the list of all entities
     * persisted and a cursor to the next page.
     */
    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listSite")
    public CollectionResponse<Site> listSite(
            @Nullable @Named("cursor") String cursorString,
            @Nullable @Named("limit") Integer limit) {

        Cursor cursor = null;
        List<Site> execute = new ArrayList<>(0);
        Iterable<Site> sites = ofy().load().type(Site.class);
        for (Site site : sites) {
            execute.add(site);
        }
        Site site = new Site();
        site.setFqdn("test.site");
        site.setAvailable(true);
        ofy().save().entity(site).now();

        return CollectionResponse.<Site>builder().setItems(execute)
                .setNextPageToken(cursorString).build();
    }

    /**
     * This method gets the entity having primary key id. It uses HTTP GET
     * method.
     *
     * @param id the primary key of the java bean.
     * @return The entity with primary key id.
     */
    @ApiMethod(name = "getSite")
    public Site getSite(@Named("id") String id) {
        Site site = null;
        try {
        } finally {
        }
        return site;
    }

    /**
     * This inserts a new entity into App Engine datastore. If the entity
     * already exists in the datastore, an exception is thrown. It uses HTTP
     * POST method.
     *
     * @param site the entity to be inserted.
     * @return The inserted entity.
     */
    @ApiMethod(name = "insertSite")
    public Site insertSite(Site site) {
        if (containsSite(site)) {
            throw new SaveException(site, site.getFqdn() + " already exists", null);
        }

        return site;
    }

    /**
     * This method is used for updating an existing entity. If the entity does
     * not exist in the datastore, an exception is thrown. It uses HTTP PUT
     * method.
     *
     * @param site the entity to be updated.
     * @return The updated entity.
     */
    @ApiMethod(name = "updateSite")
    public Site updateSite(Site site) {
        if (!containsSite(site)) {
            throw new NotFoundException();
        }
        return site;
    }

    /**
     * This method removes the entity with primary key id. It uses HTTP DELETE
     * method.
     *
     * @param id the primary key of the entity to be deleted.
     */
    @ApiMethod(name = "removeSite")
    public void removeSite(@Named("id") String id) {
        ofy().delete().type(Site.class).id(id).now();
    }

    private boolean containsSite(Site site) {
        Site item = null;
        if (site != null && site.getFqdn() != null) {
            item = ofy().load().type(Site.class).id(site.getFqdn()).now();
        }
        return item != null;
    }

}
