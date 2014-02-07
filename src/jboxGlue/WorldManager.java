package jboxGlue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jgame.platform.JGEngine;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;


public class WorldManager
{
    public static List<World> ourWorlds = new ArrayList<World>();
    private static List<Vec2> myCentersOfMass = new ArrayList<Vec2>();
    private static HashMap<String, Body> myBodies = new HashMap<String, Body>();
    private static HashSet<Spring> mySprings=new HashSet<Spring>();
    public static World getWorld(int i)
    {
        // make sure we have a world, just in case...
        if (ourWorlds.get(i) == null) {
            throw new RuntimeException("call initWorld() before getWorld()!");
        }
        return ourWorlds.get(i);
    }
    public static List<World> getWorlds(){
    	return ourWorlds;
    }

    public static void initWorld (JGEngine engine)
    {
        AABB worldBounds = new AABB(new Vec2(0, 0),
                                    new Vec2(engine.displayWidth(), engine.displayHeight()));
        Vec2 gravity = new Vec2(0.0f, 0.0f);
        ourWorlds.add(new World(worldBounds, gravity, true));
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
    
    public static Vec2 getCenterOfMass(int i){
    	return myCentersOfMass.get(i);
    }
    public static void setCenterOfMass(Vec2 newCenter, int i){
    	if(myCentersOfMass.size()>i){
    	myCentersOfMass.set(i,newCenter);
    	}
    	else{
    		myCentersOfMass.add(newCenter);
    	}
    }
}
