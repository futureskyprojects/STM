package vn.vistark.stm.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import vn.vistark.stm.R
import vn.vistark.stm.data.db.ClassDB
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectDB
import vn.vistark.stm.ui.register.RegisterActivity
import java.util.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Init db create
        initDatabaseCreator()


        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(Intent(this@SplashScreen, RegisterActivity::class.java))
                finish()
            }
        }, 1500)
    }

    private fun initDatabaseCreator() {
        ClassDB(this)
        SubjectDB(this)
        StudentDB(this)
    }
}
