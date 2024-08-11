package com.dhruv.angularapps.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dhruv.angularapps.data.models.Group
import com.dhruv.angularapps.data.models.Group.Companion.toStr
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPrefImpl(private val dataStore: DataStore<Preferences>) : UserPref {
    override suspend fun saveGroups(groups: List<Group>) {
        dataStore.edit {
            val groupsData = groups.joinToString(separator = BR5) { group -> group.toStr() }
            Log.d(TAG, "groups data => " + groupsData)
            it[k_Group] = groupsData
        }
    }

    override suspend fun getGroups(): List<Group> {
        val groups = mutableListOf<Group>()
        dataStore.data.catch {
            Log.d(TAG, "throwing: $it")
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[k_Group]?.let {
                Log.d(TAG, "Got from store => " + it)
                if (it.trim() != "") {
                    groups.addAll(
                        it.split(BR5).map { Group.fromStr(it) }
                    )
                }
            }
        }.first()
        return groups
    }

    override fun getGroupsFlow(): Flow<List<Group>> {
        return dataStore.data.map {
            val data = it[k_Group]
            if (data == null || data.trim() == ""){
                emptyList()
            }
            else {
                data.splitToSequence(BR5).map { Group.fromStr(it) }.toList()
            }
        }
    }

    override suspend fun saveData(key: Preferences.Key<String>, data: String) {
        dataStore.edit {
            it[key] = data
        }
    }

    override suspend fun getData(key: Preferences.Key<String>): String? {
        var v : String? = ""
        dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
             v = preferences[key]
        }.first()
        return v
    }

    companion object {
        val TAG = "User Pref"
        val k_Group = stringPreferencesKey("Groups")
    }
}