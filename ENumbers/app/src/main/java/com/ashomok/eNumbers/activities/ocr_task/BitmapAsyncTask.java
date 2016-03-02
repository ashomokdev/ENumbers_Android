package com.ashomok.eNumbers.activities.ocr_task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Iuliia on 24.12.2015.
 */
    public class BitmapAsyncTask extends AsyncTask<String, Integer, Bitmap> {

    private BitmapTaskDelegate delegate;
    private Bitmap background;
    public static final String TAG = "BitmapAsyncTask";


    public BitmapAsyncTask(BitmapTaskDelegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imagePath;
        try {
            imagePath = params[0];
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return  null;
        }

        background = prepareImage(imagePath);

        return background;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        delegate.TaskCompletionResult(result);
    }

    private Bitmap prepareImage(String path) {
        try {
            return getCorrectlyOrientedImage(path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
        return null;
    }

    /**
     * decrease the size and correct orientation
     * @param _path
     * @return
     * @throws IOException
     */
    private Bitmap getCorrectlyOrientedImage(String _path) throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

        try {
            ExifInterface exif = new ExifInterface(_path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.v(TAG, "Orient: " + exifOrientation);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(TAG, "Rotation: " + rotate);

            if (rotate != 0) {

                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.postRotate(rotate);

                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);

            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }
}
