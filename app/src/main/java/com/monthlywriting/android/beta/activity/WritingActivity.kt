package com.monthlywriting.android.beta.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.ActivityWritingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWritingBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: WritingActivityViewModel by viewModels()
    private val args: WritingActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_writing)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.nav_writing_main) {
                binding.toolbar.setNavigationIcon(R.drawable.ic_btn_back)
            }
        }

        viewModel.title.observe(this) {
            binding.toolbarTitle.text = it
        }

        setToolbarTitle(resources.getStringArray(R.array.month_name)[args.month - 1])
        setData(args.year, args.month)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_writing)
        return navController.navigateUp(appBarConfiguration) || super.onNavigateUp()
    }

    fun setToolbarTitle(title: String) {
        viewModel.setTitle(title)
    }

    private fun setData(year: Int, month: Int) {
        if (viewModel.monthlyGoalTempList.value != null) {
            viewModel.saveWriting()
        } else {
            viewModel.getMonthlyGoalList(year, month)
        }
    }

    fun getCurrentYear(): Int {
        return args.year
    }

    fun getCurrentMonth(): Int {
        return args.month
    }

    fun getAllPhotoList() {
        viewModel.getPhotoList(args.year, args.month)
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
}