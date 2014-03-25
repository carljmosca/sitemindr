package com.github.sitemindr.service;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.github.sitemindr.entity.Person;
import com.googlecode.objectify.NotFoundException;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.SaveException;
import java.util.ArrayList;

@Api(name = "personendpoint", namespace = @ApiNamespace(ownerDomain = "github.com",
        ownerName = "github.com", packagePath = "sitemindr.service"))
public class PersonEndpoint {

    /**
     * This method lists all the entities inserted in datastore. It uses HTTP
     * GET method and paging support.
     *
     * @param cursorString
     * @param limit
     * @return A CollectionResponse class containing the list of all entities
     * persisted and a cursor to the next page.
     */
    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listPerson")
    public CollectionResponse<Person> listPerson(
            @Nullable @Named("cursor") String cursorString,
            @Nullable @Named("limit") Integer limit) {

        Cursor cursor = null;
        List<Person> execute = new ArrayList<>(0);

        Iterable<Person> persons = ofy().load().type(Person.class);
        for (Person person : persons) {
            execute.add(person);
        }

        return CollectionResponse.<Person>builder().setItems(execute)
                .setNextPageToken(cursorString).build();
    }

    /**
     * This method gets the entity having primary key id. It uses HTTP GET
     * method.
     *
     * @param id the primary key of the java bean.
     * @return The entity with primary key id.
     */
    @ApiMethod(name = "getPerson")
    public Person getPerson(@Named("id") String id) {
        Person person = null;
        person = ofy().load().type(Person.class).id(id).now();
        return person;
    }

    /**
     * This inserts a new entity into App Engine datastore. If the entity
     * already exists in the datastore, an exception is thrown. It uses HTTP
     * POST method.
     *
     * @param person the entity to be inserted.
     * @return The inserted entity.
     */
    @ApiMethod(name = "insertPerson")
    public Person insertPerson(Person person) {
        if (containsPerson(person)) {
            throw new SaveException(person, person.getName() + " already exists", null);
        }
        return person;
    }

    /**
     * This method is used for updating an existing entity. If the entity does
     * not exist in the datastore, an exception is thrown. It uses HTTP PUT
     * method.
     *
     * @param person the entity to be updated.
     * @return The updated entity.
     */
    @ApiMethod(name = "updatePerson")
    public Person updatePerson(Person person) {
        if (!containsPerson(person)) {
            throw new NotFoundException();
        }
        return person;
    }

    /**
     * This method removes the entity with primary key id. It uses HTTP DELETE
     * method.
     *
     * @param id the primary key of the entity to be deleted.
     */
    @ApiMethod(name = "removePerson")
    public void removePerson(@Named("id") String id) {
        ofy().delete().type(Person.class).id(id).now();
    }

    private boolean containsPerson(Person person) {
        Person item = null;
        if (person != null && person.getName() != null) {
            item = ofy().load().type(Person.class).id(person.getName()).now();
        }
        return item != null;
    }

}
