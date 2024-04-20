package com.example.educationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UpdateQuestion extends AppCompatActivity {
    Button updateButton;
    EditText updateSubtitle, updateQuestionList,updateCorrect,updateOptions,updateQuestion;
    String subtitle, questionList,options,correct,question;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_question);
        updateButton = findViewById(R.id.updateButton);
        updateSubtitle = findViewById(R.id.updateSubtitle);
        updateQuestionList = findViewById(R.id.updateQuestionList);
        updateCorrect = findViewById(R.id.updateCorrect);
        updateOptions = findViewById(R.id.updateOptions);
        updateQuestion = findViewById(R.id.updateQuestion);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            updateSubtitle.setText(bundle.getString("subtitle"));
            updateQuestionList.setText(bundle.getString("questionList"));
            key = bundle.getString("Key");

        }
        databaseReference = FirebaseDatabase.getInstance().getReference("questionList").child(key);
        databaseReference = FirebaseDatabase.getInstance().getReference("correct").child(key);
        databaseReference = FirebaseDatabase.getInstance().getReference("options").child(key);
        databaseReference = FirebaseDatabase.getInstance().getReference("question").child(key);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(UpdateQuestion.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void saveData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateQuestion.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void updateData(){
        subtitle = updateSubtitle.getText().toString().trim();
        questionList = updateQuestionList.getText().toString().trim();
        correct = updateCorrect.getText().toString().trim();
        options = updateOptions.getText().toString().trim();
        question = updateQuestion.getText().toString().trim();
        DataClass dataClass = new DataClass(subtitle, questionList,correct,options,question);
        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(UpdateQuestion.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateQuestion.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}