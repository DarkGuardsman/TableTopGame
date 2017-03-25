#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 rotation;
uniform vec3 offset = vec3(0.0);

out DATA
{
	vec2 tc;
} vs_out;

void main()
{
	gl_Position = pr_matrix * ((position * rotation) + vec4(offset, 0));
	vs_out.tc = tc;
}