package com.alejandrosanchez.mbfoursquareapisample.models;

/**
 * Created by Alejandro on 07/09/2016.
 */
public class ResponseWrapper {

  private VenuesResponse response;

  public ResponseWrapper(VenuesResponse response) {
    this.response = response;
  }

  public VenuesResponse getResponse() {
    return response;
  }
}
