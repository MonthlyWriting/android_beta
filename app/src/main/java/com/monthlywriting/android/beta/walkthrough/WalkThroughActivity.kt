package com.monthlywriting.android.beta.walkthrough

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.adapter.WalkThroughAdapter
import com.monthlywriting.android.beta.databinding.ActivityMainBinding
import com.monthlywriting.android.beta.databinding.ActivityWalkThroughBinding

class WalkThroughActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkThroughBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkThroughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(null)

        val navController = findNavController(R.id.nav_host_fragment_activity_walkthrough)

        appBarConfiguration = AppBarConfiguration(navController.graph)
    }
}