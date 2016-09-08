package com.alejandrosanchez.mbfoursquareapisample.models;

/**
 * Created by Alejandro on 05/09/2016.
 */
public class Location {
  
  private double lat;
  private double lng;
  // The distance is Double and not double in order to avoid parsing empty distances i.e. 0.0.
  private Double distance;
  private String[] formattedAddress;

  public Location(double lat, double lng, Double distance, String[] formattedAddress) {
    this.lat = lat;
    this.lng = lng;
    this.distance = distance;
    this.formattedAddress = formattedAddress;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public Double getDistance() {
    return distance;
  }

  public String[] getFormattedAddress() {
    return formattedAddress;
  }
}
