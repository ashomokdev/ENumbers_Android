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

import android.widget.TextView;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;


public class HelloWorldActivity extends Activity implements IGetInfoByENumber {
    Button searchBtn;
    TextView outputEditText;
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
        outputEditText = (TextView) findViewById(R.id.outputE);
        outputEditText.setKeyListener(null); //to make EditText not editable
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ENumber result = GetInfoByENumber(inputEditText.getText().toString());
                    outputEditText.setText(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "Hello world", Toast.LENGTH_LONG).show();
            }
        });
    }

    //TODO make automatic copying the base.xml to the res/raw after updating.
    @Override
    public ENumber GetInfoByENumber(String ENumber_input) throws Exception {
        InputStream inputStream = this.getApplicationContext().getResources().openRawResource(R.raw.base);
        try {
            Serializer serializer = new Persister();
            ENumbersCollection eNumbersCollection = serializer.read(ENumbersCollection.class, inputStream);
            for (ENumber eNumber : eNumbersCollection) {
                if (eNumber.get_code().equals(ENumber_input) ) {
                    return eNumber;
                }
            }

        } catch (Exception e) {
            //TODO
        }
        return null;
    }
}

//TODO
//unable "E" deletion in the beginning of the string
//close keyboard after Search btn pressing
//

