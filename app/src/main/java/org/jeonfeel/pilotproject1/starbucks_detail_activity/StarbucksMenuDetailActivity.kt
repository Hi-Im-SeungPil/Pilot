package org.jeonfeel.pilotproject1.starbucks_detail_activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import org.jeonfeel.pilotproject1.databinding.ActivityStarbucksmenuDetailBinding
import org.jeonfeel.pilotproject1.mainactivity.StarbucksMenuDTO

class StarbucksMenuDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityStarbucksmenuDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarbucksmenuDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getProductInfo()
        initListener()
    }

    private fun initListener() {
        binding.buttonDetailBackspace.setOnClickListener{
            finish()
        }
    }

    private fun getProductInfo() {
        val intent = this.intent
        val starbucksMenuDTO = intent.getSerializableExtra("starbucksMenuDTO") as StarbucksMenuDTO
        with(binding){
            binding.starbucksMenuDto = starbucksMenuDTO
            executePendingBindings()
        }
    }
}