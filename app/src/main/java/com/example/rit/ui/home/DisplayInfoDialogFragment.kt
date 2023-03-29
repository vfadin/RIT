package com.example.rit.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DisplayInfoDialogFragment(private val message: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .create()

    companion object {
        const val TAG = "DisplayInfoDialog"
    }
}