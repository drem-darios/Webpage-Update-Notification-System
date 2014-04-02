package memento;

import java.io.Serializable;

/**
 * @author Drem Darios
 *
 */
public interface Memento extends Serializable
{
    public void setState();
    public void getState();
}
