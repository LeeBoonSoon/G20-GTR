package com.example.educationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.educationapplication.QuizModel;
import com.example.educationapplication.QuestionModel;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateQuestionUpload extends AppCompatActivity {



    DatabaseReference databaseRef;
    String Title, Subtitle, Time, ID, ValueA, ValueB, ValueC, ValueD, CorrectAnswer,Question;

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

                title.setText("");
                AnswerA.setText("");
                AnswerB.setText("");
                AnswerC.setText("");
                AnswerD.setText("");
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

                //Database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("ASS").document("NxgdVKplB0xMUczI9iYB");

                //Get question number from firestore
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Long questionNumberLong = documentSnapshot.getLong("QuestionNumber");
                            if (questionNumberLong != null) {
                                int questionNumber = questionNumberLong.intValue();

                                //Update to real time
                                DatabaseReference quzRef = FirebaseDatabase.getInstance().getReference(String.valueOf(questionNumber));
                                quzRef.setValue(quz);

                                // Update the document with the new QuestionNumber
                                int updatedQuestionNumber = questionNumber + 1;
                                Map<String, Object> data = new HashMap<>();
                                data.put("QuestionNumber", updatedQuestionNumber);
                                docRef.set(data, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Firestore", "Document updated successfully!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Firestore", "Error updating document", e);
                                            }
                                        });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting document", e);
                    }
                });
                Intent intent = new Intent(CreateQuestionUpload.this,MainActivity.class);
                startActivity(intent);
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
                options[0] = AnswerA.getText().toString();
                options[1] = AnswerB.getText().toString();
                options[2] = AnswerC.getText().toString();
                options[3] = AnswerD.getText().toString();
            }
        });

    }
}


