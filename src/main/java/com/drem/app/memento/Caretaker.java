package com.drem.app.memento;

import com.drem.app.listener.PageModificationListener;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Drem Darios
 *
 */
public class Caretaker implements Serializable
{
    private static Map<String, Memento> savedMementoMap = new HashMap<String, Memento>();
    /**
     * The system temporary directory
     */
    private static final String TEMPDIR =  System.getProperty("java.io.tmpdir");
    /**
     * The path to the web monitor mementos
     */
    public static final String PATH = "webmonitor";

    public void init()
    {
        try
        {
            restoreFromFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Memento readMemento(String s)
    {
        return savedMementoMap.get(s);
    }

    public void writeMemento(String s, Memento m)
    {
        savedMementoMap.put(s, m);
        try
        {
            writeToFile(s, m);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void writeToFile(String filename, Memento m) throws IOException
    {
        File dir = new File(TEMPDIR+PATH);

        // if the directory does not exist, create it
        if (!dir.exists())
        {
          dir.mkdir();
        }
        File f = new File(dir+File.separator+filename);
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        fos = new FileOutputStream(f); // filename
        out = new ObjectOutputStream(fos);
        out.writeObject(m);
        out.close();
    }


    /**
     * @throws IOException
     * @throws ClassNotFoundException
     *
     */
    private void restoreFromFile() throws IOException, ClassNotFoundException
    {
        File dir = new File(TEMPDIR+PATH);
        if (dir.exists())
        {
            String[] files = dir.list();
            for (String filename : files)
            {
                FileInputStream fis = null;
                ObjectInputStream in = null;
                fis = new FileInputStream(dir.getPath()+File.separator+filename);
                in = new ObjectInputStream(fis);
                Memento m = (Memento) in.readObject();
                PageModificationListener p = new PageModificationListener();
                p.setMemento(m);
                savedMementoMap.put(filename, m);
                in.close();
            }
        }
    }
}
