#version 150

in vec3 in_Position;
in vec4 in_Color;
in vec3 in_Normal;
in vec2 in_TexCoord;

void main(void) {
    // Since we are using flat lines, our input only had two points: x and y.
    // Set the Z coordinate to 0 and W coordinate to 1

    gl_Position = vec4(in_Position, 1.0);
}