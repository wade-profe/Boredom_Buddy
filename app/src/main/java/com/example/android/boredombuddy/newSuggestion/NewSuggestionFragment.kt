package com.example.android.boredombuddy.newSuggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android.boredombuddy.data.network.BoredAPI
import com.example.android.boredombuddy.databinding.NewSuggestionBinding
import kotlinx.coroutines.launch

class NewSuggestionFragment: Fragment() {

    private lateinit var newSuggestionBinding: NewSuggestionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newSuggestionBinding = NewSuggestionBinding.inflate(inflater, container, false)
        return newSuggestionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
                val result = BoredAPI.callApi.getSuggestion()
                if(result.isSuccessful){
                    Toast.makeText(requireContext(), result.body()?.activity, Toast.LENGTH_LONG).show()
                }
            }
    }
}