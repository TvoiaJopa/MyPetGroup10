package com.example.mypetgroup10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button


public class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
