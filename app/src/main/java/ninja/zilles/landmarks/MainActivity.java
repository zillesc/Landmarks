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
import java.util.Comparator;
import java.util.List;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private LandmarkAdapter mLandmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attach the custom ArrayAdapter to our ListView
        listView = (ListView) findViewById(R.id.listView);
        mLandmarkAdapter = new LandmarkAdapter(this, R.layout.listitem);
        listView.setAdapter(mLandmarkAdapter);

        // On long click listener launches a map at the target latitude/longitude
        // trying to label the desired location based on the name provided.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Landmark landmark = mLandmarkAdapter.getItem(position);
                Landmark.Location location = landmark.getLocation();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                String uriString = String.format("geo:%s,%s?q=%s,%s(%s)",
                        latitude, longitude,
                        latitude, longitude, landmark.getLandmarkName());
                Uri gmmIntentUri = Uri.parse(uriString);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);

                return true;
            }
        });

        // Normal clicks open a detail view; we pass a parcelable Landmark
        // to the detail page.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Landmark landmark = mLandmarkAdapter.getItem(position);
                Intent detailIntent = new Intent(parent.getContext(),
                        DetailActivity.class).putExtra("landmark", landmark);
                startActivity(detailIntent);
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
                URL url = new URL("https://data.illinois.gov/resource/pwzm-4yz8.json");

                // Create the request and open the connection
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
                // If the code didn't successfully get the data, there's no point in attemping
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
                mLandmarkAdapter.addAll(landmarks);
                mLandmarkAdapter.sort(new Comparator<Landmark>() {
                    @Override
                    public int compare(Landmark lhs, Landmark rhs) {
                        return lhs.getLandmarkName().compareTo(rhs.getLandmarkName());
                    }
                });
            }
        }
    }
}
