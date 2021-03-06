package com.builtbroken.tabletop.game.entity.ai;

import com.builtbroken.tabletop.game.entity.controller.Controller;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class AI extends Controller
{
    public AI(String id, String displayName)
    {
        super("ai." + id, displayName);
    }
}
