package com.example.ENumbers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;


public class HelloWorldActivity extends Activity implements IGetInfoByENumber{
    Button searchBtn;
    EditText outputEditText;
    EditText inputEditText;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        inputEditText = (EditText) findViewById(R.id.inputE);
        String input = inputEditText.getText().toString();
        searchBtn = (Button) findViewById(R.id.button);
        outputEditText = (EditText) findViewById(R.id.outputE);
        outputEditText.setKeyListener(null); //to make EditText not editable
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputEditText.setText("this is me");

                try {
                    String result = GetInfoByENumber("123");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "Hello world", Toast.LENGTH_LONG).show();
            }
        });
    }

    //TODO make automatic copying the base.xml to the res/raw after updating.
    @Override
    public String GetInfoByENumber(String ENumber) throws Exception {
        //TODO return formated text with different colors for each piece of info

       // XmlPullParser xpp  = this.getApplicationContext().getResources().getXml(R.raw.base);
            InputStream inputStream = this.getApplicationContext().getResources().openRawResource(R.raw.base);
//        String result = "";
//        try(java.util.Scanner s = new java.util.Scanner(inputStream)) {
//           result  =  s.useDelimiter("\\A").hasNext() ? s.next() : "";
//        }
//return result;


        try {
            //java.lang.NoClassDefFoundError: org/simpleframework/xml/core/Persister
            Serializer serializer = new Persister();
            ENumbersCollection example = serializer.read(ENumbersCollection.class, inputStream);
            System.out.println(example);
            return example.toString();
        }
        catch (Exception e) {
          System.out.println(e.getMessage());
        }

        return "";
    }
}

