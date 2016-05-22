#version 330 core

in vec3 frag_textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;

const float ambient = 0.28;

void main(void){
    out_Color = ambient * texture(cubeMap, frag_textureCoords);
}