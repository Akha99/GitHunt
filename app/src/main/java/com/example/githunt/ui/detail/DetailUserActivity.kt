package com.example.githunt.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githunt.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {


    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var binding : ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val profileImg = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
        }


        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        showLoading(true)

        viewModel.setUserDetail(username.toString())

        viewModel.getUserDetail().observe(this) { user ->
            user?.let {
                binding.apply {
                    tvName.text = user.name
                    tvUsername.text = user.login
                    tvFollowers.text = "${user.followers} Followers"
                    tvFollowing.text = "${user.following} Following"
                    Glide.with(this@DetailUserActivity)
                        .load(user.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivProvile)
                }
            }
        }


        var Checked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                count?.let {
                    binding.toggleFavorite.isChecked = it > 0
                    Checked = it > 0
                }
            }
        }


        binding.toggleFavorite.setOnClickListener {
            Checked = !Checked
            if (Checked) {
                username?.let { name ->
                    profileImg?.let { avatar ->
                        viewModel.addToFavorite(name, id, avatar)
                    }
                }
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.toggleFavorite.isChecked = Checked
        }


        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        viewModel.getUserDetail().observe(this) { userDetail ->
            userDetail?.let {
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}