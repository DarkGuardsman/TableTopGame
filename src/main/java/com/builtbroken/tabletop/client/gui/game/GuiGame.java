package com.builtbroken.tabletop.client.gui.game;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.PositionLogic;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.client.gui.component.container.ComponentRow;
import com.builtbroken.tabletop.client.render.RenderRect;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class GuiGame extends Gui
{
    //GUI parts
    ComponentRow itemRow;
    ComponentRow weaponRow;
    ComponentRow unitRow;

    //Render objects, cached for reuse
    RenderRect itemButtonBackground;
    RenderRect weaponButtonBackground;

    public GuiGame(GameDisplay display)
    {
        this.itemButtonBackground = new RenderRect("resources/textures/gui/button.icon.png", Shader.CHAR, 1, 1, GameDisplay.GAME_GUI_LAYER);
        this.weaponButtonBackground = this.itemButtonBackground;
        init(display);
    }

    public void init(GameDisplay display)
    {
        //Clear in case this is not the first call
        componentList.clear();

        //Build item row, float to left of screen
        itemRow = new ComponentRow(PositionLogic.LEFT); //set sub comp pos
        itemRow.setVisible(false); //no need to render
        itemRow.setPositionLogic(PositionLogic.LEFT); //set self pos

        itemRow.add(new Button(itemButtonBackground, 1, 1));
        itemRow.add(new Button(itemButtonBackground, 1, 1));
        itemRow.add(new Button(itemButtonBackground, 1, 1));
        itemRow.add(new Button(itemButtonBackground, 1, 1));


        //Build weapon row, float to right of screen
        weaponRow = new ComponentRow(PositionLogic.RIGHT); //set sub comp pos
        weaponRow.setVisible(false); //no need to render
        weaponRow.setPositionLogic(PositionLogic.RIGHT); //set self pos

        weaponRow.add(new Button(weaponButtonBackground, 1, 1));
        weaponRow.add(new Button(weaponButtonBackground, 1, 1));
        weaponRow.add(new Button(weaponButtonBackground, 1, 1));
        weaponRow.add(new Button(weaponButtonBackground, 1, 1));

        unitRow = new ComponentRow(PositionLogic.BOTTOM);
        unitRow.setVisible(false);
        unitRow.setPositionLogic(PositionLogic.TOP_LEFT);

        unitRow.add(new Button(weaponButtonBackground, 1, 1));
        unitRow.add(new Button(weaponButtonBackground, 1, 1));
        unitRow.add(new Button(weaponButtonBackground, 1, 1));
        unitRow.add(new Button(weaponButtonBackground, 1, 1));

        //Add containers
        add(itemRow);
        add(weaponRow);
        add(unitRow);

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
