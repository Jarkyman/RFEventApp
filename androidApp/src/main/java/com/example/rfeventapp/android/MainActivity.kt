package com.example.rfeventapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.rfeventapp.android.navigation.RootNavigationGraph
import com.github.barteksc.pdfviewer.PDFView

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
        /*val pdfView = findViewById<PDFView?>(R.id.pdfView).fromAsset("rf_map_2022.pdf")
            .enableSwipe(false)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .load()*/

        /*pdfView.fromAsset("rf_map_2022.pdf") //TODO: 2023
            .enableSwipe(false)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .load()*/
    }
}
