package com.alejandrosanchez.mbfoursquareapisample.interfaces;

import com.alejandrosanchez.mbfoursquareapisample.models.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alejandro on 07/09/2016.
 */
public interface FoursquareApiEndpoints {

  @GET("venues/search")
  Call<ResponseWrapper> venueList(@Query("client_id") String clientId,
                                  @Query("client_secret") String clientSecret,
                                  @Query("v") String releaseDate, @Query("ll") String latLng,
                                  @Query("query") String query, @Query("intent") String intentType,
                                  @Query("radius") String radius);
}
