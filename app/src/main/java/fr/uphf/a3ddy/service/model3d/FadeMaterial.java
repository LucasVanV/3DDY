package fr.uphf.a3ddy.service.model3d;

import org.intellij.lang.annotations.Language;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.shaders.FragmentShader;
import org.rajawali3d.materials.shaders.VertexShader;

public final class FadeMaterial extends Material {
    public static final String MVP_MATRIX = "uMVPMatrix";
    public static final String POSITION = "vPosition";
    public static final String TEXTURE_COORDINATE = "vTextureCoordinate";
    public static final String NORMAL = "aNormal";  // Ajout de l'attribut normal

    // Ajout des uniforms pour les matrices et le temps
    public static final String NORMAL_MATRIX = "uNormalMatrix";
    public static final String MODEL_MATRIX = "uModelMatrix";
    public static final String MODEL_VIEW_MATRIX = "uModelViewMatrix";
    public static final String TIME = "uTime";

    private static final String VERTEX_SHADER = "" +
            "uniform mat4 " + MVP_MATRIX + ";\n" +
            "uniform mat4 " + NORMAL_MATRIX + ";\n" +
            "uniform mat4 " + MODEL_MATRIX + ";\n" +
            "uniform mat4 " + MODEL_VIEW_MATRIX + ";\n" +
            "uniform float " + TIME + ";\n" +
            "attribute vec4 " + POSITION + ";\n" +
            "attribute vec2 " + TEXTURE_COORDINATE + ";\n" +
            "attribute vec4 " + NORMAL + ";\n" +
            "varying vec2 vTextureCoord;\n" +
            "varying vec4 vColor;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = " + MVP_MATRIX + " * " + POSITION + ";\n" +
            "    vTextureCoord = " + TEXTURE_COORDINATE + ";\n" +
            "    vColor = " + NORMAL + ";\n" +
            "}\n";

    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform sampler2D uTexture;\n" +
            "varying vec4 vColor;\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 textureColor = texture2D(uTexture, vTextureCoord);\n" +
            "    gl_FragColor = textureColor * vColor;\n" +
            "}\n";

    public FadeMaterial() {
        super(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));
    }
}
