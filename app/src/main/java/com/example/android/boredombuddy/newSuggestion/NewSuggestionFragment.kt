package com.example.android.boredombuddy.newSuggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.boredombuddy.MainActivity
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.databinding.NewSuggestionBinding
import com.example.android.boredombuddy.utils.InternetMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private object PreferencesKeys {
    val FIRST_TIME = booleanPreferencesKey("first_time")
}

data class FirstTime(val firstTime: Boolean)

class NewSuggestionFragment : Fragment() {

    private lateinit var newSuggestionBinding: NewSuggestionBinding
    private val viewModel: NewSuggestionViewModel by inject()
    private val datastore: DataStore<Preferences> by inject()
    private val userPreferencesFlow: Flow<FirstTime> = datastore.data
        .map { preferences ->
            val firstTime = preferences[PreferencesKeys.FIRST_TIME] ?: true
            FirstTime(firstTime)
        }
    private var newSuggestionLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newSuggestionBinding = NewSuggestionBinding.inflate(inflater, container, false)
        newSuggestionBinding.viewModel = viewModel
        newSuggestionBinding.lifecycleOwner = this

        return newSuggestionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = (activity as MainActivity)

        InternetMonitor(requireContext()).observe(viewLifecycleOwner) {
            viewModel.isConnected = it
        }

        viewModel.resultMessage.observe(viewLifecycleOwner) {
            when(it){
                ResultMessage.NEW_SUGGESTION_FAILURE -> {
                    activity.postToast(getString(R.string.error_retrieving_suggestion))
                }
                ResultMessage.GET_IMAGE_FAILURE -> activity.postToast(getString(R.string.error_retrieving_image))
                ResultMessage.CONNECTION_FAILURE -> activity.postToast(getString(R.string.internet_connection_required))
                else -> {}
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            newSuggestionLoading = it
            if (newSuggestionLoading) {
                newSuggestionBinding.motionLayout.progress = 0f
                newSuggestionBinding.motionLayout.transitionToEnd()
            }
        }

        newSuggestionBinding.motionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (newSuggestionLoading) {
                    motionLayout?.progress = 0f
                    motionLayout?.transitionToEnd()
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userPreferencesFlow.collect()
                { firstTime ->
                    if (firstTime.firstTime) {
                        viewModel.getNewSuggestion()
                        datastore.edit {
                            it[PreferencesKeys.FIRST_TIME] = false
                        }
                    }
                }
            }
        }
    }
}