package com.ashomok.eNumbers.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.capture_image.CaptureImageActivity;
import com.ashomok.eNumbers.activities.ocr_task.OCRAnimationActivity;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTaskRESTClient;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTaskStandalone;
import com.ashomok.eNumbers.data_load.EN;
import com.ashomok.eNumbers.data_load.ENCursorLoader;
import com.ashomok.eNumbers.data_load.ENumbersSQLiteAssetHelper;
import com.ashomok.eNumbers.keyboard.CustomKeyboardListener;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * Created by Iuliia on 29.08.2015.
 */
public class MainFragment extends Fragment implements TaskDelegate, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int CaptureImage_REQUEST_CODE = 1;
    private static final int OCRAnimationActivity_REQUEST_CODE = 2;

    private String img_path;
    private Uri outputFileUri;
    private ImageButton voiceInputBtn;
    private ImageButton closeBtn;
    private EditText inputEditText;
    private static String startChar;
    private ENumbersSQLiteAssetHelper db;
    private ENumberListAdapter scAdapter;
    private ListView listView;
    private TextView outputWarning;
    private FloatingActionButton fab;
    private RecognizeImageAsyncTask recognizeImageAsyncTask;
    private AudioManager audioManager;
    private Context context;

    private static final String TAG = "MainFragment";
    private KeyboardView keyboardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);

            startChar = getString(R.string.startChar);

            inputEditText = (EditText) view.findViewById(R.id.inputE);

            //hide default keyboard
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                inputEditText.setShowSoftInputOnFocus(false);
            } else {
                try {
                    final Method method = EditText.class.getMethod(
                            "setShowSoftInputOnFocus"
                            , boolean.class);
                    method.setAccessible(true);
                    method.invoke(inputEditText, false);
                } catch (Exception e) {
                    // ignore
                }
            }

            inputEditText.setSelection(inputEditText.getText().length()); //starts type after "E"
            inputEditText.addTextChangedListener(new StartCharKeeper());
            inputEditText.setOnEditorActionListener(new BtnDoneHandler());

            fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new FabClickHandler());

            voiceInputBtn = (ImageButton) view.findViewById(R.id.ic_mic);
            voiceInputBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    displaySpeechRecognizer();
                }
            });

            closeBtn = (ImageButton) view.findViewById(R.id.ic_close);
            closeBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    inputEditText.setText("");

                    showAllData();
                }
            });

            db = new ENumbersSQLiteAssetHelper(view.getContext());

            listView = (ListView) view.findViewById(R.id.ENumberList);
            outputWarning = (TextView) view.findViewById(R.id.warning);
            listView.setEmptyView(outputWarning);
            listView.setAdapter(scAdapter);

            createCustomKeyboard(view);

            context = view.getContext();

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // create Loader for data reading
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        inputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hideCustomKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        //voice inputting
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            onVoiceInputResult(data);
        }

        //making photo
        if (requestCode == CaptureImage_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                img_path = outputFileUri.getPath();
            } else {
                img_path = data.getStringExtra("file");
            }
            startOCRtask(img_path);
        }

        //ocr canceled
        if (requestCode == OCRAnimationActivity_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            recognizeImageAsyncTask.cancel(true);
        }
    }

    private void GetInfoFromInputting(String input) {

        OCREngine parser = new OCREngineImpl();
        Set<String> enumbers = parser.parseResult(input);

        GetInfoByENumbers(enumbers.toArray(new String[enumbers.size()]));
    }

    private void GetInfoByENumbers(String[] enumbers) {
        Bundle b = new Bundle();
        b.putStringArray("codes", enumbers);
        try {

            getLoaderManager().restartLoader(0, b, this);

        } catch (Exception e) {

            Log.e(this.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    private void startOCRtask(String img_path) {


        //run animation
        Intent intent = new Intent(context, OCRAnimationActivity.class);
        intent.putExtra("image", img_path);
        startActivityForResult(intent, OCRAnimationActivity_REQUEST_CODE);

        //start ocr
        if (isNetworkAvailable(context)) {
            recognizeImageAsyncTask = new RecognizeImageAsyncTaskRESTClient(img_path, this);

        } else {
            recognizeImageAsyncTask = new RecognizeImageAsyncTaskStandalone(context, img_path, this);
        }
        recognizeImageAsyncTask.execute();

    }

    private void onVoiceInputResult(Intent data) {
        List<String> results = data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS);


        inputEditText.setText(
                context.getApplicationContext().getString(R.string.startChar));

        String spokenText = results.get(0);
        inputEditText.append(spokenText);

        GetInfoFromInputting(inputEditText.getText().toString());
    }


    private void displaySpeechRecognizer() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Start the activity, the intent will be populated with the speech text
        try {
            getActivity().startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(context, "No speech recognition available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }


    private void showAllData() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public void TaskCompletionResult(String[] result) {
        getActivity().finishActivity(OCRAnimationActivity_REQUEST_CODE);
        GetInfoByENumbers(result);
    }

    private class FabClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                startBuildInCameraActivity(v);
            } else {

                Intent intent = new Intent(v.getContext(), CaptureImageActivity.class);
                startActivityForResult(intent, CaptureImage_REQUEST_CODE);

            }
        }
    }

    private class BtnDoneHandler implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                GetInfoFromInputting(textView.getText().toString());

                //to hide the soft keyboard
                InputMethodManager imm = (InputMethodManager) context.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                return true;
            }

            return false;
        }
    }

    private class StartCharKeeper implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            float vol = 0.3f; //This will be half of the default system sound
            audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (!charSequence.toString().startsWith(startChar)) {

                inputEditText.setText(startChar);
            }
            inputEditText.setSelection(inputEditText.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    private void createCustomKeyboard(View view) {
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboard);
        Keyboard customKeyboard = new Keyboard(view.getContext(), R.xml.keyboard_keys);
        keyboardView.setKeyboard(customKeyboard);
        CustomKeyboardListener numbersListener = new CustomKeyboardListener(inputEditText);

        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }

        numbersListener.setSubmitListener(new CustomKeyboardListener.SubmitListener() {
            @Override
            public void onSubmit() {
                GetInfoFromInputting(inputEditText.getText().toString());
                hideCustomKeyboard();
            }
        });
        keyboardView.setOnKeyboardActionListener(numbersListener);

        inputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showCustomKeyboard();
                return false;
            }
        });

        inputEditText.setOnFocusChangeListener(focusChangeListener);

        audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    private void hideCustomKeyboard() {
        keyboardView.setVisibility(View.GONE);
    }

    private void showCustomKeyboard() {
        keyboardView.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).commit();
    }

    private void hideDefaultKeyboard(View v) {

        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                android.content.Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keyboardView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        showCustomKeyboard();
    }

    /**
     * to get high resolution image from camera
     * @param v
     */
    private void startBuildInCameraActivity(View v) {
        try {
            String IMGS_PATH = Environment.getExternalStorageDirectory().toString() + "/ENumbers/imgs";
            prepareDirectory(IMGS_PATH);

            img_path = IMGS_PATH + "/ocr.jpg";

            File file = new File(img_path);
            outputFileUri = Uri.fromFile(file);

            final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CaptureImage_REQUEST_CODE);
            }
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    private void prepareDirectory(String path) throws Exception {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v(TAG, "ERROR: Creation of directory " + path
                        + " on sdcard failed");
                throw new Exception(
                        "Could not create folder" + path);
            }
        } else {
            Log.v(TAG, "Created directory " + path + " on sdcard");
        }
    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                hideDefaultKeyboard(v);
            } else {
                hideCustomKeyboard();
            }
        }
    };


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Prepare the loader
        return new ENCursorLoader(context, db, bundle);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        try {
            scAdapter = new ENumberListAdapter(context, cursor, 0);

            listView.setAdapter(scAdapter);
            scAdapter.changeCursor(cursor);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                    Cursor cursor = (Cursor) parent.getAdapter().getItem(position);

                    EN enumb = new EN(cursor);

                    Intent intent = new Intent(context, ENDetailsActivity.class);

                    intent.putExtra("en", enumb);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.swapCursor(null);
    }

}