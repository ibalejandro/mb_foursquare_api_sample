package com.alejandrosanchez.mbfoursquareapisample.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.alejandrosanchez.mbfoursquareapisample.R;
import com.alejandrosanchez.mbfoursquareapisample.implementations.MapPresenterImpl;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.MapPresenter;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.MapView;
import com.alejandrosanchez.mbfoursquareapisample.models.FoursquareMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapView {

  private static final String TAG = "MapActivity";
  public static final String LATITUDE = "LATITUDE";
  public static final String LONGITUDE = "LONGITUDE";
  public static final String RANGE = "RANGE";
  public static final String SEARCH_TERM = "SEARCH_TERM";
  public static final double MAX_RANGE = 100000;
  private static final float ZOOM_LEVEL = 16f;
  private static final float STROKE_WIDTH = 1f;

  private GoogleMap mMap;
  private double mLatitude;
  private double mLongitude;
  private double mRange;
  private String mSearchTerm;
  private MapPresenter mMapPresenter;

  @BindView(R.id.map)
  View mapFragmentView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(getResources().getString(R.string.results));
    ButterKnife.bind(this);

    // Gets the latitude, longitude, range and search term given by the user.
    getIntentExtras();

    mMapPresenter = new MapPresenterImpl(this);

    // Obtains the SupportMapFragment and gets notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  /**
   * Receives the data from the LocationSetupActivity.
   */
  public void getIntentExtras() {
    mLatitude = getIntent().getExtras().getDouble(LATITUDE);
    mLongitude = getIntent().getExtras().getDouble(LONGITUDE);
    mRange = getIntent().getExtras().getDouble(RANGE);
    mSearchTerm = getIntent().getExtras().getString(SEARCH_TERM);

    Log.i(TAG, "Lat: " + mLatitude + ", Long: " + mLongitude + ", Range: " + mRange
          + ", Search term: " + mSearchTerm + ".");
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    Log.i(TAG, "Map ready.");

    mMap = googleMap;
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    // After the map is ready, the user has to be located automatically.
    mMapPresenter.locateUser(mLatitude, mLongitude);
    // Initializes the population of the map with the places inside the given range.
    populateMap();
  }

  @Override
  public Activity getActivity() {
    return MapActivity.this;
  }

  @Override
  public void moveCamera(LatLng position) {
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_LEVEL));
  }

  @Override
  public void addMarkerForUser(LatLng userPosition, String title) {
    mMap.addMarker(new MarkerOptions().position(userPosition).title(title)
                   .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

  }

  /**
   * Shows a circle around the user's position with radius equals to the given range.
   *
   * @param userPosition LatLng The current position of the user
   */
  @Override
  public void showRange(LatLng userPosition) {
    mMap.addCircle(new CircleOptions().center(userPosition).radius(mRange).strokeWidth(STROKE_WIDTH)
                   .strokeColor(ContextCompat.getColor(this, R.color.dark_blue_transparent))
                   .fillColor(ContextCompat.getColor(this, R.color.blue_transparent)));
  }

  /**
   * Initializes the process for the API request in order to populate the map with markers.
   */
  public void populateMap() {
    mMapPresenter.populateMap(mLatitude, mLongitude, mRange, mSearchTerm);
  }

  @Override
  public void addFsMarkers(List<FoursquareMarker> fsMarkers) {
    for (FoursquareMarker fsM : fsMarkers) {
      mMap.addMarker(new MarkerOptions().position(fsM.getPosition()).title(fsM.getTitle())
                     .snippet(fsM.getSnippet()).icon(BitmapDescriptorFactory
                                                     .fromResource(R.mipmap.ic_fs)));
    }
  }

  @Override
  public void showNoInternetError() {
    Snackbar.make(mapFragmentView, getResources().getString(R.string.error_no_internet),
                  Snackbar.LENGTH_LONG)
        .setAction(getResources().getString(R.string.action_retry), new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            populateMap();
          }
        })
        .show();
  }

  @Override
  public void showReceivingDataError() {
    Snackbar.make(mapFragmentView, getResources().getString(R.string.error_receiving_data),
                  Snackbar.LENGTH_LONG)
        .setAction(getResources().getString(R.string.action_retry), new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            populateMap();
          }
        })
        .show();
  }

  @Override
  public void showEmptyDataReceived() {
    Snackbar.make(mapFragmentView, getResources().getString(R.string.message_no_places_for_range),
        Snackbar.LENGTH_LONG).show();
  }

  @Override
  protected void onDestroy() {
    mMapPresenter.onDestroy();
    super.onDestroy();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }

    return super.onOptionsItemSelected(item);
  }
}
