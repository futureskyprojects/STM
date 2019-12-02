package vn.vistark.stm.ui.main_fragments.subject_fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import vn.vistark.stm.R
import vn.vistark.stm.ui.main_fragments.BaseFrg

class SubjectFragment : Fragment(), BaseFrg {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subject, container, false)
    }

    override fun getBaseName(): String {
        return "Subjects"
    }

    companion object {
        fun newInstance(): SubjectFragment {
            return SubjectFragment()
        }
    }
}
