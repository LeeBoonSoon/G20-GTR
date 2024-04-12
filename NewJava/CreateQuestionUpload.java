package com.example.educationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.educationapplication.QuizModel;
import com.example.educationapplication.QuestionModel;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateQuestionUpload extends AppCompatActivity {



    DatabaseReference databaseRef;
    String Title, Subtitle, Time, ID, ValueA, ValueB, ValueC, ValueD, CorrectAnswer,Question;
    Integer questionnumber = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_upload);
        FirebaseApp.initializeApp(this);

        SharedPreferences sharedPreferences = getSharedPreferences("Globaldatastore", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Button uploadbutton = findViewById(R.id.add_submit_btn);
        Button clearbutton = findViewById(R.id.add_next_btn);
        //getText.toString()
        EditText title = findViewById(R.id.add_question_textview);
        EditText AnswerA = findViewById(R.id.add_answerA);
        EditText AnswerB = findViewById(R.id.add_answerB);
        EditText AnswerC = findViewById(R.id.add_answerC);
        EditText AnswerD = findViewById(R.id.add_answerD);
        Spinner spinner = findViewById(R.id.addspinner);

        title.setText("Input the Question");
        AnswerA.setText("AnswerA");
        AnswerB.setText("AnswerB");
        AnswerC.setText("AnswerC");
        AnswerD.setText("AnswerD");

        //*****************************************
        List<QuestionModel> questionList = new ArrayList<>();

        Title = sharedPreferences.getString("Title", "Null");
        Subtitle = sharedPreferences.getString("Subtitle", "Null");
        Time = sharedPreferences.getString("Time", "Null");
        ID = sharedPreferences.getString("ID", "Null");

        //To avoid update crashing ;
        Question = "";
        ValueA = "";
        ValueB = "";
        ValueC = "";
        ValueD = "";
        CorrectAnswer = "";

        //Input Array
        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Question = title.getText().toString();
                ValueA = AnswerA.getText().toString();
                ValueB = AnswerB.getText().toString();
                ValueC = AnswerC.getText().toString();
                ValueD = AnswerD.getText().toString();

                //Question
                QuestionModel questionModel = new QuestionModel(
                        Question, Arrays.asList(ValueA, ValueB, ValueC, ValueD), CorrectAnswer
                );
                //Add to list of question
                questionList.add(questionModel); // Add existing question

                Question = "";
                ValueA = "";
                ValueB = "";
                ValueC = "";
                ValueD = "";
                CorrectAnswer = "";

                title.setText("Input the Question");
                AnswerA.setText("AnswerA");
                AnswerB.setText("AnswerB");
                AnswerC.setText("AnswerC");
                AnswerD.setText("AnswerD");
            }
        });

        //Upload database
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Question = title.getText().toString();
                ValueA = AnswerA.getText().toString();
                ValueB = AnswerB.getText().toString();
                ValueC = AnswerC.getText().toString();
                ValueD = AnswerD.getText().toString();

                //Question
                QuestionModel questionModel = new QuestionModel(
                        Question, Arrays.asList(ValueA, ValueB, ValueC, ValueD), CorrectAnswer
                );
                //Add to list of question
                questionList.add(questionModel); // Add existing question

                QuizModel quz = new QuizModel(
                        ID,
                        Title,
                        Subtitle,
                        Time,
                        questionList
                );

                //GET THE NUMBER OF QUESTION
                databaseRef = FirebaseDatabase.getInstance().getReference("QuestionNumber");
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer questionNumber = snapshot.getValue(Integer.class);
                        if (questionNumber != null) {
                            questionNumber++; // Increment the retrieved value
                            Log.e("FirebaseExample", "Number: " + questionNumber);

                            // Update the database with the incremented value
                            DatabaseReference updatedRef = FirebaseDatabase.getInstance().getReference("QuestionNumber");
                            updatedRef.setValue(questionNumber);

                            // Assuming quz is the data you want to store
                            DatabaseReference quzRef = FirebaseDatabase.getInstance().getReference(questionNumber.toString());
                            quzRef.setValue(quz);
                        } else {
                            Log.e("FirebaseExample", "QuestionNumber value is null.");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseExample", "Error: " + error.getMessage());
                    }
                });
            }
        });

        //sprineer
        String[] options = {"Select the correct answer", "A", "B", "C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = options[position];
                //Change to the input Valid of user
                options[0] = AnswerA.getText().toString();
                options[1] = AnswerB.getText().toString();
                options[2] = AnswerC.getText().toString();
                options[3] = AnswerD.getText().toString();

                if (position == 0)
                    CorrectAnswer =  AnswerA.getText().toString();
                else  if (position == 1)
                    CorrectAnswer =  AnswerB.getText().toString();
                else if (position == 2)
                    CorrectAnswer =  AnswerC.getText().toString();
                else if (position == 3)
                    CorrectAnswer =  AnswerD.getText().toString();
                else
                    CorrectAnswer = "";
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}