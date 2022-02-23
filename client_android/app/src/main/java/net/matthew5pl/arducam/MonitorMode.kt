package net.matthew5pl.arducam

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL
import java.util.stream.Stream

class MonitorMode: AppCompatActivity() {
    var handler = Handler()
    var runnable: Runnable? = null
    val delay = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)
        var prefs = getSharedPreferences("", Context.MODE_PRIVATE)
        val dlURL = URL("http://" + prefs.getString("ip", "") + "/static/img.jpg")
        val monitor = findViewById<ImageView>(R.id.monitorView)

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            try {
                GlobalScope.launch {
                    val stream = dlURL.openStream()
                    val bs = BitmapFactory.decodeStream(stream)
                    runOnUiThread {
                        monitor.setImageBitmap(bs)
                    }
                }
            } catch(e: Exception) {

            }
        }.also{ runnable = it }, delay.toLong())
    }
}