package com.example.berlinclock.presentation.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.berlinclock.R
import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.model.Time
import com.example.berlinclock.presentation.ui.theme.BerlinClockTheme
import com.example.berlinclock.presentation.ui.theme.LedOff
import com.example.berlinclock.presentation.ui.theme.LedRed
import com.example.berlinclock.presentation.ui.theme.LedYellow
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel.ClockMode
import org.koin.androidx.compose.koinViewModel

@Composable
fun BerlinClockScreen(modifier: Modifier = Modifier) {
    val viewModel: BerlinClockViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
    ) {
        when (val s = state) {
            is BerlinClockViewModel.State.Content -> {
                BerlinClockContent(
                    berlinClockState = s.berlinClock,
                    formattedTime = s.formattedTime,
                    time = s.time,
                    mode = s.mode,
                    onDynamicTabClick = viewModel::onDynamicTabClick,
                    onStaticTabClick = viewModel::onStaticTabClick,
                    onSubmitStaticTimeClick = viewModel::onSubmitStaticTimeClick
                )
            }

            is BerlinClockViewModel.State.Loading,
            is BerlinClockViewModel.State.NotInitialized ->
                CircularProgressIndicator(Modifier.align(Alignment.Center))

            is BerlinClockViewModel.State.Error ->
                Text(text = s.message, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun BerlinClockContent(
    berlinClockState: BerlinClock,
    formattedTime: String,
    time: Time,
    mode: ClockMode,
    onDynamicTabClick: () -> Unit = {},
    onStaticTabClick: () -> Unit = {},
    onSubmitStaticTimeClick: (Time) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(top = 30.dp)
    ) {
        BerlinClockLeds(berlinClockState = berlinClockState)

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryTabRow(selectedTabIndex = mode.ordinal) {
            Tab(
                selected = mode == ClockMode.DYNAMIC,
                onClick = onDynamicTabClick,
                text = { Text(text = stringResource(R.string.tab_dynamic)) }
            )
            Tab(
                selected = mode == ClockMode.STATIC,
                onClick = onStaticTabClick,
                text = { Text(text = stringResource(R.string.tab_static)) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (mode) {
            ClockMode.DYNAMIC ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = formattedTime,
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Black
                    )
                }

            ClockMode.STATIC ->
                StaticTimeInput(initial = time, onSubmit = onSubmitStaticTimeClick)
        }
    }
}

@Composable
private fun BerlinClockLeds(
    berlinClockState: BerlinClock,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        //seconds
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
                    .clip(CircleShape)
                    .background(
                        color = if (berlinClockState.second) LedYellow else LedOff
                    )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        //hoursBy5
        BerlinClockRow(
            leds = berlinClockState.hoursBy5,
            horizontalSpacing = 10.dp
        ) { _, isOn -> if (isOn) LedRed else LedOff }

        Spacer(modifier = Modifier.height(20.dp))

        //hoursBy1
        BerlinClockRow(
            leds = berlinClockState.hoursBy1,
            horizontalSpacing = 10.dp
        ) { _, isOn -> if (isOn) LedRed else LedOff }

        Spacer(modifier = Modifier.height(20.dp))

        //minutesBy5
        BerlinClockRow(
            leds = berlinClockState.minutesBy5,
            horizontalSpacing = 5.dp
        ) { index, isOn ->
            if (!isOn) {
                LedOff
            } else if (minutesIsRed(index + 1)) {
                LedRed
            } else {
                LedYellow
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        //minutesBy1
        BerlinClockRow(
            leds = berlinClockState.minutesBy1,
            horizontalSpacing = 10.dp
        ) { _, isOn -> if (isOn) LedYellow else LedOff }
    }
}

@Composable
private fun StaticTimeInput(
    initial: Time,
    onSubmit: (Time) -> Unit,
    modifier: Modifier = Modifier
) {
    var hours by rememberSaveable { mutableStateOf(initial.hours.toString()) }
    var minutes by rememberSaveable { mutableStateOf(initial.minutes.toString()) }
    var seconds by rememberSaveable { mutableStateOf(initial.seconds.toString()) }

    val parsedHours = hours.toIntOrNull()
    val parsedMinutes = minutes.toIntOrNull()
    val parsedSeconds = seconds.toIntOrNull()

    val hoursValid = parsedHours != null && parsedHours in 0..23
    val minutesValid = parsedMinutes != null && parsedMinutes in 0..59
    val secondsValid = parsedSeconds != null && parsedSeconds in 0..59
    val allValid = hoursValid && minutesValid && secondsValid

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = hours,
                onValueChange = { hours = it },
                label = { Text(text = stringResource(R.string.hours)) },
                isError = !hoursValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = minutes,
                onValueChange = { minutes = it },
                label = { Text(text = stringResource(R.string.minutes)) },
                isError = !minutesValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = seconds,
                onValueChange = { seconds = it },
                label = { Text(text = stringResource(R.string.seconds)) },
                isError = !secondsValid,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSubmit(Time(parsedHours!!, parsedMinutes!!, parsedSeconds!!)) },
            enabled = allValid
        ) {
            Text(text = stringResource(R.string.submit))
        }
    }
}

@Composable
private fun BerlinClockRow(
    leds: List<Boolean>,
    horizontalSpacing: Dp,
    modifier: Modifier = Modifier,
    ledColor: (index: Int, isOn: Boolean) -> Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        modifier = modifier.fillMaxWidth()
    ) {
        val itemsCount = leds.size
        val outline = MaterialTheme.colorScheme.outline
        leds.forEachIndexed { index, isOn ->
            val itemShape = rowItemShape(index, itemsCount)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(itemShape)
                    .border(2.dp, color = outline, shape = itemShape)
                    .background(color = ledColor(index, isOn))
            )
        }
    }
}

private fun minutesIsRed(i: Int): Boolean = (i != 0) && (i % 3 == 0)

private fun rowItemShape(index: Int, itemCount: Int): Shape {
    val radius = 12.dp
    return when {
        index == 0 -> RoundedCornerShape(topStart = radius, bottomStart = radius)
        index == itemCount - 1 -> RoundedCornerShape(topEnd = radius, bottomEnd = radius)
        else -> RectangleShape
    }
}

@Preview(device = PIXEL_9_PRO_XL, showSystemUi = true)
@Preview(device = PIXEL_9_PRO_XL, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BerlinClockScreenPreview() {
    BerlinClockTheme {
        Scaffold {
            BerlinClockContent(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 15.dp),
                formattedTime = "00:00:00",
                time = Time(hours = 0, minutes = 0, seconds = 0),
                mode = ClockMode.DYNAMIC,
                berlinClockState = BerlinClock(
                    second = true,
                    hoursBy5 = listOf(
                        true,
                        true,
                        false,
                        false
                    ),
                    hoursBy1 = listOf(
                        true,
                        true,
                        false,
                        false
                    ),
                    minutesBy5 = listOf(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                    ),
                    minutesBy1 = listOf(
                        true,
                        true,
                        false,
                        false
                    ),
                )
            )
        }
    }
}
