package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.mesh.Mesh;
import com.builtbroken.tabletop.client.graphics.textures.Texture;
import com.builtbroken.tabletop.client.graphics.textures.TextureLoader;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.living.EntityLiving;
import com.builtbroken.tabletop.game.items.Item;
import com.builtbroken.tabletop.game.items.ItemState;
import com.builtbroken.tabletop.game.items.Items;
import com.builtbroken.tabletop.game.items.armor.Armor;

import java.io.File;
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
    public static HashMap<Armor, RenderRect> armorRenders = new HashMap();
    public static HashMap<Item, RenderRect> heldItemRenders = new HashMap();

    public static HashMap<String, Texture> armorSetTexture = new HashMap();

    public static Texture sheet;
    public static RenderRect body;
    public static RenderRect head;

    public static void render(Entity entity, float x, float y, float z, float scale)
    {
        render(entity, x, y, z, entity.getRotation(), scale);
    }

    public static void render(Entity entity, float xf, float yf, float z, float rot, float scale)
    {
        float x = xf + (scale / 2f);
        float y = yf + (scale / 2f);
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
                            RenderRect rect = armorRenders.get(item);
                            if (rect != null)
                            {
                                rect.render(x, y, z, rot, scale);
                                renderedSlots.add(slot);
                            }
                        }
                    }
                }
            }

            //Render held item
            if (living.heldItem != null && heldItemRenders.containsKey(living.heldItem))
            {
                RenderRect rect = heldItemRenders.get(living.heldItem);
                if (rect != null)
                {
                    rect.render(x, y + 1, z, rot, scale);
                    //TODO do render over shoulder or under shoulder check to improve visuals
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
        body = new RenderRect(sheet, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, GameDisplay.ENTITY_LAYER, Mesh.generateUV(0, 0, uvScale, uvScale)));
        head = new RenderRect(sheet, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, GameDisplay.ENTITY_LAYER + 0.05f, Mesh.generateUV(uvScale, 0, uvScale, uvScale)));

        for (Map.Entry<String, List<Armor>> armorSet : Items.armorSets.entrySet())
        {
            Texture texture = TextureLoader.get(GameDisplay.TEXTURE_PATH + "entity/armor/" + armorSet.getKey() + ".png");
            uvScale = 64f / texture.width;

            for (Armor armor : armorSet.getValue())
            {
                if (armor.slotType == Armor.ArmorSlot.CHEST)
                {
                    armorRenders.put(armor, new RenderRect(texture, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, GameDisplay.ENTITY_LAYER, Mesh.generateUV(0, 0, uvScale, uvScale))));
                }
                else if (armor.slotType == Armor.ArmorSlot.HEAD)
                {
                    armorRenders.put(armor, new RenderRect(texture, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(1, GameDisplay.ENTITY_LAYER + 0.05f, Mesh.generateUV(uvScale, 0, uvScale, uvScale))));
                }
            }
        }

        for (Item item : Items.ITEMS)
        {
            if (item != null && item.canBeHeld())
            {
                final String path = GameDisplay.TEXTURE_PATH + "entity/items/" + item.uniqueID + ".png";
                final File file = new File(path);
                if (file.exists())
                {
                    Texture texture = TextureLoader.get(path);
                    uvScale = texture.width / 32f;
                    heldItemRenders.put(item, new RenderRect(texture, Shader.GLOBAL_SHADER, Mesh.createMeshForSize(0.5f, GameDisplay.ENTITY_LAYER + 0.05f, Mesh.generateUV(0, 0, uvScale, uvScale))));
                    //TODO load weapon data >> hold animation, shoulder pos, rotation origin, firing animation, weapon effects to use
                }
                else
                {
                    System.err.println("No item texture for " + file);
                }
            }
        }
    }

    public static void dispose()
    {
        body.dispose();
        head.dispose();

        for (RenderRect renderRect : armorRenders.values())
        {
            if (renderRect != null)
            {
                renderRect.dispose();
            }
        }
        armorRenders.clear();

        for (RenderRect renderRect : heldItemRenders.values())
        {
            if (renderRect != null)
            {
                renderRect.dispose();
            }
        }
        heldItemRenders.clear();
    }
}
