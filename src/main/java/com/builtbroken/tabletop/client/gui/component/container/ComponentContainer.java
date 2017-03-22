package com.builtbroken.tabletop.client.gui.component.container;

import com.builtbroken.tabletop.client.gui.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2017.
 */
public class ComponentContainer extends Component
{
    public List<Component> componentList = new ArrayList();

    public boolean areComponentsVisible = true;

    public ComponentContainer(String name)
    {
        super(name);
    }

    public ComponentContainer(String name, float width, float height, String texture, float x, float y)
    {
        super(name, width, height, texture, x, y);
    }

    @Override
    public void render(float mouseX, float mouseY)
    {
        super.render(mouseX, mouseY);
        if (areComponentsVisible)
        {
            for (Component component : componentList)
            {
                component.render(mouseX, mouseY, x(), y(), 0);
            }
        }
    }

    @Override
    public void updatePosition()
    {
        super.updatePosition();
        for (Component component : componentList)
        {
            component.updatePosition();
        }
    }

    /**
     * Gets the GUI component that mouse is currently over
     *
     * @param mouseX - position
     * @param mouseY - position
     * @return component
     */
    public Component getComponent(float mouseX, float mouseY)
    {
        for (Component component : componentList)
        {
            float lowerX = component.x() + x();
            float higherX = component.x() + x() + component.getWidth();
            float lowerY = component.y() + y();
            float higherY = component.y() + y() + component.getHeight();
            if (mouseX > lowerX && mouseX < higherX && mouseY > lowerY && mouseY < higherY)
            {
                if (component instanceof ComponentContainer)
                {
                    Component c = ((ComponentContainer) component).getComponent(mouseX, mouseY);
                    if (c != null)
                    {
                        return c;
                    }
                }
                return component;
            }
        }
        return null;
    }


    public void onResize(Component component)
    {

    }

    public <C extends Component> C add(C component)
    {
        if (!componentList.contains(component))
        {
            componentList.add(component);
            component.parent = this;
        }
        return component;
    }

    @Override
    public String toString()
    {
        return "ComponentContainer[" + name + "  " + x() + "x,  " + y() + "y,  n = " + componentList.size() + "]@" + hashCode();
    }
}
