package com.drem.app.listener;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * @author Drem Darios
 *
 */
public class SendEmail implements Observer, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String[] to;
    private String from;
    public String subject;
    public String message;
    private String host = "localhost";
    private String port = "8080";
    private boolean debug = false;

    public SendEmail(String subject, String message, String from, String... to)
    {
        this.subject = subject;
        this.message = message;
        this.from = from;
        this.to = to;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        System.out.println("EMAIL: Inside update method");
        postMail();
        System.out.println("EMAIL: Email sent");
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    /**
     * @return the to
     */
    public String[] getTo()
    {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String[] to)
    {
        this.to = to;
    }

    /**
     * @return the from
     */
    public String getFrom()
    {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from)
    {
        this.from = from;
    }

    /**
     * @return the host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * @return the port
     */
    public String getPort()
    {
        return port;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public void postMail()
    {
         Properties props = new Properties();
         props.put("mail.smtp.host", host);
         props.put("mail.smtp.port", port);

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

        try
        {
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);

            InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++)
            {
                addressTo[i] = new InternetAddress(to[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);

            msg.addHeader("Notification", "Page Modified");

            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        }
        catch(MessagingException ex)
        {

        }
    }
}
