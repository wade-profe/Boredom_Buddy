package com.example.android.boredombuddy.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData

class InternetMonitor(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager =
        ContextCompat.getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager

    init {
        isConnected()
    }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        (ContextCompat.getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager).registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        (ContextCompat.getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager).unregisterNetworkCallback(networkCallback)
    }

    private fun isConnected() {
        postValue(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false)
    }
}