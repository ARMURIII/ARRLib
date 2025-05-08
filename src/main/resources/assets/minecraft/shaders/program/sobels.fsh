#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
in vec2 oneTexel;


uniform vec2 InSize;
uniform float Time;
uniform vec4 ColorModulate;

uniform float MosaicSize;
uniform float Resolution;
uniform float Saturation;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;

out vec4 fragColor;

void main(){

    //wobble
    float xOffset = sin(texCoord.y * Frequency.x + Time * 3.1415926535 * 2.0) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + Time * 3.1415926535 * 2.0) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset)/2;

    //outline
    vec4 c = texture(DiffuseSampler, texCoord);
    vec4 u = texture(DiffuseSampler, texCoord + vec2(        0.0, -oneTexel.y));
    vec4 d = texture(DiffuseSampler, texCoord + vec2(        0.0,  oneTexel.y));
    vec4 l = texture(DiffuseSampler, texCoord + vec2(-oneTexel.x,         0.0));
    vec4 r = texture(DiffuseSampler, texCoord + vec2( oneTexel.x,         0.0));

    vec4 nc = normalize(c);
    vec4 nu = normalize(u);
    vec4 nd = normalize(d);
    vec4 nl = normalize(l);
    vec4 nr = normalize(r);

    float du = dot(nc, nu);
    float dd = dot(nc, nd);
    float dl = dot(nc, nl);
    float dr = dot(nc, nr);

    float i = 64.0;

    float f = 1.0;
    f += (du * i) - (dd * i);
    f += (dr * i) - (dl * i);

    vec4 color = c * clamp(f, 0.5, 2.0);

    //tiktok
    float a = 1.0 - texture(DepthSampler, texCoord).r;
    vec2 tiktokOffset = vec2(-a, -a )/128 + offset;
    vec4 center = texture(DiffuseSampler, texCoord+tiktokOffset);
    vec4 left   = texture(DiffuseSampler, texCoord - vec2(oneTexel.x, 0.0));
    vec4 right  = texture(DiffuseSampler, texCoord + vec2(oneTexel.x, 0.0));
    vec4 up     = texture(DiffuseSampler, texCoord - vec2(0.0, oneTexel.y));
    vec4 down   = texture(DiffuseSampler, texCoord + vec2(0.0, oneTexel.y));
    vec4 leftDiff  = center - left;
    vec4 rightDiff = center - right;
    vec4 upDiff    = center - up;
    vec4 downDiff  = center - down;
    vec4 total1 = clamp(rightDiff + upDiff, 0.0, 1.0);
    vec4 total2 = clamp(leftDiff + downDiff, 0.0, 1.0);
    total1.g *= 0;
    total2.g *= 0;
    float threshold = 0.125;
    if (total1.r < threshold) {
        total1.r = 0;
    }
    if (total1.b < threshold) {
        total1.b = 0;
    }
    if (total2.r < threshold) {
        total2.r = 0;
    }
    if (total2.b < threshold) {
        total2.b = 0;
    }
    total1.b *= 1.25;

    total2.r /= 1.25;

    vec4 total = clamp(total1+total2,0,1);
    if (total.b <= 0.25) {
        total.r = 0;
    }
    vec4 tiktok = vec4(mix(vec3(0,0,0),total.rgb,0.75),1);
    fragColor = vec4(mix(tiktok.rgb,color.rgb,0.075),1);
}
