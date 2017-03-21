package com.builtbroken.tabletop.game;

/**
 * All objects that exist in the world placed or existing elsewhere extend this class.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class GameObject
{
    /** Unique ID used to reference this object type, instances should use a separate field for separation */
    public final String uniqueID;

    /**
     * Default constructor
     *
     * @param uniqueID - unique ID for the type
     */
    public GameObject(String uniqueID)
    {
        this.uniqueID = uniqueID;
    }

    @Override
    public String toString()
    {
        return "GameObject[" + uniqueID + "]@" + hashCode();
    }
}
