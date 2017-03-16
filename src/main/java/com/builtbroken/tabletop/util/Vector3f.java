package com.builtbroken.tabletop.util;

public class Vector3f
{
    public float x, y, z;

    public Vector3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Vector3f)
        {
            if (Math.abs(x - ((Vector3f) object).x) > 0.001)
            {
                return false;
            }
            if (Math.abs(y - ((Vector3f) object).y) < 0.001)
            {
                return false;
            }
            if (Math.abs(z - ((Vector3f) object).z) < 0.001)
            {
                return false;
            }
        }
        return false;
    }
}
