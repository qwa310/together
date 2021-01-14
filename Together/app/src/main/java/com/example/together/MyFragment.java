package com.example.together;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class MyFragment extends Fragment {

    ArrayList<String> myListItem;
    ArrayList<String> logoutListItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        ListView lvMy = view.findViewById(R.id.lvMy);
        ListView lvLogout = view.findViewById(R.id.lvLogout);

        initListItem();
        final MyAdapter myAdapter = new MyAdapter(this.getContext(), myListItem);
        final LogoutAdapter logoutAdapter = new LogoutAdapter(this.getContext(), logoutListItem);

        lvMy.setAdapter(myAdapter);
        lvLogout.setAdapter(logoutAdapter);

        lvMy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Toast.makeText(getContext(), myAdapter.getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0: // 긴급 전화번호 관리
                        Toast.makeText(getContext(), "긴급전화번호 관리 페이지로 넘어갑니다",
                                Toast.LENGTH_LONG).show();
                        break;

                    case 1: // 보호자 관리
                        break;

                    default: // 피보호자 관리
                }
            }
        });

        lvLogout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: logout
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void initListItem() {
        myListItem = new ArrayList<String>();
        logoutListItem = new ArrayList<String>();

        myListItem.add("긴급 전화번호 관리");
        myListItem.add("보호자 관리");
        myListItem.add("피보호자 관리");
        logoutListItem.add("로그아웃");
    }
}