package com.alejandrosanchez.mbfoursquareapisample.implementations;

import android.app.Activity;
import android.util.Log;

import com.alejandrosanchez.mbfoursquareapisample.interfaces.FoursquareApiEndpoints;
import com.alejandrosanchez.mbfoursquareapisample.interfaces.MapInteractor;
import com.alejandrosanchez.mbfoursquareapisample.models.ResponseWrapper;
import com.alejandrosanchez.mbfoursquareapisample.network.ConnectivitytInterceptor;

import java.io.IOException;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alejandro on 06/09/2016.
 */
public class MapInteractorImpl implements MapInteractor {

  private static final String TAG = "MapInteractorImpl";
  public static final String BASE_URL = "https://api.foursquare.com/v2/";
  private static final String CLIENT_ID = "YOUR_CLIENT_ID";
  private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
  private static final String RELEASE_DATE = "20160907";
  private static final String DEFAULT_QUERY = "gimnasio";
  private static final String INTENT_TYPE = "browse";

  private Retrofit mRetrofit;
  private Activity mActivity;

  public MapInteractorImpl(Activity activity) {
    this.mActivity = activity;
  }

  @Override
  public void getData(double latitude, double longitude, double range, String searchTerm,
                      final OnDataReceivedListener listener) {
    buildRetrofitInstance();
    FoursquareApiEndpoints apiService = mRetrofit.create(FoursquareApiEndpoints.class);

    // Locale.US transforms every "," in a ".", which is necessary for the request.
    String latLng = String.format(Locale.US, "%f,%f", latitude, longitude);
    // If no searchTerm was given, the DEFAULT_QUERY will be used.
    String query = (!searchTerm.isEmpty() ? searchTerm : DEFAULT_QUERY);
    Call<ResponseWrapper> call = apiService.venueList(CLIENT_ID, CLIENT_SECRET, RELEASE_DATE,
                                                      latLng, query, INTENT_TYPE,
                                                      String.valueOf(range));
    call.enqueue(new Callback<ResponseWrapper>() {
      @Override
      public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
        Log.i(TAG, "onResponse called.");

        if (response.isSuccessful()) {
          // The response code was 2XX.
          listener.onDataReceivedSuccess(response.body().getResponse().getVenues());
        }
        else listener.onDataReceivedError();
      }

      @Override
      public void onFailure(Call<ResponseWrapper> call, Throwable t) {
        Log.i(TAG, "onFailure called.");

        if (t instanceof IOException &&
            ConnectivitytInterceptor.NO_INTERNET_ERROR.equals(t.getMessage())) {
          listener.onNoInternetError();
        }
        else listener.onDataReceivedError();
      }
    });
  }

  /**
   * The OKHttpClient is the base for Retrofit and an Interceptor has to be added to it in order to
   * check connectivity.
   */
  public void buildRetrofitInstance() {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new ConnectivitytInterceptor(mActivity))
        .build();

    mRetrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }
}
