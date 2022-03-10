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

    val TAG = StarbucksMenuDetailActivity::class.java.simpleName
    lateinit var binding: ActivityStarbucksmenuDetailBinding
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

    /**
     * 리스너
     * */
    @Suppress("RedundantIf")
    private fun initListener() {
        binding.btnDetailBackspace.setOnClickListener {
            setResult()
            finish()
        }

        binding.btnDetailFavorite.setOnClickListener {
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
        val starbucksMenuDTO = intent.getSerializableExtra("starbucksMenuDTO") as StarbucksMenuDTO
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

    private fun setResult() {
        if (favoriteIsClicked) {
            val resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra("productCD", productCD)
            resultIntent.putExtra("favoriteIsChecked", favoriteIsChecked)
            setResult(RESULT_OK, resultIntent)
        }
    }

    override fun onBackPressed() {
        setResult()
        super.onBackPressed()
    }
}