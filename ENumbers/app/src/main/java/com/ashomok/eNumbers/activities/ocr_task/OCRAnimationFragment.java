package com.ashomok.eNumbers.activities.ocr_task;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.ashomok.eNumbers.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Iuliia on 18.12.2015.
 */
public class OCRAnimationFragment extends Fragment {
    //    private ImageView image;
    private static final int MAX_IMAGE_DIMENSION = 512;

    private Uri imageUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment
        // setRetainInstance(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            imageUri = bundle.getParcelable("image");
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.ocr_animation_fragment, container, false);

        return new OCRAnimationView(getActivity());
    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        image = (ImageView) view.findViewById(R.id.image);
//        imageBitmap = prepareImage(imageUri);
//        image.setImageBitmap(imageBitmap);
//
//    }

    private Bitmap prepareImage(Uri imageUri) {
        try {
            return getCorrectlyOrientedImage(getActivity().getApplicationContext(), imageUri);
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
            rotate -= 90;
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

    private class OCRAnimationView extends View {
//        private final float acc;
        private float dX;
        Bitmap background;
        Bitmap binarizated;
        Bitmap scanband;
        int screenW;
        int screenH;
        float X;
        float Y;
        /**
         * Boolean that tells me how to treat a transparent pixel (Should it be black?)
         */
        private static final boolean TRASNPARENT_IS_BLACK = false;
        /**
         * This is a point that will break the space into Black or white
         * In real words, if the distance between WHITE and BLACK is D;
         * then we should be this percent far from WHITE to be in the black region.
         * Example: If this value is 0.5, the space is equally split.
         */
       // private static final double SPACE_BREAKING_POINT = 13.0 / 30.0;
        private static final double SPACE_BREAKING_POINT = 0.72;

        public OCRAnimationView(Context context) {
            super(context);

            X = 0;
            Y = 0;
//            acc = 0.07f; //acceleration
            dX = 0.7f; //horizontal speed

            Bitmap imageBitmap = prepareImage(imageUri);
            if (imageBitmap != null) {
                background = imageBitmap;
            } else {
                throw new NullPointerException("imageBitmap == null. Obtain imageBitmap before call.");
            }

            scanband = BitmapFactory.decodeResource(getResources(), R.drawable.scan_band); //load a scanband image

            if (! background.isMutable())
            {
                background = convertToMutable(background);
            }
            binarizated = createBinarizated(background);
        }


        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            screenW = w;
            screenH = h;
            background = Bitmap.createScaledBitmap(background, w, h, true); //Resize background to fit the screen.
            scanband = Bitmap.createScaledBitmap(scanband, scanband.getWidth(), h, true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //Draw background.
            canvas.drawBitmap(background, 0, 0, null);

//            //Draw recognized part.
//            canvas.drawBitmap(binarizated, 0, 0, null);

           //Compute scanband speed and location.
            int scanBandW = 0; //todo
            X+= dX; //Increase or decrease horizontal position.
            if ((X > (screenW - scanBandW)) || (X < 0)) {
                dX=(-1f)*dX; //Reverse speed when one side hit.
            }
            //Draw scanband
            canvas.drawBitmap(scanband, X, Y, null);

            //Call the next frame.
            invalidate();
        }

        private boolean shouldBeBlack(int pixel) {
            int alpha = Color.alpha(pixel);
            int redValue = Color.red(pixel);
            int blueValue = Color.blue(pixel);
            int greenValue = Color.green(pixel);
            if (alpha == 0x00) //if this pixel is transparent let me use TRASNPARENT_IS_BLACK
                return TRASNPARENT_IS_BLACK;
            // distance from the white extreme
            double distanceFromWhite = Math.sqrt(Math.pow(0xff - redValue, 2) + Math.pow(0xff - blueValue, 2) + Math.pow(0xff - greenValue, 2));
            // distance from the black extreme //this should not be computed and might be as well a function of distanceFromWhite and the whole distance
            double distanceFromBlack = Math.sqrt(Math.pow(0x00 - redValue, 2) + Math.pow(0x00 - blueValue, 2) + Math.pow(0x00 - greenValue, 2));
            // distance between the extremes //this is a constant that should not be computed :p
            double distance = distanceFromBlack + distanceFromWhite;
            // distance between the extremes
            return ((distanceFromWhite / distance) > SPACE_BREAKING_POINT);
        }

        private Bitmap createBinarizated(Bitmap source) {
            int customColor = ContextCompat.getColor(getActivity(), R.color.blue);
            binarizated = Bitmap.createBitmap(source);
            // I will look at each pixel and use the function shouldBeBlack to decide
            // whether to make it black or otherwise white
            for (int i = 0; i < binarizated.getWidth(); i++) {
                for (int c = 0; c < binarizated.getHeight(); c++) {
                    int pixel = binarizated.getPixel(i, c);
                    if (shouldBeBlack(pixel)) {
                        try {
                            binarizated.setPixel(i, c, customColor);
                            //binarizated.setPixel(i, c, Color.BLACK);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    } else {
                        //binarizated.setPixel(i, c, Color.WHITE);
                    }
                }
            }
            return binarizated;
        }

        /**
         * @author Derzu
         *
         * @see http://stackoverflow.com/a/9194259/833622
         *
         * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
         * more memory that there is already allocated.
         *
         * @param imgIn - Source image. It will be released, and should not be used more
         * @return a copy of imgIn, but muttable.
         */
        public Bitmap convertToMutable(Bitmap imgIn) {
            try {
                //this is the file going to use temporally to save the bytes.
                // This file will not be a image, it will store the raw image data.
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

                //Open an RandomAccessFile
                //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
                //into AndroidManifest.xml file
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

                // get the width and height of the source bitmap.
                int width = imgIn.getWidth();
                int height = imgIn.getHeight();
                Bitmap.Config type = imgIn.getConfig();

                //Copy the byte to the file
                //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
                FileChannel channel = randomAccessFile.getChannel();
                MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
                imgIn.copyPixelsToBuffer(map);
                //recycle the source bitmap, this will be no longer used.
                imgIn.recycle();
                System.gc();// try to force the bytes from the imgIn to be released

                //Create a new bitmap to load the bitmap again. Probably the memory will be available.
                imgIn = Bitmap.createBitmap(width, height, type);
                map.position(0);
                //load it back from temporary
                imgIn.copyPixelsFromBuffer(map);
                //close the temporary file and channel , then delete that also
                channel.close();
                randomAccessFile.close();

                // delete the temp file
                file.delete();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imgIn;
        }
    }
}
