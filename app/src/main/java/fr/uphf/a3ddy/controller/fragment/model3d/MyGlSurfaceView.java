package fr.uphf.a3ddy.controller.fragment.model3d;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGlSurfaceView extends GLSurfaceView {

    private static final float TOUCH_SCALE = 180f/320;

    private final MyRender render;

    private float previousX, previousY;

    public MyGlSurfaceView(Context context) {
        super(context);
        this.render = new MyRender(getContext(),"robot.obj");
        setEGLContextClientVersion(2);
        setRenderer(render);
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                float dx = x - previousX;
                float dy = y - previousY;

                if(y > getHeight() /2) {
                    dx =dx *-1;
                }
                if(x > getHeight() /2) {
                    dy =dy *-1;
                }

                render.setAngle(render.getAngle() + ((dx+dy) * TOUCH_SCALE));
                requestRender();
            }
        }
        previousX = x;
        previousY = y;
        return false;
    }
}

