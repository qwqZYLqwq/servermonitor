package com.servermonitor.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.servermonitor.MainViewModel

@Composable
fun MainScreen(vm: MainViewModel) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val saveableStateHolder = rememberSaveableStateHolder()

    // 错误弹框：支持一键复制错误信息
    vm.errorMessage?.let { msg ->
        val clipboardManager = LocalClipboardManager.current
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { vm.dismissError() },
            title = { Text("出错了") },
            text = { Text(msg) },
            confirmButton = {
                TextButton(onClick = {
                    clipboardManager.setText(AnnotatedString(msg))
                    Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = null)
                    Text("  复制")
                }
            },
            dismissButton = {
                TextButton(onClick = { vm.dismissError() }) { Text("关闭") }
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "主页") },
                    label = { Text("主页") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.Dns, contentDescription = "服务器") },
                    label = { Text("服务器") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Info, contentDescription = "关于") },
                    label = { Text("关于") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            saveableStateHolder.SaveableStateProvider(selectedTab) {
                when (selectedTab) {
                    0 -> HomeScreen(vm)
                    1 -> ServerScreen(vm)
                    2 -> AboutScreen(vm)
                }
            }
        }
    }
}