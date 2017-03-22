package com.builtbroken.tabletop.client.gui.component.button.actions;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.game.entity.living.Character;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/21/2017.
 */
public class ButtonLogicWeapon extends ButtonLogic
{
    int itemIndex = 0;

    public ButtonLogicWeapon(int itemIndex)
    {
        this.itemIndex = itemIndex;
    }

    protected void leftClick(GameDisplay display, Button button, float mouseX, float mouseY)
    {
        if (display.selectedEntity instanceof Character && ((Character) display.selectedEntity).usableWeapons.size() > itemIndex)
        {
            ((Character) display.selectedEntity).activeWeapon = ((Character) display.selectedEntity).usableWeapons.get(itemIndex);
        }
    }
}
