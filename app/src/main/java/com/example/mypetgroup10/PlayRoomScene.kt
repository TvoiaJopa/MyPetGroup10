package com.example.mypetgroup10

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.ProgressBar

class PlayRoom : AppCompatActivity() {

    private lateinit var healthBar: ProgressBar
    private var currentHealth: Int = 100 // Initialize with default health

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_room)

        // Get the petImageView
        val petImageView: ImageView = findViewById(R.id.petImageView)

        // Create an AnimationDrawable
        val animationDrawable = AnimationDrawable()

        // Add frames to the AnimationDrawable
        for (i in 1..30) {
            val drawableId = resources.getIdentifier(
                "lechita" + String.format("%04d", i),
                "drawable",
                packageName
            )
            val frame = resources.getDrawable(drawableId, null)
            animationDrawable.addFrame(frame, 100) // Adjust duration as needed
        }

        // Set the AnimationDrawable to the ImageView
        petImageView.setImageDrawable(animationDrawable)

        // Start the animation
        animationDrawable.start()

        // Initialize and update ProgressBar
        healthBar = findViewById(R.id.healthBar)
        currentHealth = retrieveHealthFromSharedPreferences()
        healthBar.progress = currentHealth

        // Initialize toys and set onClickListeners
        val toy1: ImageView = findViewById(R.id.toy1)
        toy1.setOnClickListener {
            increaseHealthBy(5)
        }

        val toy2: ImageView = findViewById(R.id.toy2)
        toy2.setOnClickListener {
            increaseHealthBy(10)
        }

        val toy3: ImageView = findViewById(R.id.toy3)
        toy3.setOnClickListener {
            increaseHealthBy(25)
        }

        val toy4: ImageView = findViewById(R.id.toy4)
        toy4.setOnClickListener {
            increaseHealthBy(50)
        }

        // Set onClickListeners for buttons
        val homeButton: FloatingActionButton = findViewById(R.id.gohome_b)
        homeButton.setOnClickListener {
            SetNewScreen(Screens.Home)
        }

        val walkButton: FloatingActionButton = findViewById(R.id.gowalk_b)
        walkButton.setOnClickListener {
            SetNewScreen(Screens.Walk)
        }

        val shopButton: FloatingActionButton = findViewById(R.id.goshop_b)
        shopButton.setOnClickListener {
            SetNewScreen(Screens.Shop)
        }

        // Start the health timer when the activity starts
        startHealthTimer()
    }

    private fun startHealthTimer() {
        val healthTimer = object : CountDownTimer(60000, 1000) { // 60 seconds countdown, updates every 1 second
            override fun onTick(millisUntilFinished: Long) {
                decreaseHealth()
            }

            override fun onFinish() {
                // Handle when the timer finishes (optional)
            }
        }
        healthTimer.start()
    }

    private fun decreaseHealth() {
        // Decrease the health periodically
        currentHealth -= 5 // Decrease health by 5 every 1 second (adjust as needed)

        if (currentHealth <= 0) {
            currentHealth = 0
            // Handle when health reaches zero (optional)
        }

        saveHealthToSharedPreferences(currentHealth)
        healthBar.progress = currentHealth
    }

    private fun increaseHealthBy(value: Int) {
        currentHealth += value
        if (currentHealth > 100) {
            currentHealth = 100
        }
        saveHealthToSharedPreferences(currentHealth)
        healthBar.progress = currentHealth
    }

    private fun saveHealthToSharedPreferences(health: Int) {
        val sharedPreferences = getSharedPreferences("your_game_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("health", health)
        editor.apply()
    }

    private fun retrieveHealthFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("your_game_prefs", MODE_PRIVATE)
        return sharedPreferences.getInt("health", 100) // Default health is 100 if not found
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

