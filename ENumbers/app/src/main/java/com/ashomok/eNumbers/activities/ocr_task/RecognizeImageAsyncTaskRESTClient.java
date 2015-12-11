package com.ashomok.eNumbers.activities.ocr_task;

import android.app.Fragment;

import com.ashomok.eNumbers.activities.TaskDelegate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;


/**
 * Created by Iuliia on 10.12.2015.
 */
public final class RecognizeImageAsyncTaskRESTClient extends RecognizeImageAsyncTask {

    final private String URL = "http://enumb.azurewebsites.net/enumbservice-0.3.0/upload";

    private String img_path;

    public RecognizeImageAsyncTaskRESTClient(Fragment parentFragment, String img_path, TaskDelegate delegate) {
        super(parentFragment, delegate);

        this.img_path = img_path;
    }


    @Override
    protected String[] doInBackground(Void... params) {

        RestTemplate restTemplate = new RestTemplate(true);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(new File(img_path)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
                map, headers);
        ResponseEntity<String[]> responseEntity = restTemplate.exchange(
                URL, HttpMethod.POST, requestEntity,
                String[].class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        String[] result;
        if (statusCode == HttpStatus.ACCEPTED) {
            result = responseEntity.getBody();
        } else {
            result = new String[0];
        }
        return result;

    }

}
