package com.example.notificationplanner.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notificationplanner.data.NotificationConfig
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForm(
    notificationConfig: NotificationConfig,
    modifier: Modifier = Modifier
) {
    val colorDefault = FilterChipDefaults.filterChipColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        selectedContainerColor = MaterialTheme.colorScheme.onSecondary
    )
    val borderDefault = FilterChipDefaults.filterChipBorder(
        selectedBorderColor = Color.Black,
        selectedBorderWidth = 1.dp,
        borderColor = MaterialTheme.colorScheme.onSecondary,
        borderWidth = 2.dp
    )

    val filterChipModifier = Modifier

    var temperature by remember { mutableStateOf(notificationConfig.weather_temperature) }
    var cloudCover by remember { mutableStateOf(notificationConfig.weather_cloud_cover) }
    var dewPoint by remember { mutableStateOf(notificationConfig.weather_dew_point) }
    var precipitation by remember { mutableStateOf(notificationConfig.weather_precipitation) }
    var relativeHumidity by remember { mutableStateOf(notificationConfig.weather_relative_humidity) }
    var sunshine by remember { mutableStateOf(notificationConfig.weather_sunshine) }
    var visibility by remember { mutableStateOf(notificationConfig.weather_visibility) }
    var windDirection by remember { mutableStateOf(notificationConfig.weather_wind_direction) }
    var windSpeed by remember { mutableStateOf(notificationConfig.weather_wind_speed) }

    Column(modifier = modifier) {

        FlowRow(
            mainAxisAlignment = MainAxisAlignment.Start,
            mainAxisSize = SizeMode.Expand,
            crossAxisSpacing = 2.dp,
            mainAxisSpacing = 4.dp
        ) {


            FilterChip(
                onClick = {
                    temperature = !temperature
                    notificationConfig.weather_temperature = temperature
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Temperature")
                },
                selected = temperature,
                colors = colorDefault, border = borderDefault,

                )
            FilterChip(
                onClick = {
                    sunshine = !sunshine
                    notificationConfig.weather_sunshine = sunshine
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Sunshine ")
                },
                selected = sunshine,
                colors = colorDefault, border = borderDefault
            )
            FilterChip(
                onClick = {
                    precipitation = !precipitation
                    notificationConfig.weather_precipitation = precipitation
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Precipitation")
                },
                selected = precipitation,
                colors = colorDefault, border = borderDefault
            )
            FilterChip(
                onClick = {
                    cloudCover = !cloudCover
                    notificationConfig.weather_cloud_cover = cloudCover
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Cloud over ")
                },
                selected = cloudCover,
                colors = colorDefault, border = borderDefault
            )
            FilterChip(
                onClick = {
                    dewPoint = !dewPoint
                    notificationConfig.weather_dew_point = dewPoint
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Dew point")
                },
                selected = dewPoint,
                colors = colorDefault, border = borderDefault
            )
            FilterChip(
                onClick = {
                    visibility = !visibility
                    notificationConfig.weather_visibility = visibility
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Visibility")
                },
                selected = visibility,
                colors = colorDefault, border = borderDefault
            )
            FilterChip(
                onClick = {
                    relativeHumidity = !relativeHumidity
                    notificationConfig.weather_relative_humidity = relativeHumidity
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Relative humidity")
                },
                selected = relativeHumidity,
                colors = colorDefault, border = borderDefault
            )
            FilterChip(
                onClick = {
                    windDirection = !windDirection
                    notificationConfig.weather_wind_direction = windDirection
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Wind direction ")
                },
                selected = windDirection,
                colors = colorDefault, border = borderDefault
            )
            FilterChip(
                onClick = {
                    windSpeed = !windSpeed
                    notificationConfig.weather_wind_speed = windSpeed
                },
                modifier = filterChipModifier,
                label = {
                    Text(text = "Wind speed")
                },
                selected = windSpeed,
                colors = colorDefault, border = borderDefault
            )
        }
    }
}