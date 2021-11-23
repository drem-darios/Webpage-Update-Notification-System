package com.drem.app.listener;

import com.drem.app.memento.Caretaker;
import com.drem.app.memento.Memento;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Listener class to listen for modifications of a web page.
 *
 * @author Drem Darios
 *
 */
public class PageModificationListener extends Observable implements Serializable
{
    /**
     * Time the web page was last check for modifications
     */
    private long lastChecked = Long.MAX_VALUE;
    /**
     * Last known time the web page was modified
     */
    private long lastModified = Long.MIN_VALUE;
    /**
     * Web page URL being monitored
     */
    private static URL url;
    /**
     * Memento caretaker
     */
    private static Caretaker caretaker = new Caretaker();
    /**
     * List of Observers waiting to be notified of a page modification
     */
    private static List<Observer> observers = new ArrayList<Observer>();

    public PageModificationListener()
    {

    }
    /**
     * Constructs a new <code>PageModificationListener</code> which checks to
     * see if the last modification date for the URL given is more recent than
     * the current time.
     *
     * @param urlString The URL string to listen for modifications
     * @throws MalformedURLException
     */
    public PageModificationListener(String urlString) throws MalformedURLException
    {
        url = new URL(urlString);
    }

    @Override
    public void addObserver(Observer observer)
    {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer)
    {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object url)
    {
        Iterator<Observer> elements = observers.iterator();
        while (elements.hasNext())
        {
            elements.next().update(this, url);
        }
        clearChanged();
    }

    @Override
    public int countObservers()
    {
        return observers.size();
    }
    /**
     * Creates a new memento object
     * @return The memento object created
     */
    public Memento createMemento()
    {
        Memento m = new PageListenerMemento(lastModified, lastChecked);
        caretaker.writeMemento(url.getAuthority(), m);
        return m;
    }

    public Memento getMemento(String s)
    {
        return caretaker.readMemento(s);
    }

    /**
     * Loads the memento passed in. If null is passed in, a new memento will be
     * created and loaded.
     * @param m The memento to load
     */
    public void setMemento(Memento m)
    {
        if (m == null)
        {
            m = createMemento();
        }
        m.getState();
    }

    public URLConnection createConnection() throws IOException
    {
        return url.openConnection();
    }

    /**
     * Starts listening for page modifications and notifies observers when a
     * page has been modified.
     * @throws IOException
     */
    public void startListening() throws IOException
    {
        URLConnection connection = createConnection();
        while (lastModified < lastChecked)
        {
            lastModified = connection.getLastModified();
            lastChecked = System.currentTimeMillis();
            createMemento();
            try
            {
                System.out.println("Sleeping...");
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        setChanged();
        notifyObservers(url.toString());
    }

    private static class PageListenerMemento implements Memento
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private Properties props = new Properties();
        private long lastModified;
        private long lastChecked;

        public PageListenerMemento(final long lastModified, final long lastChecked)
        {
            this.lastModified = lastModified;
            this.lastChecked = lastChecked;
            setState();
        }

        @Override
        public void setState()
        {
            props.put("url", url);
            props.put("observers", observers);
            props.put("lastModifiedDate", lastModified);
            props.put("lastCheckedDate", lastChecked);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void getState()
        {
            url = (URL) props.get("url");
            observers = (List<Observer>) props.get("observers");
            lastModified = (Long) props.get("lastModifiedDate");
            lastChecked = (Long) props.get("lastCheckedDate");
        }
    }
}
