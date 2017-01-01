package sebinsunny.gps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AndroidGPSTrackingActivity extends Activity {
	private static final String REGISTER_URL = "https://tomgeorge.me/tracker/save";
	public static final String KEY_USERNAME = "lat";
	public static final String KEY_PASSWORD = "lng";
	String lat,log;
	Button btnShowLocation;
	
	// GPSTracker class
	GPSTracker gps;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// create class object
				gps = new GPSTracker(AndroidGPSTrackingActivity.this);

				// check if GPS enabled		
				if (gps.canGetLocation()) {

					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();
					lat = "" + latitude;
					log = "" + longitude;
					// \n is for new line
					Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + log, Toast.LENGTH_LONG).show();
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

				StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								Toast.makeText(AndroidGPSTrackingActivity.this, response, Toast.LENGTH_LONG).show();
							}
						},
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(AndroidGPSTrackingActivity.this, error.toString(), Toast.LENGTH_LONG).show();
							}
						}) {
					@Override
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put(KEY_USERNAME, lat);
						params.put(KEY_PASSWORD, log);
						return params;
					}

				};
				RequestQueue requestQueue = Volley.newRequestQueue(AndroidGPSTrackingActivity.this);
				requestQueue.add(stringRequest);

			}
		});
    }

}