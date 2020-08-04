package com.project.crudfirebase;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private ArrayList<Question> listQuestion;
    private ArrayList<String> listSpecificQuestion;
    private Context context;
    private FirebaseUser auth;
    int jumlahJawaban;


    public interface dataListener {
        void onDeleteData(Question data, int position);
    }

    dataListener listener;

    public RecyclerViewAdapter(ArrayList<Question> listQuestion, Context context) {
        this.listQuestion = listQuestion;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Email, Isi, Views;
        private CardView ListItem;


        ViewHolder(View itemView) {
            super(itemView);
            Email = itemView.findViewById(R.id.question_author);
            Isi = itemView.findViewById(R.id.question_title);
            ListItem = itemView.findViewById(R.id.card_view);
            Views = itemView.findViewById(R.id.viewsValue);

            Email.setVisibility(View.GONE);
            ListItem.setVisibility(View.GONE);
            Isi.setVisibility(View.GONE);
            Views.setVisibility(View.GONE);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String Email = listQuestion.get(position).getAuthor();
        final String Isi = listQuestion.get(position).getDescription();
        final String nilai = listQuestion.get(holder.getAdapterPosition()).toString();
        final String valueID = listQuestion.get(position).getId();
        final String Views = listQuestion.get(position).getViews();



        DatabaseReference countAnswer = FirebaseDatabase.getInstance().getReference().child("Jawaban");
        countAnswer.child(valueID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    jumlahJawaban = (int) dataSnapshot.getChildrenCount();
                    Log.e("Jumlah Jawaban : ", Integer.toString(jumlahJawaban));
                    holder.Email.setVisibility(View.VISIBLE);
                    holder.ListItem.setVisibility(View.VISIBLE);
                    holder.Isi.setVisibility(View.VISIBLE);
                    holder.Views.setVisibility(View.VISIBLE);
                } else {
                    //listQuestion.remove(getItemViewType(position));
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.getLayoutParams().height = 0;
                    holder.itemView.requestLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.Email.setText(Email);
        holder.Isi.setText(Isi);
        holder.Views.setText(Views);

        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dataCategory", listQuestion.get(position).getCategory());
                bundle.putString("dataAuthor", listQuestion.get(position).getAuthor());
                bundle.putString("dataDescription", listQuestion.get(position).getDescription());
                bundle.putString("question_id", listQuestion.get(position).getId());
                Intent intent = new Intent(view.getContext(), ViewQuestion.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                final DatabaseReference pushViews = FirebaseDatabase.getInstance().getReference();
                pushViews.child("Question").orderByChild("id").equalTo(listQuestion.get(position).getId())
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