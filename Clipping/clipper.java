//
//  Clipper.java
//
//
//  Created by Joe Geigel on 1/21/10.
//  Coendyright 2010 __MyCompanyName__. All rights reserved.
//

/**
 * Object for performing clipping
 *
 */

public class clipper {

	/**
	 * clipPolygon
	 *
	 * Clip the polygon with vertex count in and vertices inx/iny against the
	 * rectangular clipping region specified by lower-left corner (x0,y0) and
	 * upper-right corner (x1,y1). The resulting vertices are placed in
	 * outx/outy.
	 *
	 * The routine should return the the vertex count of the polygon resulting
	 * from the clipping.
	 *
	 * @param in
	 *            the number of vertices in the polygon to be clipped
	 * @param inx
	 *            - x coords of vertices of polygon to be clipped.
	 * @param iny
	 *            - y coords of vertices of polygon to be clipped.
	 * @param outx
	 *            - x coords of vertices of polygon resulting after clipping.
	 * @param outy
	 *            - y coords of vertices of polygon resulting after clipping.
	 * @param x0
	 *            - x coord of lower left of clipping rectangle.
	 * @param y0
	 *            - y coord of lower left of clipping rectangle.
	 * @param x1
	 *            - x coord of upper right of clipping rectangle.
	 * @param y1
	 *            - y coord of upper right of clipping rectangle.
	 *
	 * @return number of vertices in the polygon resulting after clipping
	 *
	 */

	/*
	 * References : http://www.cs.rit.edu/~icss571/clipTrans/PolyClipBack.html
	 * and the one explained in class room
	 */

	public int clipPolygon(int in, float inx[], float iny[], float outx[],
			float outy[], float x0, float y0, float x1, float y1) {
		float endx, endy, startx, starty, Clip_x0 = 0, Clip_x1 = 0, Clip_y0 = 0, Clip_y1 = 0;
		endx = inx[in - 1];
		endy = iny[in - 1];
		float outstartxy0[][] = new float[outx.length][outx.length];

		float NewXY[] = new float[2];
		int outputlen_1 = -1;

		Clip_x0 = x0;
		Clip_y0 = y0; // bottom edge
		Clip_x1 = x1;
		Clip_y1 = y0;

		outstartxy0[0] = outx;
		outstartxy0[1] = outy;

		for (int index = 0; index < in; index++) {
			startx = inx[index];
			starty = iny[index];
			if (insideEdge(startx, starty, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) {// Cases
																					// 1
																					// &
																					// 4
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case																					// 1
					
					outstartxy0 = outputList(startx, starty, ++outputlen_1,
							outstartxy0);
				} else { // Case 4
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy0 = outputList(NewXY[0], NewXY[1], ++outputlen_1,
							outstartxy0);
					outstartxy0 = outputList(startx, starty, ++outputlen_1,
							outstartxy0);
				}
			} else { // Cases 2 & 3
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// 2
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy0 = outputList(NewXY[0], NewXY[1], ++outputlen_1,
							outstartxy0);
				} else // case 3
				{
					// Case 3 has no outputList
				}
			}
			endx = startx;
			endy = starty;
		}

		// Right Edge
		float outstartxy1[][] = new float[outstartxy0[0].length][outstartxy0[0].length];

		int outputlen_2 = -1;
		if (outputlen_1 != -1) {
			endx = outstartxy0[0][outputlen_1];
			endy = outstartxy0[1][outputlen_1];
		}
		Clip_x0 = x1;
		Clip_y0 = y0; // right edge vertices
		Clip_x1 = x1;
		Clip_y1 = y1;

		outstartxy1[0] = outstartxy0[0];
		outstartxy1[1] = outstartxy0[1];

