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
    vec4 total = color;
    //normal
    vec2 mosaicInSize = InSize;
    vec2 fractPix = fract(texCoord * mosaicInSize) / mosaicInSize;

    vec4 baseTexel = texture(DiffuseSampler, texCoord - fractPix+offset);
    float red = dot(baseTexel.rgb, vec3(1,0,0));
    float green = dot(baseTexel.rgb, vec3(0,1,0));
    float blue = dot(baseTexel.rgb, vec3(0,0,1));
    //tiktok outline
    float a = 1.0 - texture(DepthSampler, texCoord).r;

    vec2 tiktokOffset = vec2(a / 3.0, -a / 4.0)/128 + offset;
    vec4 redTiktok = texture(DiffuseSampler, texCoord + tiktokOffset);
    vec4 blueTikTok = texture(DiffuseSampler, texCoord - tiktokOffset);

    float lumred = redTiktok.r * (0.299) + redTiktok.g * (0.587) + redTiktok.b * (0.114);
    float lumblue = blueTikTok.r * (0.299) + blueTikTok.g * (0.587) + blueTikTok.b * (0.114);

    if (round(lumblue*2) == round(lumred*2))  {
        lumblue = 0.0;
        lumred = 0.0;
    }
    lumblue = lumblue >= 0.9 || lumblue <= 0.2 ? 0 : lumblue;
    lumred = lumred >= 0.9 || lumred <= 0.2 ? 0 : lumred;

    vec4 tiktok = vec4(lumred,lumblue,lumblue,1);
    vec3 tiktokColor = RGBtoHSV(tiktok.rgb);
    if (tiktokColor.r > 0.5) tiktokColor.r -=0.4;
    if (tiktokColor.r < 0.3) tiktokColor.r +=0.6;
    tiktokColor.r += 0.09;
    tiktokColor.g -= 0.25;
    if (tiktokColor.g <= 0.25) {
        if (tiktokColor.g <= 0.1) tiktokColor.b = 0;
        if (tiktokColor.g <= 0.25 && tiktokColor.b > 0.1) tiktokColor += 0.2;
    }

    tiktok = vec4(HSVtoRGB(tiktokColor),1);

    //
    vec4 final = mix(vec4(red, green/2, blue, 1.75),vec4(total.r,total.g/2,total.b, 0.25),0.85);

    fragColor = mix(final,vec4(HSVtoRGB(tiktokColor),1.0) ,0.95);
}
