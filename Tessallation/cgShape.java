
//Reference :https://github.com/lcm1115/Computer-Graphics-I/blob/master/project6/cg1Shape.cpp
//  cgShape.java
//
//  Class that includes routines for tessellating a number of basic shapes.
//
//  Students are to supply their implementations for the functions in
//  this file using the function "addTriangle()" to do the tessellation.
//

import java.awt.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

import java.io.*;

public class cgShape extends simpleShape {
	/**
	 * constructor
	 */
	public cgShape() {
	}

	/**
	 * makeCube - Create a unit cube, centered at the origin, with a given
	 * number of subdivisions in each direction on each face.
	 *
	 * @param subdivision
	 *            - number of equal subdivisons to be made in each direction
	 *            along each face
	 *
	 *            Can only use calls to addTriangle()
	 */
	
	//makeCube implementation idea is taken from below reference
	//Reference :https://github.com/lcm1115/Computer-Graphics-I/blob/master/project6/cg1Shape.cpp 
		
	public void makeCube(int subdivisions) {
		if (subdivisions < 1)
			subdivisions = 1;

		ArrayList<Float> coord = new ArrayList<Float>();
		coord.add(-0.5f);
		coord.add(-0.5f);
		coord.add(0.5f);

		for (int first = 0; first < subdivisions; first++) {
			coord.set(0, -0.5f);
			if (first > 0) {
				coord.set(1, (1 / (float) subdivisions) + (float) coord.get(1));
			}
			for (int second = 0; second < subdivisions; second++) {
				if (second > 0) {
					coord.set(0, (1 / (float) subdivisions) + coord.get(0));
				}
				ArrayList<Float> div_val = new ArrayList<Float>();
				div_val.add(((1 / (float) subdivisions) + (float) coord.get(0)));
				div_val.add(((1 / (float) subdivisions) + (float) coord.get(1)));
				div_val.add(((1 / (float) subdivisions) + (float) coord.get(2)));
				addTriangle(
						(float) coord.get(0),
						(float) coord.get(1),
						(float) coord.get(2), // front top
						(float) coord.get(0), (float) div_val.get(1),
						(float) coord.get(2), (float) div_val.get(0),
						(float) coord.get(1), (float) coord.get(2));

				addTriangle((float) coord.get(0), (float) div_val.get(1),
						(float) coord.get(2), (float) div_val.get(0),
						(float) div_val.get(1), (float) coord.get(2),
						(float) div_val.get(0), coord.get(1), coord.get(2)); // front
				// bottom

				addTriangle(
						(float) div_val.get(0),
						(float) coord.get(1),
						-(float) coord.get(2), // back top
						(float) coord.get(0), (float) div_val.get(1),
						-(float) coord.get(2), (float) coord.get(0),
						(float) coord.get(1), -(float) coord.get(2));

				addTriangle(
						(float) div_val.get(0),
						(float) coord.get(1),
						-(float) coord.get(2), // reverse
						(float) div_val.get(0), (float) div_val.get(1),
						-(float) coord.get(2), (float) coord.get(0),
						(float) div_val.get(1), -(float) coord.get(2));

			}
		}

		coord = new ArrayList<Float>();
		coord.add(0.5f);
		coord.add(-0.5f);
		coord.add(0.5f);

		for (int first = 0; first < subdivisions; first++) {
			coord.set(0, -0.5f);

			if (first > 0) {
				coord.set(1, (1 / (float) subdivisions) + (float) coord.get(1));
			}
			for (int second = 0; second < subdivisions; second++) {
				if (second > 0) {
					coord.set(0, (1 / (float) subdivisions) + coord.get(0));
				}
				ArrayList<Float> div_val = new ArrayList<Float>();
				div_val.add(((1 / (float) subdivisions) + coord.get(0)));
				div_val.add(((1 / (float) subdivisions) + coord.get(1)));
				div_val.add(((1 / (float) subdivisions) + coord.get(2)));

				addTriangle(div_val.get(0), coord.get(2), coord.get(1),
						coord.get(0), coord.get(2), div_val.get(1),
						coord.get(0), coord.get(2), coord.get(1));

				addTriangle(div_val.get(0), coord.get(2), coord.get(1),
						div_val.get(0), coord.get(2), div_val.get(1),
						coord.get(0), coord.get(2), div_val.get(1));

				addTriangle(coord.get(0), -coord.get(2),
						coord.get(1), // back top
						coord.get(0), -coord.get(2), div_val.get(1),
						div_val.get(0), -coord.get(2), coord.get(1));

				addTriangle(coord.get(0), -coord.get(2),
						div_val.get(1), // reverse
						div_val.get(0), -coord.get(2), div_val.get(1),
						div_val.get(0), -coord.get(2), coord.get(1));

			}
		}

		coord = new ArrayList<Float>();
		coord.add(0.5f);
		coord.add(-0.5f);
		coord.add(0.5f);

		for (int first = 0; first < subdivisions; first++) {
			coord.set(0, -0.5f);

			if (first > 0) {
				coord.set(1, (1 / (float) subdivisions) + (float) coord.get(1));
			}
			for (int second = 0; second < subdivisions; second++) {
				if (second > 0) {
					coord.set(0, (1 / (float) subdivisions) + coord.get(0));
				}
				ArrayList<Float> div_val = new ArrayList<Float>();
				div_val.add(((1 / (float) subdivisions) + coord.get(0)));
				div_val.add(((1 / (float) subdivisions) + coord.get(1)));
				div_val.add(((1 / (float) subdivisions) + coord.get(2)));

				addTriangle(coord.get(2), coord.get(1), div_val.get(0),
						coord.get(2), div_val.get(1), coord.get(0),
						coord.get(2), coord.get(1), coord.get(0));

				addTriangle(coord.get(2), coord.get(1),
						div_val.get(0), // front bottom
						coord.get(2), div_val.get(1), div_val.get(0),
						coord.get(2), div_val.get(1), coord.get(0));

				addTriangle(-coord.get(2), coord.get(1),
						coord.get(0), // back top
						-coord.get(2), div_val.get(1), coord.get(0),
						-coord.get(2), coord.get(1), div_val.get(0));

				addTriangle(-coord.get(2), div_val.get(1),
						coord.get(0), // reverse
						-coord.get(2), div_val.get(1), div_val.get(0),
						-coord.get(2), coord.get(1), div_val.get(0));
			}
		}

	}

