package com.egidanuajisantoso.assesment2_mobpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.egidanuajisantoso.assesment2_mobpro.navigation.SetupNavGraph
import com.egidanuajisantoso.assesment2_mobpro.ui.theme.Assesment2_MobproTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assesment2_MobproTheme {
                SetupNavGraph()
            }
        }
    }
}