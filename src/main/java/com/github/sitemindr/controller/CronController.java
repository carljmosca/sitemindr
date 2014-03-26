package com.github.sitemindr.controller;

import com.github.sitemindr.entity.PersonAuthority;
import com.github.sitemindr.entity.Person;
import com.github.sitemindr.entity.Site;
import com.github.sitemindr.util.Notifier;
import com.github.sitemindr.util.PingResult;
import com.github.sitemindr.util.Pinger;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/cron")
public class CronController {

    private static Logger logger = Logger.getLogger(Notifier.class.getName());
    static {
        ObjectifyService.register(Person.class);
        ObjectifyService.register(Site.class);
    }

    @RequestMapping(value = "/httpget/{website}/", method = RequestMethod.GET)
    public String httpGet(@PathVariable String website, ModelMap model) {
        if (isUserAthorized()) {
            PingResult result = Pinger.doHttpGet(website, 10);
            updateSite(website, result.isOk());
            model.addAttribute("message", result.getMessage());
        }
        return "list";
    }

    @RequestMapping(value = "/httpgetall", method = RequestMethod.GET)
    public String httpGetAll(ModelMap model) {
        for (Site site : getSites()) {
            PingResult result = Pinger.doHttpGet(site.getFqdn(), site.getWaitTimeout());
            updateSite(site.getFqdn(), result.isOk());
            System.out.println(site.getFqdn());
            model.addAttribute("message", result.getMessage());
        }
        return "list";
    }

    private boolean isUserAthorized() {
        PersonAuthority authority = userAuthority();
        switch (authority) {
            case USER:
            case ADMINISTRATOR:
                return true;
            default:
                return false;
        }
    }

    private PersonAuthority userAuthority() {
        if (ofy().load().type(Person.class).count() <= 1) {
            return PersonAuthority.ADMINISTRATOR;
        }
        PersonAuthority result = PersonAuthority.NONE;
        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            Person person = ofy().load().type(Person.class).id(currentUser.getEmail()).now();
            if (person == null) {
                person = new Person();
                person.setEmail(currentUser.getEmail());
                person.setAuthority(PersonAuthority.NONE);
            } else {
                result = person.getAuthority();
            }
            person.setUpdated(new Date());
            ofy().save().entity(person).now();
        }
        return result;
    }

    private List<Site> getSites() {
        List<Site> list = new ArrayList<>();
        Iterable<Site> sites = ofy().load().type(Site.class);
        for (Site site : sites) {
            list.add(site);
        }
        return list;
    }

    private List<Person> getInterestedParties() {
        List<Person> list = new ArrayList<>();
        Iterable<Person> persons = ofy().load().type(Person.class);
        for (Person person : persons) {
            list.add(person);
        }
        return list;
    }

    private void updateSite(String url, Boolean available) {

        Logger.getLogger(Notifier.class.getName()).log(Level.INFO, "updating {0}", url);
        Site site = ofy().load().type(Site.class).id(url).now();
        boolean wasAvailable = false;
        if (site == null) {
            site = new Site();
            site.setFqdn(url);
            updateInterestedParty("test", "test@here.com", "test@here.com");
        } else {
            wasAvailable = site.isAvailable();
        }
        logger.log(Level.INFO, "{0} was {1}available", new Object[]{url, wasAvailable ? "" : "not "});
        logger.log(Level.INFO, "{0} is {1}available", new Object[]{url, site.isAvailable() ? "" : "not "});
        site.setAvailable(available);
        if (available) {
            site.setLastAvailable(new Date());
        } else {
            site.setLastUnavailable(new Date());
        }
        ofy().save().entity(site).now();
        if ((available && !wasAvailable) || (!available && wasAvailable)) {
            Notifier.notifyInterestedParties(site, getInterestedParties());
        }
    }

    private void updateInterestedParty(String name, String email, String notifyEmail) {
        logger.log(Level.INFO, "updating interested party: {0}", name);
        Person person = new Person();
        person.setName(name);
        person.setEmail(email);
        person.setNotifyEmail(notifyEmail);
        ofy().save().entity(person).now();
    }

}
