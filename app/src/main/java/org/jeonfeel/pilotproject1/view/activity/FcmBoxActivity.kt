package org.jeonfeel.pilotproject1.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jeonfeel.pilotproject1.databinding.FcmBoxActivityBinding
import org.jeonfeel.pilotproject1.ui.FCMBoxList

class FcmBoxActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FcmBoxScreen()
        }
    }
}

@Composable
fun FcmBoxScreen() {
    Scaffold() { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)){
            FCMBoxList()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FcmBoxScreenPreview() {
    FcmBoxScreen()
}