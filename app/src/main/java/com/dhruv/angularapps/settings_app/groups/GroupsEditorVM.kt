package com.dhruv.angularapps.settings_app.groups

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.dhruv.angularapps.R
import com.dhruv.angularapps.apps.AppManager
import com.dhruv.angularapps.data.UserPref
import com.dhruv.angularapps.data.models.AppData
import com.dhruv.angularapps.data.models.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class GroupsEditorVM @Inject constructor(appManager: AppManager, val userPref: UserPref) : ViewModel() {
    // editor state
    var showGroupEditingDialog      by mutableStateOf                       (false)
    var showGroupIconChoices        by mutableStateOf                       (false)
    // editing
    var selectedGroup               by mutableStateOf<Group?>               (null)
    var selectedGroupPos            by mutableStateOf<Int?>                 (null)
    var nameValue                   by mutableStateOf                       (TextFieldValue())
    var keyValue                    by mutableStateOf                       ("")
    var groups                      =                                       userPref.getGroupsFlow()
    var apps                        by mutableStateOf                       (emptyList<AppData>())
    var message                     by mutableStateOf                       ("")
    var appsIcons                   by mutableStateOf                       (emptyMap<String, Drawable>())
    private var selectedApps        by mutableStateOf                       (emptyList<AppData>())

    init {
        appManager.appsData.observeForever {
            apps = it ?: emptyList()
            Log.d("Apps Manager in VM", apps.map { it.packageName }.toList().toString())
        }

        appManager.appsIcon.observeForever { iconsMap ->
            appsIcons = iconsMap ?: emptyMap()
        }
    }

    val selectedGroupIcon : Int
        get() = GroupIcons[selectedGroup?.key] ?: R.drawable.round_report_gmailerrorred_24

    fun selectGroup (group: Group, idx: Int) {
        selectedGroup = group
        selectedGroupPos = idx
        selectedApps = group.apps
        nameValue = TextFieldValue(selectedGroup!!.name)
        keyValue = group.key
        showGroupEditingDialog = true
    }

    fun addNewGroup() {
        val g = Group(
            name = "new group",
            apps = emptyList()
        )
        runBlocking {
            val prev = userPref.getGroups().toMutableList()
            Log.d("User Pref", "adding new to : " + prev.toString())
            prev.add(g)
            Log.d("User Pref", "new list : " + prev.toString())
            userPref.saveGroups(prev.toList())
            selectedGroup = g
            selectedGroupPos = 0
            selectedApps = emptyList()
            nameValue = TextFieldValue(g.name)
            keyValue = g.key
            showGroupEditingDialog = true
        }
    }

    fun isAppSelected(app: AppData): Boolean {
        return selectedApps.firstOrNull{it.packageName == app.packageName} != null
    }

    fun addApp(app: AppData){
        val prev = selectedApps.toMutableList()
        prev.add(app)
        selectedApps = prev.toList()
    }

    fun removeApp(app: AppData){
        val prev = selectedApps.toMutableList()
        prev.remove(app)
        selectedApps = prev.toList()
    }

    fun confirm() {
        runBlocking {
            val prev = userPref.getGroups().toMutableList()
            prev.remove(selectedGroup)
            prev.add(selectedGroupPos?:0, Group(
                name = nameValue.text,
                apps = selectedApps,
                key = keyValue
            ))
            userPref.saveGroups(prev.toList())
            showGroupEditingDialog = false
        }
    }

    fun delete() {
        runBlocking {
            val prev = userPref.getGroups().toMutableList()
            prev.remove(selectedGroup)
            userPref.saveGroups(prev.toList())
            showGroupEditingDialog = false
        }
    }

    fun dismiss (){
        showGroupEditingDialog = false
    }

}