#version 120
//in --> varying (reception du vertex)
//out --> (delete)

varying vec4 color;
varying vec2 TexCoord0;

uniform sampler2D texture_block;

void main(void) {
	gl_FragColor = texture2D(texture_block, gl_TexCoord[0].xy);// * color;
}