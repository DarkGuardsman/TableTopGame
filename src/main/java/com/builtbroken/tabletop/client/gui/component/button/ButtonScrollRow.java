package com.builtbroken.tabletop.client.gui.component.button;

import com.builtbroken.tabletop.client.gui.component.container.ComponentRow;
import com.builtbroken.tabletop.client.render.RenderRect;

/**
 * Row of buttons that has a button on either side to scroll through the list
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class ButtonScrollRow extends ComponentRow
{
    public static RenderRect upArrow;
    public static RenderRect downArrow;
    public static RenderRect leftArrow;
    public static RenderRect rightArrow;

    public Button increaseButton, decreaseButton;

    public ButtonScrollRow()
    {

    }
}
