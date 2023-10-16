package com.example.android.boredombuddy.setNotification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.databinding.FragmentSetNotificationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetNotification : Fragment() {

    private lateinit var setNotificationBinding: FragmentSetNotificationBinding
    val viewModel: SetNotificationViewModel by viewModel()
    private lateinit var suggestion: Suggestion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setNotificationBinding = FragmentSetNotificationBinding.inflate(inflater, container, false)
        setNotificationBinding.lifecycleOwner = this
        setNotificationBinding.viewModel = viewModel
        suggestion = SetNotificationArgs.fromBundle(requireArguments()).suggestion
        setNotificationBinding.suggestion = suggestion

        viewModel.launchTimePicker.observe(viewLifecycleOwner) {
            if(it){
                TimePickerFragment().show(childFragmentManager, TimePickerFragment.TAG)
                viewModel.timePickerLaunched()
            }
        }

        // TODO Fix set reminder views positioning

        return setNotificationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNotificationBinding.chooseTime.setOnClickListener {
            DatePickerFragment().show(childFragmentManager, DatePickerFragment.TAG)
        }

        setNotificationBinding.setNotification.setOnClickListener {
            if(viewModel.scheduleNotification(requireContext().applicationContext, suggestion)){
                findNavController().popBackStack()
            }
        }
    }
}