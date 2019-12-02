package vn.vistark.stm.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.vistark.stm.ui.main_fragments.BaseFrg
import vn.vistark.stm.ui.main_fragments.class_fragment.ClassFragment
import vn.vistark.stm.ui.main_fragments.student_fragment.StudentFragment
import vn.vistark.stm.ui.main_fragments.subject_fragment.SubjectFragment

class SectionsPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val frgClasses = arrayListOf(
        SubjectFragment.newInstance(),
        StudentFragment.newInstance(),
        ClassFragment.newInstance()
    )

    override fun getItem(position: Int): Fragment {
        return frgClasses[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return (frgClasses[position] as BaseFrg).getBaseName()
    }

    override fun getCount(): Int {
        return frgClasses.size
    }


}