//
//  cgCanvas.java
//
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester Institute of Technology. All rights reserved.
//

/**
 * This is a simple canvas class for adding functionality for the
 * 2D portion of Computer Graphics.
 *
 */

import Jama.*;

import java.util.*;

// Reference : https://github.com/smy2748/Friendly-Coconut/blob/master/cgCanvas.java
@SuppressWarnings("serial")
public class cgCanvas extends simpleCanvas {

	float bottom, right, top, left;
	float Sx, Sy, tx, ty;
	Matrix Transform = null;
	int id;
	@SuppressWarnings("rawtypes")
	List poly_List;
	int width, height, x, y;
	simpleCanvas C;
	int max_width, max_height;
	int count=0;
	float new_x_val[];
	float new_y_val[];
	/**
	 * Constructor
	 *
	 * @param w
	 *            width of canvas
	 * @param h
	 *            height of canvas
	 */
	@SuppressWarnings("rawtypes")
	cgCanvas(int w, int h) {

		super(w, h);
		max_width = w;
		max_height = h;
		this.id = 0;
		Transform = Matrix.identity(3, 3);
		bottom = 0;
		right = 0;
		top = 0;
		left = 0;
		poly_List = new ArrayList<ArrayList>();
		width = 0;
		height = 0;
		x = 0;
		y = 0;
		new_x_val = new float[150];
		new_y_val = new float[150];
	}

	/**
	 * addPoly - Adds and stores a polygon to the canvas. Note that this method
	 * does not draw the polygon, but merely stores it for later draw. Drawing
	 * is initiated by the draw() method.
	 *
	 * Returns a unique integer id for the polygon.
	 *
	 * @param x
	 *            - Array of x coords of the vertices of the polygon to be added
	 * @param y
	 *            - Array of y coords of the vertices of the polygin to be added
	 * @param n
	 *            - Number of verticies in polygon
	 *
	 * @return a unique integer identifier for the polygon
	 */
	@SuppressWarnings("unchecked")
	public int addPoly(float x[], float y[], int n) {
		@SuppressWarnings("rawtypes")
		List Ind_poly = new ArrayList();
		float x_realVertices[] = new float[n];
		float y_realVertices[] = new float[n];

		for (int i = 0; i < n; i++) {
			x_realVertices[i] = x[i];
			y_realVertices[i] = y[i];
		}

		Ind_poly.add(x_realVertices);
		Ind_poly.add(y_realVertices);
		Ind_poly.add(n);
		poly_List.add(id);
		poly_List.set(id, Ind_poly);
		return id++;
	}

	/**
	 * drawPoly - Draw the polygon with the given id. Should draw the polygon
	 * after applying the current transformation on the vertices of the polygon.
	 *
	 * @param polyID
	 *            - the ID of the polygon to be drawn
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void drawPoly(int polyID) {
		List curr_poly = new ArrayList();

		curr_poly = (List) poly_List.get(polyID);

		curr_poly = (List) Transform(curr_poly, Transform);

		float x0 = bottom;
		float y0 = left;
		float x1 = right;
		float y1 = top;

		int num_sides = clipPolygon((int) curr_poly.get(2),
				(float[]) curr_poly.get(0), (float[]) curr_poly.get(1),
				new_x_val, new_y_val, x0, y0, x1, y1);
		List upd_Vertices = new ArrayList();

		upd_Vertices.add(new_x_val);
		upd_Vertices.add(new_y_val);
		upd_Vertices.add(num_sides);

		Matrix View_port = null;

		Sx = (width) / (right - left);
		Sy = (height) / (top - bottom);
		tx = (right * x - left * (x + width)) / (right - left);
		ty = (top * y - bottom * (y + height)) / (top - bottom);

		
		double view_arr[][] = new double[3][3];
		view_arr[0][0] = Sx;
		view_arr[0][1] = 0;
		view_arr[0][2] = tx;
		view_arr[1][0] = 0;
		view_arr[1][1] = Sy;
		view_arr[1][2] = ty;
		view_arr[2][0] = 0;
		view_arr[2][1] = 0;
		view_arr[2][2] = 1;
		View_port = Matrix.constructWithCopy(view_arr);

		curr_poly = Transform(upd_Vertices, View_port);
		drawPolygon((int) curr_poly.get(2), (float[]) curr_poly.get(0),
				(float[]) curr_poly.get(1), this);
	}

	/**
	 * clearTransform - Set the current transformation to the identity matrix.
	 */
	public void clearTransform() {
		Transform = Matrix.identity(3, 3);
	}

