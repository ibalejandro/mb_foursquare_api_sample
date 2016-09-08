package com.alejandrosanchez.mbfoursquareapisample.interfaces;

/**
 * Created by Alejandro on 06/09/2016.
 */
public interface LocationSetupInteractor {

  interface OnLocationChangedListener {
    void onGettingLocationError();

    void onGpsDisabledError();

    void onGettingLocationSuccess();
  }

  interface OnRangeValidatedListener {
    void onValidatingRangeError();

    void onValidatingRangeSuccess(double validatedRange);
  }

  boolean isLocationAvailable();

  void getLocation(OnLocationChangedListener listener);

  void validateRange(double range, OnRangeValidatedListener listener);

  double getLatitude();

  double getLongitude();
}
