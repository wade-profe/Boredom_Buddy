package com.example.android.boredombuddy.newSuggestion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.boredombuddy.databinding.NewSuggestionBinding
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
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

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
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