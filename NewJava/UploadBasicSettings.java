package com.example.educationapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UploadBasicSettings extends AppCompatActivity {

    //store into local to uisng other java call



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_basic_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("Globaldatastore", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText addtitle = findViewById(R.id.add_edit_title);
        EditText addsubtitile = findViewById(R.id.add_edit_subtitle);
        EditText addtime = findViewById(R.id.add_edit_time);
        EditText addid = findViewById(R.id.add_edit_id);
        Button createexam = findViewById(R.id.editsubmitbotton);

        createexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Title",addtitle.getText().toString());
                editor.putString("Subtitle",addsubtitile.getText().toString());
                editor.putString("ID",addid.getText().toString());
                editor.putString("Time",addtime.getText().toString());
                editor.apply();

                Intent intent = new Intent(UploadBasicSettings.this,CreateQuestionUpload.class);
                startActivity(intent);
            }
        });

    }
}