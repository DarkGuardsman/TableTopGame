package com.builtbroken.tabletop.client.gui.game;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.PositionLogic;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.client.gui.component.container.ComponentRow;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class GuiGame extends Gui
{
    ComponentRow itemRow;
    ComponentRow weaponRow;

    public GuiGame(GameDisplay display)
    {
        init(display);
    }

    public void init(GameDisplay display)
    {
        //Clear in case this is not the first call
        componentList.clear();

        //Build item row
        itemRow = new ComponentRow();
        itemRow.setVisible(false);
        itemRow.setPositionLogic(PositionLogic.FLOAT_LEFT);
        itemRow.componentPositionLogic = PositionLogic.FLOAT_LEFT;

        itemRow.add(new Button("resources/textures/gui/button.icon.png", 1, 1));
        itemRow.add(new Button("resources/textures/gui/button.icon.png", 1, 1));
        itemRow.add(new Button("resources/textures/gui/button.icon.png", 1, 1));


        //Build weapon row
        weaponRow = new ComponentRow();
        weaponRow.setVisible(false);
        weaponRow.setPositionLogic(PositionLogic.FLOAT_RIGHT);
        weaponRow.componentPositionLogic = PositionLogic.FLOAT_RIGHT;

        weaponRow.add(new Button("resources/textures/gui/button.icon.png", 1, 1));
        weaponRow.add(new Button("resources/textures/gui/button.icon.png", 1, 1));
        weaponRow.add(new Button("resources/textures/gui/button.icon.png", 1, 1));

        //Add containers
        add(itemRow);
        add(weaponRow);

        //update all
        for (Component component : componentList)
        {
            component.updatePosition(display);
        }
    }


    @Override
    public void onResize(GameDisplay display)
    {
        super.onResize(display);
    }
}
