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

const val MAX_APPS_PER_GROUP = 15
const val MAX_GROUPS = 10

@HiltViewModel
class GroupsEditorVM @Inject constructor(
    private val appManager: AppManager,
    val userPref: UserPref,
) : ViewModel() {
    var showGroupEditingDialog by mutableStateOf(false)
    var errorPop by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var showGroupIconChoices by mutableStateOf(false)

    var selectedGroup by mutableStateOf<Group?>(null)
    var selectedGroupPos by mutableStateOf<Int?>(null)
    var nameValue by mutableStateOf(TextFieldValue())
    var keyValue by mutableStateOf("")
    var groups = userPref.getGroupsFlow()
    var apps by mutableStateOf(emptyMap<String, String>())
    var message by mutableStateOf("")
    var appsIcons by mutableStateOf(emptyMap<String, Drawable>())
    private var selectedApps by mutableStateOf(emptyList<String>())

    var appsCategory by mutableStateOf(emptyMap<String, Int>())

    var showAutoGroupSheet by mutableStateOf(false)
    var suggestions by mutableStateOf(emptyList<AutoGroupBuilder.Suggestion>())
    var selectedSuggestionKeys by mutableStateOf(setOf<String>())

    val selectedAppsForUi: List<String>
        get() = selectedApps

    init {
        appManager.appsData.observeForever {
            apps = it ?: emptyMap()
        }

        appManager.appsIcon.observeForever { iconsMap ->
            appsIcons = iconsMap ?: emptyMap()
        }

        appManager.appsCategory.observeForever { catMap ->
            appsCategory = catMap ?: emptyMap()
        }
    }

    val selectedGroupIcon: Int
        get() = GroupIcons[selectedGroup?.key] ?: R.drawable.round_report_gmailerrorred_24

    fun selectGroup(group: Group, idx: Int) {
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
            apps = emptyList(),
        )
        runBlocking {
            val prev = userPref.getGroups().toMutableList()
            if (prev.size >= MAX_GROUPS) {
                errorPop = true
                errorMessage = "We recommend you to use maximum of $MAX_GROUPS groups for ease of use"
            } else {
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

    fun openAutoGroupSheet() {
        runBlocking {
            val existing = userPref.getGroups()
            val usedKeys = existing.map { it.key }.filter { it.isNotBlank() }.toSet()
            val raw = AutoGroupBuilder.suggest(apps, appsCategory, MAX_APPS_PER_GROUP)
            val filtered = raw.filter { it.group.key !in usedKeys }
            suggestions = filtered
            selectedSuggestionKeys = filtered.map { it.group.key }.toSet()
            showAutoGroupSheet = true
        }
    }

    fun toggleSuggestionKey(key: String) {
        val next = selectedSuggestionKeys.toMutableSet()
        if (!next.add(key)) next.remove(key)
        selectedSuggestionKeys = next
    }

    fun dismissAutoGroupSheet() {
        showAutoGroupSheet = false
        suggestions = emptyList()
        selectedSuggestionKeys = emptySet()
    }

    fun confirmAutoGroups() {
        val chosen = suggestions.filter { it.group.key in selectedSuggestionKeys }
        if (chosen.isEmpty()) {
            dismissAutoGroupSheet()
            return
        }
        runBlocking {
            val prev = userPref.getGroups().toMutableList()
            val room = MAX_GROUPS - prev.size
            if (room <= 0) {
                errorPop = true
                errorMessage =
                    "You already have $MAX_GROUPS groups. Remove some before adding suggestions."
                dismissAutoGroupSheet()
                return@runBlocking
            }
            val toAdd = chosen.take(room)
            for (s in toAdd) {
                prev.add(s.group)
            }
            userPref.saveGroups(prev.toList())
            if (chosen.size > room) {
                errorPop = true
                errorMessage =
                    "Added ${toAdd.size} group(s). ${chosen.size - toAdd.size} could not be added (max $MAX_GROUPS groups total)."
            } else {
                message = "Added ${toAdd.size} group(s)"
            }
            dismissAutoGroupSheet()
        }
    }

    fun isAppSelected(app: String): Boolean {
        return selectedApps.contains(app)
    }

    fun addApp(app: String) {
        val prev = selectedApps.toMutableList()
        if (prev.size >= MAX_APPS_PER_GROUP) {
            errorPop = true
            errorMessage =
                "We recommend adding at most $MAX_APPS_PER_GROUP apps to a group to make them easier to find"
        } else {
            prev.add(app)
            selectedApps = prev.toList()
        }
    }

    fun removeApp(app: String) {
        val prev = selectedApps.toMutableList()
        prev.remove(app)
        selectedApps = prev.toList()
    }

    fun closeErrorPopup() {
        errorPop = false
    }

    fun confirm() {
        runBlocking {
            val prev = userPref.getGroups().toMutableList()
            prev.remove(selectedGroup)
            prev.add(
                selectedGroupPos ?: 0,
                Group(
                    name = nameValue.text,
                    apps = selectedApps,
                    key = keyValue,
                ),
            )
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

    fun dismiss() {
        showGroupEditingDialog = false
    }
}
