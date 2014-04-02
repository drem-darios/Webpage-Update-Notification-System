package test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;
import listener.PageModificationListener;
import listener.PrintNotification;
import listener.SendEmail;
import memento.Caretaker;
import memento.Memento;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Drem Darios
 *
 */
public class TestPageModificationListener extends Assert
{
    
    @Test
    public void testLoadMementoExists() throws MalformedURLException
    {
        String subject = "Subject";
        String message = "Message";
        String from = "from@email.com";
        String to = "to@email.com";
        
        String urlString = "http://www.test.url.com/";
        URL url = new URL(urlString);
        PageModificationListener listener = new PageModificationListener(urlString);
        listener.addObserver(new PrintNotification());
        listener.addObserver(new SendEmail(subject, message, from, to));
        Caretaker caretaker = new Caretaker();
        Memento m = listener.createMemento();
        caretaker.writeMemento(url.getAuthority(), m);
        
        Caretaker caretaker2 = new Caretaker();
        caretaker2.init();
    }
    
    @Test
    public void testLoadMementoNotExists() throws MalformedURLException
    {
        String urlString = "http://www.no.memento.exists.forthis.com/";
        Caretaker caretaker = new Caretaker();
        URL url = new URL(urlString);
        caretaker.init();
        assertTrue(caretaker.readMemento(url.getAuthority()) == null);
    }
    /**
     * Verifies that a non-null memento is created by the page modification 
     * listener
     * @throws MalformedURLException
     */
    @Test
    public void testCreateMemento() throws MalformedURLException
    {
        String subject = "Subject";
        String message = "Message";
        String from = "from@email.com";
        String to = "to@email.com";
        
        String url = "http://www.eli.sdsu.edu/";
        PageModificationListener listener = new PageModificationListener(url);
        listener.addObserver(new PrintNotification());
        listener.addObserver(new SendEmail(subject, message, from, to));
        Memento m = listener.createMemento();
        assertNotNull(m);
    }
    
    @Test
    public void testPageUnmodified() throws IOException
    {
        String url = "http://www.eli.sdsu.edu/";
        PageModificationListener unmodifiedListener = new MockPageUnmodified(url);
        
        SendEmail email = Mockito.mock(SendEmail.class);
        PrintNotification notification = Mockito.mock(PrintNotification.class);
        
        unmodifiedListener.addObserver(email);
        unmodifiedListener.addObserver(notification);
        
        unmodifiedListener.startListening();
        
    }
    
    @Test
    public void testPageModified() throws IOException
    {
        String url = "http://www.eli.sdsu.edu/";
        PageModificationListener modifiedListener = new MockPageModified(url);
        
        SendEmail email = Mockito.mock(SendEmail.class); 
        PrintNotification notification = Mockito.mock(PrintNotification.class);
        
        modifiedListener.addObserver(email);
        modifiedListener.addObserver(notification);

        modifiedListener.startListening();
        
        Mockito.verify(email).update(modifiedListener, url);
        Mockito.verify(notification).update(modifiedListener, url);
    }
    
 
    
    @Test
    public void testWriteMemento() throws MalformedURLException
    {
        Caretaker c = new Caretaker();
        String urlString = "http://www.eli.sdsu.edu/";
        URL url = new URL(urlString);
        Memento m = Mockito.mock(Memento.class);
        c.writeMemento(url.getAuthority(), m);
        File f = new File(System.getProperty("java.io.tmpdir") 
                +Caretaker.PATH+File.separator+ url.getAuthority());
        assertTrue(f.exists());
    }
    
    @Test
    public void testReadMemento() throws MalformedURLException
    {
        /** Writes the memento out to a file */
        Caretaker c = new Caretaker();
        String urlString = "http://www.eli.sdsu.edu/";
        URL url = new URL(urlString);
        Memento m = Mockito.mock(Memento.class);
        c.writeMemento(url.getAuthority(), m);
        /** End writing memento */
        assertEquals(m, c.readMemento(url.getAuthority()));
    }
    
    @Test
    public void testRestoreObservers()
    {
        
    }
}
