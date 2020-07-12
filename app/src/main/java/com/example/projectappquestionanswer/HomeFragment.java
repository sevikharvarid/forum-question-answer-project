package com.example.projectappquestionanswer;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.projectappquestionanswer.DynamicFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private String x;
    public ViewPager viewPager;
    public TabLayout mTabLayout;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;

    private ListView mainListView;
    private FirebaseAuth auth;
    private String desc, tilt, cat;
    private ArrayList<String> list = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.tabs);
        initViews();

        return view;


    }

    public static class MyFragment extends Fragment {
        public MyFragment() {

        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

    }

    private void initViews() {


        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setDynamicFragmentToTabLayout();


    }


    private void setDynamicFragmentToTabLayout() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SpinnerData");
        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    mTabLayout.addTab(mTabLayout.newTab().setText(item.getValue().toString()));
                    list.add(item.getValue().toString());
                    DynamicFragmentAdapter mDynamicFragmentAdapter = new DynamicFragmentAdapter(getFragmentManager(),
                            mTabLayout.getTabCount(), list);
                    viewPager.setAdapter(mDynamicFragmentAdapter);
                    viewPager.setCurrentItem(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}