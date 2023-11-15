package com.example.mypetgroup10

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar


 class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Set data

        // Save data
        getData()


        val shopButton: Button = findViewById(R.id.shop_b)
        shopButton.setOnClickListener {
            Log.d("MyApp", "shop_b ")

            SetNewScreen(Screens.Shop)
        }

        val walkButton: Button = findViewById(R.id.walk_b)
        walkButton.setOnClickListener {
            Log.d("MyApp", "walk_b")

            SetNewScreen(Screens.Walk)
        }

        val playroomButton: Button = findViewById(R.id.playroom_b)
        playroomButton.setOnClickListener {
            Log.d("MyApp", "playroom_b " )

            SetNewScreen(Screens.PlayRoom)
        }

        val foodButton: Button = findViewById(R.id.food_b)
        foodButton.setOnClickListener {
            Log.d("MyApp", "foodButton " )

            giveFood()
        }

    }

    private fun getData() {
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)

        // Retrieve data with default values
        var hunger = sharedPreferences.getInt("hunger", -1)
        var hungerMax = sharedPreferences.getInt("hungerMax", -1)
        val playerName = sharedPreferences.getString("player_name", "") ?: ""

        // Check if values exist
        if (hunger == -1 || hungerMax == -1 || playerName.isEmpty()) {
            // Values don't exist, so add them
            val editor = sharedPreferences.edit()
            editor.putInt("hunger", 50)
            hunger = 50
            editor.putInt("hungerMax", 100)
            hungerMax = 100
            editor.putString("player_name", "John Doe")
            editor.apply()
        }

        val foodSlider: ProgressBar = findViewById(R.id.food_mainact_slider)
        foodSlider.max = hungerMax
        foodSlider.progress = hunger
    }

    private fun giveFood() {
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)
        var hunger = sharedPreferences.getInt("hunger", -1)
        var hungerMax = sharedPreferences.getInt("hungerMax", -1)

        if (hunger > 100) {
            hunger = 100;
        } else if (hunger + 25 > 100) {
            hunger = 100;

        } else {
            hunger += 25

        }

        val editor = sharedPreferences.edit()
        editor.putInt("hunger", hunger)
        editor.apply()

        val foodSlider: ProgressBar = findViewById(R.id.food_mainact_slider)
        foodSlider.max = hungerMax
        foodSlider.progress = hunger
    }
    private fun SetNewScreen(screen: Screens) {
        // Implement the logic to switch to the selected screen based on the 'screen' parameter.
        Log.d("MyApp", "Screen " + screen)
        when (screen) {
            Screens.Home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)            }
            Screens.Shop -> {

                val intent = Intent(this, ShopScreen::class.java)
                startActivity(intent)
            }
            Screens.PlayRoom -> {
                val intent = Intent(this,PlayRoom::class.java)
                startActivity(intent)
            }
            Screens.Walk -> {
                val intent = Intent(this, WalkScreen::class.java)
                startActivity(intent)
            }
        }
    }

}

enum class Screens {
    Home,
    Shop,
    PlayRoom,
    Walk
}
