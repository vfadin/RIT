package com.example.rit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.rit.R
import com.example.rit.databinding.FragmentHomeBinding
import com.example.rit.databinding.FragmentHomeCustomBinding
import com.example.rit.databinding.FragmentHomeDogBinding
import com.example.rit.utils.Constants
import com.example.rit.utils.restoreChosenApi
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private var _bindingDog: FragmentHomeDogBinding? = null
    private var _bindingCustom: FragmentHomeCustomBinding? = null
    private val binding
        get() = when (requireContext().restoreChosenApi()) {
            Constants.Api.DOG.ordinal -> _bindingDog
            Constants.Api.NATIONALIZE.ordinal -> _binding
            Constants.Api.CUSTOM.ordinal -> _bindingCustom
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
            Constants.Api.CUSTOM.ordinal -> {
                _bindingCustom = FragmentHomeCustomBinding.inflate(inflater, container, false)
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
            is FragmentHomeCustomBinding -> bindCustomPart(binding as FragmentHomeCustomBinding)
        }
        bindErrorToast()
    }

    private fun bindCustomPart(fragmentHomeCustomBinding: FragmentHomeCustomBinding) {
        with(fragmentHomeCustomBinding) {
            bindToolBar(toolbar)
            bindLoadingIndicator(loadingIndicator)
            bindOnDoneAction(textFieldCustom) { text ->
                viewModel.sendCustomRequest(text)
            }
            textFieldCustom.addTextChangedListener {
                toolbar.title = it
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.customResponseStateFlow.collect {
                        textViewRequestAnswerCustom.text = it
                    }
                }
            }
        }
    }

    private fun bindDogPart(fragmentHomeDogBinding: FragmentHomeDogBinding) {
        with(fragmentHomeDogBinding) {
            bindToolBar(toolbar)
            bindLoadingIndicator(loadingIndicator)
            button.setOnClickListener {
                viewModel.getImage()
            }
            image.setOnClickListener {
                openImageFullScreen()
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.imageUrlStateFlow.collect {
                        Glide.with(requireContext()).load(it).into(image)
                    }
                }
            }
        }
    }

    private fun bindLoadingIndicator(loadingIndicator: CircularProgressIndicator) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingSharedFlow.collect {
                    loadingIndicator.isVisible = it
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
            bindLoadingIndicator(loadingIndicator)
            bindOnDoneAction(textFieldNationalize) { text ->
                viewModel.getNameInCountryProbability(text)
                dialog.show(childFragmentManager, DisplayInfoDialogFragment.TAG)
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    private fun bindOnDoneAction(textField: TextInputEditText, onDone: suspend (String) -> Unit) {
        textField.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnEditorActionListener { text, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        onDone(text.text.toString())
                    }
                    return@setOnEditorActionListener true
                }
                false
            }
            isSingleLine = true
        }
    }

    private fun bindErrorToast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorSharedFlow.collect {
                    Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}