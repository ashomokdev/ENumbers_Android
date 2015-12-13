package com.ashomok.eNumbers.activities.ocr_task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.sql.EN;

/**
 * Created by Iuliia on 13.12.2015.
 */
public class OCRAnimationActivity extends AppCompatActivity {

    private Button cancel;
    private ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_animation_layout);

        cancel = (Button)findViewById(R.id.cancel_btn);
        image = (ImageView) findViewById(R.id.image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            byte[] bytes = getIntent().getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            image.setImageBitmap(bitmap);
        }
        else
        {
            try {
                throw new Exception("thumbnail was not obtained.");
            } catch (Exception e) {
                Log.e(this.getClass().getCanonicalName(), e.getMessage());
                e.printStackTrace();
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

}
