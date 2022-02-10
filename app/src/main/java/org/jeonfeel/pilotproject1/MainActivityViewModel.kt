package org.jeonfeel.pilotproject1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
//뷰 모델 추후 설계
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val productInfoLiveData: MutableLiveData<List<StarbucksMenuDTO>> by lazy {
        MutableLiveData<List<StarbucksMenuDTO>>().also {
            loadProductInfo()
        }
    }

    private fun loadProductInfo(){
    }
}