	/**
	 * translate - Add a translation to the current transformation by
	 * pre-multiplying the appropriate translation matrix to the current
	 * transformation matrix.
	 *
	 * @param x
	 *            - Amount of translation in x
	 * @param y
	 *            - Amount of translation in y
	 */
	public void translate(float x, float y) {
		double[][] factor = new double[3][3];
		factor[0][0] = 1;
		factor[0][1] = 0;
		factor[0][2] = x;
		factor[1][0] = 0;
		factor[1][1] = 1;
		factor[1][2] = y;
		factor[2][0] = 0;
		factor[2][1] = 0;
		factor[2][2] = 1;
		Matrix fact_mat = Matrix.constructWithCopy(factor);
		Transform = fact_mat.times(Transform);
	}

	/**
	 * rotate - Add a rotation to the current transformation by pre-multiplying
	 * the appropriate rotation matrix to the current transformation matrix.
	 *
	 * @param degrees
	 *            - Amount of rotation in degrees
	 */
	public void rotate(float degrees) {
		double rotate_arr[][] = new double[3][3];
		rotate_arr[0][0] = Math.cos(Math.toRadians(degrees));
		rotate_arr[0][1] = -1 * (Math.sin(Math.toRadians(degrees)));
		rotate_arr[0][2] = 0;
		rotate_arr[1][0] = Math.sin(Math.toRadians(degrees));
		rotate_arr[1][1] = Math.cos(Math.toRadians(degrees));
		rotate_arr[1][2] = 0;
		rotate_arr[2][0] = 0;
		rotate_arr[2][1] = 0;
		rotate_arr[2][2] = 1;
		Matrix rotate_Matrix = Matrix.constructWithCopy(rotate_arr);

		Transform = rotate_Matrix.times(Transform);

	}

	/**
	 * scale - Add a scale to the current transformation by pre-multiplying the
	 * appropriate scaling matrix to the current transformation matrix.
	 *
	 * @param x
	 *            - Amount of scaling in x
	 * @param y
	 *            - Amount of scaling in y
	 */
	public void scale(float x, float y) {

		double scale_arr[][] = new double[3][3];
		scale_arr[0][0] = x;
		scale_arr[0][1] = 0;
		scale_arr[0][2] = 0;
		scale_arr[1][0] = 0;
		scale_arr[1][1] = y;
		scale_arr[1][2] = 0;
		scale_arr[2][0] = 0;
		scale_arr[2][1] = 0;
		scale_arr[2][2] = 1;
		Matrix scale_matrix = Matrix.constructWithCopy(scale_arr);

		Transform = scale_matrix.times(Transform);
	}

	/**
	 * setClipWindow - defines the clip window
	 *
	 * @param bottom
	 *            - y coord of bottom edge of clip window (in world coords)
	 * @param top
	 *            - y coord of top edge of clip window (in world coords)
	 * @param left
	 *            - x coord of left edge of clip window (in world coords)
	 * @param right
	 *            - x coord of right edge of clip window (in world coords)
	 */
	public void setClipWindow(float bottom, float top, float left, float right) {
		this.bottom = bottom;
		this.top = top;
		this.left = left;
		this.right = right;
	}

