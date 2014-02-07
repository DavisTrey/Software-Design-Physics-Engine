package jboxGlue;

import jgame.JGColor;
import org.jbox2d.collision.CircleDef;


public class PhysicalObjectCircle extends PhysicalObject
{
    private double myRadius;

    public PhysicalObjectCircle (String id,
                                 int collisionId,
                                 JGColor color,
                                 double radius, int world)
    {
        this(id, collisionId, color, radius, 0, world);
    }

    public PhysicalObjectCircle (String id,
                                 int collisionId,
                                 JGColor color,
                                 double radius,
                                 double mass, int world)
    {
        super(id, collisionId, color, world);
        init(radius, mass);
    }

    public PhysicalObjectCircle (String id,
                                 int collisionId,
                                 String gfxname,
                                 double radius)
    {
        this(id, collisionId, gfxname, radius, 0);
    }

    public PhysicalObjectCircle (String id,
                                 int collisionId,
                                 String gfxname,
                                 double radius,
                                 double mass)
    {
        super(id, collisionId, gfxname);
        init(radius, mass);
    }

    private void init (double radius, double mass)
    {
        // save arguments
        myRadius = radius;
        int intRadius = (int)radius;
        // make it a circle
        CircleDef shape = new CircleDef();
        shape.radius = (float)radius;
        shape.density = (float)mass;
        createBody(shape);
        setBBox(-intRadius, -intRadius, 2 * intRadius, 2 * intRadius);
    }

    @Override
    public void paintShape ()
    {
        myEngine.setColor(myColor);
        myEngine.drawOval(x, y, (float)myRadius * 2, (float)myRadius * 2, true, true);
    }
}
