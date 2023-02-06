package com.nyc.schools.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.nyc.schools.ui.SingleLiveEvent

open class Connectivity constructor(
    private val connectivityManager: ConnectivityManager,
    private val context: Context
) : SingleLiveEvent<Boolean>() {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager, application.applicationContext
    )

    /** Respond to changes in the default network. Only used on API levels 24+. */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }

        override fun onUnavailable() {
            postValue(false)
        }
    }

    open fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNw = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNw) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ) -> true
            else -> false
        }
    }

    override fun onActive() {
        super.onActive()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}