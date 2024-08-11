package com.dhruv.angularapps.data

import androidx.datastore.preferences.core.Preferences
import com.dhruv.angularapps.data.models.Group
import kotlinx.coroutines.flow.Flow

interface UserPref {
    suspend fun saveGroups(groups: List<Group>)
    suspend fun getGroups() : List<Group>
    fun getGroupsFlow() : Flow<List<Group>>
    suspend fun saveData(key: Preferences.Key<String>, data: String)
    suspend fun getData(key: Preferences.Key<String>): String?
}