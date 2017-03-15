package com.builtbroken.tabletop.client.controls;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/14/2017.
 */
public class KeyboardInput extends GLFWKeyCallback
{
    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        keys[key] = action != GLFW.GLFW_RELEASE;
    }

    public static boolean isKeyDown(int key)
    {
        return keys[key];
    }

    public static boolean zoomIn()
    {
        return isKeyDown(GLFW.GLFW_KEY_KP_ADD);
    }

    public static boolean zoomOut()
    {
        return isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT);
    }
}