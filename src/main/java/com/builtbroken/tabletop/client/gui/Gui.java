package com.builtbroken.tabletop.client.gui;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.controls.MouseInput;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.container.ComponentContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Container object for GUI {@link Component}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2017.
 */
public class Gui
{
    /** List of components contained in the GUI */
    public List<Component> componentList = new ArrayList();

    /** Can the user see the GUI / should it render */
    public boolean isVisible = true;
    /** Can the user use the GUI */
    public boolean isActivate = true;

    /**
     * Called to update the display
     *
     * @param display
     * @return component the mouse is currently over
     */
    public Component update(GameDisplay display, float mouseX, float mouseY)
    {
        //Update all components
        for (Component component : componentList)
        {
            component.update(display);
            component.setMouseOver(false);
        }
        if (MouseInput.inWindow && isVisible && isActivate)
        {
            //Get current component that the mouse is over
            Component selected = getComponent(mouseX, mouseY);
            if (selected != null)
            {
                selected.setMouseOver(true);
                return selected;
            }
        }
        return null;
    }

    /**
     * Render the GUI on the screen
     *
     * @param mouseX
     * @param mouseY
     */
    public void render(float mouseX, float mouseY)
    {
        if (isVisible)
        {
            for (Component component : componentList)
            {
                component.render(mouseX, mouseY);
            }
        }
    }

    /**
     * Called when the game display has resized
     *
     * @param display
     */
    public void onResize(GameDisplay display)
    {
        for (Component component : componentList)
        {
            component.onResize(display);
        }
    }

    /**
     * Checks if the mouse is over a component
     *
     * @param mouseX - position
     * @param mouseY - position
     * @return true if over GUI
     */
    public boolean isMouseOnGUI(float mouseX, float mouseY)
    {
        return getComponent(mouseX, mouseY) != null;
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
            float lowerX = component.x();
            float higherX = component.x() + component.getWidth();
            float lowerY = component.y();
            float higherY = component.y() + component.getHeight();
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

    public void add(Component component)
    {
        if (!componentList.contains(component))
        {
            componentList.add(component);
        }
    }
}
