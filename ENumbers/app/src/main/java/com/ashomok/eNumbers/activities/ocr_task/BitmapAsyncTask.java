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

//    /**
//     * Boolean that tells me how to treat a transparent pixel (Should it be black?)
//     */
//    private static final boolean TRASNPARENT_IS_BLACK = false;
//    /**
//     * This is a point that will break the space into Black or white
//     * In real words, if the distance between WHITE and BLACK is D;
//     * then we should be this percent far from WHITE to be in the black region.
//     * Example: If this value is 0.5, the space is equally split.
//     */
//    private static final double SPACE_BREAKING_POINT = 0.5;

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


//    private boolean shouldBeBlack(int pixel) {
//        int alpha = Color.alpha(pixel);
//        int redValue = Color.red(pixel);
//        int blueValue = Color.blue(pixel);
//        int greenValue = Color.green(pixel);
//        if (alpha == 0x00) //if this pixel is transparent let me use TRASNPARENT_IS_BLACK
//            return TRASNPARENT_IS_BLACK;
//        // distance from the white extreme
//        double distanceFromWhite = Math.sqrt(Math.pow(0xff - redValue, 2) + Math.pow(0xff - blueValue, 2) + Math.pow(0xff - greenValue, 2));
//        // distance from the black extreme //this should not be computed and might be as well a function of distanceFromWhite and the whole distance
//        double distanceFromBlack = Math.sqrt(Math.pow(0x00 - redValue, 2) + Math.pow(0x00 - blueValue, 2) + Math.pow(0x00 - greenValue, 2));
//        // distance between the extremes //this is a constant that should not be computed :p
//        double distance = distanceFromBlack + distanceFromWhite;
//        // distance between the extremes
//        return ((distanceFromWhite / distance) > SPACE_BREAKING_POINT);
//    }
//
//    private Bitmap createBinarizated(Bitmap source) {
//
//        long startTime = System.currentTimeMillis();
//
//        int customColor = ContextCompat.getColor(context, R.color.light_blue);
//        binarizated = Bitmap.createBitmap(source);
//        // I will look at each pixel and use the function shouldBeBlack to decide
//        // whether to make it black or otherwise white
//        for (int i = 0; i < binarizated.getWidth(); i++) {
//            for (int c = 0; c < binarizated.getHeight(); c++) {
//                int pixel = binarizated.getPixel(i, c);
//                if (shouldBeBlack(pixel)) {
//                    try {
//                        binarizated.setPixel(i, c, customColor);
//                        //binarizated.setPixel(i, c, Color.BLACK);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    //binarizated.setPixel(i, c, Color.WHITE);
//                }
//            }
//        }
//
//        long endTime = System.nanoTime();
//        Log.e(this.getClass().getCanonicalName(), "createBinarizated" + (endTime-startTime));
//        return binarizated;
//    }
//
//    /**
//     * @param imgIn - Source image. It will be released, and should not be used more
//     * @return a copy of imgIn, but muttable.
//     * @author Derzu
//     * <p/>
//     * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
//     * more memory that there is already allocated.
//     */
//    public Bitmap convertToMutable(Bitmap imgIn) {
//        try {
//            //this is the file going to use temporally to save the bytes.
//            // This file will not be a image, it will store the raw image data.
//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");
//
//            //Open an RandomAccessFile
//            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
//            //into AndroidManifest.xml file
//            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
//
//            // get the width and height of the source bitmap.
//            int width = imgIn.getWidth();
//            int height = imgIn.getHeight();
//            Bitmap.Config type = imgIn.getConfig();
//
//            //Copy the byte to the file
//            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
//            FileChannel channel = randomAccessFile.getChannel();
//            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
//            imgIn.copyPixelsToBuffer(map);
//            //recycle the source bitmap, this will be no longer used.
//            imgIn.recycle();
//            System.gc();// try to force the bytes from the imgIn to be released
//
//            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
//            imgIn = Bitmap.createBitmap(width, height, type);
//            map.position(0);
//            //load it back from temporary
//            imgIn.copyPixelsFromBuffer(map);
//            //close the temporary file and channel , then delete that also
//            channel.close();
//            randomAccessFile.close();
//
//            // delete the temp file
//            file.delete();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return imgIn;
//    }


}
