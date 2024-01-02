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
    Button logoutBtn;
    Button DeleteBtn;
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        logoutBtn = view.findViewById(R.id.outBtn);
        DeleteBtn = view.findViewById(R.id.deleteAccountBtn);

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMyAccount();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });
        usernameTxtView = view.findViewById(R.id.usernameTxt);
        nameTxtView = view.findViewById(R.id.nameTxt);
        String username = getUsernameFromPreferences();
        String name = getNameFromPreferences();
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
    private void logout() {
        String rememberedUsername = getRememberedUsername();
        String rememberedPassword = getRememberedPassword();
        clearUserData();
        Intent intent = new Intent(getActivity(), LoginMainActivity.class);
        if (!rememberedUsername.isEmpty() && !rememberedPassword.isEmpty()) {

            intent.putExtra("username", rememberedUsername);
            intent.putExtra("password", rememberedPassword);
        }
        startActivity(intent);
        getActivity().finish();
    }
    private void clearUserData() {
        SharedPreferences myPrefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

    //get name, username
    private String getUsernameFromPreferences() {
        SharedPreferences myPrefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return myPrefs.getString("username", "");
    }
    private String getNameFromPreferences(){
        SharedPreferences myPrefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return myPrefs.getString("name", "");
    }

    //get remembered data
    private String getRememberedUsername() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("rememberedUsername", "");
    }

    private String getRememberedPassword() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("rememberedPassword", "");
    }
}