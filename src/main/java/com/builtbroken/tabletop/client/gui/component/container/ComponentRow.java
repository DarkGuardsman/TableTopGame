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

    public ComponentRow(String name)
    {
        super(name);
    }

    public ComponentRow(String name, PositionLogic logic)
    {
        super(name);
        this.componentPositionLogic = logic;
    }

    @Override
    public void onResize(GameDisplay display)
    {
        super.onResize(display);
        calculateSize();
        positionComponents();
    }

    protected void positionComponents()
    {
        positionComponents(0, 0, 0, 0);
    }

    protected void positionComponents(float paddingLeft, float paddingRight, float paddingTop, float paddingBottom)
    {
        if (componentPositionLogic == PositionLogic.LEFT || componentPositionLogic == PositionLogic.RIGHT)
        {
            //Left to right button placement
            //1 2 3 4 5
            float x = spacing + paddingLeft;
            for (Component component : componentList)
            {
                component.setPosition(x, 0);
                x += component.getWidth() + spacing;
            }
        }
        else if (componentPositionLogic == PositionLogic.TOP || componentPositionLogic == PositionLogic.BOTTOM)
        {
            //bottom to top button placement
            float y = spacing + paddingTop;
            for (Component component : componentList)
            {
                component.setPosition(0, y);
                y += component.getHeight() + spacing;
            }
        }
    }

    protected void calculateSize()
    {
        float width = 0;
        float height = 0;
        if (componentPositionLogic == PositionLogic.LEFT || componentPositionLogic == PositionLogic.RIGHT)
        {
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
        else
        {
            for (Component component : componentList)
            {
                height += component.getHeight();
                if (component.getWidth() > width)
                {
                    width = component.getWidth();
                }
            }
            setWidth(width);
            setHeight(height + ((componentList.size() + 1) * spacing));
        }
    }

    @Override
    public <C extends Component> C add(C component)
    {
        super.add(component);
        calculateSize();
        positionComponents();
        return component;
    }

    @Override
    public String toString()
    {
        return "ComponentRow[" + name + "  " + x() + "x  " + y() + "y  " + componentPositionLogic + "]@" + hashCode();
    }
}
