package com.example.voiceclone.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.voiceclone.R

@Composable
fun MainApp() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_1920x1080),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // dark overlay
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            TopAppBar(
                title = { Text("VoiceClone", color = Color(0xFFCFCFE0)) },
                backgroundColor = Color.Transparent,
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_toolbar_logo_108),
                        contentDescription = null,
                        modifier = Modifier.size(44.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0x1A000000)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Record or Upload a WAV", color = Color(0xFFCFCFE0))
                    Spacer(modifier = Modifier.height(8.dp))
                    // Buttons and controls will be added here in following steps
                }
            }
        }
    }
}
