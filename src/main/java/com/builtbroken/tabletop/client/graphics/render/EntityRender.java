package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Mesh;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.textures.Texture;
import com.builtbroken.tabletop.client.graphics.textures.TextureLoader;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.living.EntityLiving;
import com.builtbroken.tabletop.game.items.Item;
import com.builtbroken.tabletop.game.items.ItemState;
import com.builtbroken.tabletop.game.items.Items;
import com.builtbroken.tabletop.game.items.armor.Armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/22/2017.
 */
public class EntityRender
{
    public static HashMap<Armor, RenderRect> renders = new HashMap();
    public static HashMap<String, Texture> armorSetTexture = new HashMap();

    public static Texture sheet;
    public static RenderRect body;
    public static RenderRect head;

    public static void render(Entity entity, float x, float y, float scale)
    {
        render(entity, x, y, GameDisplay.TILE_LAYER, scale);
    }

    public static void render(Entity entity, float x, float y, float z, float scale)
    {
        render(entity, x, y, z, entity.getRotation(), scale);
    }

    public static void render(Entity entity, float x, float y, float z, float rot, float scale)
    {
        if (entity instanceof EntityLiving)
        {
            EntityLiving living = (EntityLiving) entity;

            List<Armor.ArmorSlot> renderedSlots = new ArrayList();

            if (living.getArmor().hasArmor(Armor.ArmorSlot.SUIT))
            {
                //TODO implement by cycling through suit armor pieces
            }
            else
            {
                for (Armor.ArmorSlot slot : Armor.ArmorSlot.values())
                {
                    if (slot != Armor.ArmorSlot.SUIT && living.getArmor().hasArmor(slot))
                    {
                        ItemState state = living.getArmor().get(slot);
                        Item item = state.item;
                        if (item instanceof Armor)
                        {
                            RenderRect rect = renders.get((Armor) item);
                            if (rect != null)
                            {
                                rect.render(x, y, z, rot, scale);
                                renderedSlots.add(slot);
                            }
                        }
                    }
                }
            }

            //Backup renders / no armor renders
            if (!renderedSlots.contains(Armor.ArmorSlot.SUIT))
            {
                if (!renderedSlots.contains(Armor.ArmorSlot.CHEST))
                {
                    body.render(x, y, z, rot, scale);
                }
                if (!renderedSlots.contains(Armor.ArmorSlot.HEAD))
                {
                    head.render(x, y, z, rot, scale);
                }
            }
        }
        else
        {
            head.render(x, y, z, rot, scale);
        }
    }

    public static void load()
    {
        sheet = TextureLoader.get(GameDisplay.TEXTURE_PATH + "entity/body/body.png");

        float uvScale = 64f / sheet.width;
        body = new RenderRect(sheet, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, 1, GameDisplay.ENTITY_LAYER, Mesh.generateUV(0, 0, uvScale, uvScale)));
        head = new RenderRect(sheet, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, 1, GameDisplay.ENTITY_LAYER + 0.05f, Mesh.generateUV(uvScale, 0, uvScale, uvScale)));

        for (Map.Entry<String, List<Armor>> armorSet : Items.armorSets.entrySet())
        {
            Texture texture = TextureLoader.get(GameDisplay.TEXTURE_PATH + "entity/armor/" + armorSet.getKey() + ".png");
            uvScale = 64f / texture.width;

            for (Armor armor : armorSet.getValue())
            {
                if (armor.slotType == Armor.ArmorSlot.CHEST)
                {
                    renders.put(armor, new RenderRect(texture, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, 1, GameDisplay.ENTITY_LAYER, Mesh.generateUV(0, 0, uvScale, uvScale))));
                }
                else if (armor.slotType == Armor.ArmorSlot.HEAD)
                {
                    renders.put(armor, new RenderRect(texture, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, 1, GameDisplay.ENTITY_LAYER + 0.05f, Mesh.generateUV(uvScale, 0, uvScale, uvScale))));
                }
            }
        }
    }

    public static void dispose()
    {
        body.dispose();
        head.dispose();

        for (RenderRect renderRect : renders.values())
        {
            if (renderRect != null)
            {
                renderRect.dispose();
            }
        }
        renders.clear();
    }
}
