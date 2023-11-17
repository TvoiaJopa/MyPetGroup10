package com.example.mypetgroup10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.ProgressBar

class PlayRoom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_room)

        // Initialize and update ProgressBar
        val healthBar: ProgressBar = findViewById(R.id.healthBar)
        var currentHealth = 10  // replace this with your pet's current health
        healthBar.progress = currentHealth

        // Initialize toys and set onClickListeners
        val toy1: ImageView = findViewById(R.id.toy1)
        toy1.setOnClickListener {
            currentHealth += 5  // replace 10 with the health you want to add
            if (currentHealth > 100) {
                currentHealth = 100
            }
            healthBar.progress = currentHealth
        }
        val toy2: ImageView = findViewById(R.id.toy2)
        toy2.setOnClickListener {
            currentHealth += 10  // replace 10 with the health you want to add
            if (currentHealth > 100) {
                currentHealth = 100
            }
            healthBar.progress = currentHealth
        }
        val toy3: ImageView = findViewById(R.id.toy3)
        toy3.setOnClickListener {
            currentHealth += 25
            if (currentHealth > 100) {
                currentHealth = 100
            }
            healthBar.progress = currentHealth
        }
        val toy4: ImageView = findViewById(R.id.toy4)
        toy4.setOnClickListener {
            currentHealth += 50
            if (currentHealth > 100) {
                currentHealth = 100
            }
            healthBar.progress = currentHealth
        }


        val homeButton: FloatingActionButton = findViewById(R.id.gohome_b)
        homeButton.setOnClickListener {
            Log.d("MyApp", "shop_b")
            SetNewScreen(Screens.Home)
        }
        val walkButton: FloatingActionButton = findViewById(R.id.gowalk_b)
        walkButton.setOnClickListener {
            Log.d("MyApp", "shop_b")
            SetNewScreen(Screens.Walk)
        }
        val shopButton: FloatingActionButton = findViewById(R.id.goshop_b)
        shopButton.setOnClickListener {
            Log.d("MyApp", "shop_b")
            SetNewScreen(Screens.Shop)
        }

    }

    private fun SetNewScreen(screen: Screens) {
        // Implement the logic to switch to the selected screen based on the 'screen' parameter.
        Log.d("MyApp", "Screen " + screen)
        when (screen) {
            Screens.Home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            Screens.Shop -> {
                val intent = Intent(this, ShopScreen::class.java)
                startActivity(intent)
            }
            Screens.PlayRoom -> {
                val intent = Intent(this, PlayRoom::class.java)
                startActivity(intent)
            }
            Screens.Walk -> {
                val intent = Intent(this, WalkScreen::class.java)
                startActivity(intent)
            }
        }
    }


}

