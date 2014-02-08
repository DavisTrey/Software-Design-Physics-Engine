package jboxGlue;

import jgame.JGColor;
import jgame.JGObject;
import jgame.impl.JGEngineInterface;
import org.jbox2d.collision.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;


public abstract class PhysicalObject extends JGObject
{
    protected JGEngineInterface myEngine;
    protected boolean myHasImage;
    protected JGColor myColor;
    protected Body myBody;
    protected float myRotation;
    protected boolean isDestroyed;
    protected int worldID;

    protected PhysicalObject (String name, int collisionId, JGColor color, int world)
    {
        super(name, true, 0, 0, collisionId, null);
        isDestroyed = false;
        worldID = world;
		JGColor myColor = JGColor.white;
		if(world%8==1)
			myColor = JGColor.red;
		if(world%8==2)
			myColor = JGColor.cyan;
		if(world%8==3)
			myColor = JGColor.magenta;
		if(world%8==4)
			myColor = JGColor.yellow;
		if(world%8==5)
			myColor = JGColor.pink;
		if(world%8==6)
			myColor = JGColor.gray;
		if(world%8==7)
			myColor = JGColor.blue;
        init(myColor, false);
    }

    protected PhysicalObject (String name, int collisionId, String gfxname)
    {
        super(name, true, 0, 0, collisionId, gfxname);
        init(null, true);
        if (gfxname == null) {
            throw new IllegalArgumentException("gfxname cannot be null!");
        }
    }

    private void init (JGColor color, boolean hasImage)
    {
        // init defaults
        myEngine = eng;
        myColor = color;
        myHasImage = hasImage;
    }

    protected void createBody (ShapeDef shapeDefinition)
    {
        myBody = WorldManager.getWorld(worldID).createBody(new BodyDef());
        myBody.createShape(shapeDefinition);
        myBody.setUserData(this); // for following body back to JGObject
        myBody.setMassFromShapes();
        myBody.m_world = WorldManager.getWorld(worldID);
    }

    public Body getBody ()
    {
        return myBody;
    }

    public JGColor getColor ()
    {
        return myColor;
    }

    public void setColor (JGColor val)
    {
        myColor = val;
    }

    @Override
    public void setBBox (int x, int y, int width, int height)
    {
        // NOTE: 
        //  If bounding box is same size as physical object,
        //  JGame will never see the bounding boxes overlap.
        // So fudge box size a little bit so JGame can see the collisions.
        final int FUDGE_TERM = 4;
        x -= FUDGE_TERM;
        y -= FUDGE_TERM;
        width += FUDGE_TERM * 2;
        height += FUDGE_TERM * 2;
        super.setBBox(x, y, width, height);
    }

    @Override
    public void move ()
    {
        // if the JGame object was deleted, remove the physical object too
        if (myBody.m_world != WorldManager.getWorld(worldID)) {
            remove();
            return;
        }
        // copy the position and rotation from the JBox world to the JGame world
        Vec2 position = myBody.getPosition();
        x = position.x;
        y = position.y;
        myRotation = -myBody.getAngle();
    }

    @Override
    public void setPos (double x, double y)
    {
        // there's no body while the game object is initializing
        if (myBody != null) { 
            // set position of jbox2d object, not jgame object
            myBody.setXForm(new Vec2((float)x, (float)y), -myRotation);
        }
    }

    public void setForce (double x, double y)
    {
        myBody.applyForce(new Vec2((float)x, (float)y), myBody.m_xf.position);
    }

    @Override
    public void destroy ()
    {
    	isDestroyed = true;
    	// This moves the body very far away from the play area because destroyBody totally doesn't do what it says it does
    	// so this is how I'm dealing with it.
    	myBody.setXForm(new Vec2((float)10000, (float)10000), -myRotation);
    	myBody.getWorld().destroyBody(myBody);
    }

    @Override
    public void paint ()
    {
        // only paint something if we need to draw a shape. Images are already drawn
    	if (!isDestroyed){
        if (!myHasImage) {
            paintShape();
        }
    	}
    }

    protected abstract void paintShape ();
}
