package org.jeonfeel.pilotproject1.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import org.jeonfeel.pilotproject1.databinding.FcmBoxActivityBinding

class FcmBoxActivity : AppCompatActivity() {

    lateinit var binding: FcmBoxActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FcmBoxActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }
}