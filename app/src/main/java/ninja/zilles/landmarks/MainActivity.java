package ninja.zilles.landmarks;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    List<Landmark> landmarkList = new ArrayList<>();
    List<String> landmarkNames = new ArrayList<>();
    private ArrayAdapter<String> listStringAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        listStringAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, landmarkNames);
        listView.setAdapter(listStringAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Landmark landmark = landmarkList.get(position);
                Landmark.Location location = landmark.getLocation();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Create a Uri from an intent string. Use the result to create an Intent.
//                Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + latitude + "," + longitude);
//                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + landmark.getLandmarkName());
//                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude +
                        "(" + landmark.getLandmarkName() + ")");


                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            }
        });

        FetchData fetchData = new FetchData();
        fetchData.execute();
    }


    public class FetchData extends AsyncTask<Void, Void, Landmark[]> {
        @Override
        protected Landmark[] doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("https://data.illinois.gov/resource/pwzm-4yz8.json");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Gson gson = new Gson();
                Landmark[] landmarks = gson.fromJson(forecastJsonStr, Landmark[].class);
                for (Landmark landmark : landmarks) {
                    Log.d("FetchData", "landmark: " + landmark.getLandmarkName());
                }
                return landmarks;
            } catch (IOException e) {
                Log.e("FetchData", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FetchData", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Landmark[] landmarks) {
            if (landmarks != null) {
                for (Landmark landmark : landmarks) {
                    landmarkList.add(landmark);
                    landmarkNames.add(landmark.getLandmarkName());
                }
                Collections.sort(landmarkNames);
                Collections.sort(landmarkList);
                listStringAdapter.notifyDataSetChanged();
            }
        }
    }
}
