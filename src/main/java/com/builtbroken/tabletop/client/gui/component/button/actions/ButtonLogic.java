package com.builtbroken.tabletop.client.gui.component.button.actions;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.component.button.Button;

/**
 * Object for handling actions for a button indirectly from the button object
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class ButtonLogic
{
    /**
     * Called when a button has been clicked
     *
     * @param display
     * @param mouseX
     * @param mouseY
     * @param left
     */
    public void onClick(GameDisplay display, Button button, float mouseX, float mouseY, boolean left)
    {
        if (left)
        {
            leftClick(display, button, mouseX, mouseY);
        }
        else
        {
            rightClick(display, button, mouseX, mouseY);
        }
    }

    protected void leftClick(GameDisplay display, Button button, float mouseX, float mouseY)
    {

    }


    protected void rightClick(GameDisplay display, Button button, float mouseX, float mouseY)
    {

    }

    public boolean isEnabled(GameDisplay display)
    {
        return true;
    }

    public boolean shouldRender(GameDisplay display)
    {
        return true;
    }

    public float getWidth(GameDisplay display)
    {
        return -1;
    }

    public float getHeight(GameDisplay display)
    {
        return -1;
    }
}
