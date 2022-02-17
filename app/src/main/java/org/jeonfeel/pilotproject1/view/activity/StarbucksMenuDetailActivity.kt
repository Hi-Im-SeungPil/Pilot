package org.jeonfeel.pilotproject1.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
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
    private val db = AppDatabase.getDbInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarbucksmenuDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getProductInfo()
        initListener()
    }

    private fun initListener() {
        binding.buttonDetailBackspace.setOnClickListener{
            val newIntent = Intent(this, MainActivity::class.java)
            newIntent.putExtra("productCD", productCD)
            newIntent.putExtra("favoriteIsChecked", favoriteIsChecked)
            setResult(RESULT_OK, newIntent)
            finish()
        }

        binding.buttonDetailFavorite.setOnClickListener{
            if (!favoriteIsChecked) {
                val thread = Thread{
                    val favorite = Favorite(starbucksMenuDTO.product_CD)
                    db.favoriteDao().insert(favorite)
                    favoriteIsChecked = true
                }
                thread.start()
            } else {
                val thread = Thread{
                    val favorite = Favorite(starbucksMenuDTO.product_CD)
                    db.favoriteDao().delete(favorite)
                    favoriteIsChecked = false
                }
                thread.start()
            }
        }
    }

    private fun getProductInfo() {
        val intent = this.intent
        starbucksMenuDTO = intent.getSerializableExtra("starbucksMenuDTO") as StarbucksMenuDTO
        productCD = intent.getStringExtra("productCD").toString()
        favoriteIsChecked = intent.getBooleanExtra("favoriteIsChecked",false)
        with(binding){
            binding.starbucksMenuDto = starbucksMenuDTO
            executePendingBindings()
        }
    }

    override fun onBackPressed() {
        val newIntent = Intent(this,MainActivity::class.java)
        newIntent.putExtra("productCD",productCD)
        newIntent.putExtra("favoriteIsSelected",productCD)
        setResult(RESULT_OK,newIntent)
        super.onBackPressed()
    }
}