package jboxGlue;

import jgame.JGColor;
import org.jbox2d.collision.CircleDef;


public class PhysicalObjectCircle extends PhysicalObject
{
    private double myRadius;
    private static JGColor myColor1 = JGColor.white;
    private static JGColor myColor2 = JGColor.red;
    private static JGColor myColor3 = JGColor.cyan;
    private static JGColor myColor4 = JGColor.magenta;
    private static JGColor myColor5 = JGColor.yellow;
    private static JGColor myColor6 = JGColor.pink;
    private static JGColor myColor7 = JGColor.grey;
    private static JGColor myColor8 = JGColor.blue;
    public PhysicalObjectCircle (String id,
                                 int collisionId,
                                 JGColor color,
                                 double radius,
                                 double mass, int world)
    {
        super(id, collisionId, color, world);
        if(world%8==0)
        	myColor = myColor1;
		if(world%8==1)
        	myColor = myColor2;
		if(world%8==2)
        	myColor = myColor3;
		if(world%8==3)
        	myColor = myColor4;
		if(world%8==4)
        	myColor = myColor5;
		if(world%8==5)
        	myColor = myColor6;
		if(world%8==6)
        	myColor = myColor7;
		if(world%8==7)
        	myColor = myColor8;
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

	public static void setColors(JGColor massColor1, JGColor massColor2,
			JGColor massColor3, JGColor massColor4, JGColor massColor5, JGColor massColor6,
			JGColor massColor7, JGColor massColor8) {
		myColor1 = massColor1;
		myColor2 = massColor2;
		myColor3 = massColor3;
		myColor4 = massColor4;
		myColor5 = massColor5;
		myColor6 = massColor6;
		myColor7 = massColor7;
		myColor8 = massColor8;
		
	}
}
