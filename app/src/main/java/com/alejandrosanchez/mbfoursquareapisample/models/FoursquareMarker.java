package com.alejandrosanchez.mbfoursquareapisample.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alejandro on 07/09/2016.
 */
public class FoursquareMarker {

  private LatLng position;
  private String title;
  private String snippet;

  public FoursquareMarker(LatLng position, String title, String snippet) {
    this.position = position;
    this.title = title;
    this.snippet = snippet;
  }

  public LatLng getPosition() {
    return position;
  }

  public String getTitle() {
    return title;
  }

  public String getSnippet() {
    return snippet;
  }
}
