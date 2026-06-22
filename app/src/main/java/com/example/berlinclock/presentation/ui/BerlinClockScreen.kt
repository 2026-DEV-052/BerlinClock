package com.example.berlinclock.presentation.ui

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.berlinclock.domain.model.BerlinClock
import com.example.berlinclock.domain.usecase.ConvertTimeToBerlinClockUseCase
import com.example.berlinclock.presentation.viewmodel.BerlinClockViewModel
import java.time.LocalTime

@Composable
fun BerlinClockScreen(modifier: Modifier = Modifier) {
    val viewModel = remember {
        BerlinClockViewModel(
            convertTimeToBerlinClock = ConvertTimeToBerlinClockUseCase()
        )
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.init() }

    Box(
        modifier = modifier
    ) {
        when (val s = state.value) {
            is BerlinClockViewModel.State.Content -> {
                BerlinClockComposition(currentTime = s.time, berlinClockState = s.berlinClock)
            }

            is BerlinClockViewModel.State.Error -> {}
            is BerlinClockViewModel.State.Initialized -> {}
            is BerlinClockViewModel.State.Loading -> {}
        }
    }
}

@Composable
fun BerlinClockComposition(
    modifier: Modifier = Modifier,
    currentTime: LocalTime,
    berlinClockState: BerlinClock
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, color = Color.Black, shape = CircleShape)
                    .clip(CircleShape)
                    .background(
                        color = if (berlinClockState.second) Color.Red else Color.White
                    )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val itemsCount = berlinClockState.hoursBy5.size
            berlinClockState.hoursBy5.forEachIndexed { index, isOn ->
                val itemShape = rowItemShape(index, itemsCount)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(itemShape)
                        .border(2.dp, color = Color.Black, shape = itemShape)
                        .background(
                            color = if (isOn) Color.Red else Color.White
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val itemsCount = berlinClockState.hoursBy1.size
            berlinClockState.hoursBy1.forEachIndexed { index, isOn ->
                val itemShape = rowItemShape(index, itemsCount)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(itemShape)
                        .border(2.dp, color = Color.Black, shape = itemShape)
                        .background(
                            color = if (isOn) Color.Red else Color.White
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val itemsCount = berlinClockState.minutesBy5.size
            berlinClockState.minutesBy5.forEachIndexed { index, isOn ->
                val itemShape = rowItemShape(index, itemsCount)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(itemShape)
                        .border(2.dp, color = Color.Black, shape = itemShape)
                        .background(
                            color = if (!isOn) {
                                Color.Transparent
                            } else if (minutesIsRed(index + 1)) {
                                Color.Red
                            } else {
                                Color.Yellow
                            }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val itemsCount = berlinClockState.minutesBy1.size
            berlinClockState.minutesBy1.forEachIndexed { index, isOn ->
                val itemShape = rowItemShape(index, itemsCount)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(itemShape)
                        .border(2.dp, color = Color.Black, shape = itemShape)
                        .background(
                            color = if (isOn) Color.Yellow else Color.White
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${currentTime.hour}:${currentTime.minute}:${currentTime.second}",
                fontSize = 68.sp,
                fontWeight = FontWeight.Black
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

@Preview(device = PIXEL_9_PRO_XL, backgroundColor = 1, showSystemUi = true)
@Composable
fun BerlinClockScreenPreview() {
    Scaffold {
        BerlinClockComposition(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 15.dp),
            currentTime = LocalTime.now(),
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