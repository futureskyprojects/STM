package vn.vistark.stm.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import vn.vistark.stm.R
import vn.vistark.stm.data.Prefs
import vn.vistark.stm.ui.main.SectionsPagerAdapter
import java.io.*


class MainActivity : AppCompatActivity() {
    private val pickImage = 32453
    private var avatarPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirstArguments()
        initViewPage()
        loadExistedAvatar()
        loadUsername()
        initEvents()
    }

    private fun loadUsername() {
        tvUserName.text = Prefs.getUserName(this)
    }

    private fun initEvents() {

        civUserAvatar.setOnClickListener {
            showAvatarPopupMenu()
        }
    }

    private fun initViewPage() {
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun initFirstArguments() {
        avatarPath = "$externalCacheDir${File.separator}avatar.png"
    }

    private fun loadExistedAvatar() {
        pbAvatarLoading.visibility = View.VISIBLE
        Thread {
            try {
                val f = File(avatarPath)
                val fi = FileInputStream(f)

                val bm = BitmapFactory.decodeStream(fi)
                if (bm != null)
                    pbAvatarLoading.post { civUserAvatar.setImageBitmap(bm) }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            pbAvatarLoading.post { pbAvatarLoading.visibility = View.GONE }
        }.start()
    }

    @SuppressLint("InflateParams")
    private fun showAvatarPopupMenu() {
        val popupWindow = PopupWindow(this)
        val inflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.avt_menu_layout, null)
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = view
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.showAsDropDown(
            civUserAvatar,
            0,
            (0 * resources.displayMetrics.density).toInt()
        )
        // init event for per button
        view.findViewById<LinearLayout>(R.id.lnChangeAvatar).setOnClickListener {
            avatarChanger()
            popupWindow.dismiss()
        }

        view.findViewById<LinearLayout>(R.id.lnChangePassword).setOnClickListener {
            changePasswordDialog()
            popupWindow.dismiss()
        }
    }

    private fun avatarChanger() {
        pbAvatarLoading.visibility = View.VISIBLE
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImage) {
            val bm = decodeUriToBitmap(this, data?.data)
            if (bm != null) {
                civUserAvatar.setImageBitmap(bm)
                saveAvatar(bm)
            } else {
                pbAvatarLoading.visibility = View.GONE
                Toasty.warning(this, "Update avatar not success", Toast.LENGTH_SHORT, false).show()
            }


        }
    }

    private fun saveAvatar(bm: Bitmap) {
        Thread {
            val f = File(avatarPath)
            if (f.exists()) {
                f.delete()
            }
            val fo = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.PNG, 100, fo)
            fo.flush()
            fo.close()
            pbAvatarLoading.post {
                pbAvatarLoading.visibility = View.GONE
                Toasty.success(
                    this@MainActivity,
                    "Update avatar success",
                    Toast.LENGTH_SHORT,
                    false
                ).show()
            }
        }.start()
    }

    private fun decodeUriToBitmap(mContext: Context, sendUri: Uri?): Bitmap? {
        var getBitmap: Bitmap? = null
        try {
            val imageStream: InputStream?
            try {
                imageStream = mContext.contentResolver.openInputStream(sendUri!!)
                getBitmap = BitmapFactory.decodeStream(imageStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return getBitmap
    }

    private fun changePasswordDialog() {
        val v = LayoutInflater.from(this).inflate(R.layout.change_password, null, false)
        val dialog = AlertDialog.Builder(this).create()
        dialog.setView(v)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.show()
        // Event
        v.findViewById<ImageView>(R.id.ivCloseBtn).setOnClickListener {
            dialog.dismiss()
        }
        v.findViewById<Button>(R.id.btnConfirmChange).setOnClickListener {
            val securePass = v.findViewById<EditText>(R.id.edtSecureNumber).text.toString()
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
            Prefs.setSecurePass(this, securePass)
            Toasty.success(
                this,
                "Update your secure password success",
                Toast.LENGTH_SHORT,
                false
            ).show()
            dialog.dismiss()
        }
    }
}