package com.builtbroken.tabletop.util;

import java.nio.FloatBuffer;

public class Matrix4f
{
    public static final int SIZE = 4 * 4;
    public float[] elements = new float[SIZE];

    public Matrix4f()
    {

    }

    public Matrix4f reset()
    {
        for (int i = 0; i < SIZE; i++)
        {
            elements[i] = 0.0f;
        }
        return this;
    }

    public Matrix4f resetToIdentity()
    {
        reset();
        elements[0 + 0 * 4] = 1.0f;
        elements[1 + 1 * 4] = 1.0f;
        elements[2 + 2 * 4] = 1.0f;
        elements[3 + 3 * 4] = 1.0f;
        return this;
    }

    public static Matrix4f identity()
    {
        Matrix4f result = new Matrix4f();
        result.resetToIdentity();
        return result;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far)
    {
        Matrix4f result = identity();

        result.elements[0 + 0 * 4] = 2.0f / (right - left);

        result.elements[1 + 1 * 4] = 2.0f / (top - bottom);

        result.elements[2 + 2 * 4] = 2.0f / (near - far);

        result.elements[0 + 3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.elements[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    public static Matrix4f translated(Vector3f vector)
    {
        return identity().translate(vector);
    }

    public Matrix4f translate(Vector3f vector)
    {
        return translate(vector.x, vector.y, vector.z);
    }

    public Matrix4f translate(float x, float y, float z)
    {
        elements[0 + 3 * 4] = x;
        elements[1 + 3 * 4] = y;
        elements[2 + 3 * 4] = z;
        return this;
    }

    public Matrix4f rotate(float angle)
    {
        resetToIdentity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        elements[0 + 0 * 4] = cos;
        elements[1 + 0 * 4] = sin;

        elements[0 + 1 * 4] = -sin;
        elements[1 + 1 * 4] = cos;

        return this;
    }

    public Matrix4f multiply(Matrix4f matrix, Matrix4f result)
    {
        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++)
                {
                    sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
                }
                result.elements[x + y * 4] = sum;
            }
        }
        return result;
    }

    public FloatBuffer toFloatBuffer()
    {
        return BufferUtils.createFloatBuffer(elements);
    }

}
