uniform mat4 pr_matrix;
attribute vec4 Color;
attribute vec2 TexCoord;
            
varying vec4 vColor;
            
varying vec2 vTexCoord;

void main() 
{
    vColor = Color;            
    vTexCoord = TexCoord;
    //gl_Position = pr_matrix * vec4(0, 0, 0, 1.0);
}