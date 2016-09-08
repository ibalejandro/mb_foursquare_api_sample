package com.alejandrosanchez.mbfoursquareapisample.interfaces;

/**
 * Created by Alejandro on 06/09/2016.
 */
public interface MapPresenter {

  void locateUser(double latitude, double longitude);

  void populateMap(double latitude, double longitude, double range, String searchTerm);

  void onDestroy();
}
