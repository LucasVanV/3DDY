package fr.uphf.a3ddy.controller.fragment.model3d;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import fr.uphf.a3ddy.R;


public class SceneView3D extends AppCompatActivity {
    private static final String TAG = "TEST AR Core ";
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

            // Définissez le layout qui contient votre SceneView
            setContentView(R.layout.fragment_model_troisdsceneview);
            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                        Anchor anchor = hitResult.createAnchor();
                        loadModel(anchor, Uri.parse("robot.sfb"));//to load model 3D
                    });

    }


    private void loadModel(Anchor anchor, Uri uri) {
        ModelRenderable.builder()
                .setSource(
                        this,
                                uri)
                .build()
                .thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable))
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable." + throwable.getMessage(), throwable);
                            return null;
                        });

    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode node = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(node);
        transformableNode.setRenderable(modelRenderable);

        arFragment.getArSceneView().getScene().addChild(node);
        transformableNode.select();
    }


    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

}
