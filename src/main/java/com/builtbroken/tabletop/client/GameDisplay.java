package com.builtbroken.tabletop.client;


import com.builtbroken.jlib.math.dice.Dice;
import com.builtbroken.tabletop.client.controls.ControlMode;
import com.builtbroken.tabletop.client.controls.KeyboardInput;
import com.builtbroken.tabletop.client.controls.MouseInput;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.button.ButtonScrollRow;
import com.builtbroken.tabletop.client.gui.game.GuiGame;
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
import org.lwjgl.glfw.GLFWWindowSizeCallback;
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
    public static final float DEFAULT_LEFT_CAMERA_BOUND = -10.0f;
    public static final float DEFAULT_RIGHT_CAMERA_BOUND = 10.0f;
    public static final float DEFAULT_BOTTOM_CAMERA_BOUND = -10.0f * 9.0f / 16.0f;
    public static final float DEFAULT_TOP_CAMERA_BOUND = 10.0f * 9.0f / 16.0f;

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

    //Default screen size
    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;

    public static final String RESOURCE_PATH = "resources/";
    public static final String TEXTURE_PATH = RESOURCE_PATH + "textures/";
    public static final String GUI_PATH = TEXTURE_PATH + "gui/";

    //Game logic data
    protected Game game;
    protected Entity selectedEntity;

    //GUIS
    protected HashMap<String, Gui> guiMap = new HashMap();

    //Render logic data
    protected float zoom = 1;

    protected int width = 1280;
    protected int height = 720;

    //Calculates the size of the screen 2D
    public float screenSizeX;
    public float screenSizeY;
    public float cameraBoundLeft;
    public float cameraBoundRight;
    public float cameraBoundBottom;
    public float cameraBoundTop;

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
    protected ControlMode controlMode = ControlMode.SELECTION;

    //Main method
    public static void main(String... args)
    {
        //Load test game
        Game game = new Game();
        game.load(false, "");

        //Init tiles
        Tiles.load();

        //Create a test map
        StaticMapData map = new StaticMapData();
        map.load();
        game.getWorld().setMapData(map);


        //Generate some characters to render
        game.getWorld().getEntities().add(new Character("bob").setPosition(2, 0, 0));
        game.getWorld().getEntities().add(new Character("jim").setPosition(-2, 0, 0));
        game.getWorld().getEntities().add(new Character("paul").setPosition(0, 2, 0));
        game.getWorld().getEntities().add(new Character("tim").setPosition(0, -2, 0));

        //Create display and start display thread
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

        ///Debug gl version
        System.out.println("OpenGL: " + glGetString(GL_VERSION));


        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //Load shader programs
        Shader.loadAll();
        Shader.BACKGROUND.setUniform1i("tex", 1);
        Shader.CHAR.setUniform1i("tex", 1);

        //Init display data
        resizeDisplay();

        //Register event for resize
        glfwSetWindowSizeCallback(windowID, new GLFWWindowSizeCallback()
        {
            @Override
            public void invoke(long window, int width, int height)
            {
                GameDisplay.this.width = width;
                GameDisplay.this.height = height;
                resizeDisplay();
            }
        });


        //Init render code
        background_render = new RenderRect(TEXTURE_PATH + "background.png", Shader.CHAR, 20, 20, -0.98f);
        character_render = new RenderRect(TEXTURE_PATH + "char.png", Shader.CHAR, 1, 1, EFFECT_LAYER);
        target_render = new RenderRect(TEXTURE_PATH + "target.png", Shader.CHAR, 1, 1, SELECTION_LAYER);
        box_render = new RenderRect(TEXTURE_PATH + "box.png", Shader.CHAR, 1, 1, SELECTION_LAYER);

        ButtonScrollRow.downArrow = new RenderRect(GUI_PATH + "button.down.png", Shader.CHAR, 1, 0.2f, GAME_GUI_LAYER);
        ButtonScrollRow.upArrow = new RenderRect(GUI_PATH + "button.up.png", Shader.CHAR, 1, 0.2f, GAME_GUI_LAYER);
        ButtonScrollRow.leftArrow = new RenderRect(GUI_PATH + "button.left.png", Shader.CHAR, 0.2f, 1, GAME_GUI_LAYER);
        ButtonScrollRow.rightArrow = new RenderRect(GUI_PATH + "button.right.png", Shader.CHAR, 0.2f, 1, GAME_GUI_LAYER);

        TileRenders.load();

        loadGUIs();
    }

    protected void loadGUIs()
    {
        //camera is 20x 11.5y
        guiMap.put("game", new GuiGame(this));
    }

    protected void resizeDisplay()
    {
        //Enforce min value
        width = Math.max(width, DEFAULT_WIDTH);
        height = Math.max(height, DEFAULT_HEIGHT);

        //Reset screen render size, lock to min value
        glViewport(0, 0, width, height);

        //Get change in size from orginal
        float pw = (width / (float) DEFAULT_WIDTH) - 1;
        float ph = (height / (float) DEFAULT_HEIGHT) - 1;

        //Reset the camera bounds
        cameraBoundLeft = DEFAULT_LEFT_CAMERA_BOUND + (DEFAULT_LEFT_CAMERA_BOUND * pw / 2);
        cameraBoundRight = DEFAULT_RIGHT_CAMERA_BOUND + (DEFAULT_RIGHT_CAMERA_BOUND * pw / 2);
        cameraBoundBottom = DEFAULT_BOTTOM_CAMERA_BOUND + (DEFAULT_BOTTOM_CAMERA_BOUND * ph / 2);
        cameraBoundTop = DEFAULT_TOP_CAMERA_BOUND + (DEFAULT_TOP_CAMERA_BOUND * ph / 2);

        //Recalc size of camera
        screenSizeX = cameraBoundRight - cameraBoundLeft;
        screenSizeY = cameraBoundTop - cameraBoundBottom;

        //Rebuild projection matrix
        pr_matrix = Matrix4f.orthographic(cameraBoundLeft, cameraBoundRight, cameraBoundBottom, cameraBoundTop, -1.0f, 1.0f);

        //Load projection matrix into shader
        Shader.BACKGROUND.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.CHAR.setUniformMat4f("pr_matrix", pr_matrix);

        //Note that screen has resized
        for (Gui gui : guiMap.values())
        {
            if (gui != null)
            {
                gui.onResize(this);
            }
        }
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

            //End update
            //Update data for next tick
            MouseInput.prev_mouseX = MouseInput.mouseX;
            MouseInput.prev_mouseY = MouseInput.mouseY;
        }

        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

    protected void update(double delta)
    {
        long time = System.currentTimeMillis();
        glfwPollEvents();

        //Update entities
        for (Entity entity : game.getWorld().getEntities())
        {
            entity.update(delta);
        }

        //update GUI
        for (Gui gui : guiMap.values())
        {
            if (gui != null)
            {
                gui.update(this);
            }
        }

        //Zoom controls
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

        //Movement camera controls
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

        //Key handlers
        if (MouseInput.leftClick())
        {
            clickLeft = true;
        }

        if (MouseInput.rightClick())
        {
            clickRight = true;
        }

        //Handles movement of camera
        if (KeyboardInput.isKeyDown(GLFW_KEY_LEFT_SHIFT) && MouseInput.rightClick())
        {
            //disable left and right click actions
            clickLeft = false;
            clickRight = false;

            //Move screen
            double deltaX = ((MouseInput.prev_mouseX - MouseInput.mouseX) / width) * screenSizeX;
            double deltaY = -((MouseInput.prev_mouseY - MouseInput.mouseY) / height) * screenSizeY;
            if (deltaX != 0 || deltaY != 0)
            {
                this.cameraPosition.x += deltaX;
                this.cameraPosition.y += deltaY;
            }
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
            System.out.println("GLError: " + error);
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
        int tiles_x = (int) Math.ceil(screenSizeX / tileSize) + 2;
        int tiles_y = (int) Math.ceil(screenSizeY / tileSize) + 2;

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
                if (tile_x >= cameraBoundLeft && tile_x <= cameraBoundRight)
                {
                    if (tile_y >= cameraBoundBottom && tile_y <= cameraBoundTop)
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
                gui.render(mouseLocationX * screenSizeX, mouseLocationY * screenSizeY);
            }
        }

        float mx = mouseLocationX * screenSizeX / tileSize;
        float my = mouseLocationY * screenSizeY / tileSize;

        int tx = (int) Math.floor(center_x + mx);
        int ty = (int) Math.floor(center_y + my);

        Tile tile = game.getWorld().getTile(tx, ty, 0);

        float x = tx * tileSize;
        float y = ty * tileSize;

        //System.out.println(x + "  " + y + "  " + zoom + " " + tx + " " + ty + " " + tile);

        if (controlMode == ControlMode.ATTACK)
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
        if (controlMode == ControlMode.ATTACK)
        {
            controlMode = ControlMode.SELECTION;
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
        else if (controlMode == ControlMode.SELECTION)
        {
            selectedEntity = game.getWorld().getEntity(tileX, tileY, floor);
        }
    }

    protected void doRightClickAction(int tileX, int tileY, int floor)
    {
        if (controlMode == ControlMode.ATTACK)
        {
            controlMode = ControlMode.SELECTION;
        }
        else if (controlMode == ControlMode.MOVE)
        {
            if (selectedEntity != null)
            {
                Entity entity = game.getWorld().getEntity(tileX, tileY, floor);
                if (entity == null)
                {
                    selectedEntity.setPosition(tileX, tileY, floor);
                }
            }
        }
    }
}
