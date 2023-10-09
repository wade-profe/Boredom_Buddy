package com.example.android.boredombuddy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.boredombuddy.data.SuggestionRepository

open class MainViewModel(repository: SuggestionRepository): ViewModel() {

    val message: LiveData<String> = repository.message

}