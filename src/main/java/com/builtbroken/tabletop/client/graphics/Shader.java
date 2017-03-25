package com.builtbroken.tabletop.client.graphics;

import com.builtbroken.tabletop.util.Matrix4f;
import com.builtbroken.tabletop.util.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    public static Shader GLOBAL_SHADER, SHADER;

    private boolean enabled = false;

    public final int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    public Shader(String vertex, String fragment)
    {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll()
    {
        GLOBAL_SHADER = new Shader("resources/shaders/char.vert", "resources/shaders/char.frag");
        //SHADER = new Shader("resources/shaders/shader.vert", "resources/shaders/shader.frag");
    }

    public static void disposeAll()
    {
        GLOBAL_SHADER.dispose();
        //SHADER.dispose();
    }

    private void dispose()
    {
        disable();
        glDeleteProgram(ID);
    }

    public int getUniform(String name)
    {
        if (locationCache.containsKey(name))
        {
            return locationCache.get(name);
        }

        int result = glGetUniformLocation(ID, name);
        if (result == -1)
        {
            System.err.println("Could not find uniform variable '" + name + "'!");
        }
        else
        {
            locationCache.put(name, result);
        }
        return result;
    }

    public void setUniform1i(String name, int value)
    {
        if (!enabled)
        {
            enable();
        }
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value)
    {
        if (!enabled)
        {
            enable();
        }
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y)
    {
        if (!enabled)
        {
            enable();
        }
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector)
    {
        if (!enabled)
        {
            enable();
        }
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniform3f(String name, float x, float y, float z)
    {
        if (!enabled)
        {
            enable();
        }
        glUniform3f(getUniform(name), x, y, z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix)
    {
        if (!enabled)
        {
            enable();
        }
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }


    public int getAttributeLocation(CharSequence name)
    {
        return glGetAttribLocation(ID, name);
    }


    public void enableVertexAttribute(int location)
    {
        glEnableVertexAttribArray(location);
    }


    public void disableVertexAttribute(int location)
    {
        glDisableVertexAttribArray(location);
    }


    public void pointVertexAttribute(int location, int size, int stride, int offset)
    {
        glVertexAttribPointer(location, size, GL11.GL_FLOAT, false, stride, offset);
    }

    public void enable()
    {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable()
    {
        glUseProgram(0);
        enabled = false;
    }
}
