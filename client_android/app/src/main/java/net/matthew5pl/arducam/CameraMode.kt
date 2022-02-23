package net.matthew5pl.arducam

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.Utf8
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset

data class camApiResponseType (
    val camApiResponseTrue: String = """
        <html>
            <div>
                <p>True</p>
            </div>
        </html>
    """.trimIndent(),
    val camApiResponseFalse: String = """
        <html>
            <div>
                <p>False</p>
            </div>
        </html>
    """.trimIndent()
)

data class CameraHTTPData(
    var data: String
)

interface CameraHTTPService {
    @POST("/")
    fun postDo(@Body data: CameraHTTPData): Call<List<CameraHTTPData>>
}

class CameraMode: AppCompatActivity() {
    private val cameraRequest = 1888
    lateinit var apparat: Fotoapparat
    var handler = Handler()
    var runnable: Runnable? = null
    val delay = 5000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cameramode)
        var prefs = getSharedPreferences("", Context.MODE_PRIVATE)
        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), cameraRequest)
        }
        val camView = findViewById<CameraView>(R.id.mainCam)
        apparat = Fotoapparat(this, camView, scaleType = ScaleType.CenterCrop, lensPosition = back(), logger = loggers(logcat(), fileLogger(this)), cameraErrorCallback = {e -> })
        apparat.start()
        val urlStr = "http://" + prefs.getString("ip", "")
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            GlobalScope.launch {
                var res = URL(urlStr + "/entrystatus").readText(Charset.forName("UTF-8"))
                println(res)
                if(res == camApiResponseType().camApiResponseTrue) {
                    var result = apparat.takePicture()
                    result.toBitmap().whenAvailable { photo ->
                        val baos = ByteArrayOutputStream()
                        photo?.bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val b = baos.toByteArray()
                        val base64jpg = Base64.encodeToString(b, Base64.DEFAULT)
                        var ft: Retrofit = Retrofit.Builder().baseUrl(urlStr).addConverterFactory(GsonConverterFactory.create()).build()
                        val chs = ft.create(CameraHTTPService::class.java)
                        val dataH: CameraHTTPData = CameraHTTPData(base64jpg)
                        val ps = chs.postDo(dataH)
                        try {
                            GlobalScope.launch {
                                ps.enqueue(object : Callback<List<CameraHTTPData>> {
                                    override fun onResponse(
                                        call: Call<List<CameraHTTPData>>,
                                        response: Response<List<CameraHTTPData>>
                                    ) {
                                        println("request done")
                                    }

                                    override fun onFailure(call: Call<List<CameraHTTPData>>, t: Throwable) {
                                        println("error" + t.localizedMessage)
                                    }
                                })
                            }
                        } catch(e: Exception) {

                        }
                    }
                }
            }
        }.also{ runnable = it }, delay.toLong())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        apparat.stop()
        finish()
        System.exit(0)
    }
}