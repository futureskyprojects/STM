package vn.vistark.stm.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*
import vn.vistark.stm.ui.MainActivity
import vn.vistark.stm.R
import vn.vistark.stm.data.Prefs
import java.io.File
import java.io.FileInputStream

class RegisterActivity : AppCompatActivity() {

    var isFirstUse = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#A6FFCB")
        }
        setContentView(R.layout.activity_register)

        isFirstUse = Prefs.getIsFirstUse(this)
        if (!isFirstUse) {
            edtUserName.visibility = View.GONE
            tvWelcomeText.visibility = View.VISIBLE
            tvWelcomeText.text = "Hi, ${Prefs.getUserName(this)}!"
            loadExistedAvatar()
        } else {
            edtUserName.visibility = View.VISIBLE
            tvWelcomeText.visibility = View.GONE
        }

        btnConfirm.setOnClickListener {
            val securePass = edtSecure4Number.text.toString()
            if (securePass.isBlank() || securePass.isEmpty() || securePass.length < 4) {
                Toasty.warning(
                    this,
                    "Password length must be from 8 character",
                    Toast.LENGTH_SHORT,
                    false
                )
                    .show()
                return@setOnClickListener
            }
            if (isFirstUse) {
                val userName = edtUserName.text.toString()
                if (userName.isBlank() || userName.isEmpty() || userName.split(" ").size < 2) {
                    Toasty.warning(this, "Please input yor full name", Toast.LENGTH_SHORT, false)
                        .show()
                    return@setOnClickListener
                } else {
                    isFirstUse = false
                    Prefs.setIsFirstUse(this, isFirstUse)
                    Prefs.setUserName(this, userName)
                    Prefs.setSecurePass(this, securePass)
                    Toasty.success(
                        this,
                        "Welcome $userName!",
                        Toast.LENGTH_SHORT,
                        false
                    ).show()
                }
            } else {
                if (securePass == Prefs.getSecurePass(this)) {
                    Toasty.success(
                        this,
                        "Welcome ${Prefs.getUserName(this)}!",
                        Toast.LENGTH_SHORT,
                        false
                    ).show()
                } else {
                    Toasty.error(
                        this,
                        "Wrong secure password!",
                        Toast.LENGTH_SHORT,
                        false
                    )
                        .show();
                    return@setOnClickListener
                }
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    private fun loadExistedAvatar() {
        civLogoWelcome.setImageResource(R.drawable.user)
        Thread {
            try {
                val f = File("$externalCacheDir${File.separator}avatar.png")
                val fi = FileInputStream(f)

                val bm = BitmapFactory.decodeStream(fi)
                if (bm != null) {
                    civLogoWelcome.post {
                        civLogoWelcome.setImageBitmap(bm)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
