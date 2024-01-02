package com.lib.webserviceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


//Sally Bassis - 1191958
//Sec - 2
//My Application is about Free-To-Paly Games, get Data from 2 API,
//Find the original link to the user's favorite game in the fastest way
//Find Free Givaways based in Platform
public class HomeFragment extends Fragment {
    private final String url = "https://www.freetogame.com/api/games"; //first Web Service API for free games
    private final String url2 = "https://www.gamerpower.com/api/giveaways"; //Second Web Service API for free Giveaways
    private Spinner spinnerPlatform;
    private TextView giveawayTxtResult;
    private RequestQueue queue;
    EditText URLResult;
    private String lastResult;
    SharedPreferences myPrefs;
    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        queue = Volley.newRequestQueue(requireActivity());

        myPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE);
        URLResult = view.findViewById(R.id.edtResult);

        if (savedInstanceState != null) {
            lastResult = savedInstanceState.getString("lastResult", "");
            URLResult.setText(lastResult);
        }

        //URLResult = requireView().findViewById(R.id.edtResult);
        spinnerPlatform = view.findViewById(R.id.spinnerPlatform);
        giveawayTxtResult = view.findViewById(R.id.txtGiveaway);
        setupPlatformSpinner();


        view.findViewById(R.id.btnFind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });
        view.findViewById(R.id.btnSearchGiveaways).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchGiveaways();
        }
    });

        return view;
}
    private void fetchData() {
        EditText edtTitle = requireView().findViewById(R.id.edtTitle);
        String userEnteredTitle = edtTitle.getText().toString().trim();
        if (!userEnteredTitle.isEmpty()) {
            JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // Handle the JSON response
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject gameObj = response.getJSONObject(i);

                                    String title = gameObj.getString("title");
                                    String gameUrl = gameObj.getString("game_url");

                                    // Check if the entered title matches the current game's title
                                    if (userEnteredTitle.equalsIgnoreCase(title)) {

                                        displayGameUrl(gameUrl);
                                        return;
                                    }
                                }
                                displayGameUrl("Game not found!");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle errors
                    Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonRequest);
        } else {
            // If empty
            displayGameUrl("Please enter a title!");
        }
    }
    private void displayGameUrl(String gameUrl) {
        lastResult = gameUrl;
        URLResult.setVisibility(View.VISIBLE);
        URLResult.setText(gameUrl);
        saveResultToSharedPreferences();
    }
    private void setupPlatformSpinner() {
        List<String> platforms = new ArrayList<>();
        platforms.add("PC");
        platforms.add("Android");
        platforms.add("OS");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, platforms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlatform.setAdapter(adapter);
    }
    @Override
    public void onPause() {
        super.onPause();
        //save the result when user leave the app ( paused)
        saveResultToSharedPreferences();
    }
    public void onResume() {
        super.onResume();
        //retrieve the last saved result
        lastResult = myPrefs.getString("lastResult", "");
        URLResult.setText(lastResult);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("lastResult", lastResult);
    }

    private void saveResultToSharedPreferences() {
        // Save the URL result to SharedPreferences
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("lastResult", lastResult);
        editor.commit();
    }
    private void searchGiveaways() {
        String selectedPlatform = spinnerPlatform.getSelectedItem().toString();
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            StringBuilder giveawayText = new StringBuilder();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject giveawayObj = response.getJSONObject(i);

                                String title = giveawayObj.getString("title");
                                String openGiveawayUrl = giveawayObj.getString("open_giveaway_url");
                                String platforms = giveawayObj.getString("platforms");

                                if (platforms.contains(selectedPlatform)) {
                                    giveawayText.append("Title: ").append(title).append("\n")
                                            .append("URL: ").append(openGiveawayUrl).append("\n\n");
                                }
                            }
                            if (giveawayText.length() > 0) {
                                // Display giveaway titles and URLs in the result EditText
                                giveawayTxtResult.setVisibility(View.VISIBLE);
                                giveawayTxtResult.setText(giveawayText.toString());
                            } else {
                                giveawayTxtResult.setVisibility(View.VISIBLE);
                                giveawayTxtResult.setText("No giveaways found for the selected platform.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonRequest);
    }
}