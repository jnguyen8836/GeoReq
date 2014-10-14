package com.gmail.jnguyendev.georeq;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import com.gmail.jnguyendev.georeq.R;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


/*
 * This class was largely written with help from the following tutorial:
 * http://www.lynda.com/Android-tutorials/Geocoding-address-get-its-coordinates/133347/144372-4.html
 */
public class MainActivity extends Activity {
	
	GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				   
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	}
	
	public void getLocation(View view) throws IOException {
		EditText text = (EditText) findViewById(R.id.editTextLocation);
		
		String location = text.getText().toString();
		
		Geocoder g = new Geocoder(this);
		List<Address> list = g.getFromLocationName(location, 1);
		Address addr = list.get(0);
		
		double latitude = addr.getLatitude();
		double longitude = addr.getLongitude();
		
		float zoom;		
		if(addr.getMaxAddressLineIndex() == 0) zoom = 2.5f; //country
		else if(addr.getMaxAddressLineIndex() == 1 && addr.getLocality() == null) zoom = 4; //state
		else if(addr.getMaxAddressLineIndex() == 1 && addr.getLocality() != null) zoom = 7.5f; //city or zip code
		else zoom = 15; //street or full address
		
		goToLocation(addr.getPostalCode(), latitude, longitude, zoom);
		
//		Toast.makeText(this, latitude + " " + longitude, Toast.LENGTH_LONG).show();
//		Toast.makeText(this, text.getText().toString(), Toast.LENGTH_LONG).show();
	}
	
	private void goToLocation(String location, double latitude, double longitude, float zoom) {
		LatLng ll = new LatLng(latitude, longitude);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
		mMap.clear();
		mMap.moveCamera(update);
		mMap.addMarker(new MarkerOptions().position(ll).title(location));
		mMap.setTrafficEnabled(true);
	}
}
