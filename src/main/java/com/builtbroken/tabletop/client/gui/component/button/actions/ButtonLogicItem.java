package com.builtbroken.tabletop.client.gui.component.button.actions;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.game.entity.living.Character;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/21/2017.
 */
public class ButtonLogicItem extends ButtonLogic
{
    int itemIndex = 0;

    public ButtonLogicItem(int itemIndex)
    {
        this.itemIndex = itemIndex;
    }

    @Override
    protected void leftClick(GameDisplay display, Button button, float mouseX, float mouseY)
    {
        if (display.selectedEntity instanceof Character && ((Character) display.selectedEntity).actionableItems.size() > itemIndex)
        {
            ((Character) display.selectedEntity).activeItem = ((Character) display.selectedEntity).actionableItems.get(itemIndex);
        }
    }

    @Override
    public float getWidth(GameDisplay display)
    {
        if (((Character) display.selectedEntity).actionableItems.size() > itemIndex)
        {
            return ((Character) display.selectedEntity).actionableItems.get(itemIndex).width;
        }
        return -1;
    }

    @Override
    public float getHeight(GameDisplay display)
    {
        if (((Character) display.selectedEntity).actionableItems.size() > itemIndex)
        {
            return ((Character) display.selectedEntity).actionableItems.get(itemIndex).height;
        }
        return -1;
    }
}
