package com.mymusic.util

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun ProgressBarComposable(enabled: Boolean) {
    if (enabled) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ButtonComposable(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .width(180.dp)
            .height(50.dp),
        content = {
            Text(
                text = text.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}

@Composable
fun TextButtonComposable(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        content = {
            Text(
                text = text.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}

@Composable
fun SpacerComposable(i: Dp) {
    Spacer(modifier = Modifier.size(i))
}

@Composable
fun TopAppBarComposable(
    title: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onNavigateUpClicked: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                content = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                },
                enabled = enabled,
                onClick = onNavigateUpClicked
            )
        },
        backgroundColor = Color.White,
        title = {
            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun TopAppBarComposable(
    title: String
) {
    TopAppBar(
        backgroundColor = Color.White,
        title = {
            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}


@Composable
fun DarkTopAppBarComposable(
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}


@Composable
fun DarkTopAppBarComposable(
    title: String,
    actionIcon: ImageVector,
    onActionClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(
                content = {
                    Icon(imageVector = actionIcon, contentDescription = null)
                },
                onClick = onActionClick
            )
        }
    )
}

@Composable
fun TextFieldComposable(
    value: String,
    label: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector
) {
    TextField(
        label = {
            Text(text = label)
        },
        value = value,
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null)
        },
        enabled = enabled,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

@Composable
fun TextFieldPasswordComposable(
    value: String,
    label: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    TextField(
        label = {
            Text(text = label)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.VpnKey, contentDescription = null)
        },
        trailingIcon = {
            IconToggleButton(
                checked = visible,
                onCheckedChange = { b -> visible = b },
                content = {
                    Icon(
                        imageVector = if (visible) {
                            Icons.Default.ToggleOn
                        } else {
                            Icons.Default.ToggleOff
                        },
                        contentDescription = null
                    )
                }
            )
        },
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        visualTransformation = if (visible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

@Composable
fun RadioGroupComposable(
    enabled: Boolean,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    radioOptions: List<String>
) {
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .selectable(
                        enabled = enabled,
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}


@Composable
fun DatePickerComposable(
    context: Context,
    onDateSelected: (String) -> Unit,
    enabled: Boolean,
    onDismiss: () -> Unit
) {
    val dialog = remember {
        val now = Calendar.getInstance()
        val year1 = 2000
        val month1 = 0
        val day1 = 1
        val dialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth)
                onDateSelected("$dayOfMonth/${month + 1}/$year")
            },
            year1,
            month1,
            day1
        )
        onDateSelected("$day1/${month1 + 1}/$year1")
        dialog.datePicker.maxDate = now.timeInMillis - 315360000000 // 10 Year
        dialog.datePicker.minDate = now.timeInMillis - 3153600000000 // 100 Year
        dialog.setOnDismissListener { onDismiss() }
        dialog.setOnCancelListener { onDismiss() }
        dialog
    }
    if (enabled) {
        dialog.show()
    } else {
        dialog.hide()
    }
}


@Composable
fun TextFieldButtonComposable(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = value,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}


@Composable
fun TextFieldButtonComposable(icon: ImageVector, title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = value,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}



@Composable
fun DialogButtonComposable(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text.uppercase(), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun DialogTextComposable(text: String) {
    Text(text = text, fontSize = 16.sp, color = Color.Black)
}

@Composable
fun DialogBoldTextComposable(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}