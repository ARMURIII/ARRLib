#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main(){
    vec2 uv1 = texCoord.xy / InSize.xy;
    float valor = 1.0 - length(uv1); // Isso vai aumentar a medida em que a coordenada se aproxima do canto superior esquerdo (0,0)

    float linewidth =  valor/600.;//0.00149

    vec2 uv = texCoord;
    vec2 uv_b = vec2(uv.x, uv.y + linewidth);
    vec2 uv_l = vec2(uv.x + linewidth, uv.y);
    vec2 uv_r = vec2(uv.x - linewidth, uv.y);
    vec2 uv_u = vec2(uv.x, uv.y - linewidth);

    vec4 center = texture(DiffuseSampler, texCoord.st);

    float near = 0.04;
    float far = 9;

    float d1 = 1-2.0 * near * far / (far + near - (2.0 * texture(ParticlesDepthSampler, uv).x - 1.0) * (far - near)) / far;
    float d2 = 1-2.0 * near * far / (far + near - (2.0 * texture(ParticlesDepthSampler, uv_b).x - 1.0) * (far - near)) / far;
    float d3 = 1-2.0 * near * far / (far + near - (2.0 * texture(ParticlesDepthSampler, uv_u).x - 1.0) * (far - near)) / far;
    float d4 = 1-2.0 * near * far / (far + near - (2.0 * texture(ParticlesDepthSampler, uv_l).x - 1.0) * (far - near)) / far;
    float d5 = 1-2.0 * near * far / (far + near - (2.0 * texture(ParticlesDepthSampler, uv_r).x - 1.0) * (far - near)) / far;
    vec4 translucentSample = texture(TranslucentSampler, uv);
    float translucent = (translucentSample.r + translucentSample.g + translucentSample.b)/3;
    translucent = translucent*10*0.5;

    float factor = 1;

    float difference = (d1 - d2-translucent)*factor;
    float difference1 = (d1 - d3-translucent)*factor;
    float difference2 = (d1 - d4-translucent)*factor;
    float difference3 = (d1 - d5-translucent)*factor;

    float alldif = (difference + difference1 + difference2 + difference3);
    float borders = clamp(alldif, 0.0, 0.5)*2;

    vec4 limitMask = texture(BrightnessLimitMask, texCoord);
    float attenuation = 1.0 - min(limitMask.r, brightnessLimit);

    //fragColor = vec4(center.rgb+(center.rgb*(clamp(alldif*5., 0., 1.))*(d1)), center.a);

    fragColor = vec4(center.rgb + center.rgb*clamp(alldif*20., 0., 1.)*d1, center.a);
    //fragColor = vec4(alldif);

    //fragColor = vec4(floor(alldif*5.));
    //fragColor = vec4(center.rgb + ((center.rgb*attenuation*2.5) * borders), center.a); //white borders
    //fragColor = vec4(center.rgb -= (borders), center.a); //black borders
}
