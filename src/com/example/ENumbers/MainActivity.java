package com.example.eNumbers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends Activity implements IGetterInfoByENumber {

    private static final int SPEECH_REQUEST_CODE = 0;

    Button searchBtn;

    ImageButton voiceInputBtn;

    ListView listViewResult;

    EditText inputEditText;

    TextView outputWarning;

    ListView listView;

    Intent intent;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        inputEditText = (EditText) findViewById(R.id.inputE);

        listView = (ListView) findViewById(R.id.ENumberList);

        outputWarning = (TextView) findViewById(R.id.warning);

        searchBtn = (Button) findViewById(R.id.button);

        voiceInputBtn = (ImageButton) findViewById(R.id.mic_icon);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v!=null) {

                    outputWarning.setText("");

                    listView.setAdapter(null);

                    ENumber result = null;

                    try {

                        if (inputEditText.getText().toString().length() >= 3) {
                            result = GetInfoByENumber(inputEditText.getText().toString());
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    if (result == null) {

                        outputWarning.setText(getApplicationContext().getString(R.string.notFoundMessage));
                    } else {

                        listViewResult = (ListView) findViewById(R.id.ENumberList);

                        List<ENumber> data = new ArrayList<ENumber>();

                        //TODO add all result
                        data.add(result);

                        listView.setAdapter(new ENumberListAdapter(v.getContext(), data));

                        //to hide the soft keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
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

                    inputEditText.setSelection(inputEditText.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        inputEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    searchBtn.performClick();

                    return true;
                }

                return false;
            }
        });

        voiceInputBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                displaySpeechRecognizer();
            }
        });
    }

    public void displaySpeechRecognizer() {

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {

            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            //TODO Fix bug here
            String spokenText = results.get(0);
            inputEditText.append(spokenText);

            searchBtn.setPressed(true);
            searchBtn.invalidate();
            searchBtn.performClick();

            Toast.makeText(getApplicationContext(), spokenText, Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public ENumber GetInfoByENumber(String ENumber_input) throws Exception {

        //TODO check inputing before searching
        InputStream inputStream = this.getApplicationContext().getResources().openRawResource(R.raw.base);

        try {

            Serializer serializer = new Persister();
            ENumbersCollection eNumbersCollection = serializer.read(ENumbersCollection.class, inputStream);

            for (ENumber eNumber : eNumbersCollection) {

                if (eNumber.getCode().equals(ENumber_input)) {

                    return eNumber;
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}



