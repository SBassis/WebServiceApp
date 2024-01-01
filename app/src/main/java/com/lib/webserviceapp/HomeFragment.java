package com.lib.webserviceapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


//Sally Bassis - 1191958
//Sec - 2
//My Application is about Free-To-Paly Games, get Data from 2 API,
//Find the original link to the user's favorite game in the fastest way
//Find Free Givaways based in Platform
public class HomeFragment extends Fragment {
    private final String url = "https://www.freetogame.com/api/games"; //first Web Service API for free games
    private final String url2 = "https://www.gamerpower.com/api/giveaways"; //Second Web Service API for free Giveaways
    private Spinner spinnerPlatform;
    private RequestQueue queue;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        queue = Volley.newRequestQueue(requireActivity());
        view.findViewById(R.id.btnFind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });
        return view;
    }
    private void fetchData() {
        // Assuming you have an EditText for the user to enter the title with the ID edtTitle
        EditText edtTitle = requireView().findViewById(R.id.edtTitle);

        // Get the title entered by the user
        String userEnteredTitle = edtTitle.getText().toString().trim();
        if (!userEnteredTitle.isEmpty()) {
            //request
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
            // If the entered title is empty, display a message
            displayGameUrl("Please enter a title!");
        }
    }
    private void displayGameUrl(String gameUrl) {
        EditText edtResult = requireView().findViewById(R.id.edtResult);
        edtResult.setVisibility(View.VISIBLE);
        edtResult.setText(gameUrl);
    }
}