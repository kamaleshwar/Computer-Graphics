#version 120

// Gouraud shading vertex shader
//Reference : https://github.com/petermikitsh/graphics/blob/master/6/fshader.glsl
// INCOMING DATA

// Vertex location (in model space)
attribute vec4 vPosition;
uniform vec4 light_pos;
// Normal vector at vertex (in model space)
attribute vec3 vNormal;

// Model transformations
uniform vec3 theta;
uniform vec3 trans;
uniform vec3 scale;

// Camera parameters
uniform vec3 cPosition;
uniform vec3 cLookAt;
uniform vec3 cUp;

// View volume boundaries
uniform float left;
uniform float right;
uniform float top;
uniform float bottom;
uniform float near;
uniform float far;

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

// OUTGOING DATA
varying vec4 final_light_color;

void main()
{
    // Compute the sines and cosines of each rotation about each axis
    vec3 angles = radians( theta );
    vec3 c = cos( angles );
    vec3 s = sin( angles );

    // Create rotation matrices
    mat4 rxMat = mat4( 1.0,  0.0,  0.0,  0.0,
                       0.0,  c.x,  s.x,  0.0,
                       0.0,  -s.x, c.x,  0.0,
                       0.0,  0.0,  0.0,  1.0 );

    mat4 ryMat = mat4( c.y,  0.0,  -s.y, 0.0,
                       0.0,  1.0,  0.0,  0.0,
                       s.y,  0.0,  c.y,  0.0,
                       0.0,  0.0,  0.0,  1.0 );

    mat4 rzMat = mat4( c.z,  s.z,  0.0,  0.0,
                       -s.z, c.z,  0.0,  0.0,
                       0.0,  0.0,  1.0,  0.0,
                       0.0,  0.0,  0.0,  1.0 );

    mat4 xlateMat = mat4( 1.0,     0.0,     0.0,     0.0,
                          0.0,     1.0,     0.0,     0.0,
                          0.0,     0.0,     1.0,     0.0,
                          trans.x, trans.y, trans.z, 1.0 );

    mat4 scaleMat = mat4( scale.x,  0.0,     0.0,     0.0,
                          0.0,      scale.y, 0.0,     0.0,
                          0.0,      0.0,     scale.z, 0.0,
                          0.0,      0.0,     0.0,     1.0 );

    // Create view matrix
    vec3 nVec = normalize( cPosition - cLookAt );
    vec3 uVec = normalize( cross (normalize(cUp), nVec) );
    vec3 vVec = normalize( cross (nVec, uVec) );

    mat4 viewMat = mat4( uVec.x, vVec.x, nVec.x, 0.0,
                         uVec.y, vVec.y, nVec.y, 0.0,
                         uVec.z, vVec.z, nVec.z, 0.0,
                         -1.0*(dot(uVec, cPosition)),
                         -1.0*(dot(vVec, cPosition)),
                         -1.0*(dot(nVec, cPosition)), 1.0 );

    // Create projection matrix
    mat4 projMat = mat4( (2.0*near)/(right-left), 0.0, 0.0, 0.0,
                         0.0, ((2.0*near)/(top-bottom)), 0.0, 0.0,
                         ((right+left)/(right-left)),
                         ((top+bottom)/(top-bottom)),
                         ((-1.0*(far+near)) / (far-near)), -1.0,
                         0.0, 0.0, ((-2.0*far*near)/(far-near)), 0.0 );

    // Transformation order:
    //    scale, rotate Z, rotate Y, rotate X, translate
    mat4 modelMat = xlateMat * rxMat * ryMat * rzMat * scaleMat;
    mat4 modelViewMat = viewMat * modelMat;
	
	vec4 light_inf = vec4(light_pos.x,light_pos.y,light_pos.z,0.0);
	vec4 vert_e = modelViewMat * vPosition;
	vec4 light_e = viewMat * light_inf;
	vec4 normal_4 = vec4(vNormal , 0.0);
	vec4 norm_e = normalize(modelViewMat * normal_4 );	
	vec3 l =  light_e.xyz;
	vec3 v = vert_e.xyz;
	vec3 norm = norm_e.xyz;	
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
	final_light_color = ambient + diffuse + specular;
    // Transform the vertex location into clip space
    gl_Position =  projMat * viewMat  * modelMat * vPosition;
}
