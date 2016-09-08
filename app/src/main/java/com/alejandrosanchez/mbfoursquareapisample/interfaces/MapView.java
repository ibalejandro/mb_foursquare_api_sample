package com.alejandrosanchez.mbfoursquareapisample.interfaces;

import android.app.Activity;

import com.alejandrosanchez.mbfoursquareapisample.models.FoursquareMarker;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Alejandro on 06/09/2016.
 */
public interface MapView {

  Activity getActivity();

  void moveCamera(LatLng position);

  void addMarkerForUser(LatLng userPosition, String title);

  void showRange(LatLng userPosition);

  void addFsMarkers(List<FoursquareMarker> fsMarkers);

  void showNoInternetError();

  void showReceivingDataError();

  void showEmptyDataReceived();
}
