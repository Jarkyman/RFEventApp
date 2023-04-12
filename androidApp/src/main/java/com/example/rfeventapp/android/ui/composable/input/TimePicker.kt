package com.example.rfeventapp.android.ui.composable.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.rfeventapp.android.R
import com.example.rfeventapp.utils.AppColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onOkClicked: () -> Unit = {},
    startDate: LocalTime = LocalTime.of(10, 30),
) {
    val timeDialog = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = timeDialog,
        backgroundColor = Color(AppColor.backgroundColor),
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle(
                    color = Color(AppColor.textColor)
                ),
                onClick = onOkClicked
            )
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(
                    color = Color(AppColor.textColor)
                )
            )
        }
    ) {
        timepicker(
            is24HourClock = true,
            initialTime = startDate,
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = Color(AppColor.orange),
                //inactiveBackgroundColor = ,
                activeTextColor = Color(AppColor.textColor),
                inactiveTextColor = Color(AppColor.textColor),
                inactivePeriodBackground = Color(AppColor.textColor),
                selectorColor = Color(AppColor.orange),
                selectorTextColor = Color(AppColor.textColor),
                headerTextColor = Color(AppColor.textColor),
                borderColor = Color(AppColor.orange)
            )
        ) { time ->
            val formattedDate = time.format(
                DateTimeFormatter.ofPattern("HH:mm")
            )
            onValueChange(TextFieldValue(formattedDate))
        }
    }
    Row(
        modifier = modifier.clickable { timeDialog.show() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(48.dp).padding(start = 15.dp),
            painter = painterResource(id = R.drawable.clock),
            contentDescription = "clock icon"
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = value.text,
            color = Color(AppColor.textColor),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}