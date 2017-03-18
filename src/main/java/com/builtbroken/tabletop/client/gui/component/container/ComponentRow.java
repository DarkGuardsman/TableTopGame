package com.builtbroken.tabletop.client.gui.component.container;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.PositionLogic;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class ComponentRow extends ComponentContainer
{
    public float spacing = 0.05f;

    public PositionLogic componentPositionLogic = PositionLogic.LEFT;

    public ComponentRow()
    {
    }

    public ComponentRow(PositionLogic logic)
    {
        this.componentPositionLogic = logic;
    }

    @Override
    public void onResize(GameDisplay display)
    {
        super.onResize(display);
        calculateWidth();
        positionComponents();
    }

    protected void positionComponents()
    {
        if (componentPositionLogic == PositionLogic.LEFT)
        {
            //Left to right button placement
            //1 2 3 4 5
            float x = spacing;
            for (Component component : componentList)
            {
                component.setPosition(x, 0);
                x += component.getWidth() + spacing;
            }
        }
        else if (componentPositionLogic == PositionLogic.RIGHT)
        {
            //Right to left button placement
            //5 4 3 2 1
            float x = getWidth() - spacing;
            for (Component component : componentList)
            {
                component.setPosition(x - component.getWidth(), 0);
                x -= component.getWidth() + spacing;
            }
        }
        else if (componentPositionLogic == PositionLogic.BOTTOM)
        {
            //bottom to top button placement
        }
        else if (componentPositionLogic == PositionLogic.TOP)
        {
            //top to bottom button placement
        }
    }

    protected void calculateWidth()
    {
        float width = 0;
        float height = 0;
        for (Component component : componentList)
        {
            width += component.getWidth();
            if (component.getHeight() > height)
            {
                height = component.getHeight();
            }
        }
        setWidth(width + ((componentList.size() + 1) * spacing));
        setHeight(height);
    }

    @Override
    public void add(Component component)
    {
        super.add(component);
        calculateWidth();
        positionComponents();
    }
}
