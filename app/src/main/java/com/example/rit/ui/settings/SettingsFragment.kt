package com.example.rit.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.rit.R
import com.example.rit.databinding.FragmentSettingsBinding
import com.example.rit.databinding.ItemChooseApiBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

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
        bindUi()

    }

    private fun bindUi() {
        with(binding) {
            toolbar.apply {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
            }
            bindRow(rowDogApi, "DOG API")
            bindRow(rowNationalizeApi, "NATIONALIZE API")
            bindRow(rowCustomApi, "CUSTOM API")
        }
    }

    private fun bindRow(row: ItemChooseApiBinding, text: String, onClick: () -> Unit = {}) {
        row.apply {
            textView.text = text
            root.setOnClickListener { onClick() }
        }
    }
}