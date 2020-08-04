package com.project.crudfirebase;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.text.TextUtils.isEmpty;


/**
 * A simple {@link Fragment} subclass.
 */
public class AskFragment extends Fragment {
    EditText title, description, emailNotify;
    Button btnPost;
    int num = 0;
    FirebaseAuth auth;
    Spinner spinner;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    public AskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask, container, false);
        spinner = view.findViewById(R.id.questionCategory);
        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
        spinner.setAdapter(adapter);


        description = view.findViewById(R.id.questionDescription);
        btnPost = view.findViewById(R.id.btnPostNewQuestion);
        auth = FirebaseAuth.getInstance();

        retreiveData();
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUserID = auth.getCurrentUser().getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference getReference;
                final String getCategory = spinner.getSelectedItem().toString();
                final String getDescription = description.getText().toString();

                getReference = database.getReference();
                final String keyID = getReference.child("Question").push().getKey();
                if (isEmpty(getCategory) && isEmpty(getDescription)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Description field cannot be left empty!", Toast.LENGTH_SHORT).show();
                } else {
                    final String key = getReference.child("Question").child(getUserID).push().getKey();
                    getReference.child("Question").push()
                            .setValue(new Question(getCategory, getDescription, keyID, auth.getCurrentUser().getEmail(), String.valueOf(num)))
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    description.setText("");
                                    Toast.makeText(getActivity().getApplicationContext(), "Post Berhasil!", Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("nilai", key);
                                    bundle.putString("dataCategory", getCategory);
                                    bundle.putString("dataDescription", getDescription);
                                    bundle.putString("keyID",keyID);
                                    HomeFragment fragmentobj = new HomeFragment();
                                    fragmentobj.setArguments(bundle);
                                    ((MainFeed) getActivity()).ChangeToHome();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Post Gagal!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

    public void retreiveData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SpinnerData");
        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    spinnerDataList.add(item.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
