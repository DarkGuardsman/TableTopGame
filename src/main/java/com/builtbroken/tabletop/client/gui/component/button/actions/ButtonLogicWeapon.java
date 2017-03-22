package com.builtbroken.tabletop.client.gui.component.button.actions;

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

    @Override
    protected void leftClick(float mouseX, float mouseY)
    {
        if (button.display().selectedEntity instanceof Character && ((Character) button.display().selectedEntity).usableWeapons.size() > itemIndex)
        {
            ((Character) button.display().selectedEntity).activeWeapon = ((Character) button.display().selectedEntity).usableWeapons.get(itemIndex);
        }
    }

    @Override
    public float getWidth()
    {
        if (((Character) button.display().selectedEntity).usableWeapons.size() > itemIndex)
        {
            return ((Character) button.display().selectedEntity).usableWeapons.get(itemIndex).width;
        }
        return -1;
    }

    @Override
    public float getHeight()
    {
        if (((Character) button.display().selectedEntity).usableWeapons.size() > itemIndex)
        {
            return ((Character) button.display().selectedEntity).usableWeapons.get(itemIndex).height;
        }
        return -1;
    }
}
