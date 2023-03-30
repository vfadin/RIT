package com.example.rit.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.rit.R
import com.example.rit.databinding.FragmentFullImageBinding

class FullScreenImageFragment : Fragment(R.layout.fragment_full_image) {

    private var _binding: FragmentFullImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeSystemBarsVisibility()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFullImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun changeSystemBarsVisibility(to: Boolean = false) {
        activity?.apply {
            WindowCompat.setDecorFitsSystemWindows(window, to)
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            if (to) {
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            } else {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.let {
            Glide.with(requireContext())
                .load(arguments?.getString("url", ""))
                .into(it)
            it.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        changeSystemBarsVisibility(true)
        super.onDestroyView()
    }
}