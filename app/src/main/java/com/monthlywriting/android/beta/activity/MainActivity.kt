package com.monthlywriting.android.beta.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ActivityMainBinding
import com.monthlywriting.android.beta.di.App
import com.monthlywriting.android.beta.ui.guide.GuideContainerFragment
import com.monthlywriting.android.beta.util.toDp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_collection, R.id.nav_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home,
                R.id.nav_collection,
                R.id.nav_setting,
                -> {
                    binding.navView.visibility = View.VISIBLE
                }
                else -> {
                    binding.navView.visibility = View.GONE
                    binding.toolbar.setNavigationIcon(R.drawable.ic_btn_back)
                }
            }
        }

        viewModel.title.observe(this) {
            binding.toolbarTitle.text = it
        }

        if (!App.prefs.guidePref) {
            openGuide()
            App.prefs.guidePref = true
        }

        viewModel.insertWriting()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onNavigateUp()
    }

    fun setToolbarTitle(title: String) {
        viewModel.setTitle(title)
    }

    fun toggleDeleteBtn(toShow: Boolean, function: () -> Unit) {
        when (toShow) {
            true -> {
                binding.toolbarDelete.visibility = View.VISIBLE
                binding.toolbarDelete.setOnClickListener {
                    function()
                }
            }
            false -> {
                binding.toolbarDelete.visibility = View.GONE
                binding.toolbarDelete.setOnClickListener(null)
            }
        }
    }

    fun toggleSaveBtn(toShow: Boolean, function: () -> Unit) {
        when (toShow) {
            true -> {
                binding.toolbarSave.visibility = View.VISIBLE
                binding.toolbarSave.setOnClickListener {
                    function()
                }
            }
            false -> {
                binding.toolbarSave.visibility = View.GONE
                binding.toolbarSave.setOnClickListener(null)
            }
        }
    }

    fun setFragmentMargin(isDefault: Boolean) {
        when (isDefault) {
            true -> {
                binding.flContainer.layoutParams =
                    (binding.flContainer.layoutParams as ConstraintLayout.LayoutParams).also {
                        it.marginEnd = 16.toDp(this)
                        it.marginStart = 16.toDp(this)
                    }
            }
            false -> {
                binding.flContainer.layoutParams =
                    (binding.flContainer.layoutParams as ConstraintLayout.LayoutParams).also {
                        it.marginEnd = 0
                        it.marginStart = 0
                    }
            }
        }
    }

    private fun openGuide() {
        val fragment = GuideContainerFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.full_container, fragment)
            .commit()
    }
}