//
//  Rasterizer.java
//  
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

/**
 * 
 * This is a class that performas rasterization algorithms
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
	 *            - number of scanlines
	 *
	 */
	Rasterizer(int n) {
		n_scanlines = n;
	}

	/**
	 * Draw a filled polygon in the simpleCanvas C.
	 *
	 * The polygon has n distinct vertices. The coordinates of the vertices
	 * making up the polygon are stored in the x and y arrays. The ith vertex
	 * will have coordinate (x[i], y[i])
	 *
	 * You are to add the implementation here using only calls to C.setPixel()
	 */
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
		
		//Iterating through each scanline
		for (int scanline = scanlineMin; scanline <= scanlineMax; scanline++) {		 
			
			int parity=0;
			//Add to global table		
			for (int index = 0; index < Global_Edges.size(); index++) {
				if (Math.round((float) Global_Edges.get(index).get(0)) == scanline) {
					List Entries = new ArrayList();
					Entries.add(Global_Edges.get(index).get(1));
					Entries.add(Global_Edges.get(index).get(2));
					Entries.add(Global_Edges.get(index).get(3));
					Active_Table.add((ArrayList) Entries);
					
				}
								
			}
			
			
//			delete from global table
				//------------------------------------------------------------------------------		
						ArrayList<ArrayList> GlobalcopyArr=new ArrayList<ArrayList>(Global_Edges);
						for(List copy:Global_Edges)
						{
							if (Math.round((float) copy.get(0)) == scanline) {
								GlobalcopyArr.remove(copy);
							}
							
						}
						Global_Edges.clear();
						Global_Edges.addAll(GlobalcopyArr);
						
				//------------------------------------------------------------------------------
						
					
			//delete from active table
			//------------------------------------------------------------------------------				
			ArrayList<ArrayList> ActivecopyArr=new ArrayList<ArrayList>(Active_Table);
			for(List copy:Active_Table)
			{
				if (Math.round((float) copy.get(0)) == scanline) {
					ActivecopyArr.remove(copy);
				}
				
			}
			Active_Table.clear();
			Active_Table.addAll(ActivecopyArr);			
			//------------------------------------------------------------------------------
			
			Active_Table = sortActiveTab(Active_Table);
			//processing Active table
			for(int i=0;i<Active_Table.size()-1;i++)
			{	
				double max_X=getMax_X(Active_Table);
				double xinc=(double)Active_Table.get(i).get(1);
				parity++;
				while(xinc < (double)Active_Table.get(i+1).get(1))
				{
					if((parity%2)!=0)
					{	
						C.setPixel((int) Math.ceil(xinc), scanline);
						xinc++;
						
					}
					else
					{						
						xinc++;
					}
					
				}
			}
			Active_Table=adjustXVal(Active_Table);	
			
		}		
	}
	
	//get next element from the list
	public double getNextElem(List<ArrayList> arr,int index) {
		
		double next = -1;		
			if((index+1)<arr.size()){
			next=(double)arr.get(index+1).get(1);
		}
		return next;
	}
	
	//get max x value
	public double getMax_X(List<ArrayList> arr) {
		double max_x = 0;

		for (int i = 0; i < arr.size(); i++) {
			if (max_x < (double) arr.get(i).get(1)) {
				
				max_x = (double) arr.get(i).get(1);
			}
		}

		return max_x;
	}
	//adjust x values
	public List<ArrayList> adjustXVal(List<ArrayList> arr) {
		
		for(List copy:arr)
		{
			copy.set(1, (double)copy.get(1)+(double)copy.get(2));			
		}		
		return arr;
		}
	//sort active table
	public List<ArrayList> sortActiveTab(List<ArrayList> arr) {
		for (int k = 0; k < arr.size() - 1; k++) {
			for (int i = 0; i < arr.size() - 1; i++) {
				if((double) arr.get(i).get(1) > (double) arr.get(i + 1).get(1))
					{						
						ArrayList temp = arr.get(i);
						arr.set(i, arr.get(i + 1));
						arr.set(i + 1, temp);						
					}
				}
			}
		
		return arr;
	}

	//get max y value
	public float getMax_Maxy(List<ArrayList> arr) {
		int max_ymax = Math.round((float) arr.get(0).get(1));

		for (int i = 0; i < arr.size(); i++) {
			if (max_ymax < Math.round((float) arr.get(i).get(1))) {
				max_ymax = Math.round((float) arr.get(i).get(1));
			}
		}

		return max_ymax;
	}

	//get min y value
	public float getMin_Miny(List<ArrayList> arr) {
		int min_ymin = Math.round(((float) arr.get(0).get(0)));

		for (int i = 0; i < arr.size(); i++) {
			if (min_ymin > (float) arr.get(i).get(0)) {
				min_ymin = Math.round((float) arr.get(i).get(0));
			}
		}

		return min_ymin;
	}
	// get max x value
	public double getMax_Xval(List<ArrayList> arr) {
		int max_xval = Math.round((float) arr.get(0).get(1));

		for (int i = 0; i < arr.size(); i++) {
			if (max_xval > Math.round((float) arr.get(i).get(1))) {
				max_xval = Math.round((float) arr.get(i).get(1));
			}
		}

		return max_xval;
	}
}
