package com.alejandrosanchez.mbfoursquareapisample.models;

import java.util.List;

/**
 * Created by Alejandro on 07/09/2016.
 */
public class VenuesResponse {

  private List<Venue> venues;

  public VenuesResponse(List<Venue> venues) {
    this.venues = venues;
  }

  public List<Venue> getVenues() {
    return venues;
  }
}
