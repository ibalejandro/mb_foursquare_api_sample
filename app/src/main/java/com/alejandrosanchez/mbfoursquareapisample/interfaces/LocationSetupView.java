package com.alejandrosanchez.mbfoursquareapisample.interfaces;

import android.app.Activity;

/**
 * Created by Alejandro on 06/09/2016.
 */
public interface LocationSetupView {

  Activity getActivity();

  String getRange();

  void setRangeError();

  String getSearchTerm();

  void showGettingLocation();

  void showRetry();

  void showGoToMap();

  void showEnableGpsSettings();

  void navigateToMap(double latitude, double longitude, double range, String searchTerm);
}
