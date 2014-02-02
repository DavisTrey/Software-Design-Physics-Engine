package jboxGlue;

import java.util.HashMap;
import java.util.HashSet;

import jgame.platform.JGEngine;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;


public class WorldManager
{
    public static World ourWorld;
    private static HashMap<String, Body> myBodies = new HashMap<String, Body>();
    private static HashSet<Spring> mySprings=new HashSet<Spring>();
    static {
        ourWorld = null;
    }

    public static World getWorld ()
    {
        // make sure we have a world, just in case...
        if (ourWorld == null) {
            throw new RuntimeException("call initWorld() before getWorld()!");
        }
        return ourWorld;
    }

    public static void initWorld (JGEngine engine)
    {
        AABB worldBounds = new AABB(new Vec2(0, 0),
                                    new Vec2(engine.displayWidth(), engine.displayHeight()));
        Vec2 gravity = new Vec2(0.0f, 0.0f);
        ourWorld = new World(worldBounds, gravity, true);
    }

    public static HashMap<String, Body> getBodies(){
    	return myBodies;
    }
    
    public static void addBody(String id, Body body){
    	myBodies.put(id, body);
    }
    public static HashSet<Spring> getSprings(){
    	return mySprings;
    }
    public static void addSpring(Spring s){
    	mySprings.add(s);
    }
}
