package com.dhruv.angularapps.apps

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppManager @Inject constructor() {

    private val _appsIconData = MutableLiveData<Map<String,Drawable>?>(null)
    val appsIcon: LiveData<Map<String, Drawable>?>
        get() = _appsIconData

    private val _appsData = MutableLiveData<Map<String,String>?>(null)
    val appsData: LiveData<Map<String,String>?>
        get() = _appsData

    private var initialized = false
    fun initialize(packageManager: PackageManager, force: Boolean = false) {
        if (initialized) {
            Log.d(TAG, "already initialized")
            if (force){
                Log.d(TAG, "force initializing")
            }
            else{
                return
            }
        }
        Log.d(TAG, "initializing")
        initialized = true
        _appsIconData.postValue(null)
        _appsData.postValue(null)
        // get all apps
        Log.d(TAG, "initialization: start")
        val main = Intent(Intent.ACTION_MAIN, null)
        main.addCategory(Intent.CATEGORY_LAUNCHER)
        val appsL = packageManager.queryIntentActivities(main, 0)

        CoroutineScope(Dispatchers.IO).launch {
            val newDataAsync = async { getAllAppIcons(packageManager, appsL) }
            val newData = newDataAsync.await()
            _appsIconData.postValue(newData)
            Log.d(TAG, "initialized: apps icons => ${newData.map { it.key }}")
        }

        CoroutineScope(Dispatchers.IO).launch {
            val newDataAsync = async { getAllInstalledAppsData(packageManager, appsL) }
            val newData = newDataAsync.await()
            _appsData.postValue(newData)
            Log.d(TAG, "initialized: apps data => ${newData.map { it.value }}")
        }
    }

    companion object {
        val TAG = "Apps Manager"

        suspend fun getAllInstalledAppsData (
            packageManager: PackageManager,
            appsL: MutableList<ResolveInfo>
        ): Map<String,String> {
            return withContext(Dispatchers.IO){
                val apps: MutableMap<String,String> = mutableMapOf()

                for (app in appsL) {
                    apps[app.activityInfo.packageName] = app.loadLabel(packageManager) as String
                }

                apps
            }
        }

        suspend fun getAllAppIcons (
            packageManager: PackageManager,
            appsL: MutableList<ResolveInfo>
        ): Map<String, Drawable>{
            return withContext(Dispatchers.IO) {
                val icons: MutableMap<String, Drawable> = mutableMapOf()

                for (app in appsL) {
                    if (app.activityInfo.packageName == "com.google.android.googlequicksearchbox") continue
                    icons[app.activityInfo.packageName] = app.loadIcon(packageManager)
                }
                icons
            }
        }
    }
}