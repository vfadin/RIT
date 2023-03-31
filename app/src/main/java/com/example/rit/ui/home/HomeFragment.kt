package com.example.rit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.rit.R
import com.example.rit.databinding.FragmentHomeBinding
import com.example.rit.databinding.FragmentHomeDogBinding
import com.example.rit.utils.Constants
import com.example.rit.utils.restoreChosenApi
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private var _bindingDog: FragmentHomeDogBinding? = null
    private val binding
        get() = when (requireContext().restoreChosenApi()) {
            Constants.Api.DOG.ordinal -> _bindingDog
            Constants.Api.NATIONALIZE.ordinal -> _binding
            else -> _bindingDog
        }
    private val viewModel by viewModels<HomeViewModel>()
    private var dialog = DisplayInfoDialogFragment("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        when (requireContext().restoreChosenApi()) {
            Constants.Api.DOG.ordinal -> {
                _bindingDog = FragmentHomeDogBinding.inflate(inflater, container, false)
            }
            Constants.Api.NATIONALIZE.ordinal -> {
                _binding = FragmentHomeBinding.inflate(inflater, container, false)
            }
            else -> {
                _bindingDog = FragmentHomeDogBinding.inflate(inflater, container, false)
            }
        }
        return binding?.root ?: View(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun openImageFullScreen() {
        viewModel.imageUrlStateFlow.value?.let {
            findNavController().navigate(
                R.id.action_HomeFragment_to_fullScreenImageFragment,
                Bundle().apply {
                    putString("url", it)
                }
            )
        }
    }

    private fun bindUi() {
        when (binding) {
            is FragmentHomeDogBinding -> bindDogPart(binding as FragmentHomeDogBinding)
            is FragmentHomeBinding -> bindNationalizePart(binding as FragmentHomeBinding)
        }
    }

    private fun bindDogPart(fragmentHomeDogBinding: FragmentHomeDogBinding) {
        with(fragmentHomeDogBinding) {
            bindToolBar(toolbar)
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

    private fun bindToolBar(toolbar: Toolbar) {
        toolbar.apply {
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_settings -> {
                        findNavController().navigate(R.id.action_HomeFragment_to_settingsFragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun bindNationalizePart(fragmentHomeBinding: FragmentHomeBinding) {
        with(fragmentHomeBinding) {
            bindToolBar(toolbar)
            bindTextField(textField)
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