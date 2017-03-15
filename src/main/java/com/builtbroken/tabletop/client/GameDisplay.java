package com.builtbroken.tabletop.client;


import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.render.RenderRect;
import com.builtbroken.tabletop.game.Game;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.living.Character;
import com.builtbroken.tabletop.util.Matrix4f;
import com.builtbroken.tabletop.util.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengles.GLES20.glActiveTexture;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class GameDisplay implements Runnable
{
    // The window handle
    private long windowID;

    private int width = 1200;
    private int height = 720;

    private boolean running = true;
    private Game game;

    private Thread thread;

    private Character player;

    private RenderRect background_render;
    private RenderRect character_render;

    public static GameDisplay createAndRun(Game game)
    {
        GameDisplay display = new GameDisplay();
        display.thread = new Thread(display, "GameDisplayThread");
        display.thread.start();
        return display;
    }

    @Override
    public void run()
    {
    	init();
    	
        //https://www.lwjgl.org/guide
        System.out.println("LWJGL " + Version.getVersion());
        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init()
    {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        //GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        windowID = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
        if (windowID == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });
        glfwSetKeyCallback(windowID, new Input());

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowID, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    windowID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically



        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);
        // Enable v-sync
        glfwSwapInterval(1); //TODO move to settings

        // Make the window visible
        glfwShowWindow(windowID);
        GL.createCapabilities();

        //Set screen color
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
       
        Shader.loadAll();

        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);

        Shader.CHAR.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.CHAR.setUniform1i("tex", 1);

        player = new Character("bob");
        game.getWorld().getEntities().add(player);

        background_render = new RenderRect("resources/assets/textures/background.png", 10, 10, 0);
        character_render = new RenderRect("resources/assets/textures/char.png", 1, 1, 0.1f);
    }

    private void loop()
    {
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
                update();
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
            if (glfwWindowShouldClose(windowID))
            {
                running = false;
            }
        }
    }

    private void update()
    {
        glfwPollEvents();
    }

    private void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        background_render.render(new Vector3f(0, 0, 0), 0);

        for (Entity entity : game.getWorld().getEntities())
        {
            character_render.render(new Vector3f(entity.xi() / 16.0f, entity.yi() / 16.0f, entity.zi() / 16.0f), 0);
        }

        int error = glGetError();
        if (error != GL_NO_ERROR)
        {
            System.out.println(error);
        }

        glfwSwapBuffers(windowID); // swap the color buffers
    }

    public static void main(String... args)
    {
        Game game = new Game();
        game.load(false, "");
        createAndRun(game);
    }
}
