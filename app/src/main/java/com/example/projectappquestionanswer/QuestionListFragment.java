package com.example.projectappquestionanswer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class QuestionListFragment extends Fragment {

    private ListView mainListView;
    private TextView coba;
    private FirebaseAuth auth;
    private String desc, tilt, cat;
    private HomeFragment homeFragment;
    private int page;
    private String title;
    private ArrayList<String> tes;
    private ArrayList<Question> questionlist;


    public static QuestionListFragment newInstance() {
        return new QuestionListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_question_list_fragment, container, false);

        mainListView = view.findViewById(R.id.question_listview);
        final String searchText = getArguments().getString("position");
        auth = FirebaseAuth.getInstance();
        //coba = view.findViewById(R.id.coba);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Question");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String test = item.getKey();
                // initViews(test,searchText);
                Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>) dataSnapshot.getValue();
                if (map != null) {
                    for (String key : map.keySet()) {
                        Map<String, ArrayList<String>> map2 = (Map<String, ArrayList<String>>) map.get(key);
                        for (String key2 : map2.keySet()) {
                            Map<String, ArrayList<String>> map3 = (Map<String, ArrayList<String>>) map2.get(key2);

                            Gson gson = new Gson();
                            String dataTest = auth.getUid();
                            String data = gson.toJson(map3);
                            tes = new ArrayList<>();
                            questionlist = new ArrayList<>();
                            Question question = new Question();
                            question = gson.fromJson(data,Question.class);
                            questionlist.add(question);
                            if(getActivity()!=null){
                                ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.question_layout,
                                        R.id.question_title ,questionlist);
                                mainListView.setAdapter(adapter);
                                Log.e("questionList : ",questionlist.toString());
                            }

             /*
                            //tes.add("LHg7w4WywLf3apg0bPvIXMufiyD2");
                            //tes.add("mPmNweW9LJZuU4YCmuBZv1sJmdB2");
                            String keyData = key2.substring(1, key2.length());
                            String kk = EncodeString(keyData);
                            tes.add(kk);
                            Log.e("Nilai : ", tes.toString());

                            String testing = "MBgm4IWiFCvy3a5DrDJ";
                            */
                        }
                    }
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }

    public static String EncodeString(String string) {
        return string.replace("[", "");
    }

    private void initViews(String test, String searchText) {
        auth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("Question");
        final DatabaseReference db = dbRef.child(test);
        Query firebaseSearchView = db.orderByChild("category").startAt(searchText).endAt(searchText + "\uf8ff");
        final FirebaseListAdapter<Question> firebaseListAdapter = new FirebaseListAdapter<Question>(
                this.getActivity(),
                Question.class,
                R.layout.question_layout,
                firebaseSearchView
        ) {
            @Override
            protected void populateView(final View v, final Question model, int position) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                TextView questionTitle = v.findViewById(R.id.question_title);
                TextView questionAuthor = v.findViewById(R.id.question_author);

                questionTitle.setText(model.getDescription());
//                questionAuthor.setText("by " + currentUser.getEmail());
                questionAuthor.setText("by " + model.getAuthor());

                desc = model.getDescription();
                tilt = model.getTitle();
                cat = model.getCategory();

            }
        };
        mainListView.setAdapter(firebaseListAdapter);


        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("dataCategory", cat);
                bundle.putString("dataTitle", tilt);
                bundle.putString("dataDescription", desc);
                Intent intent = new Intent(view.getContext(), ViewQuestion.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
