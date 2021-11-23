package com.drem.app.listener;

import java.net.MalformedURLException;
import java.util.Date;


/**
 * @author Drem Darios
 *
 */
public class UpdateNotificationMain
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String to = "you@test.com";
        String from = "me@test.com";
        String subject = "/****************** NOTHING! ************/";
        String message = "IT WORKED! "+new Date().toString();

        PageModificationListener listener = null;
        try
        {
            listener = new PageModificationListener("http://www.eli.sdsu.edu/");
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SendEmail email = new SendEmail(subject, message, from, to);
        PrintNotification notification = new PrintNotification();

        listener.addObserver(email);
        listener.addObserver(notification);

        //listener.startListening();
    }
}
