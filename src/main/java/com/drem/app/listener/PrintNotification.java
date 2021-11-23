package com.drem.app.listener;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Drem Darios
 *
 */
public class PrintNotification implements Observer, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private void printNotification(Object arg)
    {
        System.out.println("Url: "+ arg.toString());
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg)
    {
        System.out.println("PRINT: Inside update method");
        printNotification(arg);
    }

}
