package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Mesh;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.textures.Texture;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.items.armor.Armor;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/22/2017.
 */
public class CharRender
{
    public static HashMap<Armor, RenderRect> renders = new HashMap();

    public static Texture sheet;
    public static RenderRect body;
    public static RenderRect head;

    public static void load()
    {
        sheet = Texture.get(GameDisplay.TEXTURE_PATH + "entity/body/body.png");

        float uvScale = 64f / sheet.width;
        body = new RenderRect(sheet, Shader.CHAR, Mesh.createMeshForSize(1, 1, GameDisplay.ENTITY_LAYER, Mesh.generateUV(0, 0, uvScale, uvScale)));
        head = new RenderRect(sheet, Shader.CHAR, Mesh.createMeshForSize(1, 1, GameDisplay.ENTITY_LAYER + 0.05f, Mesh.generateUV(uvScale, 0, uvScale, uvScale)));
    }

    public static void render(Entity entity, float x, float y, float scale)
    {
        render(entity, x, y, GameDisplay.TILE_LAYER, scale);
    }

    public static void render(Entity entity, float x, float y, float z, float scale)
    {
        render(entity, x, y, z, 0, scale);
    }

    public static void render(Entity entity, float x, float y, float z, float rot, float scale)
    {
        body.render(x, y, z, rot, scale);
        head.render(x, y, z, rot, scale);
    }
}
