package com.example.rit.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rit.R
import com.example.rit.databinding.FragmentSettingsBinding
import com.example.rit.databinding.ItemChooseApiBinding
import com.example.rit.utils.Constants
import com.example.rit.utils.restoreChosenApi
import com.example.rit.utils.setChosenApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var checkedIndex = MutableStateFlow(-1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkedIndex.value = requireContext().restoreChosenApi()
        viewLifecycleOwner.lifecycleScope.launch {
            checkedIndex.collect {
                bindUi()
            }
        }
    }

    private fun bindUi() {
        with(binding) {
            toolbar.apply {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
            }
            bindRow(rowDogApi, "DOG API", Constants.Api.DOG)
            bindRow(rowNationalizeApi, "NATIONALIZE API", Constants.Api.NATIONALIZE)
            bindRow(rowCustomApi, "CUSTOM API", Constants.Api.CUSTOM)
        }
    }

    private fun bindRow(
        row: ItemChooseApiBinding,
        text: String,
        api: Constants.Api,
    ) {
        row.apply {
            checkBox.isChecked = checkedIndex.value == api.ordinal
            checkBox.setOnClickListener { onRowClick(text, api) }
            textView.text = text
            root.setOnClickListener { onRowClick(text, api) }
        }
    }

    private fun onRowClick(text: String, api: Constants.Api) {
        checkedIndex.value = api.ordinal
        requireContext().setChosenApi(api)
        makeToast(text)
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), "Api changed to: $text", Toast.LENGTH_SHORT).show()
    }
}