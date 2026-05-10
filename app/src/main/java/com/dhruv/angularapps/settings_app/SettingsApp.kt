package com.dhruv.angularapps.settings_app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R
import com.dhruv.angularapps.settings_app.groups.GroupsEditor
import com.dhruv.angularapps.settings_app.groups.GroupsEditorVM
import com.dhruv.angularapps.settings_app.settings.Settings
import com.dhruv.angularapps.settings_app.settings.SettingsVM

private data class AppTab(
    val name: String,
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsApp(
    modifier: Modifier = Modifier,
    groupsEditorVM: GroupsEditorVM,
    settingsVM: SettingsVM,
    snackbarHostState: SnackbarHostState,
    checkDrawOverlayPermission: () -> Boolean,
    enableOverlayPermission: () -> Unit,
    startOverlayService: () -> Unit,
    stopOverlayService: () -> Unit,
    isOverlayServiceRunning: () -> Boolean,
) {
    val tabs = listOf(
        AppTab(
            name = "Groups",
            title = "Groups",
            selectedIcon = painterResource(R.drawable.baseline_group_work_24),
            unselectedIcon = painterResource(R.drawable.outline_group_work_24),
        ),
        AppTab(
            name = "Home",
            title = "Home",
            selectedIcon = painterResource(R.drawable.baseline_home_24),
            unselectedIcon = painterResource(R.drawable.outline_home_24),
        ),
        AppTab(
            name = "Settings",
            title = "Settings",
            selectedIcon = painterResource(R.drawable.baseline_settings_24),
            unselectedIcon = painterResource(R.drawable.outline_settings_24),
        ),
    )
    var selectedTab by remember { mutableIntStateOf(1) }

    val haveOverlayPermission = checkDrawOverlayPermission()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scrollConn = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = tabs[selectedTab].title) },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    val selected = selectedTab == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                painter = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.name,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        label = { Text(tab.name) },
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { groupsEditorVM.addNewGroup() },
                    icon = {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    },
                    text = { Text("New group") },
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                (fadeIn() + slideInHorizontally { it / 4 }) togetherWith
                    (fadeOut() + slideOutHorizontally { -it / 4 })
            },
            label = "tabContent",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) { tab ->
            when (tab) {
                0 -> GroupsEditor(
                    modifier = Modifier.fillMaxSize(),
                    vm = groupsEditorVM,
                    snackbarHostState = snackbarHostState,
                    scrollConnection = scrollConn,
                )
                1 -> Home(
                    modifier = Modifier.fillMaxSize(),
                    haveOverlayPermission = haveOverlayPermission,
                    enableOverlayPermission = enableOverlayPermission,
                    startOverlayService = startOverlayService,
                    stopOverlayService = stopOverlayService,
                    isOverlayServiceRunning = isOverlayServiceRunning,
                    scrollConnection = scrollConn,
                )
                2 -> Settings(
                    modifier = Modifier.fillMaxSize(),
                    vm = settingsVM,
                    snackbarHostState = snackbarHostState,
                    haveOverlayPermission = haveOverlayPermission,
                    isOverlayServiceRunning = isOverlayServiceRunning,
                    onStartService = startOverlayService,
                    onStopService = stopOverlayService,
                    scrollConnection = scrollConn,
                )
                else -> Box(Modifier.fillMaxSize())
            }
        }
    }
}
