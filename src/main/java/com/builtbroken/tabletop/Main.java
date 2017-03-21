package com.builtbroken.tabletop;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Main
{
    public static void main(String... args) throws InterruptedException
    {
        Logger logger = LogManager.getRootLogger();

        logger.info("Loading Table Top Game Manager...");
        logger.info("Parsing arguments...");

        HashMap<String, String> launchSettings = loadArgs(args);

        logger.info("Creating game object");
        Game game = new Game();

        logger.info("Creating display object");
        //Create display, TODO check if server only
        GameDisplay display = new GameDisplay(game);

        logger.info("Starting threads");
        //Start threads
        game.start();
        display.start();

        while (display.running || game.running)
        {
            if (!display.running)
            {
                game.running = false;
                game.save(true);
            }
            if (!game.running)
            {
                display.running = false;
            }
            Thread.sleep(1000);
        }
        logger.info("End of main");
        System.exit(0);
    }

    /**
     * Converts arguments into a hashmap for usage
     *
     * @param args
     * @return
     */
    public static HashMap<String, String> loadArgs(String... args)
    {
        final HashMap<String, String> map = new HashMap();
        if (args != null)
        {
            String currentArg = null;
            String currentValue = "";
            for (int i = 0; i < args.length; i++)
            {
                String next = args[i].trim();
                if (next == null)
                {
                    throw new IllegalArgumentException("Null argument detected in program arguments");
                }
                else if (next.startsWith("-"))
                {
                    if (currentArg != null)
                    {
                        map.put(currentArg, currentValue);
                        currentValue = "";
                    }

                    if (next.contains("="))
                    {
                        String[] split = next.split("=");
                        currentArg = split[0].substring(1).trim();
                        currentValue = split[1].trim();
                    }
                    else
                    {
                        currentArg = next.substring(1).trim();
                    }
                }
                else if (currentArg != null)
                {
                    if (!currentValue.isEmpty())
                    {
                        currentValue += ",";
                    }
                    currentValue += next.replace("\"", "").replace("'", "").trim();
                }
                else
                {
                    throw new IllegalArgumentException("Value has no argument associated with it [" + next + "]");
                }
            }
            //Add the last loaded value to the map
            if (currentArg != null)
            {
                map.put(currentArg, currentValue);
            }
        }
        return map;
    }
}
