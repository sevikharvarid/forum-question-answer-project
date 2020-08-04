package com.project.crudfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private ArrayList<Question> listQuestion;
    private Context context;
    int jumlahJawaban;


    public RecyclerViewAdapter(ArrayList<Question> listQuestion, Context context) {
        this.listQuestion = listQuestion;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvEmail, tvIsi, tvViewsCount;
        private CardView cvListItem;


        ViewHolder(View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.question_author);
            tvIsi = itemView.findViewById(R.id.question_title);
            cvListItem = itemView.findViewById(R.id.card_view);
            tvViewsCount = itemView.findViewById(R.id.viewsValue);

            tvEmail.setVisibility(View.GONE);
            cvListItem.setVisibility(View.GONE);
            tvIsi.setVisibility(View.GONE);
            tvViewsCount.setVisibility(View.GONE);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Question question = listQuestion.get(position);
        final String Email = question.getAuthor();
        final String Isi = question.getDescription();
        final String valueID = question.getId();
        final String viewsCount = question.getViews();





        holder.tvEmail.setText(Email);
        holder.tvIsi.setText(Isi);
        holder.tvViewsCount.setText(viewsCount);

        holder.cvListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dataCategory", question.getCategory());
                bundle.putString("dataAuthor", question.getAuthor());
                bundle.putString("dataDescription", question.getDescription());
                bundle.putString("question_id", question.getId());
                Intent intent = new Intent(view.getContext(), ViewQuestion.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                final DatabaseReference pushViews = FirebaseDatabase.getInstance().getReference();
                pushViews.child("Question").orderByChild("id").equalTo(question.getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>) dataSnapshot.getValue();
                                String key = map.keySet().toString();
                                String key1 = key.substring(1, key.length() - 1);
                                Log.e("Key", key1.toString());
                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                    int viewCount = Integer.parseInt(item.child("views").getValue().toString());
                                    pushViews.child("Question").child(key1).child("views").setValue(String.valueOf(viewCount + 1));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listQuestion.size();
    }


}