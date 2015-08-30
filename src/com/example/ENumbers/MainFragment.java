package com.example.eNumbers;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iuliia on 29.08.2015.
 */
public class MainFragment extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 0;

    private Button searchBtn;

    private ImageButton voiceInputBtn;

    private EditText inputEditText;

    private TextView outputWarning;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        inputEditText = (EditText) view.findViewById(R.id.inputE);
        inputEditText.setSelection(inputEditText.getText().length()); //starts type after "E"

        listView = (ListView) view.findViewById(R.id.ENumberList);

        outputWarning = (TextView) view.findViewById(R.id.warning);

        searchBtn = (Button) view.findViewById(R.id.button);

        voiceInputBtn = (ImageButton) view.findViewById(R.id.mic_icon);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v != null) {

                    outputWarning.setText("");

                    listView.setAdapter(null);

                    ENumber result = null;
                    String inputing = inputEditText.getText().toString();
                    try {

                        if (inputing.length() >= 3) {
                            result = GetInfoByENumber(inputing);
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    if (result == null) {

                        outputWarning.setText(getActivity().getApplicationContext().getString(R.string.notFoundMessage));
                    } else {
                        List<ENumber> data = new ArrayList<ENumber>();

                        //TODO add all result
                        data.add(result);

                        listView.setAdapter(new ENumberListAdapter(v.getContext(), data));

                        //to hide the soft keyboard
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
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

                String startChar = getActivity().getApplicationContext().getString(R.string.startChar);

                if (charSequence.toString().startsWith(startChar)) {

                } else {

                    inputEditText.setText(startChar);
                }
                inputEditText.setSelection(inputEditText.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        inputEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    searchBtn.setPressed(true);
                    searchBtn.invalidate();
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

    @Override
    public  void onPause()
    {
        super.onPause();
    }

    public void displaySpeechRecognizer() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {

            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);


            inputEditText.setText(
                    getActivity().getApplicationContext().getString(R.string.startChar));
            String spokenText = results.get(0);
            inputEditText.append(spokenText);

            searchBtn.setPressed(true);
            searchBtn.invalidate();
            searchBtn.performClick();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ENumber GetInfoByENumber(String eNumber_input) throws Exception {

        if (inputingIsValid(eNumber_input)) {
            InputStream inputStream = getActivity().getApplicationContext().getResources().openRawResource(R.raw.base);

            try {
                Serializer serializer = new Persister();
                ENumbersCollection eNumbersCollection = serializer.read(ENumbersCollection.class, inputStream);

                for (ENumber eNumber : eNumbersCollection) {

                    if (eNumber.getCode().equals(eNumber_input)) {

                        return eNumber;
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }

    private boolean inputingIsValid(String ENumber_input) {
        if (ENumber_input.startsWith(getActivity().getApplicationContext().getString(R.string.startChar))) {

            if (ENumber_input.length() > 3 && ENumber_input.length() < 7) {
                String numb_without_E = ENumber_input.substring(1);

                if (tryParse(numb_without_E) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private Integer tryParse(Object obj) {
        Integer retVal;
        try {
            retVal = Integer.parseInt((String) obj);
        } catch (NumberFormatException nfe) {
            retVal = null;
        }
        return retVal;
    }
}

