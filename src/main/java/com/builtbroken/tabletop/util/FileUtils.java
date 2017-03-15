package com.builtbroken.tabletop.util;

import java.io.*;
import java.net.URL;

public class FileUtils
{

    private FileUtils()
    {
    }

    public static String loadAsString(String file)
    {
        try
        {
            return buildStringFromInput(new FileReader(file));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadResourceAsString(String path)
    {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (cl != null)
        {
            URL url = cl.getResource(path);
            if (url == null)
            {
                url = cl.getResource("/" + path);
            }
            if (url != null)
            {
                try
                {
                    return buildStringFromInput(new InputStreamReader(url.openStream()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String buildStringFromInput(Reader readerInput)
    {
        StringBuilder result = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(readerInput);
            String buffer = "";
            while ((buffer = reader.readLine()) != null)
            {
                result.append(buffer + '\n');
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }

}
