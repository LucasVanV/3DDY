package fr.uphf.a3ddy.controller.fragment.model3d;


import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class Obj3DBulder {

    private ObjParser objParser;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
            "uniform mat4 uMVPMatrix;"+
                    "void main() {" +
                    "  gl_Position = uMVPMatrix + vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private ShortBuffer drawListBuffer;
    private final int mProgram;


    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    private float vertexCoords[];
    private int positionHandle;

    public Obj3DBulder(InputStream pathObj) throws IOException {
        this.objParser = new ObjParser();
        objParser.parseObjFile(pathObj);
        Log.d("Nb GetVertices.size : ", String.valueOf(objParser.getVertices().size()));
        this.vertexCount = objParser.getVertices().size() / COORDS_PER_VERTEX;

        vertexCoords = toPrimitivList(objParser.getVertices()); //TODO
        short[] drawOrder = toPrimitivShort(objParser.getFaces()); //TODO
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertexCoords.length * 4);
        // use the device hardware's native byte order

        bb.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertexCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length*2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        int vertexShader = MyRender.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyRender.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();
        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);
        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);
        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    private int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 3; // 3 bytes per vertex

    public void draw(float[] mvpmatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);
        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);

        //TODO create a bug
        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMVPMatrix");
        GLES20.glUniformMatrix4fv(vPMatrixHandle,1,false,mvpmatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }


    public float[] toPrimitivList(List<Float> arrayList){
        float primitive[] = new float[arrayList.size()];
        for(int i = 0 ; i<arrayList.size(); i++) {
            primitive[i]=arrayList.indexOf(i);
        }
        return primitive;
    }

    public short[] toPrimitivShort(List<Integer> arrayList){
        short primitive[] = new short[arrayList.size()];
        for(int i = 0 ; i<arrayList.size(); i++) {
            primitive[i] = arrayList.get(i).shortValue();
        }
        return primitive;
    }

}