package com.example.android.boredombuddy.setNotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.databinding.FragmentSetNotificationBinding
import com.example.android.boredombuddy.databinding.NewSuggestionBinding

class SetNotification : Fragment() {

    private lateinit var setNotificationBinding: FragmentSetNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setNotificationBinding = FragmentSetNotificationBinding.inflate(inflater, container, false)
        val suggestion = SetNotificationArgs.fromBundle(requireArguments()).suggestion
        setNotificationBinding.suggestion = suggestion
        return setNotificationBinding.root
    }
}