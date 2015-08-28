package com.example.eNumbers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IGetterInfoByENumber {

    private static final int SPEECH_REQUEST_CODE = 0;

    private Button searchBtn;

    private ImageButton voiceInputBtn;

    private ListView listViewResult;

    private EditText inputEditText;

    private TextView outputWarning;

    private ListView listView;

    private Intent intent;

    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

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

//        mTitle = mDrawerTitle = getTitle();
//        mScreenTitles = getResources().getStringArray(R.array.screen_array);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//
//        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, mScreenTitles));
//        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                mDrawerList.setItemChecked(i, true);
//                setTitle("......");
//
//                String text= "menu click... should be implemented";
//                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
//                mDrawerLayout.closeDrawer(mDrawerList);
//            }
//        });
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this, /* host Activity */
//                mDrawerLayout, /* DrawerLayout object */
//                R.drawable.ic_menu_black_24dp, /* nav drawer icon to replace 'Up' caret */
//                R.string.drawer_open, /* "open drawer" description */
//                R.string.drawer_close /* "close drawer" description */
//        ) {
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                getSupportActionBar().setTitle(mTitle);
//                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                getSupportActionBar().setTitle(mDrawerTitle);
//                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
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


            inputEditText.setText(
                    getApplicationContext().getString(R.string.startChar));
            String spokenText = results.get(0);
            inputEditText.append(spokenText);

            searchBtn.setPressed(true);
            searchBtn.invalidate();
            searchBtn.performClick();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //TODO
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public ENumber GetInfoByENumber(String eNumber_input) throws Exception {

        if (inputingIsValid(eNumber_input)) {
            InputStream inputStream = this.getApplicationContext().getResources().openRawResource(R.raw.base);

            try {
                Serializer serializer = new Persister();
                ENumbersCollection eNumbersCollection = serializer.read(ENumbersCollection.class, inputStream);

                for (ENumber eNumber : eNumbersCollection) {

                    if (eNumber.getCode().equals(eNumber_input)) {

                        return eNumber;
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); //error here
            }
        }
        return null;
    }

    private boolean inputingIsValid(String ENumber_input) {
        if (ENumber_input.startsWith(getApplicationContext().getString(R.string.startChar))) {

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



