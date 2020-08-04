package com.project.crudfirebase;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class DynamicFragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private String nameCategory;
    private ArrayList<String> list = new ArrayList<String>();

    DynamicFragmentAdapter(FragmentManager fm, int NumOfTabs,ArrayList<String> list) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.list = list;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putString("position", list.get(position));
        //Fragment frag = QuestionListFragment.newInstance();
        Fragment frag = MyListData.newInstance();
        frag.setArguments(b);
        return frag;

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}
