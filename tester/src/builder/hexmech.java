package builder;

import java.awt.*;

/* This is a companion class to hexgame.java. It handles all of the mechanics related to hexagon grids. */

public class hexmech
{
	/*
#define HEXEAST 0
#define HEXSOUTHEAST 1
#define HEXSOUTHWEST 2
#define HEXWEST 3
#define HEXNORTHWEST 4
#define HEXNORTHEAST 5
	 */

	//Constants
	public static boolean XYVertex=true;	//true: x,y are the co-ords of the first vertex.
	//false: x,y are the co-ords of the top left rect. co-ord.

	private static int BORDERS=50;	//default number of pixels for the border.

	private static int s=0;	// length of one side
	private static int t=0;	// short side of 30o triangle outside of each hex
	private static int r=0;	// radius of inscribed circle (centre to middle of each side). r= h/2
	private static int h=0;	// height. Distance between centres of two adjacent hexes. Distance between two opposite sides in a hex.

	public static void setXYasVertex(boolean b) {
		XYVertex=b;
	}
	public static void setBorders(int b){
		BORDERS=b;
	}

	/** This functions takes the Side length in pixels and uses that as the basic dimension of the hex.
            It calculates all other needed constants from this dimension.
	*/
	public static void setHeight(int height) {
		h = height;			// h = basic dimension: height (distance between two adj centres aka size)
		r = h/2;			// r = radius of inscribed circle
		s = (int) (h / 1.73205);	// s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h / sqrt(3)
		t = (int) (r / 1.73205);	// t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2 sqrt(3)) = r / sqrt(3)
	}

/*********************************************************
Name: hex()
Parameters: (x0,y0) This point is normally the top left corner 
    of the rectangle enclosing the hexagon. 
    However, if XYVertex is true then (x0,y0) is the vertex of the 
    top left corner of the hexagon. 
Returns: a polygon containing the six points.
Called from: drawHex(), fillhex()
Purpose: This function takes two points that describe a hexagon
and calculates all six of the points in the hexagon.
*********************************************************/
	public static Polygon hex (int x0, int y0) {

		int y = y0 + BORDERS;
		int x = x0 + BORDERS; // + (XYVertex ? t : 0); //Fix added for XYVertex = true.
		if (s == 0  || h == 0) {
			System.out.println("ERROR: size of hex has not been set");
			return new Polygon();
		}

		int[] cx,cy;

		if (XYVertex) 
			cx = new int[] {x,x+s,x+s+t,x+s,x,x-t};  //this is for the top left vertex being at x,y. Which means that some of the hex is cutoff.
		else
			cx = new int[] {x+t,x+s+t,x+s+t+t,x+s+t,x+t,x};	//this is for the whole hexagon to be below and to the right of this point

		cy = new int[] {y,y,y+r,y+r+r,y+r+r,y+r};
		return new Polygon(cx,cy,6);
	}

/********************************************************************
Name: drawHex()
Parameters: (i,j) : the x,y coordinates of the initial point of the hexagon
	    g2: the Graphics2D object to draw on.
Returns: void
Calls: hex() 
Purpose: This function draws a hexagon based on the initial point (x,y).
The hexagon is drawn in the colour specified in hexgame.COLOURELL.
*********************************************************************/
	public static void drawHex(int i, int j, Graphics2D g2) {
		int x = i * (s+t);
		int y = j * h + (i%2) * h/2;
		Polygon poly = hex(x,y);
		g2.setColor(hexgame.COLOURCELL);
		g2.fillPolygon(poly);
		g2.setColor(hexgame.COLOURGRID);
		g2.drawPolygon(poly);
	}

/***************************************************************************
* Name: fillHex()
* Parameters: (i,j) : the x,y coordinates of the initial point of the hexagon
		n   : an integer number to indicate a letter to draw in the hex
		g2  : the graphics context to draw on
* Return: void
* Called from:
* Calls: hex()
*Purpose: This draws a filled in polygon based on the coordinates of the hexagon.
	  The colour depends on whether n is negative or positive.
	  The colour is set by hexgame.COLOURONE and hexgame.COLOURTWO.
	  The value of n is converted to letter and drawn in the hexagon.
*****************************************************************************/
	public static void fillHex(int i, int j, int n, Graphics2D g2) {
		int x = i * (s+t);
		int y = j * h + (i%2) * h/2;
		Polygon poly = hex(x,y);
		if (n < 0) {
			g2.setColor(hexgame.COLOURCELL);
			g2.fillPolygon(poly);
			g2.setColor(hexgame.COLOURGRID);
			g2.drawPolygon(poly);
		}
		if (n > 0) {
			g2.setColor(hexgame.COLOURTWO);
			g2.fillPolygon(poly);
			g2.setColor(hexgame.COLOURGRID);
			g2.drawPolygon(poly);
		}
		if (n == 0) {
			g2.setColor(hexgame.COLOURTHREE);
			g2.fillPolygon(poly);
			g2.setColor(hexgame.COLOURGRID);
			g2.drawPolygon(poly);
		}
	}

	//This function changes pixel location from a mouse click to a hex grid location
/*****************************************************************************
* Name: pxtoHex (pixel to hex)
* Parameters: mx, my. These are the co-ordinates of mouse click.
* Returns: point. A point containing the co-ordinates of the hex that is clicked in.
           If the point clicked is not a valid hex (i.e. on the borders of the board, (-1,-1) is returned.
* Function: This only works for hexes in the FLAT orientation. The POINTY orientation would require
            a whole other function (different math).
            It takes into account the size of borders.
            It also works with XYVertex being True or False.
*****************************************************************************/
	public static Point pxtoHex(int mx, int my) {
		Point p = new Point(-1,-1);

		//correction for BORDERS and XYVertex
		mx -= BORDERS;
		my -= BORDERS;
		if (XYVertex) mx += t;

		int x = (mx / (s+t)); //this gives a quick value for x. It works only on odd cols and doesn't handle the triangle sections. It assumes that the hexagon is a rectangle with width s+t (=1.5*s).
		int y = ((my - (x%2)*r)/h); //this gives the row easily. It needs to be offset by h/2 (=r)if it is in an even column

		//dx,dy are the number of pixels from the hex boundary. (i.e. relative to the hex clicked in)
		int dx = mx - x*(s+t);
		int dy = my - y*h;

		if (my - (x%2)*r < 0) return p; // prevent clicking in the open halfhexes at the top of the screen

		
		//even columns
		if (x%2==0) {
			if (dy > r) {	//bottom half of hexes
				if (dx * r /t < dy - r) {
					x--;
				}
			}
			if (dy < r) {	//top half of hexes
				if ((t - dx)*r/t > dy ) {
					x--;
					y--;
				}
			}
		} else {  // odd columns
			if (dy > h) {	//bottom half of hexes
				if (dx * r/t < dy - h) {
					x--;
					y++;
				}
			}
			if (dy < h) {	//top half of hexes
				if ((t - dx)*r/t > dy - r) {
					x--;
				}
			}
		}
		p.x=x;
		p.y=y;
		return p;
	}
}