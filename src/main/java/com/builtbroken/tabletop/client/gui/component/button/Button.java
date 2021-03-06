package com.builtbroken.tabletop.client.gui.component.button;

import com.builtbroken.tabletop.client.graphics.render.RenderRect;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.button.actions.ButtonLogic;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class Button extends Component
{
    /** Logic object for the button */
    public ButtonLogic logic;

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
    public void render(float mouseX, float mouseY, float xoffset, float yoffset, float zoffset)
    {
        super.render(mouseX, mouseY, xoffset, yoffset, zoffset);
        if (isVisible() && logic != null)
        {
            logic.render(mouseX, mouseY, x() + xoffset, y() + yoffset, zoffset);
        }
    }

    @Override
    public void onClick(float mouseX, float mouseY, boolean left)
    {
        if (logic != null && logic.isEnabled())
        {
            logic.onClick(mouseX, mouseY, left);
        }
        System.out.println("Click " + left + "  " + this);
    }

    @Override
    public boolean isVisible()
    {
        return super.isVisible() && (logic == null || logic.shouldRender());
    }

    @Override
    public String toString()
    {
        return "Button[" + name + "  " + x() + "x  " + y() + "y]";
    }

    public Button setLogic(ButtonLogic logic)
    {
        this.logic = logic;
        this.logic.button = this;
        return this;
    }

    @Override
    public void onResize()
    {
        updatePosition();
    }

    @Override
    public float getWidth()
    {
        if (logic != null && logic.getWidth() > 0)
        {
            return logic.getWidth();
        }
        return super.getWidth();
    }

    @Override
    public float getHeight()
    {
        if (logic != null && logic.getHeight() > 0)
        {
            return logic.getHeight();
        }
        return super.getHeight();
    }
}
