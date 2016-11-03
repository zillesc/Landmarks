package ninja.zilles.landmarks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        Landmark landmark = extras.getParcelable("landmark");

        TextView nameView = (TextView) findViewById(R.id.textView);
        nameView.setText(landmark.getLandmarkName());

        TextView typeView = (TextView) findViewById(R.id.textView2);
        typeView.setText(landmark.getTypeOfLandmark());

        TextView ownerView = (TextView) findViewById(R.id.textView3);
        ownerView.setText(landmark.getOwnership());
    }

}
