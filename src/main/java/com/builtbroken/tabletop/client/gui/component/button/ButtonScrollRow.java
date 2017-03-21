package com.builtbroken.tabletop.client.gui.component.button;

import com.builtbroken.tabletop.client.graphics.render.RenderRect;
import com.builtbroken.tabletop.client.gui.component.PositionLogic;
import com.builtbroken.tabletop.client.gui.component.container.ComponentRow;

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

    public RenderRect buttonBackground;
    float buttonWidth;
    float buttonHeight;

    public Button arrowButtonA, arrowButtonB;
    public Button[] buttonEntries;

    public float arrowButtonSize = 0.2f;

    public ButtonScrollRow(String name, PositionLogic logic, RenderRect buttonBackground, int buttons, float buttonWidth, float buttonHeight)
    {
        super(name, logic);
        this.buttonBackground = buttonBackground;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        if (logic.left() || logic.right())
        {
            arrowButtonA = add(new Button("leftArrow", leftArrow, arrowButtonSize, buttonHeight));
            setButtons(buttons);
            arrowButtonB = add(new Button("rightArrow", rightArrow, arrowButtonSize, buttonHeight));
        }
        else if (logic.top() || logic.bottom())
        {
            arrowButtonA = add(new Button("downArrow", downArrow, buttonWidth, arrowButtonSize));
            setButtons(buttons);
            arrowButtonB = add(new Button("upArrow", upArrow, buttonWidth, arrowButtonSize));
        }
        else
        {
            throw new IllegalArgumentException("ButtonScrollRow: position logic value " + logic + " is invalid, position logic can only be set to left, right, top, or bottom.");
        }
    }

    public void setButtons(int buttons)
    {
        boolean reset = false;

        //Clear if we already had buttons
        if (buttonEntries != null)
        {
            reset = true;
            componentList.clear();
        }
        //If reset add button back
        if (reset)
        {
            add(arrowButtonA);
        }
        buttonEntries = new Button[buttons];
        //Init buttons
        for (int i = 0; i < buttons; i++)
        {
            buttonEntries[i] = add(new Button("b" + i, buttonBackground, buttonWidth, buttonHeight));
        }
        //If reset add button back
        if (reset)
        {
            add(arrowButtonB);
            calculateSize();
        }
    }

    @Override
    public String toString()
    {
        return "ButtonScrollRow[" + name + "  " + x() + "x  " + y() + "y]";
    }
}
