package com.builtbroken.tabletop.client;


import com.builtbroken.jlib.math.dice.Dice;
import com.builtbroken.tabletop.client.controls.KeyboardInput;
import com.builtbroken.tabletop.client.controls.MouseInput;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.ComponentContainer;
import com.builtbroken.tabletop.client.render.RenderRect;
import com.builtbroken.tabletop.client.tile.TileRenders;
import com.builtbroken.tabletop.game.Game;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.damage.Damage;
import com.builtbroken.tabletop.game.entity.damage.DamageType;
import com.builtbroken.tabletop.game.entity.living.Character;
import com.builtbroken.tabletop.game.items.weapons.Weapon;
import com.builtbroken.tabletop.game.map.examples.StaticMapData;
import com.builtbroken.tabletop.game.tile.Tile;
import com.builtbroken.tabletop.game.tile.Tiles;
import com.builtbroken.tabletop.util.Matrix4f;
import com.builtbroken.tabletop.util.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class GameDisplay implements Runnable
{
    //Display run data
    protected Thread thread;
    protected boolean running = false;

    //Cemera bounds
    public static final float LEFT_CAMERA_BOUND = -10.0f;
    public static final float RIGHT_CAMERA_BOUND = 10.0f;
    public static final float BOTTOM_CAMERA_BOUND = -10.0f * 9.0f / 16.0f;
    public static final float TOP_CAMERA_BOUND = 10.0f * 9.0f / 16.0f;

    //Calculates the size of the screen 2D
    public static final float SCREEN_SIZE_X = RIGHT_CAMERA_BOUND - LEFT_CAMERA_BOUND;
    public static final float SCREEN_SIZE_Y = TOP_CAMERA_BOUND - BOTTOM_CAMERA_BOUND;

    //Render layers
    public static final float TILE_LAYER = 0f;
    public static final float TILE_LAYER_2 = .1f;
    public static final float ENTITY_LAYER = .2f;
    public static final float ENTITY_LAYER_2 = .3f;
    public static final float SELECTION_LAYER = 0.4f;
    public static final float EFFECT_LAYER = .5f;
    public static final float TILE_LAYER_3 = .6f;
    public static final float GAME_GUI_LAYER = .7f;
    public static final float MENU_GUI_LAYER = .8f;

    //Game logic data
    protected Game game;
    protected Entity selectedEntity;

    //GUIS
    protected HashMap<String, Gui> guiMap = new HashMap();

    //Render logic data
    protected float zoom = 1;

    protected int width = 1280;
    protected int height = 720;

    protected long windowID;

    //Renders
    protected RenderRect background_render;
    protected RenderRect character_render;
    protected RenderRect target_render;
    protected RenderRect box_render;

    protected Vector3f cameraPosition = new Vector3f(0, 0, 0);
    protected Matrix4f pr_matrix;

    //Controls
    protected long lastZoom = 0L;
    protected boolean clickLeft = false;
    protected boolean clickRight = false;
    protected boolean attackMode = false;

    //Main method
    public static void main(String... args)
    {
        Game game = new Game();
        game.load(false, "");

        Tiles.load();

        StaticMapData map = new StaticMapData();
        map.load();
        game.getWorld().setMapData(map);


        GameDisplay display = new GameDisplay(game);
        display.start();
    }

    public GameDisplay(Game game)
    {
        this.game = game;
    }

    public void start()
    {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    protected void init()
    {
        if (!glfwInit())
        {
            System.err.println("Could not initialize GLFW!");
            return;
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        windowID = glfwCreateWindow(width, height, "Project TableTopGame", NULL, NULL);
        if (windowID == NULL)
        {
            System.err.println("Could not create GLFW window!");
            return;
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowID, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        initControls();

        glfwMakeContextCurrent(windowID);
        glfwShowWindow(windowID);
        GL.createCapabilities();


        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        Shader.loadAll();

        pr_matrix = Matrix4f.orthographic(LEFT_CAMERA_BOUND, RIGHT_CAMERA_BOUND, BOTTOM_CAMERA_BOUND, TOP_CAMERA_BOUND, -1.0f, 1.0f);
        Shader.BACKGROUND.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BACKGROUND.setUniform1i("tex", 1);

        Shader.CHAR.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.CHAR.setUniform1i("tex", 1);

        game.getWorld().getEntities().add(new Character("bob"));
        game.getWorld().getEntities().add(new Character("jim"));
        game.getWorld().getEntities().add(new Character("paul"));
        game.getWorld().getEntities().add(new Character("tim"));

        background_render = new RenderRect("resources/textures/background.png", Shader.CHAR, 20, 20, -0.98f);
        character_render = new RenderRect("resources/textures/char.png", Shader.CHAR, 1, 1, EFFECT_LAYER);
        target_render = new RenderRect("resources/textures/target.png", Shader.CHAR, 1, 1, SELECTION_LAYER);
        box_render = new RenderRect("resources/textures/box.png", Shader.CHAR, 1, 1, SELECTION_LAYER);
        TileRenders.load();

        //camera is 20x 11.5y
        Gui gameGUI = new Gui();
        ComponentContainer container = new ComponentContainer(SCREEN_SIZE_X, SCREEN_SIZE_Y * .1f, "resources/textures/gui/buttonContainer.png", new Vector3f(LEFT_CAMERA_BOUND, BOTTOM_CAMERA_BOUND, 0));
        container.add(new Component(SCREEN_SIZE_X * 0.2f, SCREEN_SIZE_X * 0.04f, "resources/textures/gui/button.png", new Vector3f(LEFT_CAMERA_BOUND, BOTTOM_CAMERA_BOUND + 0.01f, .01f)));
        gameGUI.add(container);
        guiMap.put("game", gameGUI);
    }

    //Loads callbacks for keyboard, mouse, and other input devices
    protected void initControls()
    {
        glfwSetKeyCallback(windowID, new KeyboardInput());
        MouseInput.init(windowID);
    }

    @Override
    public void run()
    {
        init();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (running)
        {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0)
            {
                update(delta);
                updates++;
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                //System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            if (glfwWindowShouldClose(windowID))
            {
                running = false;
            }
        }

        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

    protected void update(double delta)
    {
        long time = System.currentTimeMillis();
        glfwPollEvents();
        for (Entity entity : game.getWorld().getEntities())
        {
            entity.update(delta);
        }

        if (time - lastZoom > 500)
        {
            if (KeyboardInput.zoomOut())
            {
                if (zoom > 1f)
                {
                    zoom = 1f;
                }
                else
                {
                    zoom = .5f;
                }
                lastZoom = time;
            }
            else if (KeyboardInput.zoomIn())
            {
                if (zoom < 1f)
                {
                    zoom = 1f;
                }
                else
                {
                    zoom = 2f;
                }
                lastZoom = time;
            }
        }

        if (KeyboardInput.left())
        {
            cameraPosition.x -= .1;
        }
        else if (KeyboardInput.right())
        {
            cameraPosition.x += .1;
        }
        else if (KeyboardInput.forward())
        {
            cameraPosition.y += .1;
        }
        else if (KeyboardInput.back())
        {
            cameraPosition.y -= .1;
        }

        if (MouseInput.leftClick())
        {
            clickLeft = true;
        }

        if (MouseInput.rightClick())
        {
            clickRight = true;
        }

        if (KeyboardInput.isKeyDown(GLFW_KEY_E))
        {
            attackMode = true;
        }
    }

    protected void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        float mouseLocationX = (float) (MouseInput.mouseX / width) - 0.5f;
        float mouseLocationY = 0.5f - (float) (MouseInput.mouseY / height);
        doRender(mouseLocationX, mouseLocationY);

        int error = glGetError();
        if (error != GL_NO_ERROR)
        {
            System.out.println(error);
        }

        glfwSwapBuffers(windowID);
    }

    protected void doRender(float mouseLocationX, float mouseLocationY)
    {
        background_render.render(-10, -10, 0, 0, 1);

        renderMap(mouseLocationX, mouseLocationY);
    }

    protected void renderMap(float mouseLocationX, float mouseLocationY)
    {

        //Calculate the tile size based on the zoom factor
        float tileSize = zoom;

        //Calculate the number of tiles that can fit on screen
        int tiles_x = (int) Math.ceil(SCREEN_SIZE_X / tileSize) + 2;
        int tiles_y = (int) Math.ceil(SCREEN_SIZE_Y / tileSize) + 2;

        //Offset tile position based on camera
        int center_x = (int) (cameraPosition.x);
        int center_y = (int) (cameraPosition.y);

        //Calculate the offset to make tiles render from the center
        int renderOffsetX = (tiles_x - 1) / 2;
        int renderOffsetY = (tiles_y - 1) / 2;

        //Render tiles
        for (int x = -renderOffsetX; x < renderOffsetX; x++)   //TODO add floor var
        {
            for (int y = -renderOffsetY; y < renderOffsetY; y++)
            {
                int tile_x = x + center_x;
                int tile_y = y + center_y;
                Tile tile = game.getWorld().getTile(tile_x, tile_y, 0);
                if (tile != Tiles.AIR)
                {
                    TileRenders.render(tile, x * tileSize, y * tileSize, zoom);
                }
            }
        }

        //Render entities
        for (Entity entity : game.getWorld().getEntities())
        {
            //Ensure the entity is on the floor we are rendering
            if (entity.zi() == 0) //TODO replace with floor var
            {
                float tile_x = (entity.xf() - center_x) * tileSize;
                float tile_y = (entity.yf() - center_y) * tileSize;

                //Ensure the entity is in the camera view
                if (tile_x >= LEFT_CAMERA_BOUND && tile_x <= RIGHT_CAMERA_BOUND)
                {
                    if (tile_y >= BOTTOM_CAMERA_BOUND && tile_y <= TOP_CAMERA_BOUND)
                    {
                        character_render.render(tile_x, tile_y, 0, 0, zoom);
                    }
                }
            }
        }

        //Render GUIs
        for (Gui gui : guiMap.values())
        {
            if (gui != null)
            {
                gui.render(mouseLocationX * SCREEN_SIZE_X, mouseLocationY * SCREEN_SIZE_Y);
            }
        }

        float mx = mouseLocationX * SCREEN_SIZE_X / tileSize;
        float my = mouseLocationY * SCREEN_SIZE_Y / tileSize;

        int tx = (int) Math.floor(center_x + mx);
        int ty = (int) Math.floor(center_y + my);

        Tile tile = game.getWorld().getTile(tx, ty, 0);

        float x = tx * tileSize;
        float y = ty * tileSize;

        //System.out.println(x + "  " + y + "  " + zoom + " " + tx + " " + ty + " " + tile);

        if (attackMode)
        {
            target_render.render(x - center_x * tileSize, y - center_y * tileSize, 0, 0, zoom);
        }
        else
        {
            box_render.render(x - center_x * tileSize, y - center_y * tileSize, 0, 0, zoom);
        }


        if (!MouseInput.leftClick() && clickLeft)
        {
            clickLeft = false;
            doLeftClickAction(tx, ty, 0);
        }

        if (!MouseInput.rightClick() && clickRight)
        {
            clickRight = false;
            doRightClickAction(tx, ty, 0);
        }
    }

    protected void doLeftClickAction(int tileX, int tileY, int floor)
    {
        if (attackMode)
        {
            attackMode = false;
            if (selectedEntity != null)
            {
                Entity entity = game.getWorld().getEntity(tileX, tileY, floor);
                if (entity != null)
                {
                    Weapon weapon = new Weapon("gun");
                    weapon.damage = new Damage(new DamageType("laser"), new Dice(6), 2);
                    selectedEntity.attack(entity, weapon);

                    if (entity.getHealth() <= 0)
                    {
                        game.getWorld().getEntities().remove(entity);
                    }
                }
            }
        }
        else
        {
            selectedEntity = game.getWorld().getEntity(tileX, tileY, floor);
        }
    }

    protected void doRightClickAction(int tileX, int tileY, int floor)
    {
        if (attackMode)
        {
            attackMode = false;
        }
        else if (selectedEntity != null)
        {
            Entity entity = game.getWorld().getEntity(tileX, tileY, floor);
            if (entity == null)
            {
                selectedEntity.setPosition(tileX, tileY, floor);
            }
        }
    }
}
