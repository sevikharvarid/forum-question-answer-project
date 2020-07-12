package com.example.projectappquestionanswer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    //Deklarasi Variable
    private ArrayList<Question> listQuestion;
    private ArrayList<String> listSpecificQuestion;
    private Context context;
    private FirebaseUser auth;

    //Membuat Interfece
    public interface dataListener {
        void onDeleteData(Question data, int position);
    }

    //Deklarasi objek dari Interfece
    dataListener listener;

    //Membuat Konstruktor, untuk menerima input dari Database
    public RecyclerViewAdapter(ArrayList<Question> listQuestion, Context context) {
        this.listQuestion = listQuestion;
        this.context = context;
        //listener = new ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Email, Isi;
        private CardView ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            Email = itemView.findViewById(R.id.question_author);
            Isi = itemView.findViewById(R.id.question_title);
            ListItem = itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String Email = listQuestion.get(position).getAuthor();
        final String Isi = listQuestion.get(position).getDescription();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.Email.setText(Email);
        holder.Isi.setText(Isi);
        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dataCategory", listQuestion.get(position).getCategory());
                bundle.putString("dataAuthor", listQuestion.get(position).getAuthor());
                bundle.putString("dataDescription", listQuestion.get(position).getDescription());
                Intent intent = new Intent(view.getContext(), ViewQuestion.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
        /*holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNIM", listMahasiswa.get(position).getNim());
                                bundle.putString("dataNama", listMahasiswa.get(position).getNama());
                                bundle.putString("dataJurusan", listMahasiswa.get(position).getJurusan());
                                bundle.putString("getPrimaryKey", listMahasiswa.get(position).getKey());
                                Intent intent = new Intent(view.getContext(), updateData.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                listener.onDeleteData(listMahasiswa.get(position), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });

        */

    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listQuestion.size();
    }


}