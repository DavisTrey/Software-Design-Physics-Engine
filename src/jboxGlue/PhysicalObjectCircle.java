package jboxGlue;

import jgame.JGColor;
import org.jbox2d.collision.CircleDef;


public class PhysicalObjectCircle extends PhysicalObject
{
    private double myRadius;
    private static int myColor1 = 0;
    private static int myColor2 = 1;
    private static int myColor3 = 2;
    private static int myColor4 = 3;
    private static int myColor5 = 4;
    private static int myColor6 = 5;
    private static int myColor7 = 6;
    private static int myColor8 = 7;
    private static final JGColor[] massColors = {JGColor.white, JGColor.red, JGColor.cyan, JGColor.magenta, 
    	JGColor.yellow, JGColor.pink, JGColor.gray, JGColor.blue};
    public PhysicalObjectCircle (String id,
                                 int collisionId,
                                 JGColor color,
                                 double radius,
                                 double mass, int world)
    {
        super(id, collisionId, color, world);
        if(world%8==0)
        	myColor = massColors[myColor1];
		if(world%8==1)
			myColor = massColors[myColor2];
		if(world%8==2)
			myColor = massColors[myColor3];
		if(world%8==3)
			myColor = massColors[myColor4];
		if(world%8==4)
			myColor = massColors[myColor5];
		if(world%8==5)
			myColor = massColors[myColor6];
		if(world%8==6)
			myColor = massColors[myColor7];
		if(world%8==7)
			myColor = massColors[myColor8];
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

	public static void setColors(int massColor1, int massColor2,
			int massColor3, int massColor4, int massColor5, int massColor6,
			int massColor7, int massColor8) {
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
