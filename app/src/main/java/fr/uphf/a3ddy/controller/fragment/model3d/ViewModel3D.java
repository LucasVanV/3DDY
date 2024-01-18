package fr.uphf.a3ddy.controller.fragment.model3d;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewModel3D extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new MyGlSurfaceView(this);
        setContentView(glSurfaceView);
    }
}
