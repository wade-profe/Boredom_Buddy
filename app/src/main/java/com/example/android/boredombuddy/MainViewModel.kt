package com.example.android.boredombuddy

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.boredombuddy.utils.SingleLiveEvent

class MainViewModel: ViewModel() {

    init {
        Log.d("WADE", "MainViewModel initialized: $this")
    }

    val message = SingleLiveEvent<String>()

}