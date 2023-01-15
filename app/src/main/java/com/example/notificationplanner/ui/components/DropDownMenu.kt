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
    modifier: Modifier = Modifier,
    items: List<T>,
    onSelectionChanged: (T) -> Unit,
    placeholder: String? = "Choose",
    selected: T? = null

) {
    val width = LocalConfiguration.current.screenWidthDp - (LocalConfiguration.current.screenWidthDp * 0.2)
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selected?.getLabelText() ?: placeholder) }
    val icon by remember {
        derivedStateOf {
            if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AssistChip(
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            label = { Text(text = selectedText.toString(), modifier = Modifier.padding(start = 5.dp)) },
            trailingIcon = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Icon(icon, "contentDescription", modifier = Modifier.padding(end = 10.dp), tint = Color.Gray)
                }
            },
            border = AssistChipDefaults.assistChipBorder(
                borderColor = Color.White,
                borderWidth = 1.dp,
            )
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