package ru.effectivemobile.rxjava.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.effectivemobile.rxjava.activity.FirstTabFragment
import ru.effectivemobile.rxjava.activity.SecondTabFragment
import ru.effectivemobile.rxjava.activity.ThirdTabFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstTabFragment()
            1 -> SecondTabFragment()
            2 -> ThirdTabFragment()
            else -> FirstTabFragment()
        }
    }
}