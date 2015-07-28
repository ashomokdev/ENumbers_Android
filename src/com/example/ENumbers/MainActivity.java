package com.example.ENumbers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.*;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity implements IGetInfoByENumber {
    Button searchBtn;
    ListView listViewResult;
    EditText inputEditText;
    TextView outputWarning;
    ListView listView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        inputEditText = (EditText) findViewById(R.id.inputE);
        listView = (ListView) findViewById(R.id.ENumberList);
        searchBtn = (Button) findViewById(R.id.button);
        outputWarning = (TextView) findViewById(R.id.warning);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    outputWarning.setText("");
                    listView.setAdapter(null);
                    ENumber result = null;
                    try {
                        result = GetInfoByENumber(inputEditText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (result == null) {
                        outputWarning.setText(getApplicationContext().getString(R.string.notFoundMessage));
                    } else {
                        listViewResult = (ListView) findViewById(R.id.ENumberList);
                        ArrayList<ENumber> data = new ArrayList<ENumber>();
                        //TODO add all result
                        data.add(result);
                        listView.setAdapter(new ENumberListAdapter(v.getContext(), data));

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
    }

    @Override
    public ENumber GetInfoByENumber(String ENumber_input) throws Exception {
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
            //TODO
        }
        return null;
    }

//    public static void GetFormated(ENumber input, TextView textView) {
//        Spannable eCode = new SpannableString(input.getCode() + "\n");
//        eCode.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.ECode), 0, eCode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.append(eCode);
//
//        Spannable eName = new SpannableString(input.getName() + "\n");
//        eName.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.EName), 0, eName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.append((eName));
//
//        if (input.getPurpose() != null) {
//            Spannable ePurpose = new SpannableString(input.getPurpose() + "\n");
//            ePurpose.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.EPurpose), 0, ePurpose.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            textView.append(ePurpose);
//        }
//
//        if (input.getStatus() != null) {
//            Spannable eStatus = new SpannableString(input.getStatus() + "\n");
//            eStatus.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.EStatus), 0, eStatus.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            textView.append(eStatus);
//        }
//    }
}