		for (int index = 0; index <= outputlen_1; index++) {
			startx = outstartxy0[0][index];
			starty = outstartxy0[1][index];
			if (insideEdge(startx, starty, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) {// Cases
																					// 1
																					// &
																					// 4
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// 1
					outstartxy1 = outputList(startx, starty, ++outputlen_2,
							outstartxy1);
				} else { // Case 4
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy1 = outputList(NewXY[0], NewXY[1], ++outputlen_2,
							outstartxy1);
					outstartxy1 = outputList(startx, starty, ++outputlen_2,
							outstartxy1);
				}
			} else { // Cases 2 & 3
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// 2
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy1 = outputList(NewXY[0], NewXY[1], ++outputlen_2,
							outstartxy1);
				} else // case 3
				{
					// Case 3 has no outputList
				}
			}
			endx = startx;
			endy = starty;
		}

		// Top Edge
		float outstartxy2[][] = new float[outstartxy1[0].length][outstartxy1[0].length];

		int outputlen_3 = -1;

		if (outputlen_2 != -1) {
			endx = outstartxy1[0][outputlen_2];
			endy = outstartxy1[1][outputlen_2];
		}

		Clip_x0 = x1;
		Clip_y0 = y1; // Top edge vertices
		Clip_x1 = x0;
		Clip_y1 = y1;

		outstartxy2[0] = outstartxy1[0];
		outstartxy2[1] = outstartxy1[1];

		for (int index = 0; index <= outputlen_2; index++) {
			startx = outstartxy1[0][index];
			starty = outstartxy1[1][index];
			if (insideEdge(startx, starty, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) {// Cases
																					// 1
																					// &
																					// 4
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// 1
					outstartxy2 = outputList(startx, starty, ++outputlen_3,
							outstartxy2);
				} else { // Case 4
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy2 = outputList(NewXY[0], NewXY[1], ++outputlen_3,
							outstartxy2);
					outstartxy2 = outputList(startx, starty, ++outputlen_3,
							outstartxy2);
				}
			} else { // Cases 2 & 3
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// 2
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy2 = outputList(NewXY[0], NewXY[1], ++outputlen_3,
							outstartxy2);
				} else // case 3
				{
					// Case 3 has no outputList
				}
			}
			endx = startx;
			endy = starty;
		}

		// Left Edge
		float outstartxy3[][] = new float[outstartxy2[0].length][outstartxy2[0].length];

		int outputlen_4 = -1;
		if (outputlen_3 != -1) {
			endx = outstartxy2[0][outputlen_3];
			endy = outstartxy2[1][outputlen_3];
		}

		Clip_x0 = x0;
		Clip_y0 = y1; // Left edge Vertices
		Clip_x1 = x0;
		Clip_y1 = y0;

		outstartxy3[0] = outstartxy2[0];
		outstartxy3[1] = outstartxy2[1];

		for (int index = 0; index <= outputlen_3; index++) {
			startx = outstartxy2[0][index];
			starty = outstartxy2[1][index];
			if (insideEdge(startx, starty, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) {// Cases
																					// 1
																					// &
																					// 4
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// 1
					outstartxy3 = outputList(startx, starty, ++outputlen_4,
							outstartxy3);
				} else { // Case 4
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy3 = outputList(NewXY[0], NewXY[1], ++outputlen_4,
							outstartxy3);
					outstartxy3 = outputList(startx, starty, ++outputlen_4,
							outstartxy3);
				}
			} else { // Cases 2 & 3
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// 2
					NewXY = intersect(endx, endy, startx, starty, Clip_x0,
							Clip_y0, Clip_x1, Clip_y1, NewXY);
					outstartxy3 = outputList(NewXY[0], NewXY[1], ++outputlen_4,
							outstartxy3);
				} else // case 3
				{
					// Case 3 has no outputList
				}
			}
			endx = startx;
			endy = starty;
		}
		outx=outstartxy3[0];
		outy=outstartxy3[1];
		return (outputlen_4 + 1); // should return number of vertices in clipped
									// poly.
	}

	public boolean insideEdge(float startx, float starty, float Clip_x0,
			float Clip_y0, float Clip_x1, float Clip_y1) {
		boolean result = false;

		if (Clip_y1 > Clip_y0) // right
		{
			if (startx <= Clip_x0) {
				result = true;
			}
		} else if (Clip_x1 > Clip_x0) // bottom
		{
			if (starty >= Clip_y0) {
				result = true;
			}
		} else if (Clip_y1 < Clip_y0) // left
		{
			if (startx >= Clip_x0) {
				result = true;
			}
		}

		else if (Clip_x1 < Clip_x0) // top
		{
			if (starty <= Clip_y0) {
				result = true;
			}
		}

		return result;
	}

	public float[][] outputList(float x, float y, int len, float[][] arr) {

		arr[0][len] = x;
		arr[1][len] = y;
		return arr;
	}

	public float[] intersect(float endx, float endy, float startx,
			float starty, float Clip_x0, float Clip_y0, float Clip_x1,
			float Clip_y1, float NewXY[]) {
		
		if(Clip_y1 == Clip_y0)
		{
			NewXY[1] = Clip_y0;
			NewXY[0] = endx+(Clip_y0 - endy)*(startx - endx)/(starty - endy);
		}
		else
		{
			NewXY[0] = Clip_x0;
			NewXY[1] = endy+(Clip_x0 - endx)*(starty - endy)/(startx - endx);
		}
		return NewXY;
	}
}
