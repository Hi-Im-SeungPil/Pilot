package org.jeonfeel.pilotproject1.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.database.AppDatabase
import org.jeonfeel.pilotproject1.data.database.entity.Favorite
import org.jeonfeel.pilotproject1.databinding.ActivityStarbucksmenuDetailBinding
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO

class StarbucksMenuDetailActivity : AppCompatActivity() {

    val TAG = "StarbucksMenuDetailActivity"
    lateinit var binding: ActivityStarbucksmenuDetailBinding
    lateinit var starbucksMenuDTO: StarbucksMenuDTO
    private var productCD: String = ""
    private var favoriteIsChecked = false
    private var favoriteIsClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarbucksmenuDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getProductInfo()
        initListener()
    }

    @Suppress("RedundantIf")
    private fun initListener() {
        binding.buttonDetailBackspace.setOnClickListener {
            if (favoriteIsClicked) {
                val newIntent = Intent(this, MainActivity::class.java)
                newIntent.putExtra("productCD", productCD)
                newIntent.putExtra("favoriteIsChecked", favoriteIsChecked)
                setResult(RESULT_OK, newIntent)
            }
            finish()
        }

        binding.buttonDetailFavorite.setOnClickListener {
            var imgRes = 0
            if (!favoriteIsChecked) {
                favoriteIsChecked = true
                imgRes = R.drawable.img_favorite_2x
            } else {
                favoriteIsChecked = false
                imgRes = R.drawable.img_favorite_unselected_2x
            }
            binding.resId = imgRes
            favoriteIsClicked = true
        }
    }

    private fun getProductInfo() {
        val intent = this.intent
        starbucksMenuDTO = intent.getSerializableExtra("starbucksMenuDTO") as StarbucksMenuDTO
        productCD = intent.getStringExtra("productCD").toString()
        favoriteIsChecked = intent.getBooleanExtra("favoriteIsChecked", false)

        with(binding) {
            binding.starbucksMenuDto = starbucksMenuDTO
            if (favoriteIsChecked) {
                binding.resId = R.drawable.img_favorite_2x
            } else {
                binding.resId = R.drawable.img_favorite_unselected_2x
            }
            executePendingBindings()
        }
    }

//    override fun onDestroy() {
//        if (favoriteIsClicked) {
//            val newIntent = Intent(this, MainActivity::class.java)
//            newIntent.putExtra("productCD", productCD)
//            newIntent.putExtra("favoriteIsSelected", favoriteIsChecked)
//            setResult(RESULT_OK, newIntent)
//        }
//        super.onDestroy()
//    }

    override fun onBackPressed() {
        if (favoriteIsClicked) {
            val newIntent = Intent(this, MainActivity::class.java)
            newIntent.putExtra("productCD", productCD)
            newIntent.putExtra("favoriteIsChecked", favoriteIsChecked)
            setResult(RESULT_OK, newIntent)
        }
        super.onBackPressed()
    }
}