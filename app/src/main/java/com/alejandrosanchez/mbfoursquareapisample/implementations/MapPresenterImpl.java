package com.alejandrosanchez.mbfoursquareapisample.implementations;

import com.alejandrosanchez.mbfoursquareapisample.interfaces.MapInteractor;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.MapPresenter;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.MapView;
import com.alejandrosanchez.mbfoursquareapisample.models.FoursquareMarker;
import com.alejandrosanchez.mbfoursquareapisample.models.Venue;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alejandro on 06/09/2016.
 */
public class MapPresenterImpl implements MapPresenter, MapInteractor.OnDataReceivedListener {

  private static final String USER = "User";

  private MapView mMapView;
  private MapInteractor mMapInteractor;

  public MapPresenterImpl(MapView mapView) {
    this.mMapView = mapView;
    this.mMapInteractor = new MapInteractorImpl(mMapView.getActivity());
  }

  @Override
  public void locateUser(double latitude, double longitude) {
    if (mMapView != null) {
      LatLng userPosition = new LatLng(latitude, longitude);
      mMapView.moveCamera(userPosition);
      mMapView.addMarkerForUser(userPosition, USER);
      mMapView.showRange(userPosition);
    }
  }

  @Override
  public void populateMap(double latitude, double longitude, double range, String searchTerm) {
    mMapInteractor.getData(latitude, longitude, range, searchTerm, this);
  }

  @Override
  public void onDestroy() {
    mMapView = null;
  }

  @Override
  public void onNoInternetError() {
    mMapView.showNoInternetError();
  }

  @Override
  public void onDataReceivedError() {
    mMapView.showReceivingDataError();
  }

  /**
   * If the data was received successfully, a list with the appropriate information for the map
   * will be constructed. If the request was successful but the received data was empty, a message
   * will tell it to the user.
   *
   * @param venues List The list received from the request's response.
   */
  @Override
  public void onDataReceivedSuccess(List<Venue> venues) {
    if (mMapView != null) {

      if (venues.isEmpty()) {
        mMapView.showEmptyDataReceived();
      }
      else {
        List<FoursquareMarker> fsMarkers = new ArrayList<>();

        LatLng position;
        String title;
        String snippet;
        Double distance;
        for (Venue v : venues) {
          position = new LatLng(v.getLocation().getLat(), v.getLocation().getLng());
          title = v.getName();
          distance = v.getLocation().getDistance();
          if (distance != null) {
            // There is a distance to show on the current marker.
            snippet = "Distance: " + String.valueOf(distance) + " m";
          }
          else {
            // The distance to this marker is unknown.
            snippet = "Distance: X";
          }

          fsMarkers.add(new FoursquareMarker(position, title, snippet));
        }

        mMapView.addFsMarkers(fsMarkers);
      }
    }
  }
}
