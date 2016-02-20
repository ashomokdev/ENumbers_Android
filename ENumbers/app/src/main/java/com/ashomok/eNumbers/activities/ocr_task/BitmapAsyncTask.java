package com.ashomok.eNumbers.activities.ocr_task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Iuliia on 24.12.2015.
 */
    public class BitmapAsyncTask extends AsyncTask<Uri, Integer, Bitmap> {

    private final Context context;
    private BitmapTaskDelegate delegate;
    private static final int MAX_IMAGE_DIMENSION = 512;
    private Bitmap background;


    public BitmapAsyncTask(Context context, BitmapTaskDelegate delegate)
    {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected Bitmap doInBackground(Uri... params) {
        Uri imageUri;
        try {
            imageUri = params[0];
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return  null;
        }

        background = prepareImage(imageUri);

        return background;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        delegate.TaskCompletionResult(result);
    }

    private Bitmap prepareImage(Uri imageUri) {
        try {
            return getCorrectlyOrientedImage(context, imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
        return null;
    }

    private Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {

        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;

        ExifInterface exif = new ExifInterface(photoUri.getEncodedPath());

        int exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */

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

        if (rotate != 0) {
            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.postRotate(rotate);

            // Rotating Bitmap
            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), mtx, true);
        }

        return srcBitmap;
    }




}
