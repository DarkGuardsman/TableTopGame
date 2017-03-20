package com.builtbroken.tabletop.client.gui.component.button;

import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.render.RenderRect;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class Button extends Component
{
    public final String name;

    protected ButtonClickAction event;

    public Button(String name, RenderRect background, float width, float height)
    {
        super(background, width, height);
        this.name = name;
    }

    public Button(String name, String texture, float width, float height)
    {
        this(name, texture, 0, 0, width, height);
    }

    public Button(String name, String texture, float x, float y, float width, float height)
    {
        super(width, height, texture, x, y);
        this.name = name;
    }
}
