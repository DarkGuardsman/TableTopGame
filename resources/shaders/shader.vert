#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 texcoord;

out vec4 vertexColor;
out vec2 textureCoord;

uniform mat4 model = mat4(1.0);
uniform mat4 view = mat4(1.0);
uniform mat4 pr_matrix;

void main() {
    vertexColor = color;
    textureCoord = texcoord;
    mat4 mvp = pr_matrix * view * model;
    gl_Position = mvp * vec4(position, 1.0);
}