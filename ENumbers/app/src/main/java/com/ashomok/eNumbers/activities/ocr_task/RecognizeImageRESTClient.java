package com.ashomok.eNumbers.activities.ocr_task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Iuliia on 10.12.2015.
 */

public final class RecognizeImageRESTClient extends RecognizeImageAsyncTask {
    private static final String TAG = RecognizeImageRESTClient.class.getSimpleName();

//    final private String URL1 = "http://enumb.azurewebsites.net/enumbservice-0.3.0/upload";
//    final private String URL2 = "http://10.0.3.2:8080/enumbservice-0.3.0/upload";
//   final private String URL3 = "http://5.196.135.1:8080/upload"; //unisender

    final private String URL = "https://enumbers-160312.appspot.com/upload";

    private String img_path;
    private static final int MaxFileSizeInMb = 2;

    public RecognizeImageRESTClient(String img_path) {
        this.img_path = img_path;
        if (img_path == null) {
            Log.e(TAG, "img_path == null");
        }
    }

    private byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap toBitmap(File image) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = scaleBitmapDown(bitmap, 1200);
        return bitmap;
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    @Override
    protected String[] doInBackground(Void... params) {
        File file = new File(img_path);
        long fileSizeInBytes = file.length();
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInBytes / 1024 / 1024;
        String[] result;
        if (fileSizeInMB < MaxFileSizeInMb) {
            result = doPOST(file);
        } else {
            try {
                File decrised = decrese(file);
                result = doPOST(decrised);
            } catch (Exception e) {
                e.printStackTrace();
                result = new String[0];
            }
        }
        return result;
    }

    private File decrese(File file) throws IOException {
        Bitmap bitmap = toBitmap(file);
        File overriden = new File(img_path);
        FileOutputStream fooStream = new FileOutputStream(overriden, false); // false to overwrite.
        byte[] myBytes = toByteArray(bitmap);
        fooStream.write(myBytes);
        fooStream.close();
        return overriden;
    }

    private String[] doPOST(File file) {
        String[] result;
        RestTemplate restTemplate = new RestTemplate(true);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(file));
        HttpHeaders headers = new HttpHeaders(); //here
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
                map, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL, HttpMethod.POST, requestEntity,
                String.class);

        HttpStatus statusCode = responseEntity.getStatusCode();

        if (statusCode == HttpStatus.ACCEPTED) {
            result = responseEntity.getBody().replaceAll("\\[", "").replaceAll("\\]","").split(", ");
        } else {
            result = new String[0];
        }
        return result;
    }

}
