package com.yjchoi.jsontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // Json Data URL
    String JsonURL = "https://rawgit.com/the1994/todaktodak/master/pet.json";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Casts results into the TextView found within the main layout XML with id jsonData
        final ListView list = (ListView) findViewById(R.id.listView1);

        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURL,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Retrieves first JSON object in outer array
                            JSONObject hospitalObj = response.getJSONObject(0);
                            // Retrieves "petHospital" from the JSON object
                            JSONArray hospitalArry = hospitalObj.getJSONArray("petHospital");
                            // Iterates through the JSON Array getting objects and adding them
                            //to the list view until there are no more objects in hospitalArray

                            String[] petHospital = new String[hospitalArry.length()];

                            for (int i = 0; i < hospitalArry.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject jsonObject = hospitalArry.getJSONObject(i);

                                // "name", "location", "phone"이라는 이름 받아오고
                                // 객체로 만든다
                                String name = jsonObject.getString("name");
                                String location = jsonObject.getString("location");
                                String phone = jsonObject.getString("phone");

                                // 각각 합쳐서 hospital String에 저장
                                String hospital = "Number " + (i + 1) + "\n" + "Hospital Name: " + name + "\n" +
                                        "location: " + location + "\n" + "phone: " + phone;

                                // 합친 String을 배열에 넣는다.
                                petHospital[i] = hospital;

                                Log.v("test", petHospital[i]);
                            }
                            // listView에 표시하기
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, petHospital);
                            list.setAdapter(adapter);
                        }
                        // JSON 에러
                        catch (JSONException e) {
                            // 에러 발생하면, 로그에 출력
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        requestQueue.add(arrayreq);
    }
}
