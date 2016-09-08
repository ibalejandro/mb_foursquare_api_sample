package com.alejandrosanchez.mbfoursquareapisample.implementations;

import com.alejandrosanchez.mbfoursquareapisample.interfaces.LocationSetupInteractor;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.LocationSetupPresenter;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.LocationSetupView;

/**
 * Created by Alejandro on 06/09/2016.
 */
public class LocationSetupPresenterImpl implements LocationSetupPresenter,
    LocationSetupInteractor.OnLocationChangedListener,
    LocationSetupInteractor.OnRangeValidatedListener {

  private LocationSetupView mLocationSetupView;
  private LocationSetupInteractor mLocationSetupInteractor;

  public LocationSetupPresenterImpl(LocationSetupView locationSetupView) {
    this.mLocationSetupView = locationSetupView;
    this.mLocationSetupInteractor = new LocationSetupInteractorImpl(mLocationSetupView
                                                                    .getActivity());
  }

  /**
   * If the location is available, the validation of the range begins. Otherwise, the getting
   * location process starts.
   */
  @Override
  public void decideAction() {
    if (mLocationSetupView != null) {
      if (mLocationSetupInteractor.isLocationAvailable()) {
        try {
          double range = Double.parseDouble(mLocationSetupView.getRange());
          mLocationSetupInteractor.validateRange(range, this);
        }
        catch (NumberFormatException nfe){
          // The range cannot be converted to a double.
          mLocationSetupView.setRangeError();
        }
      }
      else {
        mLocationSetupView.showGettingLocation();
        mLocationSetupInteractor.getLocation(this);
      }
    }
  }

  @Override
  public void onDestroy() {
    mLocationSetupView = null;
  }

  @Override
  public void onGettingLocationError() {
    if (mLocationSetupView != null) mLocationSetupView.showRetry();
  }

  @Override
  public void onGpsDisabledError() {
    if (mLocationSetupView != null) mLocationSetupView.showEnableGpsSettings();
  }

  @Override
  public void onGettingLocationSuccess() {
    if (mLocationSetupView != null) mLocationSetupView.showGoToMap();
  }

  @Override
  public void onValidatingRangeError() {
    if (mLocationSetupView != null) mLocationSetupView.setRangeError();
  }

  /**
   * If the validation of the range was successful, the user can proceed to the map.
   *
   * @param validatedRange double The LocationSetupInteractor validates a range returns it
   */
  @Override
  public void onValidatingRangeSuccess(double validatedRange) {
    if (mLocationSetupView != null) {
      mLocationSetupView.navigateToMap(mLocationSetupInteractor.getLatitude(),
                                       mLocationSetupInteractor.getLongitude(), validatedRange,
                                       mLocationSetupView.getSearchTerm());
    }
  }
}
