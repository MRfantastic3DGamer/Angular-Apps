package com.dhruv.angularapps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.OnTouchListener
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.TextView
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.util.lerp
import androidx.core.app.NotificationCompat
import androidx.core.math.MathUtils.clamp
import com.dhruv.angularapps.apps.AppManager
import com.dhruv.angularapps.apps.AppsIconsPositioning
import com.dhruv.angularapps.data.UserPref
import com.dhruv.angularapps.data.models.Group
import com.dhruv.angularapps.settings_app.groups.GroupIcons
import com.dhruv.angularapps.settings_app.settings.appsBaseRadiusKey
import com.dhruv.angularapps.settings_app.settings.appsPopKey
import com.dhruv.angularapps.settings_app.settings.appsPositioningKey
import com.dhruv.angularapps.settings_app.settings.appsSelectionRadiusKey
import com.dhruv.angularapps.settings_app.settings.groupBasePopKey
import com.dhruv.angularapps.settings_app.settings.groupSelectionPopKey
import com.dhruv.angularapps.settings_app.settings.groupsBaseRadiusKey
import com.dhruv.angularapps.settings_app.settings.groupsSelectionRadiusKey
import com.dhruv.angularapps.settings_app.settings.sliderBottomPaddingKey
import com.dhruv.angularapps.settings_app.settings.sliderHeightKey
import com.dhruv.angularapps.settings_app.settings.sliderWidthKey
import com.dhruv.angularapps.settings_app.settings.touchOffsetKey
import com.dhruv.angularapps.views.ItemValues
import com.dhruv.angularapps.views.PositionedLayoutView
import com.dhruv.angularapps.views.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.roundToInt

@AndroidEntryPoint
class OverlayService : Service(), OnTouchListener{
    //dependencies
    @Inject
    lateinit var pref: UserPref
    @Inject
    lateinit var appsManager: AppManager

    // components
    private var wm: WindowManager? = null


    private var slider: SliderView? = null
    private var trigger: View? = null
    private var triggerPrams: WindowManager.LayoutParams? = null

    private var labelText: TextView? = null
    private var labelPrams: WindowManager.LayoutParams? = null

    private var DebugText: TextView? = null
    private var DebugPrams: WindowManager.LayoutParams? = null

    private var appPositionsPreCompute = AppsIconsPositioning.generateIconCoordinates()

    private var groupsPositionedLayoutView: PositionedLayoutView? = null
    private var appsPositionedLayoutView: PositionedLayoutView? = null

    // state
    private var initialY = 0
    private var initialTouchY = 0f
    private var groups = emptyList<Group>()
    private var touchOnSliderSide = false
    private var groupSelection = -1
    private var appSelection = -1

    private var touchOffset = Offset(0f,0f)
    private var sliderHeight = 100
    private var sliderWidthOnActive = 50
    private var sliderBottomPadding = 50
    private var appBaseRadius = 30
    private var appSelectionRadius = 50
    private var groupBaseRadius = 50
    private var groupSelectionRadius = 70
    private var groupBasePop = 30
    private var groupSelectionPop = 70
    private var appsPop = 60
    private var appsPositioning = AppsIconsPositioning.IconCoordinatesGenerationScheme()
    private var appsName = mapOf<String, String>()

    private val perGroupNotchHeight:Int
        get() = dpToPx((sliderHeight.toFloat() / groups.size).roundToInt())

    // animations
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            // update groups
            updateGroups()
            updateSlider()
            handler.postDelayed(this, 8)  // Roughly 60 FPS
        }
    }

    private val groupAnimatedValues = mutableMapOf<Int,Float>()
    private val groupAnimators = mutableMapOf<Int, AnimatedFloat>()
    private var triggerTouchYPos = 0f // updated only while touch is in slider region
    private var sliderVisibility = 0f
    private val sliderVisibilityAnimator = AnimatedFloat(0f, 300L){
        sliderVisibility = it
        Log.d(TAG, "On slider visibility change: $it")
    }

    private val overlayService = this@OverlayService
    private val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onCreate()

        updatesFromPreferences()

        appsManager.initialize(this)
        appsManager.appsData.observeForever { appsName = it ?: emptyMap() }

        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "channel1"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Overlay notification",
                NotificationManager.IMPORTANCE_LOW
            )

            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Overlay Service")
                .setContentText("Overlay running")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

            startForeground(1, notification)
        }

        wm = getSystemService(WINDOW_SERVICE) as WindowManager

        triggerSetUp()
        sliderSetUp()
