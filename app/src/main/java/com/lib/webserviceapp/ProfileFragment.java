package com.lib.webserviceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    TextView usernameTxtView;
    TextView nameTxtView;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);

        // Set up the logout button click listener
        Button logoutButton = view.findViewById(R.id.deleteAccountBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMyAccount();
            }
        });

        usernameTxtView = view.findViewById(R.id.usernameTxt);
        nameTxtView = view.findViewById(R.id.nameTxt);
        String username = getUsernameFromPreferences();
        String name = getnameFromPreferences();
        usernameTxtView.setText("@" + username);
        nameTxtView.setText("" + name);

        return view;

    }
    private void DeleteMyAccount() {
        SharedPreferences myPrefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(getActivity(), LoginMainActivity.class);
        startActivity(intent);
    }
    private String getUsernameFromPreferences() {
        SharedPreferences myPrefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return myPrefs.getString("username", "");
    }
    private String getnameFromPreferences(){
        SharedPreferences myPrefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return myPrefs.getString("name", "");
    }
}