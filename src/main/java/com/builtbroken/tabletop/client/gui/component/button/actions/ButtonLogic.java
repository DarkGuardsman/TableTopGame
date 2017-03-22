package com.builtbroken.tabletop.client.gui.component.button.actions;

import com.builtbroken.tabletop.client.gui.component.button.Button;

/**
 * Object for handling actions for a button indirectly from the button object
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class ButtonLogic
{
    public Button button;
    /**
     * Called when a button has been clicked
     *
     * @param mouseX
     * @param mouseY
     * @param left
     */
    public void onClick(float mouseX, float mouseY, boolean left)
    {
        if (left)
        {
            leftClick(mouseX, mouseY);
        }
        else
        {
            rightClick(mouseX, mouseY);
        }
    }

    protected void leftClick(float mouseX, float mouseY)
    {

    }


    protected void rightClick(float mouseX, float mouseY)
    {

    }

    public void render(float mouseX, float mouseY, float x, float y, float z)
    {

    }

    public boolean isEnabled()
    {
        return true;
    }

    public boolean shouldRender()
    {
        return true;
    }

    public float getWidth()
    {
        return -1;
    }

    public float getHeight()
    {
        return -1;
    }
}
