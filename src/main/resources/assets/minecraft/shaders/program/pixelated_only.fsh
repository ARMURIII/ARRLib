#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;


uniform vec2 InSize;
uniform float Time;
uniform vec4 ColorModulate;

uniform float MosaicSize;
uniform float Resolution;
uniform float Saturation;

out vec4 fragColor;

float rand(vec2 co){
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

bool isBlack(vec4 fractTexel, float black) {
    return fractTexel.r <= black && fractTexel.g <= black && fractTexel.b <= black;
}

void main(){
    vec4 baseTexel = texture(DiffuseSampler, texCoord) * ColorModulate;
    vec2 mosaicInSize = InSize / MosaicSize;
    vec2 fractPix = fract(texCoord * mosaicInSize) / mosaicInSize;
    vec2 ceilPix = fract(-texCoord * mosaicInSize) / mosaicInSize;

    vec4 fractTexel = texture(DiffuseSampler, texCoord - fractPix);
    vec4 ceilTexel = texture(DiffuseSampler, texCoord + ceilPix);

    fractTexel.a = 1.0;
    ceilTexel.a = 1.0;

    float rr = rand(texCoord - fractPix+Time);
    vec4 random = vec4(rr, rr, rr, 1);
    float black = 0.07;

    vec4 fractinal = vec4(mix(fractTexel.rgb, random.rgb, 0.5), 0.5);
    vec4 ceilinal = vec4(mix(ceilTexel.rgb, random.rgb, 0.5), 0.5);

    if (isBlack(fractTexel,black)) {
        fragColor = vec4(0,0,0,1);
    }else
    if (isBlack(ceilTexel,black)) {
        fragColor = vec4(0,0,0,1);
    }else
        fragColor = baseTexel;
}
