package com.alejandrosanchez.mbfoursquareapisample.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Alejandro on 07/09/2016.
 */
public class ConnectivitytInterceptor implements Interceptor {

  public static final String NO_INTERNET_ERROR = "NO_INTERNET_CONNECTION";

  private Context mContext;

  public ConnectivitytInterceptor(Context context) {
    this.mContext = context;
  }

  /**
   * Intercepts the outgoing request to check the Internet connection.
   *
   * @param chain Chain Manager for requests going out and their corresponding responses
   * @return Response Request's response
   * @throws IOException No Internet connection was found
   */
  @Override
  public Response intercept(Chain chain) throws IOException {
    if (!isInternetAvailable()) throw new IOException(NO_INTERNET_ERROR);

    return chain.proceed(chain.request());
  }

  private boolean isInternetAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) mContext
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }
}
