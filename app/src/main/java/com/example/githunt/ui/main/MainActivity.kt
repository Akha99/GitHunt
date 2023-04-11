package com.example.githunt.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githunt.R
import com.example.githunt.data.model.User
import com.example.githunt.databinding.ActivityMainBinding
import com.example.githunt.ui.detail.DetailUserActivity
import com.example.githunt.ui.favorite.FavoriteActivity
import com.example.githunt.ui.setting.DataStoreManager
import com.example.githunt.ui.setting.LightingMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val isDarkMode = runBlocking { DataStoreManager(this@MainActivity).getTheme().first() }
        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)


        initAdapter()
        initRecyclerView()
        initViewModel()
        initSearchButton()
        initQueryListener()

    }

    private fun initAdapter() {
        adapter = UserAdapter().apply {
            setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: User) {
                    startActivity(Intent(this@MainActivity, DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                        putExtra(DetailUserActivity.EXTRA_ID, data.id)
                        putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    })
                }
            })
        }
    }

    //for recycler view
    private fun initRecyclerView() {
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }
    }

    //for viewModel
    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
        viewModel.getSearchUsers().observe(this) { users ->
            if (users != null) {
                adapter.setList(users)
                loadingIndicator(false)
            }
        }
    }

    //for search button
    private fun initSearchButton() {
        binding.btnSearch.setOnClickListener {
            searchUser()
        }
    }

    private fun initQueryListener() {
        binding.etQuery.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchUser()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    //for searchUser
    private fun searchUser() {
        val query = binding.etQuery.text.toString()
        if (query.isEmpty()) return
        loadingIndicator(true)
        viewModel.setSearchUsers(query)
    }

    //Loading indicator
    private fun loadingIndicator(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    //For menu on App Bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> run {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.setting_menu -> run {
                val intent = Intent(this, LightingMode::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

