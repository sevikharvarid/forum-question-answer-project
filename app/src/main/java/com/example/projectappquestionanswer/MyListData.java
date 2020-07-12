package com.example.projectappquestionanswer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyListData extends Fragment implements RecyclerViewAdapter.dataListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<Question> dataQuestion;
    private Button searchBtn;
    private EditText searchEdt;

    private FirebaseAuth auth;

    public static MyListData newInstance() {
        return new MyListData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_list_data, container, false);


        recyclerView = view.findViewById(R.id.datalist);
        auth = FirebaseAuth.getInstance();
        searchBtn = view.findViewById(R.id.search_button);
        searchEdt = view.findViewById(R.id.search_text);
        final String categoryFilter = getArguments().getString("position");
        MyRecyclerView();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence search = searchEdt.getText().toString();
                GetDataBySearch(search.toString(), categoryFilter);
            }

        });
        GetData(categoryFilter);

        return view;
    }

    private void GetDataBySearch(final String search, final CharSequence categoryFilter) {
        reference = FirebaseDatabase.getInstance().getReference();
        String key = reference.push().getKey();
        reference.child("Question")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataQuestion = new ArrayList<>();
                        Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>) dataSnapshot.getValue();
                        if (map != null) {
                            for (String key : map.keySet()) {
                                Map<String, ArrayList<String>> map2 = (Map<String, ArrayList<String>>) map.get(key);
                                for (String key2 : map2.keySet()) {
                                    Map<String, ArrayList<Question>> map3 = (Map<String, ArrayList<Question>>) map2.get(key2);

                                    Gson gson = new Gson();
                                    String test = gson.toJson(map3);
                                    Question question = new Question();
                                    question = gson.fromJson(test, Question.class);
                                    if (question.getDescription().contains(search)) {
                                        if (question.getCategory().equals(categoryFilter)) {
                                            dataQuestion.add(question);
                                            Log.e("Nilai Field : ", dataQuestion.toString());
                                        }
                                    }

                                }
                            }
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataQuestion, getContext());

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });

    }

    private void GetData(final String categoryFilter) {
        reference = FirebaseDatabase.getInstance().getReference();
        String key = reference.push().getKey();
        reference.child("Question")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataQuestion = new ArrayList<>();
                        Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>) dataSnapshot.getValue();
                        if (map != null) {
                            for (String key : map.keySet()) {
                                Map<String, ArrayList<String>> map2 = (Map<String, ArrayList<String>>) map.get(key);
                                for (String key2 : map2.keySet()) {
                                    Map<String, ArrayList<Question>> map3 = (Map<String, ArrayList<Question>>) map2.get(key2);

                                    Gson gson = new Gson();
                                    String test = gson.toJson(map3);
                                    Question question = new Question();
                                    question = gson.fromJson(test, Question.class);
                                    if (question.getDescription().equals("tes")) {
                                        if (question.getCategory().equals(categoryFilter)) {
                                            dataQuestion.add(question);
                                            Log.e("Nilai Field : ", dataQuestion.toString());
                                        }
                                    } else {
                                        if (question.getCategory().equals(categoryFilter)) {
                                            dataQuestion.add(question);
                                            Log.e("Nilai Field : ", dataQuestion.toString());
                                        }
                                    }

                                }
                            }
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataQuestion, getContext());

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });
    }

    private void MyRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 0));

    }


    @Override
    public void onDeleteData(Question data, int position) {
    /*    String userID = auth.getUid();
        if (reference != null) {
            reference.child("Admin")
                    .child(userID)
                    .child("Mahasiswa")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MyListData.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

     */
    }


}