package com.builtbroken.tabletop.client.gui.component.button.actions;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.component.button.Button;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/21/2017.
 */
public class ButtonLogicAction extends ButtonLogic
{
    public String actionID;

    public ButtonLogicAction(String actionID)
    {
        this.actionID = actionID;
    }

    @Override
    protected void leftClick(GameDisplay display, Button button, float mouseX, float mouseY)
    {
        display.currentSelectedEntityAction = actionID;
    }

    @Override
    public boolean isEnabled(GameDisplay display)
    {
        return actionID != null && display.selectedEntity != null && display.selectedEntity.actions.contains(actionID);
    }
}
