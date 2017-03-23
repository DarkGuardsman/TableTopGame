uniform mat4 pr_matrix;
attribute vec4 Color;
attribute vec2 TexCoord;
            
attribute vec3 Position;
varying vec4 vColor;
            
varying vec2 vTexCoord;

void main() 
{
    vColor = Color;            
    vTexCoord = TexCoord;
    gl_Position = pr_matrix * vec4(Position.xyz, 1.0);
}