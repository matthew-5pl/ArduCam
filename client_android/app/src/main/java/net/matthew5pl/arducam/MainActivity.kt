package net.matthew5pl.arducam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cameraBtn = findViewById<ImageButton>(R.id.cameraModeBtn)
        val arduinoBtn = findViewById<ImageButton>(R.id.serverModeBtn)
        val monitorBtn = findViewById<ImageButton>(R.id.monitorModeBtn)
        val ipName = findViewById<EditText>(R.id.serverAddress)
        val prefs = getSharedPreferences("", Context.MODE_PRIVATE)
        if(prefs.contains("ip")) {
            ipName.setText(prefs.getString("ip", ""))
        }
        val editor = prefs.edit()
        cameraBtn.animate().setDuration(200).alpha(1.0f).withEndAction {
            arduinoBtn.animate().setDuration(200).alpha(1.0f).withEndAction {
                monitorBtn.animate().setDuration(200).alpha(1.0f)
            }
        }
        cameraBtn.setOnClickListener {
            editor.putString("ip", ipName.text.toString())
            editor.apply()
            val cameraOpenIntent = Intent(this, CameraMode::class.java)
            startActivity(cameraOpenIntent)
        }

        monitorBtn.setOnClickListener {
            editor.putString("ip", ipName.text.toString())
            editor.apply()
            val monitorOpenIntent = Intent(this, MonitorMode::class.java)
            startActivity(monitorOpenIntent)
        }
    }
}