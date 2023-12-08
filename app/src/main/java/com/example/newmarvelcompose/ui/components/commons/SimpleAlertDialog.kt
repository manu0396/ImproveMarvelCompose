package com.example.newmarvelcompose.ui.components.commons

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

//At the begining is invisible and show when the user try to remove some hero.
@Composable
fun SimpleAlertDialog(
    hero: String,
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if(show){
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Remove") },
            text = { Text(text = "Would you like to remove ${hero} from your hero's?") }
        )
    }
}
