package com.github.sitemindr.controller;

import com.github.sitemindr.entity.PersonAuthority;
import com.github.sitemindr.entity.Person;
import com.github.sitemindr.entity.Site;
import com.github.sitemindr.service.SiteService;
import com.github.sitemindr.util.PingResult;
import com.github.sitemindr.util.Pinger;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/cron")
public class CronController {

    static {
        ObjectifyService.register(Person.class);
        ObjectifyService.register(Site.class);
    }

    @RequestMapping(value = "/httpget/{website}/", method = RequestMethod.GET)
    public String httpGet(@PathVariable String website, ModelMap model) {
        if (isUserAthorized()) {
            PingResult result = Pinger.doHttpGet(website, 10);
            SiteService.updateSite(website, result.isOk());
            System.out.println(website);
            model.addAttribute("message", result.getMessage());
        }
        return "list";
    }

    @RequestMapping(value = "/httpgetall", method = RequestMethod.GET)
    public String httpGetAll(ModelMap model) {
        if (isUserAthorized()) {
            for (String url : SiteService.getSites()) {
                PingResult result = Pinger.doHttpGet(url, 5);
                SiteService.updateSite(url, result.isOk());
                System.out.println(url);
                model.addAttribute("message", result.getMessage());
            }
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

}
