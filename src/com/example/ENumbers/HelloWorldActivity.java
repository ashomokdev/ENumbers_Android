package com.example.ENumbers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


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

                //Toast.makeText(getApplicationContext(), "Hello world", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public String GetInfoByENumber(String ENumber) throws FileNotFoundException {
        //TODO return formated text with different colors for each piece of info
        File file = new File("base.xml");
        if (!file.exists()) {
            try {
                throw new Exception("base.xml was not found");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileInputStream fis = new FileInputStream(file);
        XMLStreamReader xmlr = xmlif.createFilteredReader(
                xmlif.createXMLStreamReader(fis),
                new MyStreamFilter());

        int eventType = xmlr.getEventType();
        printEventType(eventType);

        while (xmlr.hasNext()) {
            eventType = xmlr.next();
            printEventType(eventType);
            printName(xmlr,eventType);
            printText(xmlr);

            if (xmlr.isStartElement()) {
                printAttributes(xmlr);
            }
            printPIData(xmlr);
            System.out.println("-----------------------");
        }

        return "";
    }


}

