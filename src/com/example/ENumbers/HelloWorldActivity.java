package com.example.ENumbers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
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

                if (v != null) {
                    ENumber result = null;
                    try {
                        result = GetInfoByENumber(inputEditText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    outputEditText.setText(result.toString());

                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String startChar = getApplicationContext().getString(R.string.startChar);
                if (charSequence.toString().startsWith(startChar)) {

                } else {
                    inputEditText.setText(startChar);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
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
                if (eNumber.get_code().equals(ENumber_input)) {
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
//2. design without search button
//3. Add text position to its plase after E deletion

