//
// lightingParams.java
//
// Simple class for setting up the viewing and projection transforms
// for the Shading Assignment.
//
// Students are to complete this class.
//

import javax.media.opengl.*;
import javax.media.opengl.fixedfunc.*; 

public class lightingParams
{
	
    // Add any global class variables you need here.

    /**
     * constructor
     */
    public lightingParams()
    {
      
    }
    /**
     * This functions sets up the lighting, material, and shading parameters
     * for the Phong shader.
     *
     * You will need to write this function, and maintain all of the values
     * needed to be sent to the vertex shader.
     *
     * @param program - The ID of an OpenGL (GLSL) shader program to which
     * parameter values are to be sent
     *
     * @param gl2 - GL2 object on which all OpenGL calls are to be made
     *
     */
    
    public void setUpPhong (int program, GL2 gl2,float a_c_x,float a_c_y,float a_c_z)
    { 	
    	int amb_color = gl2.glGetUniformLocation(program, "ambient_color");
    	gl2.glUniform4f(amb_color, a_c_x,a_c_y,a_c_z,1.0f);
    	
    	int amb_ref = gl2.glGetUniformLocation(program, "amb_reflection");
    	gl2.glUniform1f(amb_ref, 0.5f);
    	
    	int amb_light_col = gl2.glGetUniformLocation(program, "ambient_light_color");
    	gl2.glUniform4f(amb_light_col, 1f,1f,1f,1.0f);
    	
    	
    	int diff_color = gl2.glGetUniformLocation(program, "diffuse_color");
    	gl2.glUniform4f(diff_color, a_c_x,a_c_y,a_c_z,1.0f);
    	
    	int diff_exp = gl2.glGetUniformLocation(program, "diff_reflect_exp");
    	gl2.glUniform1f(diff_exp, 0.7f);
    	
    	int light_col = gl2.glGetUniformLocation(program, "light_color");
    	gl2.glUniform4f(light_col, 1.0f,1.0f,0.0f,1.0f);
    	
    	int spec_color = gl2.glGetUniformLocation(program, "spec_color");
    	gl2.glUniform4f(spec_color, a_c_x,a_c_y,a_c_z,1.0f);
    	
    	int spec_exp = gl2.glGetUniformLocation(program, "spec_exp");
    	gl2.glUniform1f(spec_exp, 10.0f);
    	
    	int spec_ref = gl2.glGetUniformLocation(program, "spec_reflection_coeff");
    	gl2.glUniform1f(spec_ref, 1.0f);
    	
    	int light_position = gl2.glGetUniformLocation(program, "light_pos");
    	gl2.glUniform4f(light_position, 1.0f,5.0f,2.0f,1.0f);
        // Add your code here.
    }
}
