package com.builtbroken.tabletop.client.gui.component.container;

import com.builtbroken.tabletop.client.GameDisplay;
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

    public ComponentContainer()
    {
    }

    public ComponentContainer(float width, float height, String texture, float x, float y)
    {
        super(width, height, texture, x, y);
    }

    @Override
    public void render(float mouseX, float mouseY)
    {
        super.render(mouseX, mouseY);
        if (areComponentsVisible)
        {
            for (Component component : componentList)
            {
                component.render(mouseX, mouseY, x, y, 0);
            }
        }
    }

    @Override
    public void updatePosition(GameDisplay display)
    {
        super.updatePosition(display);
        for (Component component : componentList)
        {
            component.updatePosition(display);
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
        return null;
    }

    public void add(Component component)
    {
        if (!componentList.contains(component))
        {
            componentList.add(component);
        }
    }
}
