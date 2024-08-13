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
import com.dhruv.angularapps.data.models.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val MAX_APPS_PER_GROUP = 10
const val MAX_GROUPS = 6

@HiltViewModel
class GroupsEditorVM @Inject constructor(appManager: AppManager, val userPref: UserPref) : ViewModel() {
    // editor state
    var showGroupEditingDialog      by mutableStateOf                       (false)
    var errorPop                    by mutableStateOf                       (false)
    var errorMessage                by mutableStateOf                       ("")
    var showGroupIconChoices        by mutableStateOf                       (false)
    // editing
    var selectedGroup               by mutableStateOf<Group?>               (null)
    var selectedGroupPos            by mutableStateOf<Int?>                 (null)
    var nameValue                   by mutableStateOf                       (TextFieldValue())
    var keyValue                    by mutableStateOf                       ("")
    var groups                      =                                       userPref.getGroupsFlow()
    var apps                        by mutableStateOf                       (emptyMap<String,String>())
    var message                     by mutableStateOf                       ("")
    var appsIcons                   by mutableStateOf                       (emptyMap<String, Drawable>())
    private var selectedApps        by mutableStateOf                       (emptyList<String>())

    init {
        appManager.appsData.observeForever {
            apps = it ?: emptyMap()
            Log.d(TAG, apps.values.toString())
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
            if (prev.size >= MAX_GROUPS){
                errorPop = true
                errorMessage = "We recommend you to use maximum of $MAX_GROUPS groups for ease of use"
            }
            else{
                showGroupIconChoices = true
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
    }

    fun isAppSelected(app: String): Boolean {
        return selectedApps.contains(app)
    }

    fun addApp(app: String){
        val prev = selectedApps.toMutableList()
        if (prev.size >= MAX_APPS_PER_GROUP){
            errorPop = true
            errorMessage = "We recommend adding at most $MAX_APPS_PER_GROUP apps to a group to make them easier to find"
        }
        else{
            prev.add(app)
            selectedApps = prev.toList()
        }
    }

    fun removeApp(app: String){
        val prev = selectedApps.toMutableList()
        prev.remove(app)
        selectedApps = prev.toList()
    }

    fun closeErrorPopup(){
        errorPop = false
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