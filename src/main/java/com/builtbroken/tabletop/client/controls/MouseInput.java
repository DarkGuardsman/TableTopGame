package com.builtbroken.tabletop.client.controls;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class MouseInput
{
    public static double mouseX, mouseY;
    public static boolean inWindow;

    public static boolean[] mouseButtons = new boolean[20];

    public static void init(long window)
    {
        //https://github.com/SilverTiger/lwjgl3-tutorial/wiki/Input
        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback()
        {
            @Override
            public void invoke(long window, double xpos, double ypos)
            {
                mouseX = xpos;
                mouseY = ypos;
            }
        });

        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback()
        {
            @Override
            public void invoke(long window, int button, int action, int mods)
            {
                if (button >= 0 && button < mouseButtons.length)
                {
                    System.out.println("button: " + button + "  action: " + action + " x: " + mouseX + "  y: " + mouseY);
                    mouseButtons[button] = action != GLFW_RELEASE;
                }
            }
        });

        glfwSetCursorEnterCallback(window, new GLFWCursorEnterCallback()
        {

            @Override
            public void invoke(long window, boolean entered)
            {
                inWindow = entered;
                System.out.println("Curse Entered: " + entered);
            }
        });
    }

    public static boolean leftClick()
    {
        return mouseButtons[0];
    }

    public static boolean rightClick()
    {
        return mouseButtons[1];
    }
}
