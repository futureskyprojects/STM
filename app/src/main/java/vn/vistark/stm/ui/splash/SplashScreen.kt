package vn.vistark.stm.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import vn.vistark.stm.R
import vn.vistark.stm.ui.register.RegisterActivity
import java.util.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(Intent(this@SplashScreen, RegisterActivity::class.java))
                finish()
            }
        }, 1500)
    }
}
