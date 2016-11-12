#version 150

in vec3 in_Position;
in vec4 in_Color;
in vec3 in_Normal;
in vec2 in_TexCoord;

out vec4 ex_Color;

void main(void) {
    gl_Position = vec4(in_Position, 1.0);

    ex_Color = in_Color;
}