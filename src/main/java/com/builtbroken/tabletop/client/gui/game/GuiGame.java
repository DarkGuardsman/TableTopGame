package com.builtbroken.tabletop.client.gui.game;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.PositionLogic;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.client.gui.component.button.ButtonScrollRow;
import com.builtbroken.tabletop.client.gui.component.container.ComponentRow;
import com.builtbroken.tabletop.client.render.RenderRect;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class GuiGame extends Gui
{
    //GUI parts
    ButtonScrollRow itemRow;
    ButtonScrollRow weaponRow;
    ButtonScrollRow unitRow;

    //Render objects, cached for reuse
    RenderRect itemButtonBackground;
    RenderRect weaponButtonBackground;
    RenderRect enemyButtonBackground;

    public GuiGame(GameDisplay display)
    {
        this.itemButtonBackground = new RenderRect("resources/textures/gui/button.icon.png", Shader.CHAR, 1, 1, GameDisplay.GAME_GUI_LAYER);
        this.weaponButtonBackground = this.itemButtonBackground;
        this.enemyButtonBackground = new RenderRect("resources/textures/gui/button.icon.png", Shader.CHAR, 0.4f, 0.4f, GameDisplay.GAME_GUI_LAYER);
        init(display);
    }

    public void init(GameDisplay display)
    {
        //Clear in case this is not the first call
        componentList.clear();

        //Build item row, float to left of screen
        itemRow = new ButtonScrollRow(PositionLogic.LEFT, itemButtonBackground, 4, 1, 1); //set sub comp pos
        itemRow.setVisible(false); //no need to render
        itemRow.setPositionLogic(PositionLogic.BOTTOM_LEFT); //set self pos


        //Build weapon row, float to right of screen
        weaponRow = new ButtonScrollRow(PositionLogic.RIGHT, itemButtonBackground, 4, 1, 1); //set sub comp pos
        weaponRow.setVisible(false); //no need to render
        weaponRow.setPositionLogic(PositionLogic.BOTTOM_RIGHT); //set self pos

        //Build unit row
        unitRow = new ButtonScrollRow(PositionLogic.BOTTOM, itemButtonBackground, 4, 1, 1);
        unitRow.setVisible(false);
        unitRow.setPositionLogic(PositionLogic.TOP_LEFT);

        ComponentRow unitRow2 = new ComponentRow(PositionLogic.TOP);
        unitRow2.setVisible(false);
        unitRow2.setPositionLogic(PositionLogic.TOP_RIGHT);

        for (int i = 0; i < 20; i++)
        {
            unitRow2.add(new Button(enemyButtonBackground, 0.4f, 0.4f));
        }

        //Add containers
        add(itemRow);
        add(weaponRow);
        add(unitRow);
        add(unitRow2);

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
