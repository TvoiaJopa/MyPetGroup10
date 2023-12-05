package com.example.mypetgroup10

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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
     override fun onResume() {
         super.onResume()

         // Start the hunger timer when the activity resumes
         hungerTimer.start()
         animationTimer.start()

     }
     val hungerTimer = object : CountDownTimer(60000, 1000) { // 60 seconds countdown, updates every 1 second
         override fun onTick(millisUntilFinished: Long) {
             // Update hunger level here
             decreaseHunger()
         }

         override fun onFinish() {
             // Handle when the timer finishes (optional)
         }
     }

     val animationTimer = object : CountDownTimer(10000, 2000) { // 10 seconds countdown, updates every 2 seconds
         override fun onTick(millisUntilFinished: Long) {
             // Trigger the animation here
             startPetAnimation()
         }

         override fun onFinish() {
             // Handle when the animation timer finishes (optional)
         }
     }

     private fun decreaseHunger() {
         val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)
         var hunger = sharedPreferences.getInt("hunger", -1)

         if (hunger > 0) {
             hunger -= 1
             // Save the updated hunger level
             val editor = sharedPreferences.edit()
             editor.putInt("hunger", hunger)
             editor.apply()

             // Update the UI
             val foodSlider: ProgressBar = findViewById(R.id.food_mainact_slider)
             foodSlider.progress = hunger
         } else {
             // Handle when the pet is starving (optional)
         }
     }
     override fun onPause() {
         super.onPause()
         hungerTimer.cancel()
         animationTimer.cancel()

     }
     private var currentDrawableId = 1

     private fun startPetAnimation() {
         val petImageView: ImageView = findViewById(R.id.imageView)

         // Create a handler to change drawables rapidly
         val handler = Handler()

         val delayMillis = 1000 // Adjust the delay as needed

         // Runnable to change drawables
         val runnable = object : Runnable {
             override fun run() {
                 // Change the drawable
                 val drawableId = getDrawableId()
                 petImageView.setImageResource(drawableId)

                 // Schedule the next drawable change
                 handler.postDelayed(this, delayMillis.toLong())
             }
         }

         // Start the initial drawable change
         handler.post(runnable)
     }

     private fun getDrawableId(): Int {
         // Get the resource ID based on the current drawable ID
         val formattedDrawableName = "lechita" + String.format("%04d", currentDrawableId)
         val drawableId = resources.getIdentifier(formattedDrawableName, "drawable", packageName)

         // Increment the drawable ID for the next iteration
         currentDrawableId++
         if (currentDrawableId > 30) {
             // Restart from the beginning when reaching the end
             currentDrawableId = 1
         }

         return drawableId
     }



     private fun updatePetImage(hunger: Int) {
         val petImageView: ImageView = findViewById(R.id.imageView)

         petImageView.setImageResource(R.drawable.lechita0001)

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

            //adds shop items here too :)
            editor.putInt("money", 0)
            editor.putInt("toy1", 0)
            editor.putInt("toy1", 0)
            editor.putInt("toy2", 0)
            editor.putInt("toy3", 0)
            editor.putInt("toy4", 0)
            editor.putInt("food1", 0)
            editor.putInt("food2", 0)
            editor.putInt("food3", 0)
            editor.putInt("food4", 0)

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
