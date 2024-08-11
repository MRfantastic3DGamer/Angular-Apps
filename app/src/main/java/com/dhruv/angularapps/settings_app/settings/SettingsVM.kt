package com.dhruv.angularapps.settings_app.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.dhruv.angularapps.apps.AppsIconsPositioning
import com.dhruv.angularapps.data.UserPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(val pref: UserPref) : ViewModel() {

    var popup               by mutableIntStateOf(0)


    // offset
    var touchOffset         by mutableStateOf(touchOffset())


    // slider
    var slHeight            by mutableIntStateOf(sliderHeight())
    var slWidth             by mutableIntStateOf(sliderWidth())
    var slBottomPadding     by mutableIntStateOf(bottomPadding())


    // apps
    var appsBaseRad         by mutableStateOf(appsBaseRadius())
    var appsSelectionRad    by mutableStateOf(appsSelectionRadius())

    var appsPositioning     by mutableStateOf(appsPositioning())
    var appsPop             by mutableIntStateOf(appsPop())
    val firstRingRadius: Float
        get() = appsPositioning.startingRadius.toFloat()
    val radiusDiff: Float
        get() = appsPositioning.radiusDiff.toFloat()
    val iconsDiff: Float
        get() = appsPositioning.iconDistance.toFloat()


    // group
    var groupBaseRad        by mutableIntStateOf(groupsBaseRadius())
    var groupBasePop        by mutableIntStateOf(groupsBasePop())
    var groupSelectionRad   by mutableIntStateOf(groupsSelectionRadius())
    var groupSelectionPop   by mutableIntStateOf(groupsSelectionPop())


    // region getters and setters
    private fun touchOffset(): Offset {
        val d = runBlocking { pref.getData(touchOffsetKey)?.split("#") ?: listOf("0", "0") }
        return Offset(d[0].toFloat(), d[1].toFloat())
    }

    private fun saveTouchOffset(offset: Offset) {
        runBlocking { pref.saveData(touchOffsetKey, "${offset.x}#${offset.y}") }
    }

    private fun sliderHeight(): Int {
        return runBlocking { pref.getData(sliderHeightKey)?.toInt() ?: 300 }
    }

    private fun saveSliderHeight(height: Int) {
        runBlocking { pref.saveData(sliderHeightKey, height.toString()) }
    }

    private fun sliderWidth(): Int {
        return runBlocking { pref.getData(sliderWidthKey)?.toInt() ?: 50 }
    }

    private fun saveSliderWidth(width: Int) {
        runBlocking { pref.saveData(sliderWidthKey, width.toString()) }
    }

    private fun bottomPadding(): Int {
        return runBlocking { pref.getData(sliderBottomPaddingKey)?.toInt() ?: 50 }
    }

    private fun saveBottomPadding(bottomPadding: Int) {
        runBlocking { pref.saveData(sliderBottomPaddingKey, bottomPadding.toString()) }
    }

    private fun appsBaseRadius(): Int {
        return runBlocking { pref.getData(appsBaseRadiusKey)?.toInt() ?: 30 }
    }

    private fun saveAppsBaseRadius(rad: Int) {
        runBlocking { pref.saveData(appsBaseRadiusKey, rad.toString()) }
    }

    private fun appsSelectionRadius(): Int {
        return runBlocking { pref.getData(appsSelectionRadiusKey)?.toInt() ?: 50 }
    }

    private fun saveAppsSelectionRadius(rad: Int) {
        runBlocking { pref.saveData(appsSelectionRadiusKey, rad.toString()) }
    }

    private fun groupsBaseRadius(): Int {
        return runBlocking { pref.getData(groupsBaseRadiusKey)?.toInt() ?: 50 }
    }

    private fun saveGroupsBaseRadius(rad: Int) {
        runBlocking { pref.saveData(groupsBaseRadiusKey, rad.toString()) }
    }

    private fun groupsSelectionRadius(): Int {
        return runBlocking { pref.getData(groupsSelectionRadiusKey)?.toInt() ?: 70 }
    }

    private fun saveGroupsSelectionRadius(rad: Int) {
        runBlocking { pref.saveData(groupsSelectionRadiusKey, rad.toString()) }
    }

    private fun groupsBasePop(): Int {
        return runBlocking { pref.getData(groupBasePopKey)?.toInt() ?: 30 }
    }

    private fun saveGroupsBasePop(pop: Int) {
        runBlocking { pref.saveData(groupBasePopKey, pop.toString()) }
    }

    private fun groupsSelectionPop(): Int {
        return runBlocking { pref.getData(groupSelectionPopKey)?.toInt() ?: 70 }
    }

    private fun saveGroupsSelectionPop(pop: Int) {
        runBlocking { pref.saveData(groupSelectionPopKey, pop.toString()) }
    }

    private fun appsPop(): Int {
        return runBlocking { pref.getData(appsPopKey)?.toInt() ?: 60 }
    }

    private fun saveAppsPop(pop: Int) {
        runBlocking { pref.saveData(appsPopKey, pop.toString()) }
    }

    private fun appsPositioning(): AppsIconsPositioning.IconCoordinatesGenerationScheme {
        return runBlocking {
            AppsIconsPositioning.IconCoordinatesGenerationScheme.fromString(
                pref.getData(appsPositioningKey)
                    ?: AppsIconsPositioning.IconCoordinatesGenerationScheme().toString()
            )
        }
    }

    private fun saveAppsPositioning(scheme: AppsIconsPositioning.IconCoordinatesGenerationScheme) {
        runBlocking { pref.saveData(appsPositioningKey, scheme.toString()) }
    }
    // endregion

    fun openPopup(pop: Int) {

        // offset
        touchOffset = touchOffset()
        // slider
        slHeight = sliderHeight()
        slWidth = sliderWidth()
        slBottomPadding = bottomPadding()
        // apps
        appsBaseRad = appsBaseRadius()
        appsSelectionRad = appsSelectionRadius()
        appsPositioning = appsPositioning()
        appsPop = appsPop()
        // group
        groupBaseRad = groupsBaseRadius()
        groupBasePop = groupsBasePop()
        groupSelectionRad = groupsSelectionRadius()
        groupSelectionPop = groupsSelectionPop()

        popup = pop
    }

    fun confirm() {
        when (popup) {
            1 -> {
                saveTouchOffset(touchOffset)
            }
            2 -> {
                saveSliderHeight(slHeight)
                saveSliderWidth(slWidth)
                saveBottomPadding(slBottomPadding)
            }
            3 -> {
                saveAppsPositioning(appsPositioning)
                saveAppsPop(appsPop)
                saveAppsBaseRadius(appsBaseRad)
                saveAppsSelectionRadius(appsSelectionRad)
            }
            4 -> {
                saveGroupsBasePop(groupBasePop)
                saveGroupsBaseRadius(groupBaseRad)
                saveGroupsSelectionPop(groupSelectionPop)
                saveGroupsSelectionRadius(groupSelectionRad)
            }
            else -> {}
        }
        popup = 0
    }

    fun dismiss() {
        popup = 0
    }
}


val touchOffsetKey = stringPreferencesKey("touch_app")
val sliderHeightKey = stringPreferencesKey("slider_height")
val sliderWidthKey = stringPreferencesKey("slider_width")
val sliderBottomPaddingKey = stringPreferencesKey("slider_bottom_padding")
val appsBaseRadiusKey = stringPreferencesKey("apps_base_radius")
val appsSelectionRadiusKey = stringPreferencesKey("apps_selection_radius")
val groupsBaseRadiusKey = stringPreferencesKey("groups_base_radius")
val groupsSelectionRadiusKey = stringPreferencesKey("groups_selection_radius")
val groupBasePopKey = stringPreferencesKey("group_base_pop_radius")
val groupSelectionPopKey = stringPreferencesKey("group_selection_pop_radius")
val appsPopKey = stringPreferencesKey("apps_pop")
val appsPositioningKey = stringPreferencesKey("apps_positioning")