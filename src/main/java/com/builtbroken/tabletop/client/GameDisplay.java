package com.builtbroken.tabletop.client;


import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.render.RenderRect;
import com.builtbroken.tabletop.client.tile.TileRenders;
import com.builtbroken.tabletop.game.Game;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.controller.Player;
import com.builtbroken.tabletop.game.entity.living.Character;
import com.builtbroken.tabletop.game.map.Tile;
import com.builtbroken.tabletop.game.map.Tiles;
import com.builtbroken.tabletop.game.map.examples.StaticMapData;
import com.builtbroken.tabletop.util.Matrix4f;
import com.builtbroken.tabletop.util.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

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
    public static final float LEFT_CAMERA_BOUND = -10.0f;
    public static final float RIGHT_CAMERA_BOUND = 10.0f;
    public static final float BOTTOM_CAMERA_BOUND = -10.0f * 9.0f / 16.0f;
    public static final float TOP_CAMERA_BOUND = 10.0f * 9.0f / 16.0f;


    private Game game;
    private Character player;

    private int width = 1280;
    private int height = 720;

    private Thread thread;
    private boolean running = false;

    private long window;

    RenderRect background_render;
    RenderRect character_render;

    Vector3f cameraPosition = new Vector3f(0, 0, 0);
    Matrix4f pr_matrix;

    public static void main(String... args)
    {
        Game game = new Game();
        game.load(false, "");

        Tile.load();

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

    private void init()
    {
        if (!glfwInit())
        {
            System.err.println("Could not initialize GLFW!");
            return;
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, "Project TableTopGame", NULL, NULL);
        if (window == NULL)
        {
            System.err.println("Could not create GLFW window!");
            return;
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwSetKeyCallback(window, new Input());

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
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

        player = new Character("bob");
        player.setController(new Player(player));
        game.getWorld().getEntities().add(player);

        background_render = new RenderRect("resources/textures/background.png", Shader.CHAR, 20, 20, 0);
        character_render = new RenderRect("resources/textures/char.png", Shader.CHAR, 1, 1, 0.2f);
        TileRenders.load();
    }

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
                System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            if (glfwWindowShouldClose(window))
            {
                running = false;
            }
        }

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void update(double delta)
    {
        glfwPollEvents();
        for (Entity entity : game.getWorld().getEntities())
        {
            entity.update(delta);
        }
    }

    private void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        doRender();

        int error = glGetError();
        if (error != GL_NO_ERROR)
        {
            System.out.println(error);
        }

        glfwSwapBuffers(window);
    }

    private void doRender()
    {
        background_render.render(new Vector3f(-10, -10, 0), 0);

        renderMap();

        for (Entity entity : game.getWorld().getEntities())
        {
            character_render.render(new Vector3f(entity.xf(), entity.yf(), entity.zf()), 0);
        }
    }

    private void renderMap()
    {
        //Find our bounds so we know what to render
        int leftStart = (int)Math.floor(LEFT_CAMERA_BOUND + cameraPosition.x);
        int rightStart = (int)Math.floor(RIGHT_CAMERA_BOUND + cameraPosition.x);
        int bottomStart = (int)Math.floor(BOTTOM_CAMERA_BOUND + cameraPosition.y);
        int topStart = (int)Math.floor(TOP_CAMERA_BOUND + cameraPosition.y);

        for(int x = leftStart; x < rightStart; x++)
        {
            for(int y = bottomStart; y < topStart; y++)
            {
                Tile tile = game.getWorld().getTile(x, y, 0);
                if(tile != Tiles.AIR)
                {
                    TileRenders.render(tile, x, y);
                }
            }
        }
    }
}
