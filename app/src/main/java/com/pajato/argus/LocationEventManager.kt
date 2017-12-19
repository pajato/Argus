package com.pajato.argus

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.*
import com.pajato.argus.event.Event
import com.pajato.argus.event.LocationEvent
import com.pajato.argus.event.LocationPermissionEvent
import com.pajato.argus.event.RxBus
import io.reactivex.disposables.Disposable
import java.util.*

object LocationEventManager : Event.EventListener, LocationCallback() {

    private var position: Int = -1
    private lateinit var activity: MainActivity
    private var subscriptions: List<Disposable> = emptyList()

    fun init(activity: MainActivity) {
        this.activity = activity
        subscriptions = listOf(RxBus.subscribeToEventType(LocationEvent::class.java, this),
                RxBus.subscribeToEventType(LocationPermissionEvent::class.java, this))
    }

    fun destroy() {
        subscriptions.forEachIndexed { _, disposable ->
            disposable.dispose()
        }
        subscriptions = emptyList()
    }

    override fun accept(event: Event) {
        when (event) {
            is LocationEvent -> handleLocationEvent(event)
            is LocationPermissionEvent -> handleLocationPermission(event)
        }
    }

    private fun handleLocationEvent(event: LocationEvent) {
        if (event.getLocation() != "")
            return

        position = event.getData()
        requestLocationAccess()
    }

    private fun handleLocationPermission(event: LocationPermissionEvent) {
        if (event.getData()) {
            requestLocationAccess()
        }
    }

    private fun requestLocationAccess() {
        val p = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (p != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            // We always want to prompt the user to let us access their location unless they ask us
            // to never ask them again. This behavior is handled by the requestPermissions() method
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MainActivity.LOCATION_REQUEST_CODE)
        } else {
            val locationReq = LocationRequest.create()
            locationReq.numUpdates = 1
            locationReq.priority = LocationRequest.PRIORITY_LOW_POWER
            val client = FusedLocationProviderClient(activity)
            client.requestLocationUpdates(locationReq, this, null)
        }
    }

    override fun onLocationResult(locationResult: LocationResult) {
        val gcd = Geocoder(activity, Locale.getDefault())
        val location = locationResult.locations[0]
        if (location != null) {
            val addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
            val locality = addresses[0].locality + ", " + addresses[0].adminArea
            Log.v("RECORD_DATE", "found locality for current location: " + locality)
            RxBus.send(LocationEvent(position, locality))
            position = -1
        }
    }
}