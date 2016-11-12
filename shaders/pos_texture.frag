#version 150

uniform sampler2D textureSampler;

in vec2 pass_TexCoord;

out vec4 gl_FragColor;

void main(void) {
    // Pass through our original color with full opacity.
    gl_FragColor = texture(textureSampler, pass_TexCoord);
}