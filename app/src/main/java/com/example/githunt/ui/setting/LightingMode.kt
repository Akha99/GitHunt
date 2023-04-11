package com.example.githunt.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githunt.R
import androidx.lifecycle.lifecycleScope
import com.example.githunt.databinding.ActivitySettingDarkBinding
import kotlinx.coroutines.launch

class LightingMode : AppCompatActivity() {

    private lateinit var binding: ActivitySettingDarkBinding
    private lateinit var viewModel: MainViewModel2
    private lateinit var dataStoreManager: DataStoreManager


    companion object {
        const val KEY_THEME_MODE = "theme_mode"
        const val THEME_LIGHT = 1
        const val THEME_DARK = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingDarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel2::class.java]
        dataStoreManager = DataStoreManager(this)

        checkThemeMode()

        binding.apply {
            modeSwitch.setOnCheckedChangeListener { _, isChecked ->
                lifecycleScope.launch {
                    when (isChecked) {
                        true -> viewModel.setTheme(true)
                        false -> viewModel.setTheme(false)

                    }
                }
            }
        }
    }

    private fun checkThemeMode() {
        binding.apply {
            viewModel.getTheme.observe(this@LightingMode) { isDarkMode ->
                val mode = if (isDarkMode) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)

                //
                lifecycleScope.launch {
                    dataStoreManager.setTheme(isDarkMode)
                }

                modeSwitch.isChecked = isDarkMode
                modeSwitch.text = if (isDarkMode) "Dark Mode" else "Light Mode"
                val animation = if (isDarkMode) R.raw.night else R.raw.day
                imgStatus.setAnimation(animation)


                viewModel.setTheme(isDarkMode)
            }
        }
    }
}

