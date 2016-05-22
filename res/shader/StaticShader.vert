#version 330 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;

out vec2 frag_texCoords;
out vec3 frag_surfaceNormal;
out vec3 frag_toLightVector[12];

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition[12];

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	frag_texCoords = texCoords;
	
	frag_surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	
	for(int i=0; i<12; i++) {
		frag_toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}	
}