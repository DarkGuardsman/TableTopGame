package com.builtbroken.tabletop.client;


import com.builtbroken.tabletop.client.controls.KeyboardInput;
import com.builtbroken.tabletop.client.controls.MouseInput;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.render.*;
import com.builtbroken.tabletop.client.graphics.textures.ITexture;
import com.builtbroken.tabletop.client.graphics.textures.TextureLoader;
import com.builtbroken.tabletop.client.gui.Gui;
import com.builtbroken.tabletop.client.gui.component.Component;
import com.builtbroken.tabletop.client.gui.component.button.ButtonScrollRow;
import com.builtbroken.tabletop.client.gui.game.GuiGame;
import com.builtbroken.tabletop.game.Game;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.actions.Action;
import com.builtbroken.tabletop.game.tile.Tile;
import com.builtbroken.tabletop.game.tile.Tiles;
import com.builtbroken.tabletop.util.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

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
    public Thread thread;
    public boolean running = false;

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

    /** Current game controlling the display */
    public final Game game;
    /** Current selected entity */
    public Entity selectedEntity;
    /** Current action selected for the entity */
    public String currentSelectedEntityAction;

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

    protected int updates = 0;
    protected int frames = 0;

    protected int prev_updates = 0;
    protected int prev_frames = 0;

    //Renders
    protected RenderRect background_render;
    protected RenderRect target_render;
    protected RenderRect box_render;

    public FontRender fontRender;

    protected Renderer renderer;
    protected ITexture testTexture;

    /** Location of the camera on the map X */
    protected float cameraPosX = 0;
    /** Location of the camera on the map X */
    protected float cameraPosY = 0;
    /** The floor the camera is point at */
    protected int cameraPosZ = 0;

    /** Projection matrix of the camera */
    protected Matrix4f pr_matrix;

    //Controls
    protected long lastZoom = 0L;
    protected boolean clickLeft = false;
    protected boolean clickRight = false;


    /** Current GUI part that the mouse is over and the user is manipulating */
    protected Component currentGuiComponetInUse;

    public GameDisplay(Game game)
    {
        this.game = game;
    }

    public void start()
    {
        running = true;
        thread = new Thread(this, "GameGraphics");
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
        target_render = new RenderRect(TEXTURE_PATH + "target.png", Shader.CHAR, 1, 1, SELECTION_LAYER);
        box_render = new RenderRect(TEXTURE_PATH + "box.png", Shader.CHAR, 1, 1, SELECTION_LAYER);

        fontRender = new FontRender(TEXTURE_PATH + "font/FontData.csv", 1, GAME_GUI_LAYER + 0.1f);

        renderer = new Renderer();
        testTexture = TextureLoader.getTexture(TEXTURE_PATH + "target.png", 0, 0, 1, 1);

        CharRender.load();

        ButtonScrollRow.downArrow = new RenderRect(GUI_PATH + "button.down.png", Shader.CHAR, 1, 0.2f, GAME_GUI_LAYER);
        ButtonScrollRow.upArrow = new RenderRect(GUI_PATH + "button.up.png", Shader.CHAR, 1, 0.2f, GAME_GUI_LAYER);
        ButtonScrollRow.leftArrow = new RenderRect(GUI_PATH + "button.left.png", Shader.CHAR, 0.2f, 1, GAME_GUI_LAYER);
        ButtonScrollRow.rightArrow = new RenderRect(GUI_PATH + "button.right.png", Shader.CHAR, 0.2f, 1, GAME_GUI_LAYER);

        TileRender.load();

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

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(cameraBoundLeft, cameraBoundRight, cameraBoundBottom, cameraBoundTop, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        //Note that screen has resized
        for (Gui gui : guiMap.values())
        {
            if (gui != null)
            {
                gui.onResize();
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
        while (running)
        {
            //Calculate the amount of time since last tick
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;


            //Calculate position of mouse on the screen based on the camera
            float mouseLocationX = (float) (MouseInput.mouseX / width) - 0.5f;
            float mouseLocationY = 0.5f - (float) (MouseInput.mouseY / height);

            //Only update 60 times a second
            if (delta >= 1.0)
            {
                update(delta, mouseLocationX, mouseLocationY);
                updates++;
                delta--;
            }

            //Render and track frames
            render(mouseLocationX, mouseLocationY);
            frames++;

            //If 1 second passes reset
            if (System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                prev_frames = frames;
                prev_updates = updates;
                updates = 0;
                frames = 0;
            }

            //Detect if window needs to close
            if (glfwWindowShouldClose(windowID))
            {
                running = false;
            }

            //End update
            //Update data for next tick
            MouseInput.prev_mouseX = MouseInput.mouseX;
            MouseInput.prev_mouseY = MouseInput.mouseY;
        }

        //Clean up
        dispose();
        glfwDestroyWindow(windowID);
        glfwTerminate();
    }


    /**
     * Clean up everything stored on the graphics card
     */
    protected void dispose()
    {
        TextureLoader.disposeTextures();
        TileRender.dispose();
        CharRender.dispose();
        fontRender.dispose();
        background_render.dispose();
        target_render.dispose();
        box_render.dispose();
    }

    protected void update(double delta, float mouseLocationX, float mouseLocationY)
    {
        long time = System.currentTimeMillis();
        glfwPollEvents();

        //update GUI
        currentGuiComponetInUse = null;
        for (Gui gui : guiMap.values())
        {
            if (gui != null)
            {
                Component component = gui.update(mouseLocationX * screenSizeX, mouseLocationY * screenSizeY);
                if (component != null)
                {
                    currentGuiComponetInUse = component;
                }
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
            cameraPosX -= .1;
        }
        else if (KeyboardInput.right())
        {
            cameraPosX += .1;
        }
        else if (KeyboardInput.forward())
        {
            cameraPosY += .1;
        }
        else if (KeyboardInput.back())
        {
            cameraPosY -= .1;
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
                this.cameraPosX += deltaX;
                this.cameraPosY += deltaY;
            }
        }
    }

    protected void render(float mouseLocationX, float mouseLocationY)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //Render background behind map
        background_render.render(-10, -10, 0, 0, 1);

        //doRender(mouseLocationX, mouseLocationY);

        renderer.startDrawing();
        renderer.draw(testTexture, -1f, -1f, 1f, 1f, .1f, 0, 1);
        renderer.endDrawing();

        /*
        int error = glGetError();
        if (error != GL_NO_ERROR)
        {
            System.out.println("GLError: " + error);
        }
        */

        glfwSwapBuffers(windowID);
    }

    protected void doRender(float mouseLocationX, float mouseLocationY)
    {


        //Render map
        renderMap(mouseLocationX, mouseLocationY);

        //Render GUIs
        for (Gui gui : guiMap.values())
        {
            if (gui != null)
            {
                gui.render(mouseLocationX * screenSizeX, mouseLocationY * screenSizeY);
            }
        }

        //Render text
        String s = "FPS: " + prev_frames + "  UPS: " + prev_updates;
        fontRender.render(s, -1, cameraBoundTop - 0.5f, 0, 0, .5f);

        s = "Entity: " + (selectedEntity != null ? selectedEntity.getDisplayName() : "none");
        fontRender.render(s, cameraBoundLeft + 1.2f, cameraBoundTop - 0.5f, 0, 0, .5f);

        s = "Action: " + (currentSelectedEntityAction != null ? currentSelectedEntityAction : "none");
        fontRender.render(s, cameraBoundLeft + 1.2f, cameraBoundTop - 1.1f, 0, 0, .5f);


        s = String.valueOf(java.lang.Character.toChars(KeyboardInput.currentKey));
        fontRender.render(s, 0, 0, 0, 0, .5f);

        /* Debug for text render
        s = "abcdefghijklmnopqrztuvwxyz";
        fontRender.render(s, 0, 0, 0, 0, 1);
        s = "ABCDEFGHIJKLMNOPQRZTUVWZYZ";
        fontRender.render(s, 0, -1, 0, 0, 1);
        s = "0123456789[]{};:'\",./?-+=_*";
        fontRender.render(s, 0, -2, 0, 0, 1);

        s = "abcdefghijklmnopqrztuvwxyz";
        fontRender.render(s, 0, -3, 0, 0, 0.5f);
        s = "ABCDEFGHIJKLMNOPQRZTUVWZYZ";
        fontRender.render(s, 0, -3.5f, 0, 0, 0.5f);
        s = "0123456789[]{};:'\",./?-+=_*";
        fontRender.render(s, 0, -4, 0, 0, 0.5f);
        */
    }

    protected void renderMap(float mouseLocationX, float mouseLocationY)
    {
        //Calculate the tile size based on the zoom factor
        float tileSize = zoom;

        //Calculate the number of tiles that can fit on screen
        int tiles_x = (int) Math.ceil(screenSizeX / tileSize) + 2;
        int tiles_y = (int) Math.ceil(screenSizeY / tileSize) + 2;

        //Offset tile position based on camera
        int center_x = (int) (cameraPosX - (zoom / 2f));
        int center_y = (int) (cameraPosY - (zoom / 2f));

        //Calculate the offset to make tiles render from the center
        int renderOffsetX = (tiles_x - 1) / 2;
        int renderOffsetY = (tiles_y - 1) / 2;

        //Get the position of the mouse based on screen size and tile scale
        float mouseScreenPosXScaled = mouseLocationX * screenSizeX / tileSize;
        float mouseScreenPosYScaled = mouseLocationY * screenSizeY / tileSize;

        //Get the position of the mouse relative to the map
        int mouseMapPosX = (int) Math.floor(center_x + mouseScreenPosXScaled);
        int mouseMapPosY = (int) Math.floor(center_y + mouseScreenPosYScaled);

        //Get the tile the mouse is currently over
        Tile tileUnderMouse = game.getWorld().getTile(mouseMapPosX, mouseMapPosY, cameraPosZ);

        //Render tiles
        for (int x = -renderOffsetX; x < renderOffsetX; x++)
        {
            for (int y = -renderOffsetY; y < renderOffsetY; y++)
            {
                int tile_x = x + center_x;
                int tile_y = y + center_y;
                Tile tile = game.getWorld().getTile(tile_x, tile_y, cameraPosZ);
                if (tile != Tiles.AIR)
                {
                    TileRender.render(tile, x * tileSize, y * tileSize, zoom);
                }
            }
        }

        //Render entities
        for (Entity entity : game.getWorld().getEntities())
        {
            //Ensure the entity is on the floor we are rendering
            if (entity.zi() == cameraPosZ)
            {
                float tile_x = (entity.xf() - center_x) * tileSize;
                float tile_y = (entity.yf() - center_y) * tileSize;

                //Ensure the entity is in the camera view
                if (tile_x >= cameraBoundLeft && tile_x <= cameraBoundRight)
                {
                    if (tile_y >= cameraBoundBottom && tile_y <= cameraBoundTop)
                    {
                        CharRender.render(entity, tile_x, tile_y, 0, 0, zoom);

                        if (mouseMapPosX == entity.xi() && mouseMapPosY == entity.yi())
                        {
                            String s = entity.getDisplayName();
                            fontRender.render(s, mouseLocationX * screenSizeX, mouseLocationY * screenSizeY, 0, 0, .5f * zoom);
                        }
                    }
                }
            }
        }

        float x = mouseMapPosX * tileSize;
        float y = mouseMapPosY * tileSize;

        //System.out.println(x + "  " + y + "  " + zoom + " " + tx + " " + ty + " " + tile);

        if (currentGuiComponetInUse != null)
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
            doLeftClickAction(mouseMapPosX, mouseMapPosY, cameraPosZ, mouseLocationX, mouseLocationY);
        }

        if (!MouseInput.rightClick() && clickRight)
        {
            clickRight = false;
            doRightClickAction(mouseMapPosX, mouseMapPosY, cameraPosZ, mouseLocationX, mouseLocationY);
        }
    }

    protected void doLeftClickAction(int tileX, int tileY, int floor, float mouseLocationX, float mouseLocationY)
    {
        boolean eventConsumed = false;
        if (currentGuiComponetInUse != null)
        {
            currentGuiComponetInUse.onClick(mouseLocationX, mouseLocationY, true);
            eventConsumed = true;
        }
        else if (currentSelectedEntityAction != null && selectedEntity != null)
        {
            Action action = Action.get(currentSelectedEntityAction);
            if (action.doesUseMouse(true))
            {
                eventConsumed = true;
                boolean completed = false;
                if (action.canDoAction(selectedEntity, game.getWorld(), tileX, tileY, floor, true))
                {
                    completed = action.doAction(selectedEntity, game.getWorld(), tileX, tileY, floor, true);
                }

                if (action.shouldFreeMouse(selectedEntity, true, completed))
                {
                    currentSelectedEntityAction = null;
                }
            }
        }

        if (!eventConsumed)
        {
            currentSelectedEntityAction = null;
            selectedEntity = game.getWorld().getEntity(tileX, tileY, floor);
            for (Gui gui : guiMap.values())
            {
                if (gui != null)
                {
                    gui.onEntitySelectionChange(this, selectedEntity);
                }
            }
        }
    }

    protected void doRightClickAction(int tileX, int tileY, int floor, float mouseLocationX, float mouseLocationY)
    {
        boolean eventConsumed = false;
        if (currentGuiComponetInUse != null)
        {
            currentGuiComponetInUse.onClick(mouseLocationX, mouseLocationY, false);
            eventConsumed = true;
        }
        else if (currentSelectedEntityAction != null)
        {
            eventConsumed = true;
            Action action = Action.get(currentSelectedEntityAction);
            if (action.doesUseMouse(false))
            {
                boolean completed = false;
                if (action.canDoAction(selectedEntity, game.getWorld(), tileX, tileY, floor, false))
                {
                    completed = action.doAction(selectedEntity, game.getWorld(), tileX, tileY, floor, false);
                }
                if (action.shouldFreeMouse(selectedEntity, false, completed))
                {
                    currentSelectedEntityAction = null;
                }
            }
        }
    }
}
