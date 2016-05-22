#version 330 core

const float ambient = 0.28;

in vec2 frag_texCoords;
in vec3 frag_surfaceNormal;
in vec3 frag_toLightVector[12];

out vec4 out_Color;

uniform sampler2D texSampler;

uniform vec3 lightColor[12];
uniform float attenuation[12];

void main(void) {
	
	vec3 unitNormal = normalize(frag_surfaceNormal);
	
	vec3 totalDiffuse = vec3(0.0);
	for(int i=0; i<12; i++) {
		float distance = length(frag_toLightVector[i]);
		float attFactor = 1.0 / (1.0 + attenuation[i] * pow(distance, 2));
	
		vec3 unitLightVector = normalize(frag_toLightVector[i]);
		float nDotl = dot(unitNormal, unitLightVector);
		float brightness = max(nDotl, 0.0);
		totalDiffuse = totalDiffuse + attFactor * (brightness * lightColor[i]);
	}
	totalDiffuse = max(totalDiffuse, ambient);
	
	vec4 texColor = texture(texSampler, frag_texCoords);
	if(texColor.a < 0.5) {
		discard;
	}
	
	vec4 finalColor = vec4(totalDiffuse, 1.0) * texColor;
	out_Color = finalColor;
}