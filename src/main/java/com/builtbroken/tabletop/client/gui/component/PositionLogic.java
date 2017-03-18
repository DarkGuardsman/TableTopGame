package com.builtbroken.tabletop.client.gui.component;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public enum PositionLogic
{
    NOTHING,
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT;

    public boolean left()
    {
        return this == LEFT || this == TOP_LEFT || this == BOTTOM_LEFT;
    }

    public boolean right()
    {
        return this == RIGHT || this == TOP_RIGHT || this == BOTTOM_RIGHT;
    }

    public boolean top()
    {
        return this == TOP || this == TOP_RIGHT || this == TOP_LEFT;
    }

    public boolean bottom()
    {
        return this == BOTTOM || this == BOTTOM_RIGHT || this == BOTTOM_LEFT;
    }
}
