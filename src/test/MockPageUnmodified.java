package test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.mockito.Mockito;

import listener.PageModificationListener;

/**
 * @author Drem Darios
 *
 */
public class MockPageUnmodified extends PageModificationListener
{
    URLConnection mockConnection;
    /**
     * Array of return values for mocking the return values of calls to
     * <code>getLastModified</code>. The final element in the array signal a page
     * modification
     */
    Long[] returnValues = new Long[]{Long.MIN_VALUE, 
            Long.MIN_VALUE, Long.MIN_VALUE, Long.MAX_VALUE};
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param url
     * @throws MalformedURLException 
     */
    public MockPageUnmodified(String url) throws MalformedURLException
    {
        super(url);
    }
    
    @Override
    public void startListening() throws IOException
    {
        super.startListening();
        // Add 1 for the first return value returned when the method was called
        Mockito.verify(mockConnection, Mockito.times(
                returnValues.length + 1)).getLastModified();
    }
    
    @Override
    public URLConnection createConnection() throws IOException
    {
        mockConnection = mock(URLConnection.class);
        when(mockConnection.getLastModified()).thenReturn(
                Long.MIN_VALUE, returnValues);
        
        return mockConnection;
    }
}
