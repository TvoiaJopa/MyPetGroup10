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
import android.widget.TextView

class PlayRoom : AppCompatActivity() {

    private lateinit var healthBar: ProgressBar
    private var currentHealth: Int = 100 // Initialize with default health

    private lateinit var toy1Text: TextView
    private lateinit var toy2Text: TextView
    private lateinit var toy3Text: TextView
    private lateinit var toy4Text: TextView

    private var toy1: Int = 3 // Initial toy quantity for toy1
    private var toy2: Int = 5 // Initial toy quantity for toy2
    private var toy3: Int = 2 // Initial toy quantity for toy3
    private var toy4: Int = 4 // Initial toy quantity for toy4

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

        // Toy TextView initialization
        toy1Text = findViewById(R.id.toy1Text)
        toy2Text = findViewById(R.id.toy2Text)
        toy3Text = findViewById(R.id.toy3Text)
        toy4Text = findViewById(R.id.toy4Text)

        // Retrieve toy quantities from SharedPreferences
        retrieveToyQuantitiesFromSharedPreferences()

        // Display initial toy counts
        updateToyCounts()

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

        // Toy buttons click listeners
        val toy1Button: ImageView = findViewById(R.id.toy1)
        toy1Button.setOnClickListener {
            clickToyButton(1)
        }

        val toy2Button: ImageView = findViewById(R.id.toy2)
        toy2Button.setOnClickListener {
            clickToyButton(2)
        }

        val toy3Button: ImageView = findViewById(R.id.toy3)
        toy3Button.setOnClickListener {
            clickToyButton(3)
        }

        val toy4Button: ImageView = findViewById(R.id.toy4)
        toy4Button.setOnClickListener {
            clickToyButton(4)
        }

        // Start the health timer when the activity starts
        startHealthTimer()
    }

    private fun updateToyCounts() {
        toy1Text.text = getString(R.string.toy_count, toy1)
        toy2Text.text = getString(R.string.toy_count, toy2)
        toy3Text.text = getString(R.string.toy_count, toy3)
        toy4Text.text = getString(R.string.toy_count, toy4)
    }

    private fun clickToyButton(toyId: Int) {
        when (toyId) {
            1 -> {
                if (toy1 > 0) {
                    increaseHealthBy(50)
                    decreaseToyQuantity(1)
                    updateToyCounts() // Update toy counts after decreasing toy1
                }
            }
            2 -> {
                if (toy2 > 0) {
                    increaseHealthBy(10)
                    decreaseToyQuantity(2)
                    updateToyCounts() // Update toy counts after decreasing toy2
                }
            }
            3 -> {
                if (toy3 > 0) {
                    increaseHealthBy(25)
                    decreaseToyQuantity(3)
                    updateToyCounts() // Update toy counts after decreasing toy3
                }
            }
            4 -> {
                if (toy4 > 0) {
                    increaseHealthBy(40)
                    decreaseToyQuantity(4)
                    updateToyCounts() // Update toy counts after decreasing toy4
                }
            }
        }
    }



    private fun decreaseToyQuantity(toyId: Int) {
        when (toyId) {
            1 -> {
                if (toy1 > 0) {
                    toy1--
                    saveToyQuantityToSharedPreferences("toy1", toy1)
                }
            }
            2 -> {
                if (toy2 > 0) {
                    toy2--
                    saveToyQuantityToSharedPreferences("toy2", toy2)
                }
            }
            3 -> {
                if (toy3 > 0) {
                    toy3--
                    saveToyQuantityToSharedPreferences("toy3", toy3)
                }
            }
            4 -> {
                if (toy4 > 0) {
                    toy4--
                    saveToyQuantityToSharedPreferences("toy4", toy4)
                }
            }
        }
        updateToyButtons() // Update toy buttons after decreasing toy quantities
        updateToyCounts() // Update toy counts after decreasing toy quantities
    }

    private fun saveToyQuantityToSharedPreferences(toyKey: String, quantity: Int) {
        val sharedPreferences = getSharedPreferences("your_game_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(toyKey, quantity)
        editor.apply()
    }

    private fun retrieveToyQuantitiesFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("your_game_prefs", MODE_PRIVATE)
        toy1 = sharedPreferences.getInt("toy1", 26)
        toy2 = sharedPreferences.getInt("toy2", 39)
        toy3 = sharedPreferences.getInt("toy3", 21)
        toy4 = sharedPreferences.getInt("toy4", 47)

        // Manually update the toy quantities to larger values if required
        //toy1 = 50
        //toy2 = 60
        //toy3 = 30
       //toy4 = 70

        // Save the updated toy quantities to SharedPreferences
        saveToyQuantityToSharedPreferences("toy1", toy1)
        saveToyQuantityToSharedPreferences("toy2", toy2)
        saveToyQuantityToSharedPreferences("toy3", toy3)
        saveToyQuantityToSharedPreferences("toy4", toy4)
    }

    private fun updateToyButtons() {
        val toy1Button: ImageView = findViewById(R.id.toy1)
        toy1Button.isEnabled = toy1 > 0

        val toy2Button: ImageView = findViewById(R.id.toy2)
        toy2Button.isEnabled = toy2 > 0

        val toy3Button: ImageView = findViewById(R.id.toy3)
        toy3Button.isEnabled = toy3 > 0

        val toy4Button: ImageView = findViewById(R.id.toy4)
        toy4Button.isEnabled = toy4 > 0
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

