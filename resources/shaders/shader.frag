#version 330 core

in vec4 vertexColor;
in vec2 textureCoord;

layout (location = 0) out vec4 fragColor;

uniform sampler2D tex;

void main() {
    vec4 textureColor = texture(tex, textureCoord);
    fragColor = vertexColor * textureColor;
}