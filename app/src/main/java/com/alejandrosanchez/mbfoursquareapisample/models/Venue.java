package com.alejandrosanchez.mbfoursquareapisample.models;

/**
 * Created by Alejandro on 05/09/2016.
 */
public class Venue {

  private String id;
  private String name;
  private Location location;

  public Venue(String id, String name, Location location) {
    this.id = id;
    this.name = name;
    this.location = location;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Location getLocation() {
    return location;
  }
}
