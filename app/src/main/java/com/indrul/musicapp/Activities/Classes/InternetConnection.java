package com.indrul.musicapp.Activities.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class InternetConnection {



        /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
        public static boolean checkConnection(Context context) {
            if (context != null) {
                ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectionManager == null) {
                    return false;
                }
                NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    return false;
                }
                return networkInfo.isConnected();
            } else {
                return false;
            }
        }
    }


