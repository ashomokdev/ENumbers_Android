package com.ashomok.eNumbers.activities.ocr_task;

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

//    final private String URL1 = "http://enumb.azurewebsites.net/enumbservice-0.3.0/upload";
//    final private String URL2 = "http://10.0.3.2:8080/enumbservice-0.3.0/upload";

    final private String URL = "http://5.196.135.1:8080/upload";

    private String img_path;

    public RecognizeImageAsyncTaskRESTClient(String img_path, TaskDelegate delegate) {
        super(delegate);

        this.img_path = img_path;
    }


    @Override
    protected String[] doInBackground(Void... params) {

        RestTemplate restTemplate = new RestTemplate(true);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(new File(img_path)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
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
