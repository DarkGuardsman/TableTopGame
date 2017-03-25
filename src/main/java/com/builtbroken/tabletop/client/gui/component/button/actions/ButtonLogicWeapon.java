package com.builtbroken.tabletop.client.gui.component.button.actions;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.render.ItemRender;
import com.builtbroken.tabletop.game.entity.living.EntityLiving;
import com.builtbroken.tabletop.game.items.Item;

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
        if (hadSelectedEntityLiving())
        {
            EntityLiving living = getSelectedEntityAsLiving();
            if (living.usableWeapons.size() > itemIndex)
            {
                living.activeWeapon = living.usableWeapons.get(itemIndex);
                living.heldItem = living.actionableItems.get(itemIndex);
            }
        }
    }

    @Override
    public float getWidth()
    {
        if (hadSelectedEntityLiving())
        {
            EntityLiving living = getSelectedEntityAsLiving();
            if (living.usableWeapons.size() > itemIndex)
            {
                return living.usableWeapons.get(itemIndex).width;
            }
        }
        return -1;
    }

    @Override
    public float getHeight()
    {
        if (hadSelectedEntityLiving())
        {
            EntityLiving living = getSelectedEntityAsLiving();
            if (living.usableWeapons.size() > itemIndex)
            {
                return living.usableWeapons.get(itemIndex).height;
            }
        }
        return -1;
    }

    @Override
    public void render(float mouseX, float mouseY, float x, float y, float z)
    {
        if (hadSelectedEntityLiving())
        {
            EntityLiving living = getSelectedEntityAsLiving();
            if (living.usableWeapons.size() > itemIndex)
            {
                Item item = living.usableWeapons.get(itemIndex);
                ItemRender.render(item, x, y, GameDisplay.GAME_GUI_LAYER + 0.1f, 1);
            }
        }
    }
}
