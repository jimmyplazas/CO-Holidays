package dev.alejo.colombian_holidays.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun HomeScreen(state: HomeState) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = state.backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}