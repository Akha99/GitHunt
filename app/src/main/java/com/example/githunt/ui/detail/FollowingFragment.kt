package com.example.githunt.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githunt.R
import com.example.githunt.databinding.FragmentFollowBinding
import com.example.githunt.ui.main.UserAdapter

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        val username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).orEmpty()
        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.adapter = adapter
        }

        loadingIndicator(true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]
        viewModel.setListFollowing(username)


        viewModel.getListFollowing().observe(viewLifecycleOwner) { following ->
            following?.let {
                adapter.setList(following)
                loadingIndicator(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingIndicator(state: Boolean) {
        binding.progressBar.visibility = when(state) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }
}