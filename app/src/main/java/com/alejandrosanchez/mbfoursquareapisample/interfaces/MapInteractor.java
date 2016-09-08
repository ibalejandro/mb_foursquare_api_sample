package com.alejandrosanchez.mbfoursquareapisample.interfaces;

import com.alejandrosanchez.mbfoursquareapisample.models.Venue;

import java.util.List;

/**
 * Created by Alejandro on 06/09/2016.
 */
public interface MapInteractor {

  interface OnDataReceivedListener {
    void onNoInternetError();

    void onDataReceivedError();

    void onDataReceivedSuccess(List<Venue> venues);
  }

  void getData(double latitude, double longitude, double range, String searchTerm,
               OnDataReceivedListener listener);
}
