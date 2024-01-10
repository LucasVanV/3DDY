// Model3dFragment.java
package fr.uphf.a3ddy.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Button;

import org.rajawali3d.surface.RajawaliSurfaceView;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.service.model3d.MyRenderer;

public class Model3dFragment extends Activity {
    private MyRenderer mRenderer;
    private RajawaliSurfaceView mSurfaceView;
    Button zoomInButton;
    Button zoomOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_model_3d);

        zoomInButton = findViewById(R.id.zoomInButton);
        zoomOutButton = findViewById(R.id.zoomOutButton);

        mSurfaceView = findViewById(R.id.mySurfaceView);
        mRenderer = new MyRenderer(this, zoomInButton, zoomOutButton) {
            @Override
            public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
                // Handle offsets change if needed
            }

            @Override
            public void onTouchEvent(MotionEvent event) {
                // Handle touch events if needed
            }
        };
        mSurfaceView.setSurfaceRenderer(mRenderer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSurfaceView != null) {
            mSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSurfaceView != null) {
            mSurfaceView.onPause();
        }
    }
}
