package com.alejandrosanchez.mbfoursquareapisample.implementations;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.alejandrosanchez.mbfoursquareapisample.activities.MapActivity;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.LocationSetupInteractor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Alejandro on 06/09/2016.
 */
public class LocationSetupInteractorImpl implements LocationSetupInteractor,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

  private static final String TAG = "LocSetupInteractorImpl";
  private static final int INTERVAL = 10 * 1000;
  private static final int FASTEST_INTERVAL = 1 * 1000;
  private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

  private Activity mActivity;
  private GoogleApiClient mGoogleApiClient;
  private Location mLocation;
  private LocationRequest mLocationRequest;
  private LocationManager mLocationManager;
  private OnLocationChangedListener mLocationChangedListener;

  public LocationSetupInteractorImpl(Activity activity) {
    // The Activity is required for the use of the GoogleApiClient.
    this.mActivity = activity;
  }

  @Override
  public boolean isLocationAvailable() {
    if (mLocation != null) return true;
    return false;
  }

  @Override
  public void getLocation(OnLocationChangedListener listener) {
    Log.i(TAG, "Enter getLocation()");

    mLocationChangedListener = listener;
    createGoogleApiClientInstance();
    connectGoogleApiClient();
    createLocationRequest();
  }

  protected synchronized void createGoogleApiClientInstance() {
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }
  }

  public void connectGoogleApiClient() {
    mGoogleApiClient.connect();

    Log.i(TAG, "mGoogleApiClient connect() triggered.");
  }

  protected synchronized void createLocationRequest() {
    if (mLocationRequest == null) {
      mLocationRequest = LocationRequest.create()
          .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(INTERVAL)
          .setFastestInterval(FASTEST_INTERVAL);
    }
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    if (connectionResult.hasResolution()) {
      try {
        // Starts an Activity that tries to resolve the error.
        connectionResult.startResolutionForResult(mActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
      }
      catch (IntentSender.SendIntentException sie) {
        Log.i(TAG, "Location services connection failed.");

        sie.printStackTrace();
        disconnectGoogleApiClient();
        notifyLocationUnavailability();
      }
    }
    else {
      // The error has no resolution.
      Log.i(TAG, "Location services connection failed with code " + connectionResult
            .getErrorCode() + ".");

      disconnectGoogleApiClient();
      notifyLocationUnavailability();
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.i(TAG, "Location services suspended. Please reconnect.");

    disconnectGoogleApiClient();
    notifyLocationUnavailability();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      // There is no ACCESS_FINE_LOCATION permission.
      disconnectGoogleApiClient();
      notifyLocationUnavailability();
    }

    mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    if (mLocation != null) {
      disconnectGoogleApiClient();
      notifyLocationAvailability(mLocation);
    }
    else {
      Log.i(TAG, "mLocation is null.");

      if (isGpsEnabled()) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                                                                 mLocationRequest, this);
      }
      else {
        Log.i(TAG, "The GPS has to be enabled.");

        disconnectGoogleApiClient();
        mLocationChangedListener.onGpsDisabledError();
        notifyLocationUnavailability();
      }
    }
  }

  private boolean isGpsEnabled() {
    mLocationManager = (LocationManager) mActivity
        .getSystemService(Context.LOCATION_SERVICE);
    return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.i(TAG, "onLocationChanged() was called.");

    disconnectGoogleApiClient();
    notifyLocationAvailability(location);
  }

  public void disconnectGoogleApiClient() {
    if (mGoogleApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
      mGoogleApiClient.disconnect();

      Log.i(TAG, "mGoogleApiClient removeLocationUpdates() triggered.");
      Log.i(TAG, "mGoogleApiClient disconnect() triggered.");
    }
  }

  public void notifyLocationAvailability(Location location) {
    Log.i(TAG, "Latitude: " + String.valueOf(location.getLatitude()) + ".");
    Log.i(TAG, "Longitude: " + String.valueOf(location.getLongitude()) + ".");

    mLocationChangedListener.onGettingLocationSuccess();
  }

  public void notifyLocationUnavailability() {
    mLocationChangedListener.onGettingLocationError();
  }

  @Override
  public void validateRange(double range, OnRangeValidatedListener listener) {
    if (range > 0 && range <= MapActivity.MAX_RANGE) listener.onValidatingRangeSuccess(range);
    else listener.onValidatingRangeError();
  }

  @Override
  public double getLatitude() {
    if (mLocation != null) return mLocation.getLatitude();
    return 0;
  }

  @Override
  public double getLongitude() {
    if (mLocation != null) return mLocation.getLongitude();
    return 0;
  }
}
