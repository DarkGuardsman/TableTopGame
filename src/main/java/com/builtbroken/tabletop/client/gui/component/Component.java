package com.builtbroken.tabletop.client.gui.component;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.render.RenderRect;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.container.ComponentContainer;

/**
 * Section, container, or part of a GUI
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2017.
 */
public class Component
{
    public final String name;
    /** Render object for the component background */
    public RenderRect background;
    /** Toggle to center the component in the x by the width */
    public boolean centerX = false;
    /** Toggle to center the component in the y by the height */
    public boolean centerY = false;


    /** Toggle to note something has changed in the GUI */
    protected boolean hasChanged = false;

    protected boolean isMouseOver;

    /** Location of the component, relative to container */
    private float x_location, y_location;

    //Is the component visible
    private boolean isVisible = true;

    //Dimensions of the component
    private float width;
    private float height;

    //Previous dimensions, used to check for change
    private float prev_width;
    private float prev_height;

    //Logic for adjusting position on change or resize of display
    private PositionLogic positionLogic = PositionLogic.NOTHING;

    public ComponentContainer parent;
    public Gui gui;


    public Component(String name)
    {
        this.name = name;
    }

    /**
     * Creates a new component
     *
     * @param width      - dimension in the x
     * @param height     - dimension in the y
     * @param background - render object to use for rendering the background of the component
     */
    public Component(String name, RenderRect background, float width, float height)
    {
        this.name = name;
        this.background = background;
        this.setSize(width, height);
    }

    /**
     * Creates a new component
     *
     * @param width   - dimension in the x
     * @param height  - dimension in the y
     * @param texture - path to texture file
     * @param x       - location x, relative to container
     * @param y       - location y, relative to container
     */
    public Component(String name, float width, float height, String texture, float x, float y)
    {
        this(name, width, height, new RenderRect(texture, Shader.CHAR), x, y);
    }

    /**
     * Creates a new component
     *
     * @param width      - dimension in the x
     * @param height     - dimension in the y
     * @param background - render object to use for rendering the background of the component
     * @param x          - location x, relative to container
     * @param y          - location y, relative to container
     */
    public Component(String name, float width, float height, RenderRect background, float x, float y)
    {
        this.name = name;
        this.background = background;
        this.setSize(width, height);
        this.setPosition(x, y);
    }

    /**
     * Sets the width and height of the component.
     * <p>
     * Will trigger a resize of the render object
     * if {@link #isVisible()} returns true
     * <p>
     * param width   - dimension in the x
     *
     * @param height - dimension in the y
     */
    public void setSize(float width, float height)
    {
        this.setWidth(width);
        this.setHeight(height);
        if (parent != null)
        {
            parent.onResize(this);
        }
    }

    /**
     * Sets the position of the component
     * <p>
     * Triggers position update on next tick
     *
     * @param x
     * @param y
     */
    public void setPosition(float x, float y)
    {
        this.x_location = x;
        this.y_location = y;
        hasChanged = true;
    }

    /**
     * Called each update tick by the display
     */
    public void update()
    {
        if (hasChanged)
        {
            hasChanged = false;
            onChanged();
        }
    }

    /**
     * Called when something about the
     * component has changed.
     * <p>
     * Use this to recalculate position
     * and size of the component
     */
    public void onChanged()
    {
        updatePosition();
    }

    /**
     * Render the GUI on the screen
     *
     * @param mouseX
     * @param mouseY
     */
    public void render(float mouseX, float mouseY)
    {
        render(mouseX, mouseY, 0, 0, 0);
    }

    /**
     * Render the GUI on the screen
     *
     * @param mouseX
     * @param mouseY
     */
    public void render(float mouseX, float mouseY, float xoffset, float yoffset, float zoffset)
    {
        if (isVisible())
        {
            if (background != null)
            {
                //Resize mesh or later init if mesh was not set
                if (prev_height != getHeight() || prev_width != getWidth() || background.mesh == null)
                {
                    background.setSize(getWidth(), getHeight());
                    prev_height = getHeight();
                    prev_width = getWidth();
                }
                background.render(x() + xoffset, y() + yoffset, zoffset, 0, 1);
            }
        }
    }

    /**
     * Called when the game display has resized
     */
    public void onResize()
    {
        onChanged();
    }

    /**
     * Called to update the position of the component on the display
     */
    public void updatePosition()
    {
        //it is assumed the starting point of a component is the bottom left
        if (getPositionLogic() != PositionLogic.NOTHING)
        {
            //Set left or right
            if (getPositionLogic().left())
            {
                x_location = display().cameraBoundLeft;
            }
            else if (getPositionLogic().right())
            {
                x_location = display().cameraBoundRight - getWidth();
            }

            //Set top or bottom
            if (getPositionLogic().top())
            {
                y_location = display().cameraBoundTop - getHeight();
            }
            else if (getPositionLogic().bottom())
            {
                y_location = display().cameraBoundBottom;
            }
        }
    }

    public void onClick(float mouseX, float mouseY, boolean left)
    {
        System.out.println("Click " + left + "  " + this);
    }

    public float x()
    {
        return x_location - (centerX ? (width / 2f) : 0);
    }

    public float y()
    {
        return y_location - (centerY ? (height / 2f) : 0);
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
        hasChanged = true;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
        hasChanged = true;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
        hasChanged = true;
    }

    public PositionLogic getPositionLogic()
    {
        return positionLogic;
    }

    public void setPositionLogic(PositionLogic positionLogic)
    {
        this.positionLogic = positionLogic;
        hasChanged = true;
    }

    public void setMouseOver(boolean mouseOver)
    {
        this.isMouseOver = mouseOver;
    }

    public Gui gui()
    {
        if (gui != null)
        {
            return gui;
        }
        if (parent != null)
        {
            return parent.gui();
        }
        return null;
    }

    public GameDisplay display()
    {
        return gui().display;
    }

    @Override
    public String toString()
    {
        return "Component[" + name + "  " + x() + "x  " + y() + "y]@" + hashCode();
    }
}
