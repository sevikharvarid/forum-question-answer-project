package com.example.projectappquestionanswer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewQuestion extends AppCompatActivity {

    TextView title, description, category,tools;
    EditText replyText;
    Button replyButton, changeReplyButton;
    int editingAnswerID,editingQuestionID;
    private Answer editingAnswer;
    private Toolbar toolbar;
    private String tool;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_question);
        toolbar = findViewById(R.id.commToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        final int id = intent.getExtras().getInt("question_id");
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ListView answerList= findViewById(R.id.answerList);
        getData();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

 /*       DatabaseReference dbRef = database.getReference("answers").child(String.valueOf(id)).getRef();
        FirebaseListAdapter<Answer> firebaseListAdapter = new FirebaseListAdapter<Answer>(
                ViewQuestion.this,
                Answer.class,
                R.layout.answer_layout,
                dbRef
        ) {
            @Override
            protected void populateView(View v, final Answer model, int position) {

                TextView answer = v.findViewById(R.id.txtAnswer);
                TextView author = v.findViewById(R.id.txtAnswerBy);
                ImageButton btnDelete = v.findViewById(R.id.btnDelete);
                ImageButton btnEdit = v.findViewById(R.id.btnEdit);
                //;
                answer.setText(model.getDescription());
                author.setText("by "+model.getAuthor());

                if( model.getAuthor().equalsIgnoreCase(currentUser.getEmail().toString()) ){
                    btnDelete.setVisibility(View.VISIBLE);
                    btnEdit.setVisibility(View.VISIBLE);

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference dbRef = database.getReference("answers");
                            dbRef.child(String.valueOf(id)).child(String.valueOf(model.getId())).removeValue();
                        }
                    });

                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference dbRef = database.getReference("answers").child(String.valueOf(id)).child(String.valueOf(model.getId())).getRef();
                            editingAnswerID=model.getId();
                            editingQuestionID=id;
                            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    editingAnswer=dataSnapshot.getValue(Answer.class);
                                    replyText.setText(dataSnapshot.child("description").getValue().toString());
                                    changeReplyButton.setVisibility(View.VISIBLE);
                                    replyButton.setVisibility(View.INVISIBLE);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                }
                else {
                    btnDelete.setVisibility(View.INVISIBLE);
                    btnEdit.setVisibility(View.INVISIBLE);
                }
            }
        };

        answerList.setAdapter(firebaseListAdapter);
*/

    }
    private void getData(){
        //Menampilkan data dari item yang dipilih sebelumnya
        final String getCategory = getIntent().getExtras().getString("dataCategory");
        final String getTitle = getIntent().getExtras().getString("dataAuthor");
        final String getDesc = getIntent().getExtras().getString("dataDescription");
        title.setText(getTitle);
        category.setText(getCategory);
        description.setText(getDesc);
    }

}
