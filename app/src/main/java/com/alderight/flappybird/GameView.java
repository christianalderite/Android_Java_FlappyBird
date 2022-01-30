package com.alderight.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View{

    // This is our custom view class
    Handler handler; // Handler is requird to scheduled a runnable after some delay
    Runnable runnable;
    final int UPDATE_MILLIS = 30;
    Bitmap background;
    Display display;
    Point point;
    int dWidth, dHeight; // Device width and height
    Rect rect;
    // Lets create a Bitmap array for the bird
    Bitmap[] birds;
    // We need an integer variable to keep track of bird frame
    int birdFrame = 0;
    int velocity = 0, gravity = 3; // Lets play around with these values
    // We need to keep track of bird position
    int birdX, birdY;

    public GameView(Context context){
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); // This will call onDraw()
            }
        };

        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bird1);
        birds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bird2);
        birdX = dWidth/2 - birds[0].getWidth()/2; // Initially bird will be on center
        birdY = dHeight/2 - birds[0].getHeight()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // We'll draw our view inside onDraw()
        // Draw the background on canvas
        // canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect, null); // fixed
        if(birdFrame == 0){
            birdFrame = 1;
        }else{
            birdFrame = 0;
        }

        // The bird should be on the screen
        if(birdY < dHeight - birds[0].getHeight() || velocity < 0){ // This way bird does not go beyond the bottom edge of the screen
            velocity += gravity; // As the bird falls, it gets faster and faster as the velocity value increases by gravity each time
            birdY += velocity;
        }

        // We want the bird to be displayed at the center of the screen
        // Both birds[0] and birds[1] have same dimension
        canvas.drawBitmap(birds[birdFrame], birdX, birdY, null);
        handler.postDelayed(runnable,UPDATE_MILLIS);
    }
    // Get the touch event

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            // This is tap is detected on screen
            // Here we want the bird to move upwards by some unit
            velocity = -30; // Lets say 30 units on upward direction

        }

        return true; // By return true indicates that we're done with touch event and no further action is required by Android
    }
}
