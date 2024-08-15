package com.dhruv.angularapps

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhruv.angularapps.apps.AppManager
import com.dhruv.angularapps.settings_app.SettingsApp
import com.dhruv.angularapps.settings_app.groups.GroupsEditorVM
import com.dhruv.angularapps.settings_app.settings.SettingsVM
import com.dhruv.angularapps.ui.theme.AngularAppsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appManager: AppManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appManager.initialize(this)
        val groupsEditorVM: GroupsEditorVM by viewModels()
        val settingsVM: SettingsVM by viewModels()
        enableEdgeToEdge()
        setContent {

            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(key1 = groupsEditorVM.message)
            {
                if (groupsEditorVM.message.isNotEmpty()) {
                    snackbarHostState.showSnackbar(groupsEditorVM.message, duration = SnackbarDuration.Short)
                    groupsEditorVM.message = ""
                }
            }

            AngularAppsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Text(text = "Angular Apps")
                        })
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                        .padding(4.dp)
                                        .padding(vertical = 30.dp)
                                        .graphicsLayer { shadowElevation = 10f }
                                        .background(color = Color.Black)
                                        .padding(vertical = 10.dp),
                                    text = groupsEditorVM.message,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ){
                        SettingsApp(
                            groupsEditorVM = groupsEditorVM,
                            settingsVM = settingsVM,
                            checkDrawOverlayPermission = { checkDrawOverlayPermission() },
                            enableOverlayPermission = { enableOverlayPermission() },
                            startOverlayService = { startOverlayService() },
                            stopOverlayService = { stopOverlayService() },
                            isOverlayServiceRunning = { isOverlayServiceRunning() },
                        )
                    }
                }
            }
        }
    }

    private fun checkDrawOverlayPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** check if we already  have permission to draw over other apps  */
            if (!Settings.canDrawOverlays(this)) {
                Log.d(TAG, "canDrawOverlays NOK")
                return false
            } else {
                Log.d(TAG, "canDrawOverlays OK")
            }
        }
        return true
    }


    private fun enableOverlayPermission(){
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        stopService(intent)
    }

    private fun isOverlayServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)
        for (service in services) {
            if (OverlayService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object{

        const val TAG: String = "Overlay"
        const val REQUEST_CODE: Int = 1
    }
}