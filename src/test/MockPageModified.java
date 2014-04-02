package test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;

import listener.PageModificationListener;

/**
 * @author Drem Darios
 *
 */
public class MockPageModified extends PageModificationListener
{
    /**
     * 
     * @param url
     * @throws MalformedURLException 
     */
    public MockPageModified(String url) throws MalformedURLException
    {
        super(url);
    }
    
    @Override
    public URLConnection createConnection() throws IOException
    {
        URLConnection mockConnection = mock(URLConnection.class);
        when(mockConnection.getLastModified()).thenReturn(Long.MAX_VALUE);
        
        return mockConnection;
    }
}