	/**
	 * makeCylinder - Create polygons for a cylinder with unit height, centered
	 * at the origin, with separate number of radial subdivisions and height
	 * subdivisions.
	 * 
	 * @param <T>
	 *
	 * @param radius
	 *            - Radius of the base of the cylinder
	 * @param radialDivision
	 *            - number of subdivisions on the radial base
	 * @param heightDivisions
	 *            - number of subdivisions along the height
	 *
	 *            Can only use calls to addTriangle()
	 */
	
	//makeCylinder implementation idea is taken from below reference
	//Reference :https://github.com/lcm1115/Computer-Graphics-I/blob/master/project6/cg1Shape.cpp 
		
	public void makeCylinder(float radius, int radialDivisions,
			int heightDivisions) {
		if (radialDivisions < 3)
			radialDivisions = 3;

		if (heightDivisions < 1)
			heightDivisions = 1;

		float unit_angle = (float) (360.0 / (float) radialDivisions);
		double total_angle = 0;
		int vertices_count = 0;
		ArrayList<float[]> coordinates_bottom = new ArrayList<float[]>();
		ArrayList<float[]> coordinates_top = new ArrayList<float[]>();

		while (total_angle < 360.0) {
			float[] x_z_coordinates = new float[3];
			x_z_coordinates[0] = (float) (radius * Math.cos(Math
					.toRadians(total_angle)));
			x_z_coordinates[1] = -0.5f;
			x_z_coordinates[2] = (float) (radius * Math.sin(Math
					.toRadians(total_angle)));
			coordinates_bottom.add(x_z_coordinates);
			total_angle += unit_angle;
			vertices_count++;
		}

		total_angle = 0;

		while (total_angle < 360.0) {
			float[] x_z_coordinates = new float[3];
			x_z_coordinates[0] = (float) (radius * Math.cos(Math
					.toRadians(total_angle)));
			x_z_coordinates[1] = 0.5f;
			x_z_coordinates[2] = (float) (radius * Math.sin(Math
					.toRadians(total_angle)));
			coordinates_top.add(x_z_coordinates);
			total_angle += unit_angle;
		}

		for (int count = 0; count < vertices_count; count++) {
			int next = count + 1;
			if ((count + 1) >= vertices_count) {
				next = 0;
			}
			float x0_b = coordinates_bottom.get(count)[0];
			float y0_b = coordinates_bottom.get(count)[1];
			float z0_b = coordinates_bottom.get(count)[2];
			float x1_b = coordinates_bottom.get(next)[0];
			float y1_b = coordinates_bottom.get(next)[1];
			float z1_b = coordinates_bottom.get(next)[2];

			addTriangle(x0_b, y0_b, z0_b, x1_b, y1_b, z1_b, 0.0f, -0.5f, 0.0f);

			float x0_t = coordinates_top.get(count)[0];
			float y0_t = coordinates_top.get(count)[1];
			float z0_t = coordinates_top.get(count)[2];
			float x1_t = coordinates_top.get(next)[0];
			float y1_t = coordinates_top.get(next)[1];
			float z1_t = coordinates_top.get(next)[2];

			addTriangle(x1_t, y1_t, z1_t, x0_t, y0_t, z0_t, 0.0f, 0.5f, 0.0f);

			float y_div = 1 / (float) heightDivisions;

			for (int counter = 0; counter < heightDivisions; counter++) {
				float x2 = x0_t;
				float y2 = y0_t;
				float z2 = z0_t;
				y2 -= y_div;
				float x3 = x1_t;
				float y3 = y1_t;
				float z3 = z1_t;
				y3 -= y_div;

				addTriangle(x0_t, y0_t, z0_t, x1_t, y1_t, z1_t, x2, y2, z2);

				addTriangle(x2, y2, z2, x1_t, y1_t, z1_t, x3, y3, z3);

				y0_t -= y_div;
				y1_t -= y_div;
			}
		}
	}

