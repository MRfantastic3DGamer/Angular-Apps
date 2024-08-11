package com.dhruv.angularapps.settings_app.settings

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

    fun touchOffset() : Offset {
        val d = runBlocking { pref.getData(touchOffsetKey)?.split("#") ?: listOf("0","0") }
        return Offset(d[0].toFloat(), d[1].toFloat())
    }
    fun saveTouchOffset(offset: Offset){
        runBlocking { pref.saveData(touchOffsetKey, "${offset.x}#${offset.y}") }
    }

    fun sliderHeight() : Int {
        return runBlocking { pref.getData(sliderHeightKey)?.toInt() ?: 300 }
    }
    fun saveSliderHeight(height: Int){
        runBlocking { pref.saveData(sliderHeightKey, height.toString()) }
    }

    fun sliderWidth() : Int {
        return runBlocking { pref.getData(sliderWidthKey)?.toInt() ?: 50 }
    }
    fun saveSliderWidth(width: Int){
        runBlocking { pref.saveData(sliderWidthKey, width.toString()) }
    }

    fun bottomPadding() : Int{
        return runBlocking { pref.getData(sliderBottomPaddingKey)?.toInt() ?: 50 }
    }
    fun saveBottomPadding(bottomPadding: Int) {
        runBlocking { pref.saveData(sliderBottomPaddingKey, bottomPadding.toString()) }
    }

    fun appsBaseRadius() : Int{
        return runBlocking { pref.getData(appsBaseRadiusKey)?.toInt() ?: 30 }
    }
    fun saveAppsBaseRadius(rad: Int){
        runBlocking { pref.saveData(appsBaseRadiusKey, rad.toString()) }
    }

    fun appsSelectionRadius() : Int{
        return runBlocking { pref.getData(appsSelectionRadiusKey)?.toInt() ?: 50 }
    }
    fun saveAppsSelectionRadius(rad: Int){
        runBlocking { pref.saveData(appsSelectionRadiusKey, rad.toString()) }
    }

    fun groupsBaseRadius() : Int{
        return runBlocking { pref.getData(groupsBaseRadiusKey)?.toInt() ?: 50 }
    }
    fun saveGroupsBaseRadius(rad: Int){
        runBlocking { pref.saveData(groupsBaseRadiusKey, rad.toString()) }
    }

    fun groupsSelectionRadius() : Int{
        return runBlocking { pref.getData(groupsSelectionRadiusKey)?.toInt() ?: 70 }
    }
    fun saveGroupsSelectionRadius(rad: Int){
        runBlocking { pref.saveData(groupsSelectionRadiusKey, rad.toString()) }
    }

    fun groupsBasePop() : Int{
        return runBlocking { pref.getData(groupBasePopKey)?.toInt() ?: 30 }
    }
    fun saveGroupsBasePop(pop: Int){
        runBlocking { pref.saveData(groupBasePopKey, pop.toString()) }
    }

    fun groupsSelectionPop() : Int{
        return runBlocking { pref.getData(groupSelectionPopKey)?.toInt() ?: 70 }
    }
    fun saveGroupsSelectionPop(pop: Int){
        runBlocking { pref.saveData(groupSelectionPopKey, pop.toString()) }
    }

    fun appsPop(): Int{
        return runBlocking { pref.getData(appsPopKey)?.toInt() ?: 60 }
    }
    fun saveAppsPop(pop: Int) {
        runBlocking { pref.saveData(appsPopKey, pop.toString()) }
    }

    fun appsPositioning(): AppsIconsPositioning.IconCoordinatesGenerationScheme {
        return runBlocking {
            AppsIconsPositioning.IconCoordinatesGenerationScheme.fromString(
                pref.getData(appsPositioningKey) ?: AppsIconsPositioning.IconCoordinatesGenerationScheme().toString()
            )
        }
    }
    fun saveAppsPositioning(scheme: AppsIconsPositioning.IconCoordinatesGenerationScheme) {
        runBlocking { pref.saveData(appsPositioningKey, scheme.toString()) }
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