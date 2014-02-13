/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sitemindr.util;

import com.github.sitemindr.entity.Person;
import com.github.sitemindr.entity.Site;
import static com.googlecode.objectify.util.LogUtils.msg;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author moscac
 */
public class Notifier {

    public static void notifyInterestedParties(Site site) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = site.getFqdn() + " appears to be " + (site.isAvailable() ? "available" : "UNAVAILABLE!");

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("sitemindr@appspot.com", "sitemindr"));
            for (Person person : site.getInterestedParties()) {
                msg.addRecipient(Message.RecipientType.BCC,
                        new InternetAddress(person.getNotifyEmail(), person.getName()));
            }
            msg.setSubject(site.getFqdn() + " status");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            Logger.getLogger(Notifier.class.getName()).log(Level.SEVERE, null, e);
        } catch (MessagingException | UnsupportedEncodingException e) {
            Logger.getLogger(Notifier.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
