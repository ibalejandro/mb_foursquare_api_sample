package com.alejandrosanchez.mbfoursquareapisample.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;

import com.alejandrosanchez.mbfoursquareapisample.R;
import com.alejandrosanchez.mbfoursquareapisample.implementations.LocationSetupPresenterImpl;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.LocationSetupPresenter;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.LocationSetupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationSetupActivity extends AppCompatActivity implements LocationSetupView {

  private static final String TAG = "LocationSetupActivity";
  private static final int ENABLE_GPS_REQUEST_CODE = 1;

  @BindView(R.id.range_edit_text)
  AppCompatEditText rangeEditText;
  @BindView(R.id.search_term_edit_text)
  AppCompatEditText searchTermEditText;
  @BindView(R.id.action_button)
  AppCompatButton actionButton;

  private LocationSetupPresenter mLocationSetupPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_setup);
    ButterKnife.bind(this);

    mLocationSetupPresenter = new LocationSetupPresenterImpl(this);

    // These two methods have to be triggered automatically when the app starts.
    showGettingLocation();
    onActionButtonClicked();
  }

  @Override
  public Activity getActivity() {
    return LocationSetupActivity.this;
  }

  @Override
  public String getRange() {
    return rangeEditText.getText().toString();
  }

  @Override
  public void setRangeError() {
    rangeEditText.setError(getString(R.string.error_invalid_range));
  }

  @Override
  public String getSearchTerm() {
    return searchTermEditText.getText().toString().trim();
  }

  @Override
  public void showGettingLocation() {
    actionButton.setEnabled(false);
    actionButton.setText(getString(R.string.message_getting_location));
  }

  @Override
  public void showRetry() {
    actionButton.setEnabled(true);
    actionButton.setText(getString(R.string.action_retry_getting_location));
  }

  @Override
  public void showGoToMap() {
    actionButton.setEnabled(true);
    actionButton.setText(getString(R.string.action_go_to_map));
  }

  /**
   * Decides what to do according to the action_button state.
   */
  @OnClick(R.id.action_button)
  public void onActionButtonClicked() {
    mLocationSetupPresenter.decideAction();
  }

  /**
   * Opens the GPS settings so that the user can enable it.
   */
  @Override
  public void showEnableGpsSettings() {
    Intent enableGPSIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivityForResult(enableGPSIntent, ENABLE_GPS_REQUEST_CODE);
  }

  @Override
  public void navigateToMap(double latitude, double longitude, double range, String searchTerm) {
    Log.i(TAG, "Lat: " + latitude + ", Long: " + longitude + ", Range: " + range + ", Search term: "
          + searchTerm + ".");

    Intent mapIntent = new Intent(this, MapActivity.class);
    mapIntent.putExtra(MapActivity.LATITUDE, latitude);
    mapIntent.putExtra(MapActivity.LONGITUDE, longitude);
    mapIntent.putExtra(MapActivity.RANGE, range);
    mapIntent.putExtra(MapActivity.SEARCH_TERM, searchTerm);
    startActivity(mapIntent);
  }

  @Override
  protected void onDestroy() {
    mLocationSetupPresenter.onDestroy();
    super.onDestroy();
  }
}
