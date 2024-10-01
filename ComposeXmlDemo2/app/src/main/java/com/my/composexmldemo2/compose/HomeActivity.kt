package com.my.composexmldemo2.compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.my.composexmldemo2.MainActivity
import com.my.composexmldemo2.R
import com.my.composexmldemo2.compose.component.CustomScreen
import com.my.composexmldemo2.compose.ui.theme.yellow

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CustomScreen(
                window = window,
                backgroundColor = yellow(),
                statusBarPadding = true,
                statusBarIconBlack = true
            ) {
                HomeUI()
            }
        }
    }
}

@Composable
fun HomeUI() {
    val localContext = LocalContext.current

    Column {
        Text(
            text= "Home UI"
        )
        Button(
            onClick = {
                val intent = Intent(localContext, MainActivity::class.java)
                localContext.startActivity(intent)
            }
        ) {
            Text(text = "MainActivity 로.. (뒤로가기 아님)")
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth(),
            factory = { context ->
                val view = LayoutInflater.from(context).inflate(R.layout.text_input_view, null, false)
                view
            },
            update = { view ->
                val etText = view.findViewById<EditText>(R.id.etText)
                val btnComplete = view.findViewById<Button>(R.id.btnComplete)

                etText.setText("ABC")
                Log.d("MYTAG", "AndroidView Update")
                btnComplete.setOnClickListener {
                    Log.d("MYTAG", "클릭 ${etText.text}")
                }
            }
        )
    }

    BackHandler(
        enabled = true,
        onBack = {
            if(localContext is ComponentActivity) {
                localContext.finish()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeUI()
}