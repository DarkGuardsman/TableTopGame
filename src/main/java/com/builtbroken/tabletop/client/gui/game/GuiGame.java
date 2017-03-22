package com.builtbroken.tabletop.client.gui.game;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.render.RenderRect;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.PositionLogic;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.client.gui.component.button.ButtonScrollRow;
import com.builtbroken.tabletop.client.gui.component.button.actions.ButtonLogicAction;
import com.builtbroken.tabletop.client.gui.component.button.actions.ButtonLogicItem;
import com.builtbroken.tabletop.client.gui.component.button.actions.ButtonLogicWeapon;
import com.builtbroken.tabletop.client.gui.component.container.ComponentGrid;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.living.Character;

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
        super(display);
        this.itemButtonBackground = new RenderRect("resources/textures/gui/button.icon.png", Shader.CHAR, 1, 1, GameDisplay.GAME_GUI_LAYER);
        this.weaponButtonBackground = this.itemButtonBackground;
        this.enemyButtonBackground = new RenderRect("resources/textures/icons/enemy/icon.enemy.1.png", Shader.CHAR, 0.5f, 0.5f, GameDisplay.GAME_GUI_LAYER);
        this.abilityButtonBackground = new RenderRect("resources/textures/icons/abilities/icon.png", Shader.CHAR, 0.5f, 0.5f, GameDisplay.GAME_GUI_LAYER);
        //icon.ability
        init();
    }

    public void init()
    {
        //Clear in case this is not the first call
        componentList.clear();

        //Build item row, float to left of screen
        itemRow = new ButtonScrollRow("itemRow", PositionLogic.LEFT, itemButtonBackground, 4, 1, 1); //set sub comp pos
        itemRow.setVisible(false); //no need to render
        itemRow.setPositionLogic(PositionLogic.BOTTOM_LEFT); //set self pos


        //Build weapon row, float to right of screen
        weaponRow = new ButtonScrollRow("weaponRow", PositionLogic.RIGHT, weaponButtonBackground, 4, 1, 1); //set sub comp pos
        weaponRow.setVisible(false); //no need to render
        weaponRow.setPositionLogic(PositionLogic.BOTTOM_RIGHT); //set self pos

        //Build unit row
        unitRow = new ButtonScrollRow("unitRow", PositionLogic.BOTTOM, weaponButtonBackground, 4, 1, 1);
        unitRow.setVisible(false);
        unitRow.setPositionLogic(PositionLogic.TOP_LEFT);

        //Enemy units
        enemyUnitRow = new ComponentGrid("enemyRow", 12, 4);
        enemyUnitRow.setVisible(false);
        enemyUnitRow.setPositionLogic(PositionLogic.TOP_RIGHT);

        abilityRow = new ComponentGrid("abilityGrid", 3, 9);
        abilityRow.centerX = true;
        abilityRow.background = new RenderRect("resources/textures/gui/button.icon.png", Shader.CHAR, 1, 1, GameDisplay.GAME_GUI_LAYER - 0.1f);
        //abilityRow.setVisible(false);
        abilityRow.setPositionLogic(PositionLogic.BOTTOM);

        //Add containers
        add(itemRow);
        add(weaponRow);
        add(unitRow);
        add(enemyUnitRow);
        add(abilityRow);

        //update all
        for (Component component : componentList)
        {
            component.updatePosition();
        }
        onEntitySelectionChange(display, null);
    }

    @Override
    protected void doUpdate(float mouseX, float mouseY)
    {

    }

    @Override
    public void onEntitySelectionChange(GameDisplay display, Entity entity)
    {
        abilityRow.clear();
        if (entity instanceof Character)
        {
            Character character = (Character) entity;
            weaponRow.setButtons(Math.max(1, character.usableWeapons.size()));
            for (int i = 0; i < character.usableWeapons.size(); i++)
            {
                ((Button) weaponRow.componentList.get(i + 1)).logic = new ButtonLogicWeapon(i);
            }
            itemRow.setButtons(Math.max(1, character.actionableItems.size()));
            for (int i = 0; i < character.actionableItems.size(); i++)
            {
                ((Button) itemRow.componentList.get(i + 1)).logic = new ButtonLogicItem(i);
            }
        }
        else
        {
            weaponRow.setVisible(false);
            itemRow.setVisible(false);
        }
        if (entity != null && entity.actions.size() > 0)
        {
            for (String actionID : entity.actions)
            {
                abilityRow.add(new Button(actionID, abilityButtonBackground, 0.5f, 0.5f).setLogic(new ButtonLogicAction(actionID)));
            }
        }
        else
        {
            abilityRow.setVisible(false);
        }
    }


    @Override
    public void onResize()
    {
        super.onResize();
    }
}
