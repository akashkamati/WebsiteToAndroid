package com.websitetoandroid

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun ExitFromAppDialog(
    onConfirm: ()-> Unit,
    onDismiss : ()-> Unit,

) {

    AlertDialog(
        title = { Text(text = "Do you want to exit from app?") },
        onDismissRequest = {
           onDismiss()
        },
        confirmButton = {
            Text(
                text = "Yes",
                fontSize = 20.sp,
                modifier = Modifier.clickable { onConfirm() })
        },
        dismissButton = {
            Text(
                text = "No",
                fontSize = 20.sp,
                modifier = Modifier.clickable { onDismiss() })
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    )

}