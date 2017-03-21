package com.builtbroken.tabletop.client.gui.component.button;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.render.RenderRect;
import com.builtbroken.tabletop.client.gui.component.Component;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class Button extends Component
{
    protected ButtonClickAction event;

    public Button(String name, RenderRect background, float width, float height)
    {
        super(name, background, width, height);
    }

    public Button(String name, String texture, float width, float height)
    {
        this(name, texture, 0, 0, width, height);
    }

    public Button(String name, String texture, float x, float y, float width, float height)
    {
        super(name, width, height, texture, x, y);
    }

    @Override
    public void onClick(GameDisplay display, float mouseX, float mouseY, boolean left)
    {
        if(event != null)
        {

        }
        System.out.println("Click " + left + "  " + this);
    }

    @Override
    public String toString()
    {
        return "Button[" + name + "  " + x() + "x  " + y() + "y]";
    }
}
