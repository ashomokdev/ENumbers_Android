package com.ashomok.eNumbers.activities.ocr_task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import com.ashomok.eNumbers.R;

/**
 * Created by Iuliia on 24.12.2015.
 */
public final class OCRAnimationView extends View implements BinarizeTaskDelegate {
    private float dX;

    private Bitmap scanband;
    private int screenW;
    private int screenH;
    private float X;
    private float Y;
    private int scanBandW = 50;
    private Bitmap background;
    private Bitmap binarizated;


    public OCRAnimationView(Context context, Uri imageUri) {
        super(context);

        X = 0;
        Y = 0;
        dX = 0.7f; //horizontal speed

        scanband = BitmapFactory.decodeResource(getResources(), R.drawable.scan_band); //load a scanband image

        BinarizeAsyncTask binarizeAsyncTask = new BinarizeAsyncTask(context, this);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB) {
            binarizeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUri);
        }
        else {
            binarizeAsyncTask.execute(imageUri);
        }
    }


    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;

        //TODO forbid rotation

        if (background != null) {
            background = Bitmap.createScaledBitmap(background, w, h, true); //Resize background to fit the screen.
        }
        if (binarizated != null) {
            binarizated = Bitmap.createScaledBitmap(binarizated, w, h, true); //Resize to fit the screen.
        }

        scanband = Bitmap.createScaledBitmap(scanband, scanband.getWidth(), h, true);
        if (X > screenW - scanBandW) {
            X = screenW - scanBandW;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (background != null) {
            //Draw background
            canvas.drawBitmap(background, 0, 0, null);
        }

        if (binarizated != null) {
            Bitmap croppedBitmap = Bitmap.createBitmap(binarizated, (int) X, 0, scanBandW, screenH);
            canvas.drawBitmap(croppedBitmap, X, 0, null);
        }

        //Compute scanband speed and location.
        X += dX; //Increase or decrease horizontal position.
        if ((X > (screenW - scanBandW)) || (X < 0)) {
            dX = (-1f) * dX; //Reverse speed when one side hit.
        }

        //Draw scanband
        if (dX > 0) //moving from left to right
        {
            canvas.drawBitmap(scanband, X + scanBandW, Y, null);
        } else {
            canvas.drawBitmap(scanband, X, Y, null);
        }

        //Call the next frame.
        invalidate();
    }

    //TODO rewrite
    @Override
    public void TaskCompletionResult(Bitmap[] result) {
        background = result[0];
        binarizated = result[1];

        if (background != null) {
            background = Bitmap.createScaledBitmap(background, screenW, screenH, true); //Resize background to fit the screen.
        }
        if (binarizated != null) {
            binarizated = Bitmap.createScaledBitmap(binarizated, screenW, screenH, true); //Resize to fit the screen.
        }
    }
}
