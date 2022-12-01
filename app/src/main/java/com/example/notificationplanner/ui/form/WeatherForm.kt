package com.example.notificationplanner.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notificationplanner.data.NotificationConfig
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForm(
    notificationConfig: NotificationConfig
) {
    val filterChipColorScheme = FilterChipDefaults.filterChipColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    val modifier = Modifier

    var temperature by remember { mutableStateOf(notificationConfig.weather_temperature) }
    var cloudCover by remember { mutableStateOf(notificationConfig.weather_cloud_cover) }
    var dewPoint by remember { mutableStateOf(notificationConfig.weather_dew_point) }
    var precipitation by remember { mutableStateOf(notificationConfig.weather_precipitation) }
    var relativeHumidity by remember { mutableStateOf(notificationConfig.weather_relative_humidity) }
    var sunshine by remember { mutableStateOf(notificationConfig.weather_sunshine) }
    var visibility by remember { mutableStateOf(notificationConfig.weather_visibility) }
    var windDirection by remember { mutableStateOf(notificationConfig.weather_wind_direction) }
    var windSpeed by remember { mutableStateOf(notificationConfig.weather_wind_speed) }

    Column(modifier = Modifier.padding(10.dp)) {

        Text(text = "Weather Form")
        FlowRow(
            mainAxisAlignment = MainAxisAlignment.Start,
            mainAxisSize = SizeMode.Expand,
            crossAxisSpacing = 2.dp,
            mainAxisSpacing = 6.dp
        ) {


            FilterChip(
                onClick = {
                    temperature = !temperature
                    notificationConfig.weather_temperature = temperature
                },
                modifier = modifier,
                label = {
                    Text(text = "Temperature")
                },
                selected = temperature,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    sunshine = !sunshine
                    notificationConfig.weather_sunshine = sunshine
                },
                modifier = modifier,
                label = {
                    Text(text = "Sunshine ")
                },
                selected = sunshine,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    precipitation = !precipitation
                    notificationConfig.weather_precipitation = precipitation
                },
                modifier = modifier,
                label = {
                    Text(text = "Precipitation")
                },
                selected = precipitation,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    cloudCover = !cloudCover
                    notificationConfig.weather_cloud_cover = cloudCover
                },
                modifier = modifier,
                label = {
                    Text(text = "Cloud over ")
                },
                selected = cloudCover,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    dewPoint = !dewPoint
                    notificationConfig.weather_dew_point = dewPoint
                },
                modifier = modifier,
                label = {
                    Text(text = "Dew point")
                },
                selected = dewPoint,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    visibility = !visibility
                    notificationConfig.weather_visibility = visibility
                },
                modifier = modifier,
                label = {
                    Text(text = "Visibility")
                },
                selected = visibility,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    relativeHumidity = !relativeHumidity
                    notificationConfig.weather_relative_humidity = relativeHumidity
                },
                modifier = modifier,
                label = {
                    Text(text = "Relative humidity")
                },
                selected = relativeHumidity,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    windDirection = !windDirection
                    notificationConfig.weather_wind_direction = windDirection
                },
                modifier = modifier,
                label = {
                    Text(text = "Wind direction ")
                },
                selected = windDirection,
                colors = filterChipColorScheme
            )
            FilterChip(
                onClick = {
                    windSpeed = !windSpeed
                    notificationConfig.weather_wind_speed = windSpeed
                },
                modifier = modifier,
                label = {
                    Text(text = "Wind speed")
                },
                selected = windSpeed,
                colors = filterChipColorScheme
            )
        }
    }
}