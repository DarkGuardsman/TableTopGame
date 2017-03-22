package com.builtbroken.tabletop.client.gui.component.button.actions;

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
    protected void leftClick(float mouseX, float mouseY)
    {
        button.display().currentSelectedEntityAction = actionID;
    }

    @Override
    public boolean isEnabled()
    {
        return actionID != null && button.display().selectedEntity != null && button.display().selectedEntity.actions.contains(actionID);
    }

    @Override
    public void render(float mouseX, float mouseY, float xoffset, float yoffset, float zoffset)
    {

    }
}
