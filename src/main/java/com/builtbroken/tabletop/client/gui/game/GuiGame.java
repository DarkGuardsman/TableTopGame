package com.builtbroken.tabletop.client.gui.game;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.button.Button;
import com.builtbroken.tabletop.client.gui.component.container.ComponentRow;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/18/2017.
 */
public class GuiGame extends Gui
{
    ComponentRow row;

    public GuiGame(GameDisplay display)
    {
        row = new ComponentRow();
        row.setVisible(false);
        row.setPosition(display.cameraBoundLeft, display.cameraBoundBottom);

        row.add(new Button("resources/textures/gui/button.png", 1, 1));
        row.add(new Button("resources/textures/gui/button.png", 2, 1));
        row.add(new Button("resources/textures/gui/button.png", 3, 1));
        row.add(new Button("resources/textures/gui/button.png", 4, 1));

        add(row);
    }

    @Override
    public void onResize(GameDisplay display)
    {
        row.setPosition(display.cameraBoundLeft, display.cameraBoundBottom); //TODO create prefab code to float left
        super.onResize(display);
    }
}
