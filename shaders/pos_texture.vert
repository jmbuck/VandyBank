#version 150

in vec3 in_Position;
in vec4 in_Color;
in vec3 in_Normal;
in vec2 in_TexCoord;

out vec2 pass_TexCoord;

void main(void) {

    gl_Position = vec4(in_Position, 1.0);
    
   	pass_TexCoord = in_TexCoord;
}