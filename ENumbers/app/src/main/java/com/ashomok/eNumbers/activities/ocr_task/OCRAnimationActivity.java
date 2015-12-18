package com.ashomok.eNumbers.activities.ocr_task;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ashomok.eNumbers.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Iuliia on 13.12.2015.
 */
public class OCRAnimationActivity extends AppCompatActivity {

    private Button cancel;
    private ImageView image;
    private static final int MAX_IMAGE_DIMENSION = 512;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_animation_layout);

        cancel = (Button) findViewById(R.id.cancel_btn);
        image = (ImageView) findViewById(R.id.image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Uri imageUri = getIntent().getParcelableExtra("image");

            Bitmap imageBitmap = prepareImage(imageUri);

            image.setImageBitmap(imageBitmap);
        } else {
            try {
                throw new Exception("thumbnail was not obtained.");
            } catch (Exception e) {
                Log.e(this.getClass().getCanonicalName(), e.getMessage());
                e.printStackTrace();
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    private Bitmap prepareImage(Uri imageUri) {
        try {
            return getCorrectlyOrientedImage(this, imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
        return null;
    }

    public Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
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
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                (rotatedWidth > rotatedHeight)) ||
                (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) &&
                        (rotatedWidth < rotatedHeight)) {
            //fits ok

        } else {
            //shod be rotated in -90 degrees.
            rotate +=90;
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
