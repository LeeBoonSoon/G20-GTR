package com.example.educationapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.educationapplication.QuizModel;
import com.example.educationapplication.QuestionModel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateQuestionUpload extends AppCompatActivity {

    DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_upload);
        FirebaseApp.initializeApp(this);

        Button uploadbutton = findViewById(R.id.add_submit_btn);
        EditText title = findViewById(R.id.add_question_textview);
        EditText text1 = findViewById(R.id.add_text1);
        EditText text2 = findViewById(R.id.add_text2);
        EditText text3 = findViewById(R.id.add_text3);
        EditText text4 = findViewById(R.id.add_text4);
        Spinner spinner =findViewById(R.id.addspinner);

        //sprineer
        String[] options = {"Select the correct answer", "", "", ""};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = options[position];
                //Change to the input Valid of user
                options[0] = "A";
                options[1] = "B";
                options[2] = "C";
                options[3] = "D";
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //*****************************************
        String a = "";
        String b = "";
        String c = "";
        String d = "";
        String e = "";

        //Database

        //Question
        QuestionModel questionModel = new QuestionModel(
                a, Arrays.asList(b,c,d,e),e
        );

        QuestionModel newquestionModel = new QuestionModel(
                a, Arrays.asList(b,c,d,e),e
        );

        //Add to list of question
        List<QuestionModel> questionList = new ArrayList<>();
        questionList.add(questionModel); // Add existing question
        questionList.add(newquestionModel); // Add new question

        QuizModel quz = new QuizModel(
                a,
                b,
                c,
                d,
                questionList
        );

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get number
                databaseRef = FirebaseDatabase.getInstance().getReference("5");
                //get id
                databaseRef.child("id");
                databaseRef.setValue(quz);
            }
        });
    }
}