	/**
	 * makeCone - Create polygons for a cone with unit height, centered at the
	 * origin, with separate number of radial subdivisions and height
	 * subdivisions.
	 *
	 * @param radius
	 *            - Radius of the base of the cone
	 * @param radialDivision
	 *            - number of subdivisions on the radial base
	 * @param heightDivisions
	 *            - number of subdivisions along the height
	 *
	 *            Can only use calls to addTriangle()
	 */
	
	//makeCone implementation idea is taken from below reference
	//Reference :https://github.com/lcm1115/Computer-Graphics-I/blob/master/project6/cg1Shape.cpp 
	public void makeCone(float radius, int radialDivisions, int heightDivisions) {
		if (radialDivisions < 3)
			radialDivisions = 3;

		if (heightDivisions < 1)
			heightDivisions = 1;

		float unit_angle = (float) (360.0 / (float) radialDivisions);
		float total_angle = 0.0f;
		int num_vertices = 0;
		ArrayList<float[]> coordinates_bottom = new ArrayList<float[]>();

		while (total_angle < 360.0) {
			float[] x_z_coordinates = new float[3];
			x_z_coordinates[0] = (float) (radius * Math.cos(Math
					.toRadians(total_angle)));
			x_z_coordinates[1] = -0.5f;
			x_z_coordinates[2] = (float) (radius * Math.sin(Math
					.toRadians(total_angle)));
			coordinates_bottom.add(x_z_coordinates);
			total_angle += unit_angle;
			num_vertices++;
		}

		for (int count = 0; count < num_vertices; count++) {
			int next = count + 1;
			if (next >= num_vertices) {
				next = 0;
			}
			float[] v0_xyz = new float[3];
			float[] v1_xyz = new float[3];

			v0_xyz[0] = coordinates_bottom.get(count)[0];
			v0_xyz[1] = coordinates_bottom.get(count)[1];
			v0_xyz[2] = coordinates_bottom.get(count)[2];
			v1_xyz[0] = coordinates_bottom.get(next)[0];
			v1_xyz[1] = coordinates_bottom.get(next)[1];
			v1_xyz[2] = coordinates_bottom.get(next)[2];

			addTriangle(v1_xyz[0], v1_xyz[1], v1_xyz[2], v0_xyz[0], v0_xyz[1],
					v0_xyz[2], 0.0f, -0.5f, 0.0f);

			float y_div = (1.0f) / (float) heightDivisions;
			float rad_mutable = radius;
			float rad_per_height = radius / (float) heightDivisions;
			float[] v0_copy = new float[3];
			float[] v1_copy = new float[3];
			v0_copy[0] = v0_xyz[0];
			v0_copy[1] = v0_xyz[1];
			v0_copy[2] = v0_xyz[2];
			v1_copy[0] = v1_xyz[0];
			v1_copy[1] = v1_xyz[1];
			v1_copy[2] = v1_xyz[2];

			for (int counter = 0; counter < heightDivisions; counter++) {
				rad_mutable -= rad_per_height;
				float[] v2_xyz = new float[3];
				float[] v3_xyz = new float[3];

				v2_xyz[0] = (rad_mutable / (float) radius) * v0_copy[0];
				v2_xyz[1] = v0_xyz[1];
				v2_xyz[2] = (rad_mutable / (float) radius) * v0_copy[2];
				v2_xyz[1] += y_div;
				v3_xyz[0] = (rad_mutable / (float) radius) * v1_copy[0];
				v3_xyz[1] = v1_xyz[1];
				v3_xyz[2] = (rad_mutable / (float) radius) * v1_copy[2];
				v3_xyz[1] += y_div;

				addTriangle(v0_xyz[0], v0_xyz[1], v0_xyz[2], v1_xyz[0],
						v1_xyz[1], v1_xyz[2], v2_xyz[0], v2_xyz[1], v2_xyz[2]);

				addTriangle(v2_xyz[0], v2_xyz[1], v2_xyz[2], v1_xyz[0],
						v1_xyz[1], v1_xyz[2], v3_xyz[0], v3_xyz[1], v3_xyz[2]);

				v0_xyz[0] = v2_xyz[0];
				v0_xyz[1] = v2_xyz[1];
				v0_xyz[2] = v2_xyz[2];
				v1_xyz[0] = v3_xyz[0];
				v1_xyz[1] = v3_xyz[1];
				v1_xyz[2] = v3_xyz[2];
			}

			addTriangle(v0_xyz[0], v0_xyz[1], v0_xyz[2], v1_xyz[0], v1_xyz[1],
					v1_xyz[2], 0.0f, 0.5f, 0.0f);

		}

	}

