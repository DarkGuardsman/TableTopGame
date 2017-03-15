package com.builtbroken.tabletop.game.entity.controller;

import com.builtbroken.tabletop.client.controls.KeyboardInput;
import com.builtbroken.tabletop.game.entity.living.Character;

import static org.lwjgl.glfw.GLFW.*;


/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Player extends Controller
{
    public final float speed = .1f;

    public boolean allowFreeMovement = false;

    public Player(Character entity)
    {
        super(entity);
    }

    @Override
    public void update(double delta)
    {
        if (allowMovement && entity != null)
        {
            if(allowFreeMovement)
            {
                if (KeyboardInput.isKeyDown(GLFW_KEY_W))
                {
                    entity.move(0, (float) (speed * delta));
                }
                else if (KeyboardInput.isKeyDown(GLFW_KEY_S))
                {
                    entity.move(0, -(float) (speed * delta));
                }
                else if (KeyboardInput.isKeyDown(GLFW_KEY_A))
                {
                    entity.move(-(float) (speed * delta), 0);
                }
                else if (KeyboardInput.isKeyDown(GLFW_KEY_D))
                {
                    entity.move((float) (speed * delta), 0);
                }
            }
        }
    }
}
