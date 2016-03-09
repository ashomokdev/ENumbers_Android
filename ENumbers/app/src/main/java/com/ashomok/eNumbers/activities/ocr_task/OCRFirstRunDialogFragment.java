package com.ashomok.eNumbers.activities.ocr_task;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashomok.eNumbers.R;

/**
 * Created by Iuliia on 26.02.2016.
 */
public class OCRFirstRunDialogFragment extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.requirement);
        View v = inflater.inflate(R.layout.ocr_first_run_dialog_layout, container);
        v.findViewById(R.id.btnOK).setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
}