	/**
	 * makeSphere - Create sphere of a given radius, centered at the origin,
	 * using spherical coordinates with separate number of thetha and phi
	 * subdivisions.
	 *
	 * @param radius
	 *            - Radius of the sphere
	 * @param slides
	 *            - number of subdivisions in the theta direction
	 * @param stacks
	 *            - Number of subdivisions in the phi direction.
	 *
	 *            Can only use calls to addTriangle
	 */
	
	//Below make sphere idea is taken from below reference	
	//Reference : http://gamedev.stackexchange.com/questions/16585/how-do-you-programmatically-generate-a-sphere
	public void makeSphere(float radius, int slices, int stacks) {
		if (slices < 3)
			slices = 3;

		if (stacks < 3)
			stacks = 3;

		for (int stack_counter = 0; stack_counter < stacks; stack_counter++) {
			int next_stack = stack_counter + 1;
			if (next_stack == stacks) {
				next_stack = 0;
			}
			double horizontal_angle_1 = (((double) stack_counter / stacks) * Math.PI);
			double horizontal_angle_2 = (((double) (next_stack) / stacks) * Math.PI);

			for (int slice_counter = 0; slice_counter < slices; slice_counter++) {
				int next_slice = slice_counter + 1;

				if (next_slice == slices) {
					next_slice = 0;
				}

				double vertical_angle_1 = (((double) slice_counter / slices) * 2 * Math.PI);
				double vertical_angle_2 = ((((double) next_slice) / slices) * 2 * Math.PI);

				float v1_x = (float) (radius * (Math.cos(horizontal_angle_1)) * (Math
						.sin(vertical_angle_1)));
				float v1_y = (float) (radius * (Math.sin(horizontal_angle_1)) * (Math
						.sin(vertical_angle_1)));
				float v1_z = (float) (radius * (Math.cos(vertical_angle_1)));

				float v2_x = (float) (radius * (Math.cos(horizontal_angle_1)) * (Math
						.sin(vertical_angle_2)));
				float v2_y = (float) (radius * (Math.sin(horizontal_angle_1)) * (Math
						.sin(vertical_angle_2)));
				float v2_z = (float) (radius * (Math.cos(vertical_angle_2)));

				float v3_x = (float) (radius * (Math.cos(horizontal_angle_2)) * (Math
						.sin(vertical_angle_2)));
				float v3_y = (float) (radius * (Math.sin(horizontal_angle_2)) * (Math
						.sin(vertical_angle_2)));
				float v3_z = (float) (radius * (Math.cos(vertical_angle_2)));

				float v4_x = (float) (radius * (Math.cos(horizontal_angle_2)) * (Math
						.sin(vertical_angle_1)));
				float v4_y = (float) (radius * (Math.sin(horizontal_angle_2)) * (Math
						.sin(vertical_angle_1)));
				float v4_z = (float) (radius * (Math.cos(vertical_angle_1)));

				if (stack_counter != 0 && stack_counter < stacks - 1) {
					 addTriangle(v1_x, v1_y, v1_z, v2_x, v2_y, v2_z, v4_x,
					 v4_y,
					 v4_z);
					 addTriangle(v4_x, v4_y, v4_z, v3_x, v3_y, v3_z, v2_x,
					 v2_y,
					 v2_z);
				} else if (stack_counter == 0) {
					addTriangle(v4_x, v4_z, v4_y, v3_x, v3_z, v3_y, v1_x,v1_z,v1_y);
				} else if (stack_counter == stacks - 1) {
					addTriangle(v3_x, v3_z, v3_y, v1_x, v1_z, v1_y, v2_x,
							v2_z, v2_y);
			
				}

			}
		}
	}
}
