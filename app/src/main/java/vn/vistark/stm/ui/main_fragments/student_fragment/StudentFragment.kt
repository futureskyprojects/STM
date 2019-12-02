package vn.vistark.stm.ui.main_fragments.student_fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import vn.vistark.stm.R
import vn.vistark.stm.ui.main_fragments.BaseFrg

class StudentFragment : Fragment(), BaseFrg {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student, container, false)
    }

    override fun getBaseName(): String {
        return "Students"
    }

    companion object {
        fun newInstance(): StudentFragment {
            return StudentFragment()
        }
    }

}
