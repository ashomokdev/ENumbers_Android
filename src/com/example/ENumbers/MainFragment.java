package com.example.eNumbers;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Iuliia on 29.08.2015.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SPEECH_REQUEST_CODE = 0;

    private Button searchBtn;

    private ImageButton voiceInputBtn;

    private ImageButton closeBtn;

    private EditText inputEditText;

    private TextView outputWarning;

    private ListView listView;

    private String startChar;

    private ENumbersSQLiteAssetHelper db;

    SimpleCursorAdapter scAdapter;

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
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Prepare the loader
            db = new ENumbersSQLiteAssetHelper(getActivity());

            // create Loader for data reading
            getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            startChar = getString(R.string.startChar);
            inputEditText = (EditText) view.findViewById(R.id.inputE);
            inputEditText.setSelection(inputEditText.getText().length()); //starts type after "E"

            outputWarning = (TextView) view.findViewById(R.id.warning);

            searchBtn = (Button) view.findViewById(R.id.button);

            voiceInputBtn = (ImageButton) view.findViewById(R.id.ic_mic);

            closeBtn = (ImageButton) view.findViewById(R.id.ic_close);

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (v != null) {

                        outputWarning.setText("");

                        String inputing = inputEditText.getText().toString();

                        if (inputing.length() >= 3) {

                            GetInfoByENumber(inputing);

//
//                                //to hide the soft keyboard
//                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
//                                        Context.INPUT_METHOD_SERVICE);
//                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
                        } else if (inputing.contentEquals(startChar)) {
                            //empty enter
                            showAllData(v);
                        } else {
                            listView.setAdapter(null);
                            outputWarning.setText(getActivity().getApplicationContext().getString(R.string.notFoundMessage));
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

                        searchBtn.performClick();

                        //to hide the soft keyboard
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

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

            closeBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    inputEditText.setText("");

                    outputWarning.setText("");

                    showAllData(view);
                }
            });

            String[] from = new String[]
                    {ENumbersSQLiteAssetHelper.COLUMN_NAME_CODE,
                            ENumbersSQLiteAssetHelper.COLUMN_NAME_NAME,
                            ENumbersSQLiteAssetHelper.COLUMN_NAME_PURPOSE,
                            ENumbersSQLiteAssetHelper.COLUMN_NAME_STATUS};
            int[] to = new int[]
                    {R.id.ECode,
                            R.id.EName,
                            R.id.EPurpose,
                            R.id.EStatus};

            scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_row_layout, null, from, to, 0);
            listView = (ListView) view.findViewById(R.id.ENumberList);
            listView.setAdapter(scAdapter);




        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void GetInfoByENumber(String inputing) {
        try {
            Bundle b = new Bundle();
            b.putStringArray("codes", new String[]{inputing});

            getLoaderManager().restartLoader(0, b, this);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void displaySpeechRecognizer() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    //deprecated
    public ENumber GetInfoByENumberFromXML(String eNumber_input) throws Exception {

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

    private void showAllData(View v) {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ENCursorLoader(getActivity(), db, bundle);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        try {
            if (cursor.getCount() < 1) {
                listView.setAdapter(null);
                outputWarning.setText(getActivity().getApplicationContext().getString(R.string.notFoundMessage));
            } else {
                listView.setAdapter(scAdapter);
                scAdapter.changeCursor(cursor);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                        //TODO
                        Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                        EN enumb = new EN(cursor);

                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        String flat = gson.toJson(enumb);

                        Intent intent = new Intent(getActivity(), ENDetailsActivity.class);

                        intent.putExtra("objectKey", flat);
                        startActivity(intent);
                    }
                });
            }
        }
        catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + e.getStackTrace().toString());
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.changeCursor(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

