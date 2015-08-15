#version 120

// Gouraud shading fragment shader

varying vec4 final_light_color;

void main()
{    		
    gl_FragColor = final_light_color;
}
