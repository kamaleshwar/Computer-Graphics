#version 120

attribute vec4 vPosition;
uniform vec3 eyepoint;
uniform vec3 lookat;
uniform vec3 up;

uniform float left_o;
uniform float right_o;
uniform float top_o;
uniform float bottom_o;
uniform float near_o;
uniform float far_o;

uniform float left_f;
uniform float right_f;
uniform float top_f;
uniform float bottom_f;
uniform float near_f;
uniform float far_f;

uniform float projection;
uniform vec3 rotate;
uniform vec3 scale;
uniform vec3 translate;			
mat4 proj_mat; 
mat4 w_c_coords ;

uniform float clear;

void main()
{
    vec3 n = normalize(eyepoint - lookat);
	vec3 u = normalize(cross(up,n));
	vec3 v = normalize(cross(n,u));
	
	
	mat4 scale_mat = mat4(scale.x, 0.0,0.0,0.0,
						  0.0, scale.y, 0.0, 0.0,
						  0.0, 0.0, scale.z, 0.0,
						  0.0, 0.0, 0.0, 1.0);
	
	
	mat4 rot_y = mat4(cos(radians(rotate.y)), 0.0, -sin(radians(rotate.y)), 0.0,
					  0.0, 1.0, 0.0, 0.0,
					  sin(radians(rotate.y)), 0.0, cos(radians(rotate.y)), 0.0,
					  0.0, 0.0, 0.0, 1.0);
					  
	mat4 rot_z = mat4(cos(radians(rotate.z)), sin(radians(rotate.z)), 0.0, 0.0,
					  -sin(radians(rotate.z)), cos(radians(rotate.z)), 0.0, 0.0,
					  0.0, 0.0, 1.0, 0.0,
					  0.0, 0.0, 0.0, 1.0);
					  
	mat4 fin_rot = rot_z*rot_y;
	
	mat4 Trans = mat4(1.0, 0.0, 0.0, 0.0,
					  0.0, 1.0, 0.0, 0.0,
					  0.0, 0.0, 1.0, 0.0,
					  translate.x, translate.y, translate.z, 1.0);					
	
	mat4 model_view_trans = Trans*fin_rot*scale_mat;
	
	w_c_coords= mat4(u.x, v.x, n.x, 0.0, 
						   u.y, v.y, n.y, 0.0,
						   u.z, v.z, n.z, 0.0,
						   -1*dot(u,eyepoint),   -1*dot(v,eyepoint),   -1*dot(n,eyepoint),    1.0);	   
	
		
	if(projection<2.0){
	proj_mat = mat4((2.0*near_f/(right_f-left_f)), 0.0, 0.0, 0.0,
						  0.0, (2*near_f/(top_f-bottom_f)), 0.0, 0.0,
						  (right_f+left_f)/(right_f-left_f), (top_f+bottom_f)/(top_f-bottom_f), -(far_f+near_f)/(far_f-near_f), -1.0,
						  0.0, 0.0, -2.0*far_f*near_f/(far_f-near_f), 0.0);	
	}	
	else{
	proj_mat = mat4((2.0/(right_o-left_o)), 0.0, 0.0, 0.0,
						  0.0, (2.0/(top_o-bottom_o)), 0.0, 0.0,
						  0.0, 0.0, -2.0/(far_o-near_o), 0.0,
						  -1.0*(right_o+left_o)/(right_o-left_o), -1.0*(top_o+bottom_o)/(top_o-bottom_o), -1.0*(far_o+near_o)/(far_o-near_o), 1.0);
	}	
						   
    gl_Position =  proj_mat*w_c_coords*model_view_trans*vPosition;
}
