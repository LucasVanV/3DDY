// MyRenderer.java
package fr.uphf.a3ddy.service.model3d;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.renderer.RajawaliRenderer;

import fr.uphf.a3ddy.R;

public class MyRenderer extends RajawaliRenderer {

    public MyRenderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    @Override
    protected void onRender(long elapsedTime, double deltaTime) {
        super.onRender(elapsedTime, deltaTime);

        // Initialisation OpenGL ES ici
        // Appel à la méthode initScene pour l'initialisation de la scène
        initScene();
    }

    // Méthode initScene pour l'initialisation de la scène
    protected void initScene() {
        // Initialisation de la scène ici
        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.football_ball);
        try {
            objParser.parse();
            getCurrentScene().addChild(objParser.getParsedObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
