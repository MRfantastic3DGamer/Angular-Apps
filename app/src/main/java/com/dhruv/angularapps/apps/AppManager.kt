package com.dhruv.angularapps.apps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.util.TypedValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min


@Singleton
class AppManager @Inject constructor() {

    private val _appsIconData = MutableLiveData<Map<String,Drawable>?>(null)
    val appsIcon: LiveData<Map<String, Drawable>?>
        get() = _appsIconData

    private val _appsData = MutableLiveData<Map<String,String>?>(null)
    val appsData: LiveData<Map<String,String>?>
        get() = _appsData

    private var initialized = false
    fun initialize(context: Context, force: Boolean = false) {
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
        val appsL = context.packageManager.queryIntentActivities(main, 0)
        val newData = getAllAppsDAta(context, appsL)
        _appsData.postValue(newData.first)
        _appsIconData.postValue(newData.second)
    }

    companion object {
        val TAG = "Apps Manager"

        fun getAllAppsDAta(
            context: Context,
            appsL: MutableList<ResolveInfo>,
        ): Pair<Map<String,String>, Map<String, Drawable>> {
            val packageManager = context.packageManager
            val apps: MutableMap<String,String> = mutableMapOf()
            val icons: MutableMap<String, Drawable> = mutableMapOf()

            for (app in appsL) {
                val pkg = app.activityInfo.packageName
                val icon = getAppIcon(packageManager, pkg, context)
                if (icon != null){ icons[pkg] = app.loadIcon(packageManager) }
                apps[pkg] = app.loadLabel(packageManager) as String
            }
            return Pair(apps, icons)
        }

        fun getAppIcon(packageManager: PackageManager, packageName: String, context: Context): Bitmap? {
            try {
                val drawable: Drawable = packageManager.getApplicationIcon(packageName)

                if (drawable is AdaptiveIconDrawable) {
                    val aid = drawable

                    // Retrieve the theme color for monochrome tinting
                    val themeColor = if (android.os.Build.VERSION.SDK_INT >= 33) {
                        val typedValue = TypedValue()
                        context.theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
                        typedValue.data
                    } else {
                        Color.BLACK // Fallback color for pre-API 33
                    }

                    val drr: Array<Drawable?> = if (android.os.Build.VERSION.SDK_INT >= 33) {
                        aid.monochrome?.let {
                            it.setTint(themeColor) // Tint the monochrome layer with the theme color
                            arrayOf(
                                ColorDrawable(Color.GRAY), // Background layer of solid color
                                it
                            )
                        } ?: arrayOf(aid.background, aid.foreground) // Use default layers if monochrome is null
                    } else {
                        arrayOf(aid.background, aid.foreground) // Use default layers for pre-API 33
                    }

                    val layerDrawable = LayerDrawable(drr)

                    val width = layerDrawable.intrinsicWidth
                    val height = layerDrawable.intrinsicHeight

                    var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)

                    layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
                    layerDrawable.draw(canvas)

                    // Clip the bitmap to a circle if needed
                    bitmap = GetBitmapClippedCircle(bitmap)
//                    Log.d(TAG, "getAppIcon: $packageName")
                    return bitmap
                } else if (drawable is BitmapDrawable) {
                    return drawable.bitmap
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return null
        }



        private fun GetBitmapClippedCircle(bitmap: Bitmap): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val path: Path = Path()
            path.addCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                min(width.toDouble(), (height / 2).toDouble()).toFloat(),
                Path.Direction.CCW
            )

            val canvas: Canvas = Canvas(outputBitmap)
            canvas.clipPath(path)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            return outputBitmap
        }

    }
}