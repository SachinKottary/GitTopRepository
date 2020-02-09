package com.sachin.gogit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.sachin.gogit.network.ServerConstants.GIT_TOP_REPO_ENDPOINT;
import static com.sachin.gogit.network.ServerConstants.REQUEST_GET_TOP_GIT_REPOS;

public class NetworkUtils {
    /**
     * checks if internet is present or not
     *
     * @param context To get the connectivity info object
     * @return boolean true if internet present, else false
     */
    public static boolean isInternetPresent(Context context) {
        if (context == null) return false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) return false;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) return networkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     *  Returns the URL endpoint for particular request
     * @param requestType int to identify the endpoint
     * @return string endpoint for the request
     */
    public static String getUrl(int requestType) {
        switch (requestType) {
            case REQUEST_GET_TOP_GIT_REPOS:
                return GIT_TOP_REPO_ENDPOINT;
        }
        return "";
    }

}
