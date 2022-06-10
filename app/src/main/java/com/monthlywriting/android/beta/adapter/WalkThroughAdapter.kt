package com.monthlywriting.android.beta.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.monthlywriting.android.beta.walkthrough.authority.AuthorityFragment
import com.monthlywriting.android.beta.walkthrough.name.NameFragment

class WalkThroughAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragments = listOf(
        NameFragment(),
        AuthorityFragment(),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}