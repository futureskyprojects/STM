package vn.vistark.stm.ui.subject_manager

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_subject_manager.*
import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.StudentObj
import vn.vistark.stm.utils.CreateQR
import vn.vistark.stm.utils.Internet

class SubjectManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_manager)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            tvTitle.text = destination.label
        }
        navView.setupWithNavController(navController)

        ivBackArrow.setOnClickListener {
            finish()
        }

        ivExportStudentQR.setOnClickListener {
            if (Internet.isAvailable().execute().get()) {
                CreateQR(this).execute()
            } else {
                Toasty.error(this, "Please connect to internet", Toasty.LENGTH_SHORT, false).show()
            }
        }
    }
}
