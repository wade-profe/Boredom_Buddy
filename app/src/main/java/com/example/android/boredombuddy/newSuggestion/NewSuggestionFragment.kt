package com.example.android.boredombuddy.newSuggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.boredombuddy.databinding.NewSuggestionBinding
import org.koin.android.ext.android.inject

class NewSuggestionFragment: Fragment() {

    private lateinit var newSuggestionBinding: NewSuggestionBinding
    private val viewModel: NewSuggestionViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newSuggestionBinding = NewSuggestionBinding.inflate(inflater, container, false)
        newSuggestionBinding.lifecycleOwner = this
        newSuggestionBinding.viewModel = viewModel

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        return newSuggestionBinding.root
    }
}