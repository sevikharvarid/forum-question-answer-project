package com.project.crudfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewQuestion extends AppCompatActivity {
    TextView title, description, category, tools;
    EditText replyText;
    Button replyButton, changeReplyButton;
    int editingAnswerID, editingQuestionID;
    private Answer editingAnswer;
    private Toolbar toolbar;
    private String tool;
    private FirebaseUser currentUser;
    private DatabaseReference getRef;
    private FirebaseListAdapter<Answer> firebaseListAdapter;
    Dialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_question);
        toolbar = findViewById(R.id.commToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myDialog = new Dialog(this);

        Intent intent = getIntent();
        final int id = intent.getExtras().getInt("question_id");
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final ListView answerList = findViewById(R.id.answerList);
        getData();
        final String getID = getIntent().getExtras().getString("question_id");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database = FirebaseDatabase.getInstance().getReference();
        getRef = database.getReference().child("Jawaban").child(getID);
        firebaseListAdapter = new FirebaseListAdapter<Answer>(
                ViewQuestion.this,
                Answer.class,
                R.layout.answer_layout,
                getRef
        ) {
            @Override
            protected void populateView(View v, final Answer model, final int position) {

                TextView answer = v.findViewById(R.id.txtAnswer);
                TextView author = v.findViewById(R.id.txtAnswerBy);
                answer.setText(model.getDescription());
                author.setText("by " + model.getAuthor());
                String emailText = model.getAuthor();

                answerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        final DatabaseReference reference = firebaseListAdapter.getRef(i);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                final TextView nip, email, nama;
                                final CircleImageView circleImageView;
                                myDialog.setContentView(R.layout.detail_admin);
                                nip = myDialog.findViewById(R.id.nipAdmin);
                                email = myDialog.findViewById(R.id.emailAdmin);
                                nama = myDialog.findViewById(R.id.nameAdmin);
                                circleImageView = myDialog.findViewById(R.id.profileImage);
                                email.setText(dataSnapshot.child("author").getValue().toString());
                                final DatabaseReference data = FirebaseDatabase.getInstance()
                                        .getReference("Admin")
                                        .child(dataSnapshot.child("key").getValue().toString());
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        nip.setText(dataSnapshot.child("nip").getValue().toString());
                                        nama.setText(dataSnapshot.child("name").getValue().toString());
                                        String message = dataSnapshot.child("imageUrl").getValue().toString();
                                        Glide.with(getApplicationContext())
                                                .asBitmap()
                                                .load(message)
                                                .into(new CustomTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                        circleImageView.setImageBitmap(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                data.addListenerForSingleValueEvent(valueEventListener);
                                myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                myDialog.show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });

            }
        };
        answerList.setAdapter(firebaseListAdapter);
    }

    private void getData() {
        final String getCategory = getIntent().getExtras().getString("dataCategory");
        final String getTitle = getIntent().getExtras().getString("dataAuthor");
        final String getDesc = getIntent().getExtras().getString("dataDescription");
        title.setText(getTitle);
        category.setText(getCategory);
        description.setText(getDesc);
    }


}
