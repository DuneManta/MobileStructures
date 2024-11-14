package builder;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*; 

/**********************************
  This is the main class of a Java program to play a game based on hexagonal tiles.
  The mechanism of handling hexes is in the file hexmech.java.

  Written by: M.H.
  Date: December 2012

 ***********************************/

public class hexgame
{
  public hexgame() {
		initGame();
		createAndShowGUI();
	}

	//constants and global variables
	final static Color COLOURBACK =  Color.GRAY;
	final static Color COLOURCELL =  Color.WHITE;
	final static Color COLOURGRID =  Color.BLACK;
	final static Color COLOURTWO = new Color(119, 138, 255, 255);
	final static Color COLOURTHREE = Color.RED;
	final static int BSIZE = 20; //board size.
	final static int HEXSIZE = 36;	//hex size in pixels
	final static int BORDERS = 15;
	final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS*3; //screen size (vertical dimension).

	int[][] board = new int[BSIZE][BSIZE];

	void initGame(){

		hexmech.setXYasVertex(false); //RECOMMENDED: leave this as FALSE.

		hexmech.setHeight(HEXSIZE); //Either setHeight or setSize must be run to initialize the hex
		hexmech.setBorders(BORDERS);

		for (int i=0;i<BSIZE;i++) {
			for (int j=0;j<BSIZE;j++) {
				board[i][j]=-1;
			}
		}

		board[9][9] = 1;
		board[9][10] = 1;
	}

	private void createAndShowGUI()
	{
		DrawingPanel panel = new DrawingPanel();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		JFrame frame = new JFrame("Grid Selector");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		Container content = frame.getContentPane();
		content.add(panel);
		frame.setSize( (int)(SCRSIZE/1.23), SCRSIZE);
		frame.setLocation( screenSize.width - frame.getWidth(), screenSize.height - frame.getHeight() - 50 );
		frame.setVisible(true);
	}


	class DrawingPanel extends JPanel
	{
		public DrawingPanel()
		{	
			setBackground(COLOURBACK);

			MyMouseListener ml = new MyMouseListener();            
			addMouseListener(ml);
		}

		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g2);
			//draw grid
			for (int i=0;i<BSIZE;i++) {
				for (int j=0;j<BSIZE;j++) {
					hexmech.drawHex(i,j,g2);
				}
			}
			//fill in hexes
			for (int i=0;i<BSIZE;i++) {
				for (int j=0;j<BSIZE;j++) {
					hexmech.fillHex(i,j,board[i][j],g2);
				}
			}
		}

		class MyMouseListener extends MouseAdapter	{	//inner class inside DrawingPanel 
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				boolean adjacentFlag = false;
				Point p = new Point( hexmech.pxtoHex(x,y) );
				if (p.x < 0 || p.y < 0 || p.x >= BSIZE || p.y >= BSIZE) return;

				int filledHexes = 0;
				int availableHexes = StructureEditor.userStructure.GetNumHexes();
				for (int i = 0; i < BSIZE; i++) {
					for (int j = 0; j < BSIZE; j++) {
						if (board[i][j] == 1) {
							filledHexes++;
							if ((p.x == i) && ((p.y + 1 == j) || (p.y - 1 == j))) {
								adjacentFlag = true;
							} else if ((p.x % 2 != 0) && ((p.x - 1 == i) || (p.x + 1 == i)) && ((p.y + 1 == j) || (p.y == j))) {
								adjacentFlag = true;
							} else if ((p.x % 2 == 0) && ((p.x - 1 == i) || (p.x + 1 == i)) && ((p.y - 1 == j) || (p.y == j))) {
								adjacentFlag = true;
							}
						}
					}
				}

				if (!adjacentFlag) {
					return;
				}

				if (availableHexes == 2 && filledHexes == 2) {
					return;
				}
				if (filledHexes == 2) {
					board[p.x][p.y] = 1;
				} else if (filledHexes < availableHexes) {
					board[p.x][p.y] *= -1;
				} else {
					board[p.x][p.y] = -1;
				}

				repaint();
            }
		} //end of MyMouseListener class 
	} // end of DrawingPanel class
}