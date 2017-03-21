package com.builtbroken.tabletop.client.gui.component.container;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.component.Component;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class ComponentGrid extends ComponentContainer
{
    public boolean fillTop = true;

    public float spacing = 0.05f;

    public float widthSpacing = 0;
    public float heightSpacing = 0;

    public int rows, cols;

    public ComponentGrid(String name, int rows, int cols)
    {
        super(name);
        this.rows = rows;
        this.cols = cols;
        calculateSize();
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
        if(fillTop)
        {
            //Left to right, top to bottom
            //1 2 3 4 5
            //6 7 8 9 10
            float x = spacing + paddingLeft;
            float y = getHeight();
            int rowCount = 0;
            for (Component component : componentList)
            {
                component.setPosition(x, y - heightSpacing - spacing);
                x += widthSpacing + spacing;
                rowCount++;
                if (rowCount == rows)
                {
                    rowCount = 0;
                    y -= heightSpacing + spacing;
                    x = spacing;
                }
            }
        }
        else
        {
            //Left to right, bottom to top
            //6 7 8 9 10
            //1 2 3 4 5
            float x = spacing + paddingLeft;
            float y = spacing;
            int rowCount = 0;
            for (Component component : componentList)
            {
                component.setPosition(x, y);
                x += widthSpacing + spacing;
                rowCount++;
                if (rowCount == rows)
                {
                    rowCount = 0;
                    y += heightSpacing + spacing;
                    x = spacing;
                }
            }
        }
    }

    protected void calculateSize()
    {
        for (Component component : componentList)
        {
            if (component.getWidth() > heightSpacing)
            {
                widthSpacing = component.getWidth();
            }
            if (component.getHeight() > heightSpacing)
            {
                heightSpacing = component.getHeight();
            }
        }
        setWidth(widthSpacing * rows + spacing * rows + spacing);
        setHeight(heightSpacing * cols + spacing * cols + spacing);
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
        return "Component[" + name + "  " + x() + "x  " + y() + "y " + rows + "x" +  cols + "]@" + hashCode();
    }
}
