package com.dhruv.angularapps.settings_app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.dhruv.angularapps.R
import com.dhruv.angularapps.settings_app.groups.GroupsEditor
import com.dhruv.angularapps.settings_app.groups.GroupsEditorVM
import com.dhruv.angularapps.settings_app.settings.Settings
import com.dhruv.angularapps.settings_app.settings.SettingsVM

data class Tab(
    val name: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsApp(
    modifier: Modifier = Modifier,
    groupsEditorVM: GroupsEditorVM,
    settingsVM: SettingsVM,
    checkDrawOverlayPermission: ()->Boolean,
    enableOverlayPermission: ()->Unit,
    startOverlayService: ()->Unit,
    stopOverlayService: ()->Unit,
    isOverlayServiceRunning: ()->Boolean,
) {
    val tabs = listOf(
        Tab(
            "groups",
            painterResource(R.drawable.baseline_group_work_24),
            painterResource(R.drawable.outline_group_work_24),
        ),
        Tab(
            "home",
            painterResource(R.drawable.baseline_home_24),
            painterResource(R.drawable.outline_home_24),
        ),
        Tab(
            "Settings",
            painterResource(R.drawable.baseline_settings_24),
            painterResource(R.drawable.outline_settings_24),
        )
    )
    var selectedTab by remember { mutableIntStateOf(1) }
    val pagerState = rememberPagerState { tabs.size }

    LaunchedEffect(selectedTab) {
        pagerState.scrollToPage(selectedTab)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier.fillMaxSize()
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed() { index, tab ->
                val selected = selectedTab == index
                Tab(
                    selected = selected,
                    text = { Text(text = tab.name) },
                    icon = {
                        Icon(
                            painter = if (selected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.name
                        )
                    },
                    onClick = {
                        selectedTab = index
                    }
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            val haveOverlayPermission = checkDrawOverlayPermission()
            when (page) {
                0 -> GroupsEditor(Modifier.fillMaxSize(), vm = groupsEditorVM)
                1 -> Home(
                    haveOverlayPermission = haveOverlayPermission,
                    enableOverlayPermission = enableOverlayPermission,
                    startOverlayService = startOverlayService,
                    stopOverlayService = stopOverlayService,
                    isOverlayServiceRunning = isOverlayServiceRunning,
                )
                2 -> Settings(Modifier.fillMaxSize(), vm = settingsVM)
                else -> {
                    Box(modifier = Modifier.fillMaxSize()){
                        Text(text = "This page have not been designed yet", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}