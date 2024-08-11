package com.dhruv.angularapps.settings_app.groups

import com.dhruv.angularapps.data.UserPref
import com.dhruv.angularapps.data.models.Group
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupsManager @Inject constructor(private val pref: UserPref) {

    private val _allGroups = mutableListOf<Group>()


    fun selectedGroup() : Group {
        TODO("")
    }
}