package com.example.ENumbers;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.*;
import android.text.style.TextAppearanceSpan;
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
        searchBtn = (Button) findViewById(R.id.button);
        outputEditText = (TextView) findViewById(R.id.outputE);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v != null) {
                    outputEditText.setText("");
                    ENumber result = null;
                    try {
                        result = GetInfoByENumber(inputEditText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (result == null) {
                        outputEditText.setText(getApplicationContext().getString(R.string.notFoundMessage));
                    } else {
                        GetFormated(result, outputEditText);

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

    public static void GetFormated(ENumber input, TextView textView) {
        Spannable eCode = new SpannableString(input.getCode() + "\n");
        eCode.setSpan( new TextAppearanceSpan(textView.getContext(), R.style.ECode), 0, eCode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(eCode);

        Spannable eName = new SpannableString(input.getName() + "\n");
        eName.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.EName), 0, eName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append((eName));

        if (input.getPurpose() != null) {
            Spannable ePurpose = new SpannableString(input.getPurpose() + "\n");
            ePurpose.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.EPurpose), 0, ePurpose.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(ePurpose);
        }

        if (input.getStatus() != null) {
            Spannable eStatus = new SpannableString(input.getStatus() + "\n");
            eStatus.setSpan(new TextAppearanceSpan(textView.getContext(), R.style.EStatus), 0, eStatus.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(eStatus);
        }
    }
}



//TODO
//2. design without search button
//4. New beautiful GU design //http://androidniceties.tumblr.com/
// https://www.google.com/design/spec/style/color.html#color-color-palette
//5. icons for different purposes
//6. add more info about ENumbers
// http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=OJ:L:2011:295:0178:0204:EN:PDF - difficult to recognize
// http://europa.eu/rapid/press-release_MEMO-11-783_en.htm?locale=en - FAQ, not E Numbers
// http://www.healthy-eating-politics.com/food-additives.html
// 1 priority  http://www.additivealert.com.au/search.php?start=0&end=10&count=298&process=next&flg=0
// http://www.fda.gov/Food/IngredientsPackagingLabeling/FoodAdditivesIngredients/ucm091048.htm

//7. Add languages support
//10. Keyboard with numbers only
//11. show few enumbers if they have deference in char at the end - for example E100a and E100b shod be shown to request E100
//13. Reaction on press button Ready
//14. Add inputting by voice http://developer.android.com/training/keyboard-input/style.html
//15. Add inputting OCR
//16. Add banned and approved fields


