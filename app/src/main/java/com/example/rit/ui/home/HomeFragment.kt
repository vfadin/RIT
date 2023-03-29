package com.example.rit.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.rit.R
import com.example.rit.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private var dialog = PurchaseConfirmationDialogFragment("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.nameCountryStateFlow.collect { list ->
                    list?.let {
                        dialog = PurchaseConfirmationDialogFragment(
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
            textField.apply {
                imeOptions = EditorInfo.IME_ACTION_DONE
                setOnEditorActionListener { text, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.getNameInCountryProbability(text.text.toString())
                            dialog.show(childFragmentManager, PurchaseConfirmationDialogFragment.TAG)
                        }
                        return@setOnEditorActionListener true
                    }
                    false
                }
                isSingleLine = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PurchaseConfirmationDialogFragment(private val message: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}