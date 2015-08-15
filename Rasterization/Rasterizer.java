//
//  Rasterizer.java
//  
//
//  Created by Joe Geigel on 1/21/10.
//	Author: Kamaleshwar Panapakam
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

/**
 * 
 * A simple class for performing rasterization algorithms.
 *
 */

import java.util.*;

public class Rasterizer {

	/**
	 * number of scanlines
	 */
	int n_scanlines;

	/**
	 * Constructor
	 *
	 * @param n
	 *            number of scanlines
	 *
	 */
	Rasterizer(int n) {
		n_scanlines = n;
	}

	/**
	 * Draw a line from (x0,y0) to (x1, y1) on the simpleCanvas C.
	 *
	 * Implementation should be using the Midpoint Method
	 *
	 * You are to add the implementation here using only calls to C.setPixel()
	 *
	 * @param x0
	 *            x coord of first endpoint
	 * @param y0
	 *            y coord of first endpoint
	 * @param x1
	 *            x coord of second endpoint
	 * @param y1
	 *            y coord of second endpoint
	 * @param C
	 *            The canvas on which to apply the draw command.
	 */
	
	public void drawLine(int x0, int y0, int x1, int y1, simpleCanvas C) {
		int dE, dW, dNW, x, y, d, dNE, dN, dS, dSW,dSE;
		int dy = y1 - y0, dx = x1 - x0;
		int slope = 0;
		
		//To avoid arthimetic exception we check for dx!=0 condition
		if (dx != 0) {
			
			// calculate slope
			slope = dy / dx;
			
			// Below code is taken from slides in class

			// First quadrant
			
			if (slope >= 0 && slope <= 1 && dx > 0 && dy > 0) {

				dE = 2 * dy;
				dNE = 2 * (dy - dx);
				d = dE - dx;

				for (x = x0, y = y0; x <= x1; ++x) {
					C.setPixel(x, y);

					if (d <= 0) { /* choose E */
						d += dE;
					} else { /* choose NE */
						++y;
						d += dNE;
					}
				}
			}
			
//			third quadrant
			else if (slope >= 0 && slope <= 1 && dx < 0 && dy < 0) {

				int temp;
				temp = x0;
				x0 = x1;
				x1 = temp;
				temp = y0;
				y0 = y1;
				y1 = temp;
				dy = y1 - y0;
				dx = x1 - x0;
				dE = 2 * dy;
				dNE = 2 * (dy - dx);
				d = dE - dx;

				for (x = x0, y = y0; x <= x1; ++x) {
					C.setPixel(x, y);

					if (d <= 0) { /* choose E */
						d += dE;
					} else { /* choose NE */
						++y;
						d += dNE;
					}
				}
			}

//			second quadrant
			else if (slope <= 0 && slope >= -1 && dx < 0 && dy > 0) {
				
				int temp=x0;
				x0=x1;
				x1=temp;
				
				dW = 2*dy;
				dNW = 2*(dy + dx);
				d = 2 * dy - dx;

				for (x = x0, y = y1; x <= x1; ++x) {
					C.setPixel(x, y);

					if (d <= 0) {
						d += dW;
					} else { 
						--y;
						d += dNW;
					}
				}
			}

//			fourth quadrant
			else if (slope <= 0 && slope >= -1 && dx > 0 && dy < 0) {
				
				int temp=y0;
				y0=y1;
				y1=temp;
				
				dE = 2*dy;
				dSE = 2*(dy + dx);
				d = dE + dx;

				for (x = x0, y = y1; x <= x1; ++x) {
					C.setPixel(x, y);

					if (d <= 0) { /* choose E */
						d += dSE;
						--y;
					} else { /* choose NE */
						d += dE;
					}
				}
			}

//			Horizontal line from right to left
			else if ( dx < 0 && dy == 0) {

				int temp;
				temp = x0;
				x0 = x1;
				x1 = temp;
				
				dE = 2 * -dx;
				d = -dx;

				for (x = x0, y = y1; x <= x1; ++x) {

					C.setPixel(x, y);

					d += dE;
				}

			}

//			Horizontal line from left to right
			else if (dx > 0 && dy == 0) {

				dNE = 2 * -dx;
				d = -dx;

				for (x = x0, y = y1; x <= x1; ++x) {

					C.setPixel(x, y);

					d += dNE;
				}

			}

//			slope from 1 to infinity .i.e more than 45 degrees
			else if (slope > 1 && dx > 0 && dy > 0) {
				
				dN = -2*dx;
				dNE = 2*dy+dN;
				d = dy-dN;

				for (x = x0, y = y0; y <= y1; ++y) {
					C.setPixel(x, y);

					if (d <= 0) { /* choose E */
						++x;
						d += dNE;
					} else { /* choose NE */						
						d += dN;
					}
				}
			}
			
//			slope from 1 to infinity .i.e more than 45 degrees from reverse
			
			else if (slope > 1 && dx < 0 && dy < 0) {

				int temp=x0;
				x0=x1;
				x1=temp;
				temp=y0;
				y0=y1;
				y1=temp;
							
				dS = -2*dx;
				dSW = 2*dy-dx;
				d = dy-2*dx;
				
				for (x = x0, y = y0; y <= y1; ++y) {
					C.setPixel(x, y);

					if (d <= 0) { /* choose E */
						d += dS;
					} else { /* choose NE */
						++x;
						d += dSW;
					}
				}
			}
			
//			slope from -1 to -infinity .i.e more than 135 degrees
			else if (slope < -1 && dx < 0 && dy > 0) {
				int temp=x0;
				x0=x1;
				x1=temp;
				
				dN = 2*dx;
				dNW = 2*dy + dx;
				d = dy + dN;

				for (x = x0, y = y1; x <= x1; --y) {
					C.setPixel(x, y);

					if (d <= 0) { 
						++x;/* choose E */
						d += dNW;
					} else { /* choose NE */						
						d += dN;
					}
				}

			}
			
//			slope from -1 to -infinity .i.e more than 135 degrees from reverse
			else if (slope < -1 && dx > 0 && dy < 0) {
				int temp=y0;
				y0=y1;
				y1=temp;
				
				dS = 2*dx;
				dSE = 2*dy + dx;
				d = dy +dS;

				for (x = x0, y = y1; x <= x1; --y) {
					C.setPixel(x, y);

					if (d <= 0) {
						d += dS;
					} else { 
						++x;
						d += dSE;
					}
				}

			}

		}

//		Vertical line
		if (dx == 0 && dy > 0) {
			dN = 2 * -dx;
			d = -dx;
			for (x = x0, y = y0; y <= y1; ++y) {
				C.setPixel(x, y);
				d += dN;
			}
		}

		else if (dx == 0 && dy < 0) {
			int temp = y0;
			y0 = y1;
			y1 = temp;
			dN = 2 * -dx;
			d = -dx;
			for (x = x1, y = y0; y <= y1; ++y) {
				C.setPixel(x, y);
				d += dN;
			}
		}

	}

}
