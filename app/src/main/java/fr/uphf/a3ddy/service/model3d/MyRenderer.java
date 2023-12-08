package fr.uphf.a3ddy.service.model3d;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.materials.*;
import org.rajawali3d.materials.shaders.FragmentShader;
import org.rajawali3d.materials.shaders.VertexShader;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import java.util.List;

import fr.uphf.a3ddy.R;

public class MyRenderer extends RajawaliRenderer {
    private float mPreviousX;
    private float mPreviousY;
    private final float TOUCH_SCALE_FACTOR = 0.2f;
    private Vector3 mObjectPosition = new Vector3();
    private ScaleGestureDetector mScaleDetector;
    private Button zoomInButton;
    private Button zoomOutButton;
    private static final float ZOOM_FACTOR = 1.2f;  // à ajuster pour le zoom

    public MyRenderer(Context context, Button zoomInButton, Button zoomOutButton) {
        super(context);
        setFrameRate(60);

        this.zoomInButton = zoomInButton;
        this.zoomOutButton = zoomOutButton;

        // Set click listeners
        this.zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajuster le zoom pour zoomer
                getCurrentCamera().setFieldOfView(getCurrentCamera().getFieldOfView() / ZOOM_FACTOR);
            }
        });

        this.zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajuster le zoom pour dézoomer
                getCurrentCamera().setFieldOfView(getCurrentCamera().getFieldOfView() * ZOOM_FACTOR);
            }
        });

        // Initialiser le détecteur de pincement
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }


    @Override
    protected void onRender(long elapsedTime, double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        initScene();
    }

    protected void initScene() {
        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.football_ball);
        try {
            objParser.parse();

            // Créer le matériau FadeMaterial
            FadeMaterial fadeMaterial = new FadeMaterial();

            // Appliquer le matériau à l'objet 3D
            objParser.getParsedObject().setMaterial(fadeMaterial);

            mObjectPosition.setAll(0f, 0f, 0f);
            objParser.getParsedObject().setPosition(mObjectPosition);

            // Ajouter l'objet à la scène
            getCurrentScene().addChild(objParser.getParsedObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        // Gérer les événements de pincement
        mScaleDetector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = x;
                mPreviousY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                getCurrentCamera().rotate(Vector3.Axis.Y, -dx * TOUCH_SCALE_FACTOR);
                getCurrentCamera().rotate(Vector3.Axis.X, -dy * TOUCH_SCALE_FACTOR);

                mObjectPosition.x += dx * 0.01f;
                mObjectPosition.y += -dy * 0.01f;
                mObjectPosition.z += dy * 0.01f;

                // Modifie la position de l'objet
                List<Object3D> childObjects = getCurrentScene().getChildrenCopy();
                for (Object3D childObject : childObjects) {
                    childObject.setPosition(mObjectPosition);
                }

                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        mPreviousX = x;
        mPreviousY = y;

        Log.d("MyRenderer", "onTouchEvent: x=" + x + ", y=" + y);
    }

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();

        // Ajuster le zoom en fonction du facteur de pincement
        getCurrentCamera().setFieldOfView(getCurrentCamera().getFieldOfView() / ZOOM_FACTOR);

        return true;
    }

}

    // Ajouter ces méthodes pour gérer les boutons de zoom
    public void setZoomInButton(Button zoomInButton) {
        this.zoomInButton = zoomInButton;
        this.zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajuster le zoom pour zoomer
                getCurrentCamera().setFieldOfView(getCurrentCamera().getFieldOfView() / ZOOM_FACTOR);
            }
        });
    }

    public void setZoomOutButton(Button zoomOutButton) {
        this.zoomOutButton = zoomOutButton;
        this.zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajuster le zoom pour dézoomer
                getCurrentCamera().setFieldOfView(getCurrentCamera().getFieldOfView() / ZOOM_FACTOR);
            }
        });
    }

    public void moveCameraForward(float distance) {
        getCurrentCamera().moveForward(distance);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        mPreviousX += xOffsetStep;
        mPreviousY += yOffsetStep;
    }
}
