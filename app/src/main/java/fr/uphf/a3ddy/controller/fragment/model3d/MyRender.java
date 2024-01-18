package fr.uphf.a3ddy.controller.fragment.model3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRender implements GLSurfaceView.Renderer {
    private Obj3DBulder obj3DBulder;
    private String OBJECT_TO_LOAD;
    private Context context;

    private final float[] vPMatrix = new float[16]; // Produit de la projection et de la vue
    private final float[] projectionMatrix = new float[16]; // Matrise de projection des object
    private final float[] viewMatrix = new float[16]; // Matrise vues avec camera
    private final float[] rotationMatrix = new float[16]; // matrice de rotation
    private volatile float angle;

    public MyRender(Context context, String object_to_load) {
        this.context = context;
        this.OBJECT_TO_LOAD = object_to_load ;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig eglConfig) {
        GLES20.glClearColor(0.5f,1f,1f,1f);
        try {
            InputStream objInputStream = context.getAssets().open(this.OBJECT_TO_LOAD);
            obj3DBulder = new Obj3DBulder(objInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int with, int height) {
        GLES20.glViewport(0,0,with,height);
        final float ratio = (float) with/height;
        Matrix.frustumM(projectionMatrix,0,-ratio,ratio,-1,1,3,7);// ta gueule c'est magique
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setRotateM(rotationMatrix,0,angle,0,0,-1.0f);
        //Placement de de la camera et du point de focus
        Matrix.setLookAtM(viewMatrix,0,1,1,3,1,2,3,0,0,0);
        //application de la vue sur la matrice
        Matrix.multiplyMM(vPMatrix,0,projectionMatrix,0,viewMatrix,0);
        Matrix.multiplyMM(scratch,0,vPMatrix,0,rotationMatrix,0);

        obj3DBulder.draw(scratch);
    }


    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