	/**
	 * setViewport - defines the viewport
	 *
	 * @param xmin
	 *            - x coord of lower left of view window (in screen coords)
	 * @param ymin
	 *            - y coord of lower left of view window (in screen coords)
	 * @param width
	 *            - width of view window (in world coords)
	 * @param height
	 *            - width of view window (in world coords)
	 */
	public void setViewport(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		// YOUR IMPLEMENTATION HERE
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public void drawPolygon(int n, float x[], float y[], simpleCanvas C) {
		float ymin = 0;
		float ymax = 0;
		double xval = 0.0;
		double Islope = 0.0;
		int scanlineMax = 0;
		int scanlineMin = 0;
		List<ArrayList> Global_Edges = new ArrayList<ArrayList>();
		List<ArrayList> Active_Table = new ArrayList<ArrayList>();

		// Prepare Global edge table
		for (int i = 0; i < n; i++) {
			int k = i + 1;
			if ((k) == n) {
				k = 0;
			}

			if (y[i] < y[k]) {
				ymax = y[k];
				ymin = y[i];
				xval = x[i];

			} else if (y[i] > y[k]) {
				ymax = y[i];
				ymin = y[k];
				xval = x[k];
			}

			if (y[k] != y[i]) {
				Islope = ((double) x[k] - x[i]) / (y[k] - y[i]);
				List Entries = new ArrayList();
				Entries.add(ymin);
				Entries.add(ymax);
				Entries.add(xval);
				Entries.add(Islope);
				Global_Edges.add((ArrayList) Entries);
			}
		}

		scanlineMax = Math.round(getMax_Maxy(Global_Edges));
		scanlineMin = Math.round(getMin_Miny(Global_Edges));

		// Iterating through each scanline
		for (int scanline = scanlineMin; scanline <= scanlineMax; scanline++) {

			int parity = 0;
			// Add to global table
			for (int index = 0; index < Global_Edges.size(); index++) {
				if (Math.round((float) Global_Edges.get(index).get(0)) == scanline) {
					List Entries = new ArrayList();
					Entries.add(Global_Edges.get(index).get(1));
					Entries.add(Global_Edges.get(index).get(2));
					Entries.add(Global_Edges.get(index).get(3));
					Active_Table.add((ArrayList) Entries);

				}

			}

			// delete from global table
			// ------------------------------------------------------------------------------
			ArrayList<ArrayList> GlobalcopyArr = new ArrayList<ArrayList>(
					Global_Edges);
			for (List copy : Global_Edges) {
				if (Math.round((float) copy.get(0)) == scanline) {
					GlobalcopyArr.remove(copy);
				}

			}
			Global_Edges.clear();
			Global_Edges.addAll(GlobalcopyArr);

			// ------------------------------------------------------------------------------

			// delete from active table
			// ------------------------------------------------------------------------------
			ArrayList<ArrayList> ActivecopyArr = new ArrayList<ArrayList>(
					Active_Table);
			for (List copy : Active_Table) {
				if (Math.round((float) copy.get(0)) == scanline) {
					ActivecopyArr.remove(copy);
				}

			}
			Active_Table.clear();
			Active_Table.addAll(ActivecopyArr);
			// ------------------------------------------------------------------------------

			Active_Table = sortActiveTab(Active_Table);
			// processing Active table
			for (int i = 0; i < Active_Table.size() - 1; i++) {
				double max_X = getMax_X(Active_Table);
				double xinc = (double) Active_Table.get(i).get(1);
				parity++;
				while (xinc < (double) Active_Table.get(i + 1).get(1)) {
					if ((parity % 2) != 0) {
						int xpoint = (int) Math.ceil(xinc);
						if (xpoint < max_width && scanline >0 && xpoint > 0) {
							C.setPixel(xpoint, scanline);
						}
						xinc++;

					} else {
						xinc++;
					}

				}
			}
			Active_Table = adjustXVal(Active_Table);

		}
	}

	@SuppressWarnings("rawtypes")
	public double getNextElem(List<ArrayList> arr, int index) {

		double next = -1;
		if ((index + 1) < arr.size()) {
			next = (double) arr.get(index + 1).get(1);
		}
		return next;
	}

	// get max x value
	@SuppressWarnings("rawtypes")
	public double getMax_X(List<ArrayList> arr) {
		double max_x = 0;

		for (int i = 0; i < arr.size(); i++) {
			if (max_x < (double) arr.get(i).get(1)) {

				max_x = (double) arr.get(i).get(1);
			}
		}

		return max_x;
	}

	// adjust x values
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ArrayList> adjustXVal(List<ArrayList> arr) {

		for (List copy : arr) {
			copy.set(1, (double) copy.get(1) + (double) copy.get(2));
		}
		return arr;
	}

	// sort active table
	@SuppressWarnings("rawtypes")
	public List<ArrayList> sortActiveTab(List<ArrayList> arr) {
		for (int k = 0; k < arr.size() - 1; k++) {
			for (int i = 0; i < arr.size() - 1; i++) {
				if ((double) arr.get(i).get(1) > (double) arr.get(i + 1).get(1)) {
					ArrayList temp = arr.get(i);
					arr.set(i, arr.get(i + 1));
					arr.set(i + 1, temp);
				}
			}
		}

		return arr;
	}

	// get max y value
	@SuppressWarnings("rawtypes")
	public float getMax_Maxy(List<ArrayList> arr) {
		int max_ymax = Math.round((float) arr.get(0).get(1));

		for (int i = 0; i < arr.size(); i++) {
			if (max_ymax < Math.round((float) arr.get(i).get(1))) {
				max_ymax = Math.round((float) arr.get(i).get(1));
			}
		}

		return max_ymax;
	}

	// get min y value
	@SuppressWarnings("rawtypes")
	public float getMin_Miny(List<ArrayList> arr) {
		int min_ymin = Math.round((float) arr.get(0).get(0));

		for (int i = 0; i < arr.size(); i++) {
			if (min_ymin > Math.round((float) arr.get(i).get(0))) {
				min_ymin = Math.round((float) arr.get(i).get(0));
			}
		}

		return min_ymin;
	}

	// get max x value
	@SuppressWarnings("rawtypes")
	public double getMax_Xval(List<ArrayList> arr) {
		int max_xval = Math.round((float) arr.get(0).get(1));

		for (int i = 0; i < arr.size(); i++) {
			if (max_xval > Math.round((float) arr.get(i).get(1))) {
				max_xval = Math.round((float) arr.get(i).get(1));
			}
		}

		return max_xval;
	}
	
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
		
		for(int i =0;i<outx.length;i++)
		{
			outstartxy0[0][i] = outx[i];
			outstartxy0[1][i] = outy[i];
		
		}
			
		for (int index = 0; index < in; index++) {
			startx = inx[index];
			starty = iny[index];
			if (insideEdge(startx, starty, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) {// Cases
																					// 1
																					// &
																					// 4
				if (insideEdge(endx, endy, Clip_x0, Clip_y0, Clip_x1, Clip_y1)) { // Case
																					// //
																					// 1

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

		for(int i =0;i<outstartxy0.length;i++)
		{
		outstartxy1[0][i] = outstartxy0[0][i];
		outstartxy1[1][i] = outstartxy0[1][i];
		}
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



		for(int i =0;i<outstartxy1.length;i++)
		{
			outstartxy2[0][i] = outstartxy1[0][i];
			outstartxy2[1][i] = outstartxy1[1][i];
		}
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
		
		for(int i =0;i<outstartxy2.length;i++)
		{
			outstartxy3[0][i] = outstartxy2[0][i];
			outstartxy3[1][i] = outstartxy2[1][i];
		}

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
		
		float final_outx[] = new float[outputlen_4 + 1];
		float final_outy[] = new float[outputlen_4 + 1];

		new_x_val= outstartxy3[0];
		new_y_val= outstartxy3[1];
		
		outx = new float[outputlen_4 + 1];
		outy = new float[outputlen_4 + 1];
		
			outx = final_outx;
			outy = final_outy;			
		
		return (outputlen_4 + 1); // should return number of vertices in clipped
									// poly.
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List Transform(List Vertices, Matrix Curr_Matrix) {
		List new_Vertex_List = new ArrayList();
		Matrix vertices_Matrix = null;
		float x_val[] = (float[]) Vertices.get(0);
		float y_val[] = (float[]) Vertices.get(1);
		float new_x[] = new float[(int) Vertices.get(2)];
		float new_y[] = new float[(int) Vertices.get(2)];

		for (int index = 0; index < (int) Vertices.get(2); index++) {
			double[][] vertex_arr = new double[3][1];
			vertex_arr[0][0] = x_val[index];
			vertex_arr[1][0] = y_val[index];
			vertex_arr[2][0] = 1;
			vertices_Matrix = Matrix.constructWithCopy(vertex_arr);
			Matrix new_Vals = Curr_Matrix.times(vertices_Matrix);
			new_x[index] = (float) new_Vals.get(0, 0);
			new_y[index] = (float) new_Vals.get(1, 0);
			new_Vertex_List.add(new_x);
			new_Vertex_List.add(new_y);
			new_Vertex_List.add(Vertices.get(2));
		}
		return new_Vertex_List;
	}
}
