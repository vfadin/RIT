package com.example.rit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.rit.R
import com.example.rit.databinding.FragmentHomeBinding
import com.example.rit.databinding.FragmentHomeDogBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private var _binding_dog: FragmentHomeDogBinding? = null
    private val binding get() = _binding!!
    private var isFullScreen = false

    // TODO: replcae
    private val binding_dog get() = _binding_dog!!
    private val viewModel by viewModels<HomeViewModel>()
    private var dialog = DisplayInfoDialogFragment("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding_dog = FragmentHomeDogBinding.inflate(inflater, container, false)
        // TODO: replace
        return binding_dog.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun openImageFullScreen() {
        viewModel.imageUrlStateFlow.value?.let {
            findNavController().navigate(
                R.id.fullScreenImageFragment,
                Bundle().apply {
                    putString("url", it)
                }
            )
        }
    }

    private fun bindUi() {
        bindNationalizePart()
        bindDogPart()
    }

    private fun bindDogPart() {
        with(binding_dog) {
            button.setOnClickListener {
                viewModel.getImage()
            }
            image.setOnClickListener {
                openImageFullScreen()
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.imageUrlStateFlow.collect {
                    Glide.with(requireContext()).load(it).into(image)
                }
            }
        }
    }

    private fun bindNationalizePart() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.nameCountryStateFlow.collect { list ->
                    list?.let {
                        dialog = DisplayInfoDialogFragment(
                            buildString {
                                list.forEach { nameProbability ->
                                    append(getString(R.string.nationalize_answer_title))
                                    append(" ${nameProbability.name} in:\n")
                                    append(nameProbability.country.joinToString(separator = "") {
                                        it.toString()
                                    })
                                }
                            })
                    }
                }
            }
            bindTextField(textField)
        }
    }

    private fun bindTextField(textField: TextInputEditText) {
        textField.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnEditorActionListener { text, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.getNameInCountryProbability(text.text.toString())
                        dialog.show(childFragmentManager, DisplayInfoDialogFragment.TAG)
                    }
                    return@setOnEditorActionListener true
                }
                false
            }
            isSingleLine = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}