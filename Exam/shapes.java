//
//  shapes.java
//
//  Students should not be modifying this file.
//
//  @author Vasudev
//
import java.util.ArrayList;
public class shapes extends simpleShape {

	/**
	 * Object selection variables
	 */
	public static final int OBJ_CUBE_1 = 0;
	public static final int OBJ_CYl = 1;
	public static final int OBJ_CUBE_2 = 2;
	public static final int OBJ_CUBE_3 = 2;
	public static final int OBJ_CUBE_4 = 2;
	public static final int OBJ_SPHERE = 3;
	public static final int OBJ_CONE = 4;
	/**
	 * Shading selection variables
	 */
	public static final int SHADE_FLAT = 0;
	public static final int SHADE_NOT_FLAT = 1;

	/**
	 * Constructor
	 */
	public shapes() {
	}

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
						(float) coord.get(0) + 0.5f, (float) div_val.get(1),
						(float) coord.get(2), (float) div_val.get(0) + 0.5f,
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

	}

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
	
	// Reference :
	// http://gamedev.stackexchange.com/questions/16585/how-do-you-programmatically-generate-a-sphere

	public void makeSphere(float radius, int slices, int stacks) {// Below make sphere idea is taken from below reference
		
		for (int stack_index = 0; stack_index < stacks; stack_index++) {

			float stack_ang_1 = (stack_index / (float)stacks) * (float) Math.PI * 2;
			float stack_ang_2 = ((stack_index + 1) / (float)stacks) * (float) Math.PI * 2;
			for (int slice_index = 0; slice_index < slices; slice_index++) {

				float slice_ang_1 = ((float) slice_index / slices) * (float) Math.PI;
				float slice_ang_2 = ((float) (slice_index + 1) / slices)
						* (float) Math.PI;

				ArrayList<Float> point_1= new ArrayList<Float>();
				ArrayList<Float> point_2= new ArrayList<Float>();
				ArrayList<Float> point_3= new ArrayList<Float>();
				ArrayList<Float> point_4= new ArrayList<Float>();
				
				
				point_1.add( radius * (float) Math.cos(stack_ang_1)
						* (float) Math.sin(slice_ang_1));
				point_1.add( radius * (float) Math.sin(stack_ang_1)
						* (float) Math.sin(slice_ang_1));
				point_1.add(radius * (float) Math.cos(slice_ang_1));
				
				point_2.add(radius * (float) Math.cos(stack_ang_1)
						* (float) Math.sin(slice_ang_2));
				point_2.add(radius * (float) Math.sin(stack_ang_1)
						* (float) Math.sin(slice_ang_2));
				point_2.add(radius * (float) Math.cos(slice_ang_2));
				
				point_3.add(radius * (float) Math.cos(stack_ang_2)
						* (float) Math.sin(slice_ang_1));
				
				point_3.add(radius * (float) Math.sin(stack_ang_2)
						* (float) Math.sin(slice_ang_1));
				
				point_3.add(radius * (float) Math.cos(slice_ang_1));
				
				point_4.add(radius * (float) Math.cos(stack_ang_2)
						* (float) Math.sin(slice_ang_2));
				
				point_4.add(radius * (float) Math.sin(stack_ang_2)
						* (float) Math.sin(slice_ang_2));
				
				point_4.add(radius * (float) Math.cos(slice_ang_2));

				addTriangle(point_1.get(0), point_1.get(1), point_1.get(2),
						point_2.get(0),point_2.get(1),point_2.get(2),
						point_3.get(0), point_3.get(1),	point_3.get(2));
				addTriangle(point_3.get(0), point_3.get(1), point_3.get(2), 
						point_2.get(0),point_2.get(1),point_2.get(2),
						point_4.get(0), point_4.get(1),	point_4.get(2));

			}

		}

	
}

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

	public void makeShape(int choice, int shadingType) {
		if (choice == shapes.OBJ_CUBE_1)
			makeCube(3);
		else if (choice == shapes.OBJ_CUBE_2)
			makeCube(3);
		else if (choice == shapes.OBJ_CUBE_3)
			makeCube(3);
		else if (choice == shapes.OBJ_CUBE_4)
			makeCube(3);
		else if (choice == shapes.OBJ_CYl)
			makeCylinder(0.5f, 100, 1);
		else if (choice == shapes.OBJ_SPHERE)
			for (int i = 3; i<50;i+=10)
			{
				makeSphere(0.5f, i, i);
			}
		else if (choice == shapes.OBJ_CONE)
			for (int i = 3; i<50;i+=10)
			{			
				makeCone(0.5f, i, i);
			}
		
		else
			makeCube(3);
	}
}
