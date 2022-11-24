package com.example.notificationplanner.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : DropDownCompatible> DropDownMenu(
    items: List<T>,
    onSelectionChanged: (T) -> Unit,
    placeholder: String? = null
) {
    val width = LocalConfiguration.current.screenWidthDp - (LocalConfiguration.current.screenWidthDp * 0.1)
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(placeholder ?: items.first().getLabelText()) }
    val icon by remember {
        derivedStateOf {
            if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
        }
    }
    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AssistChip(
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            label = { Text(text = selectedText, modifier = Modifier.padding(start = 10.dp)) },
            trailingIcon = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Icon(icon, "contentDescription", modifier = Modifier.padding(end = 10.dp), tint = Color.Gray)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(width.dp),

            ) {
            items.forEach { type ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        selectedText = type.getLabelText()
                        onSelectionChanged(type)
                        expanded = false
                    },
                    text = { Text(text = type.getLabelText()) }
                )
            }
        }
    }
}