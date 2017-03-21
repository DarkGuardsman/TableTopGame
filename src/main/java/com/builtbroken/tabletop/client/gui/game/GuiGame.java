package com.builtbroken.tabletop.client.gui.game;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.PositionLogic;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.client.gui.component.button.ButtonScrollRow;
import com.builtbroken.tabletop.client.gui.component.container.ComponentGrid;
import com.builtbroken.tabletop.client.graphics.render.RenderRect;

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
    ComponentGrid enemyUnitRow;
    ComponentGrid abilityRow;

    //Render objects, cached for reuse
    RenderRect itemButtonBackground;
    RenderRect weaponButtonBackground;
    RenderRect enemyButtonBackground;
    RenderRect abilityButtonBackground;

    public GuiGame(GameDisplay display)
    {
        this.itemButtonBackground = new RenderRect("resources/textures/gui/button.icon.png", Shader.CHAR, 1, 1, GameDisplay.GAME_GUI_LAYER);
        this.weaponButtonBackground = this.itemButtonBackground;
        this.enemyButtonBackground = new RenderRect("resources/textures/icons/enemy/icon.enemy.1.png", Shader.CHAR, 0.5f, 0.5f, GameDisplay.GAME_GUI_LAYER);
        this.abilityButtonBackground = new RenderRect("resources/textures/icons/abilities/icon.ability.png", Shader.CHAR, 0.5f, 0.5f, GameDisplay.GAME_GUI_LAYER);
        //icon.ability
        init(display);
    }

    public void init(GameDisplay display)
    {
        //Clear in case this is not the first call
        componentList.clear();

        //Build item row, float to left of screen
        itemRow = new ButtonScrollRow("itemRow", PositionLogic.LEFT, itemButtonBackground, 4, 1, 1); //set sub comp pos
        itemRow.setVisible(false); //no need to render
        itemRow.setPositionLogic(PositionLogic.BOTTOM_LEFT); //set self pos


        //Build weapon row, float to right of screen
        weaponRow = new ButtonScrollRow("weaponRow", PositionLogic.RIGHT, itemButtonBackground, 4, 1, 1); //set sub comp pos
        weaponRow.setVisible(false); //no need to render
        weaponRow.setPositionLogic(PositionLogic.BOTTOM_RIGHT); //set self pos

        //Build unit row
        unitRow = new ButtonScrollRow("unitRow", PositionLogic.BOTTOM, itemButtonBackground, 4, 1, 1);
        unitRow.setVisible(false);
        unitRow.setPositionLogic(PositionLogic.TOP_LEFT);

        //Enemy units
        enemyUnitRow = new ComponentGrid("enemyRow", 4, 12);
        enemyUnitRow.setVisible(false);
        enemyUnitRow.setPositionLogic(PositionLogic.TOP_RIGHT);

        for (int i = 0; i < 32; i++)
        {
            enemyUnitRow.add(new Button("e" + i, enemyButtonBackground, 0.5f, 0.5f));
        }

        abilityRow = new ComponentGrid("abilityGrid", 9, 3);
        abilityRow.centerX = true;
        abilityRow.background = new RenderRect("resources/textures/gui/button.icon.png", Shader.CHAR, 1, 1, GameDisplay.GAME_GUI_LAYER - 0.1f);
        //abilityRow.setVisible(false);
        abilityRow.setPositionLogic(PositionLogic.BOTTOM);
        for (int i = 0; i < 9 * 3; i++)
        {
            abilityRow.add(new Button("a" + i, abilityButtonBackground, 0.5f, 0.5f));
        }

        //Add containers
        add(itemRow);
        add(weaponRow);
        add(unitRow);
        add(enemyUnitRow);
        add(abilityRow);

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
