#version 120
//in --> attribute (reception de opengl)
//out --> varying (sortie du fichier)
//uniform ????

attribute vec3 block_position;
attribute vec2 texCoord;

uniform vec4 block_color;

varying vec4 color;
varying vec2 TexCoord0;

void main(void) {
	
	color = block_color;
	gl_Position = ftransform();
	TexCoord0 = texCoord;
	//gl_TexCoord[0] = gl_MultiTexCoord0;
}