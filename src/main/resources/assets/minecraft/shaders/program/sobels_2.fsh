#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;/*
uniform sampler2D ParticlesDepthSampler;
uniform sampler2D TranslucentSampler;
uniform sampler2D DitherSampler;*/

in vec2 texCoord;
in vec2 oneTexel;


uniform vec2 InSize;
uniform float Time;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;

out vec4 fragColor;

vec3 hue(float h)
{
    float r = abs(h * 6.0 - 3.0) - 1.0;
    float g = 2.0 - abs(h * 6.0 - 2.0);
    float b = 2.0 - abs(h * 6.0 - 4.0);
    return clamp(vec3(r,g,b), 0.0, 1.0);
}

vec3 HSVtoRGB(vec3 hsv) {
    return ((hue(hsv.x) - 1.0) * hsv.y + 1.0) * hsv.z;
}

vec3 RGBtoHSV(vec3 rgb) {
    vec3 hsv = vec3(0.0);
    hsv.z = max(rgb.r, max(rgb.g, rgb.b));
    float min = min(rgb.r, min(rgb.g, rgb.b));
    float c = hsv.z - min;

    if (c != 0.0)
    {
        hsv.y = c / hsv.z;
        vec3 delta = (hsv.z - rgb) / c;
        delta.rgb -= delta.brg;
        delta.rg += vec2(2.0, 4.0);
        if (rgb.r >= hsv.z) {
            hsv.x = delta.b;
        } else if (rgb.g >= hsv.z) {
            hsv.x = delta.r;
        } else {
            hsv.x = delta.g;
        }
        hsv.x = fract(hsv.x / 6.0);
    }
    return hsv;
}

void main(){

    //wobble
    float xOffset = sin(texCoord.y * Frequency.x + Time * 3.1415926535 * 2.0) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + Time * 3.1415926535 * 2.0) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset)/2;
    vec2 fixedoffset = vec2(-1,1)/128;

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
    //normal
    vec2 mosaicInSize = InSize;
    vec2 fractPix = fract(texCoord * mosaicInSize) / mosaicInSize;

    vec4 baseTexel = texture(DiffuseSampler, texCoord - fractPix+offset);
    float red = dot(baseTexel.rgb, vec3(1,0,0));
    float green = dot(baseTexel.rgb, vec3(0,1,0));
    float blue = dot(baseTexel.rgb, vec3(0,0,1));

    //tiktok outline
    vec4 center = texture(DiffuseSampler, texCoord+offset);
    vec4 left   = texture(DiffuseSampler, texCoord - vec2(oneTexel.x, 0.0));
    vec4 right  = texture(DiffuseSampler, texCoord + vec2(oneTexel.x, 0.0));
    vec4 up     = texture(DiffuseSampler, texCoord - vec2(0.0, oneTexel.y));
    vec4 down   = texture(DiffuseSampler, texCoord + vec2(0.0, oneTexel.y));
    vec4 leftDiff  = center - left;
    vec4 rightDiff = center - right;
    vec4 upDiff    = center - up;
    vec4 downDiff  = center - down;
    vec4 total = clamp(leftDiff + rightDiff + upDiff + downDiff, 0.0, 1.0);
    total.g /= 2;

    if (round(total.r*20) == round(total.b*20))  {
        total.r = 0.0;
        total.b = 0.0;
    }
    total.b = total.b >= 0.9 || total.b <= 0.2 ? 0 : total.b;
    total.r = total.r >= 0.9 || total.r <= 0.2 ? 0 : total.r;
    vec3 totalHSV = RGBtoHSV(total.rgb);

    if (totalHSV.r > 0.7) totalHSV.r -=0.4;
    if (totalHSV.r < 0.2) totalHSV.r +=0.6;
    totalHSV.r += 0.09;
    totalHSV.g -= 0.25;
    if (totalHSV.g <= 0.1) {
        if (totalHSV.g <= 0.1) totalHSV.b = 0;
        if (totalHSV.g <= 0.25 && totalHSV.b > 0.1) totalHSV += 0.2;
    }
    totalHSV.b /= 2;

    //
    vec4 final = mix(vec4(red, green, blue, 1.75),vec4(color.r,color.g,color.b, 0.25),0.85);

    fragColor =mix(vec4(HSVtoRGB(totalHSV),1),final , 0.1);
}
