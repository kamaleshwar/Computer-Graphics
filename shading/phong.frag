#version 120

// Phong shading fragment shader

uniform vec4 ambient_color;
uniform float amb_reflection;
uniform vec4 ambient_light_color;
uniform vec4 diffuse_color;
uniform float diff_reflect_exp;
uniform vec4 light_color;
uniform vec4 spec_color;
uniform float spec_exp;
uniform float spec_reflection_coeff;

varying vec3 l;
varying vec3 v;
varying vec3 norm;

void main()
{	
	vec3 L = normalize(l-v);
	vec3 N = normalize(norm);	
	vec3 R = normalize(reflect(L,N));
	vec3 V = normalize(v.xyz);
	float spec_reflection = pow(clamp(dot(R,V),0,1),spec_exp);
	float L_N = dot(L,N);	
	vec4 I_a = ambient_light_color * amb_reflection;
	vec4 I_d = diff_reflect_exp * L_N * diffuse_color;
	vec4 I_s = spec_reflection_coeff * spec_color * spec_reflection;
	vec4 ambient = ambient_color * I_a;
	vec4 diffuse = light_color * I_d;
	vec4 specular = light_color * I_s;		
	vec4 final_light_color = ambient + diffuse + specular;	
    gl_FragColor = final_light_color;	
}
