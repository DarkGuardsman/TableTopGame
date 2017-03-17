package com.builtbroken.tabletop.client.gui.component;

import com.builtbroken.tabletop.util.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2017.
 */
public class ComponentContainer extends Component
{
    public List<Component> componentList = new ArrayList();

    public boolean isVisible = true;

    public ComponentContainer()
    {
    }

    public ComponentContainer(float width, float height, String texture, Vector3f position)
    {
        super(width, height, texture, position);
    }

    /**
     * Render the GUI on the screen
     *
     * @param mouseX
     * @param mouseY
     */
    public void render(float mouseX, float mouseY)
    {
        super.render(mouseX, mouseY);
        if (isVisible)
        {
            for (Component component : componentList)
            {
                component.render(mouseX, mouseY);
            }
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