//        debugSetUp()
        groupsSetUp()
        appsSetUp()
        labelSetUp()

        return START_NOT_STICKY
    }

    private fun updatesFromPreferences(){
        runBlocking {

            val newGroups = pref.getGroups()
            if (groups != newGroups) {
                groups = newGroups
                groupAnimators.clear()
                List(groups.size) { i ->
                    groupAnimators[i] = AnimatedFloat(0f) { groupAnimatedValues[i] = it }
                }
            }

            val touchOff = pref.getData(touchOffsetKey)?.split("#") ?: listOf("0","0")
            touchOffset = Offset(touchOff[0].toFloat(), touchOff[1].toFloat())
            sliderHeight = pref.getData(sliderHeightKey)?.toInt() ?: 300
            sliderWidthOnActive = pref.getData(sliderWidthKey)?.toInt() ?: 50
            sliderBottomPadding = pref.getData(sliderBottomPaddingKey)?.toInt() ?: 50
            appBaseRadius = pref.getData(appsBaseRadiusKey)?.toInt() ?: 30
            appSelectionRadius = pref.getData(appsSelectionRadiusKey)?.toInt() ?: 50
            groupBaseRadius = pref.getData(groupsBaseRadiusKey)?.toInt() ?: 30
            groupSelectionRadius = pref.getData(groupsSelectionRadiusKey)?.toInt() ?: 50
            groupBasePop = pref.getData(groupBasePopKey)?.toInt() ?: 30
            groupSelectionPop = pref.getData(groupSelectionPopKey)?.toInt() ?: 70
            appsPop = pref.getData(appsPopKey)?.toInt() ?: 60

            val positioning = AppsIconsPositioning.IconCoordinatesGenerationScheme.fromString(pref.getData(appsPositioningKey) ?: AppsIconsPositioning.IconCoordinatesGenerationScheme().toString())
            if (positioning != appsPositioning) {
                appsPositioning = positioning
                appPositionsPreCompute = AppsIconsPositioning.generateIconCoordinates(appsPositioning)
            }
        }
    }

    // region set up
    private fun triggerSetUp(){


        trigger = View(this).apply {
            setBackgroundColor(Color.TRANSPARENT)
            setOnTouchListener(overlayService)
            z = sliderZ
        }
        triggerPrams = WindowManager.LayoutParams(
            dpToPx(sliderWidthOnInactive),
            dpToPx(sliderHeight),
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            y = 10000
            gravity = Gravity.END or Gravity.TOP
        }
        wm!!.addView(trigger, triggerPrams)
    }

    private fun sliderSetUp() {
        slider = SliderView(this).apply {
            setOnTouchListener(overlayService)
            z = 1000f
        }
        wm!!.addView(
            slider,
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            )
        )
    }

    private fun debugSetUp(){
        // only for debugging
        DebugText = TextView(this).apply {
            setBackgroundColor(Color.BLUE)
            text = appPositionsPreCompute.iconOffset.toString()
        }
        DebugPrams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        wm!!.addView(DebugText, DebugPrams)
    }

    private fun groupsSetUp(){
        // groups
        groups = runBlocking { pref.getGroups() }
        List(groups.size) { i ->
            groupAnimators[i] = AnimatedFloat(0f){ groupAnimatedValues[i] = it }
        }

        groupsPositionedLayoutView = PositionedLayoutView(this).apply {
            z = groupsZ
            visibility = GONE
            setBackgroundColor(Color.TRANSPARENT)
            setOnTouchListener(this@OverlayService)
        }
        wm!!.addView(groupsPositionedLayoutView, WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        })
    }

    private fun appsSetUp(){
        appsPositionedLayoutView = PositionedLayoutView(this).apply {
            z = appsZ
            visibility = GONE
            setBackgroundColor(Color.TRANSPARENT)
            setOnTouchListener(this@OverlayService)
        }
        wm!!.addView(appsPositionedLayoutView, WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        })
    }

    private fun labelSetUp(){
        labelText = TextView(this).apply {
            z = labelZ
            textSize = 30f
            text = ""
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.BLACK)
        }
        labelPrams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        wm!!.addView(labelText, labelPrams)
    }
    // endregion

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {

                    updatesFromPreferences()

                    trigger?.apply { visibility = VISIBLE }
                    slider?.apply { visibility = VISIBLE }
                    sliderVisibilityAnimator.setTargetValue(1f)
                    groupsPositionedLayoutView?.apply {
                        visibility = VISIBLE
                        val resources = resources
                        updateVisuals(groups.map { it.key }){ key ->
                            val drawable = Drawable.createFromXml(resources, resources.getXml( GroupIcons[key] ?: R.drawable.round_report_gmailerrorred_24))
                            drawable
                        }
                    }
                    appsPositionedLayoutView?.apply {
                        visibility = VISIBLE
                        updateVisuals(
                            (appsManager.appsIcon.value?.keys?.toList() ?: emptyList())
                        ) { key ->
                            val drawable = appsManager.appsIcon.value?.getOrDefault(key, null)
                            if (drawable == null) { Log.d(TAG, "onTouch: drawable is null for $key") }
                            drawable
                        }
                    }

                    initialY = triggerPrams!!.y
                    initialTouchY = initialY.toFloat()

                    handler.post(updateRunnable)
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    triggerPrams?.apply {
                        y = resources.displayMetrics.heightPixels - height - sliderBottomPadding ; width = dpToPx(sliderWidthOnInactive);
                    }
                    wm!!.updateViewLayout(trigger, triggerPrams)
                    groupsPositionedLayoutView?.apply { visibility = GONE }
                    appsPositionedLayoutView?.apply { visibility = GONE }
                    sliderVisibilityAnimator.setTargetValue(0f)
                    slider?.apply { visibility = GONE }
                    labelText?.apply { visibility = GONE }
                    handler.removeCallbacks(updateRunnable)
                    launchAppIfPossible(touchOnSliderSide)
                }
                MotionEvent.ACTION_MOVE -> {

                    val X = event.rawX + touchOffset.x
                    val Y = event.rawY + touchOffset.y

                    touchOnSliderSide = X >= resources.displayMetrics.widthPixels - dpToPx(sliderWidthOnActive)
                    val touchPositionOnSlider = Y.roundToInt() - triggerPrams!!.y
                    if (touchOnSliderSide) {
                        updateTrigger(Y)
                        triggerTouchYPos = Y
                    }

                    if (touchOnSliderSide) {
                        groupSelection = clamp(touchPositionOnSlider/perGroupNotchHeight, 0, groups.size-1)
                    }
                    val selectedGroupY = triggerPrams!!.y + ((groupSelection + 0.5f) * perGroupNotchHeight).roundToInt()
                    val selectedGroupOffset = Offset(
                        resources.displayMetrics.widthPixels - dpToPx(appsPop).toFloat(),
                        triggerTouchYPos
                    )

                    if (groups.isNotEmpty() && groupSelection != -1){

//                        updateGroups()
                        val usableData = runBlocking {
                            AppsIconsPositioning.getUsableOffsets(
                                allOffsets = appPositionsPreCompute.iconOffset,
                                center = selectedGroupOffset,
                                count = groups[groupSelection].apps.size,
                                sliderPosY = triggerPrams!!.y.toFloat(),
                                sliderHeight = sliderHeight.toFloat(),
                                right = resources.displayMetrics.widthPixels - dpToPx(sliderWidthOnActive),
                                left = dpToPx(100),
                                Top = dpToPx(100).toFloat(),
                                Bot = resources.displayMetrics.heightPixels - dpToPx(100).toFloat(),
                                LabelHeight = 0f,
                            )
                        }
                        val touchOffset = Offset(X, Y)
                        val usedOffsets = usableData.offsets

                        val appSelectionResult = AppsIconsPositioning.getIconSelection(touchOffset, usedOffsets, 200f)
                        appSelection = appSelectionResult.selectedApp
                        updateApps(usedOffsets, appSelectionResult.selectionValues)
                    }

                    updateSlider()
                    updateGroups()
                    updateLabel()


                    // debugging
//                    DebugText!!.text = touchOnSliderSide.toString()
                }
            }
        }
        return true
    }

    // region updation
    private fun updateTrigger(touchY: Float) {
        val yOffset = (touchY - initialTouchY).roundToInt()
        if (initialY + yOffset < triggerPrams!!.y){
            triggerPrams!!.y = initialY + yOffset
        }
        else if (initialY + yOffset > triggerPrams!!.y + dpToPx(sliderHeight)){
            triggerPrams!!.y = initialY + yOffset - dpToPx(sliderHeight)
        }
        wm!!.updateViewLayout(trigger, triggerPrams)
    }

    private fun updateSlider() {
        slider?.apply {
            val currentWidth = dpToPxF(sliderWidthOnActive) * sliderVisibility
            updateVisuals(
                Offset(width.toFloat() - currentWidth, triggerPrams!!.y.toFloat()),
                selectionPos = triggerTouchYPos - triggerPrams!!.y,
                width = currentWidth,
                height = dpToPxF(sliderHeight),
                radius = 125f * sliderVisibility,
                selectionPop = 60f * sliderVisibility,
                vertexCount = 20
            )
        }
    }

    private fun updateGroups() {
        groupsPositionedLayoutView?.apply {
            updateValues(groups.size) { index ->
                val selected = index == groupSelection

                groupAnimators[index]?.setTargetValue(if (selected) 1f else 0f)

                val transitionValue = groupAnimatedValues[index] ?: if (selected) 1f else 0f
                ItemValues(
                    dpToPxF(lerp(groupBaseRadius, groupSelectionRadius, transitionValue)),
                    Offset(
                        x = resources.displayMetrics.widthPixels - lerp(
                            dpToPxF(sliderWidthOnActive)*0.5f,
                            dpToPxF(groupSelectionPop),
                            transitionValue
                        ),
                        y = triggerPrams!!.y + lerp(
                            ((index + 0.5f) * perGroupNotchHeight) - groupBaseRadius / 2,
                            triggerTouchYPos - triggerPrams!!.y,
                            transitionValue
                        )
                    ),
                    groups[index].key
                )

            }
        }
    }

    private fun updateApps(usedOffsets: List<Offset>, appSelectionValues: Map<Int, Float>) {

        val selectedGroup = groups[groupSelection]
        val selectedGroupApps = selectedGroup.apps

        appsPositionedLayoutView?.apply {
            updateValues(min(selectedGroupApps.size, usedOffsets.size)){ index ->
                ItemValues(
                    radius = lerp(dpToPxF(appBaseRadius), dpToPxF(appSelectionRadius), appSelectionValues.getOrDefault(index, 0f)),
                    offset = usedOffsets[index],
                    key = selectedGroupApps[index],
                )
            }
        }
    }

    private fun updateLabel() {
        if (touchOnSliderSide or (groupSelection !in groups.indices)) {
            labelText?.apply { visibility = GONE }
            return
        }
        val selectedGroup = groups[groupSelection]
        if (appSelection !in selectedGroup.apps.indices) {
            labelText?.apply { visibility = GONE }
            return
        }
        val selectedApp = selectedGroup.apps[appSelection]
        labelText?.apply {
            visibility = VISIBLE
            text = appsName[selectedApp]
        }
        labelPrams?.apply {
            y = triggerPrams!!.y - this.height
        }
        wm!!.updateViewLayout(labelText, labelPrams)
    }
    // endregion

    private fun launchAppIfPossible(touchOnSliderSide: Boolean){

        // if possible
        if (touchOnSliderSide or (groupSelection !in groups.indices)) return
        val selectedGroup = groups[groupSelection]
        if (appSelection !in selectedGroup.apps.indices) return
        val selectedApp = selectedGroup.apps[appSelection]

        // launch
        val launchIntent = packageManager.getLaunchIntentForPackage(selectedApp)
        println("got intent for ${selectedApp}: ${launchIntent}")
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
        println("launched app ${selectedApp}")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (trigger != null) {
            wm!!.removeView(trigger); trigger = null
        }
        if (appsPositionedLayoutView != null){
            wm!!.removeView(appsPositionedLayoutView); appsPositionedLayoutView = null
        }
        if (groupsPositionedLayoutView != null){
            wm!!.removeView(groupsPositionedLayoutView); groupsPositionedLayoutView = null
        }
        if (labelText != null){
            wm!!.removeView(labelText); labelText = null
        }
        if (DebugText != null){
            wm!!.removeView(DebugText); DebugText = null
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun dpToPxF(dp: Int): Float {
        return (dp * resources.displayMetrics.density)
    }

    companion object {
        private const val TAG = "Overlay Service"

        private const val sliderWidthOnInactive = 10

        private const val sliderZ = -10f

        private const val groupsZ = 0f

        private const val MAX_APPS = 20
        private const val appsZ = 10f

        private const val labelZ = 20f
    }
}