package com.example.mypetgroup10

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ShopScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_screen)
        Log.d("MyApp", "ShopScreen start")

        //gets data for use and updates a few text views to show them
        dataSetup()
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)

        val navigateToPlayRoomButton: Button = findViewById(R.id.back_b_shoop)
        navigateToPlayRoomButton.setOnClickListener {
            SetNewScreen(Screens.Home)
        }
        //dev use money reset button
        val moneyButton: Button = findViewById((R.id.button2))
        moneyButton.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.putInt("money", 100)
            editor.apply()
        }

        //item buy buttons
        val buyFoodItem1: FloatingActionButton = findViewById(R.id.floatingActionButton)
        buyFoodItem1.setOnClickListener{
            popupFunc(5 ,1)
        }
        val buyFoodItem2: FloatingActionButton = findViewById(R.id.floatingActionButton3)
        buyFoodItem2.setOnClickListener{
            popupFunc(10 ,2)
        }
        val buyFoodItem3: FloatingActionButton = findViewById(R.id.floatingActionButton2)
        buyFoodItem3.setOnClickListener{
            popupFunc(15,3)
        }
        val buyFoodItem4: FloatingActionButton = findViewById(R.id.floatingActionButton4)
        buyFoodItem4.setOnClickListener{
            popupFunc(20,4)
        }
        val buyToyItem1: FloatingActionButton = findViewById(R.id.floatingActionButton5)
        buyToyItem1.setOnClickListener{
            popupFunc(5,5)
        }
        val buyToyItem2: FloatingActionButton = findViewById(R.id.floatingActionButton8)
        buyToyItem2.setOnClickListener{
            popupFunc(10,6)
        }
        val buyToyItem3: FloatingActionButton = findViewById(R.id.floatingActionButton7)
        buyToyItem3.setOnClickListener{
            popupFunc(15,7)
        }
        val buyToyItem4: FloatingActionButton = findViewById(R.id.floatingActionButton6)
        buyToyItem4.setOnClickListener{
            popupFunc(20,8)
        }

    }
    private fun dataSetup() {
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)

        // Retrieve data with default values
        var money = sharedPreferences.getInt("money", 0)
        var toy1 = sharedPreferences.getInt("toy1", 0)
        var toy2 = sharedPreferences.getInt("toy2", 0)
        var toy3 = sharedPreferences.getInt("toy3", 0)
        var toy4 = sharedPreferences.getInt("toy4", 0)
        var food1 = sharedPreferences.getInt("food1", 0)
        var food2 = sharedPreferences.getInt("food2", 0)
        var food3 = sharedPreferences.getInt("food3", 0)
        var food4 = sharedPreferences.getInt("food4", 0)

        //set textViews
        val textView8 : TextView = findViewById(R.id.textView8)
        textView8.text = "$money"
    }

    //shows a popup to ask weather or not to buy this item
    private fun popupFunc(cost: Int, id: Int) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirm buy")
        builder.setMessage("Are you sure you want to buy this item?")

        builder.setPositiveButton("Yes") { dialog, which ->
            itemPurchased(cost, id)
            dialog.dismiss()
        }

        builder.setNegativeButton(
            "No"
        ) { dialog, which ->
            //does nothing
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    //function to buy the item
    private fun itemPurchased(cost: Int, id: Int) {
        val sharedPreferences = getSharedPreferences("your_game_prefs", Context.MODE_PRIVATE)
        var money = sharedPreferences.getInt("money", 0)

        if (money >= cost) {
            if (id == 1) {
                var amount = sharedPreferences.getInt("food1", 0)

                //does the buying
                money -= cost
                amount += 1

                //saves the buying
                val editor = sharedPreferences.edit()
                editor.putInt("money", money)
                editor.putInt("food1", amount)
                editor.apply()

                //updates money textview
                val textView8: TextView = findViewById(R.id.textView8)
                textView8.text = "$money"
            }
            if (id == 2) {
                var amount = sharedPreferences.getInt("food2", 0)

                //does the buying
                money -= cost
                amount += 1

                //saves the buying
                val editor = sharedPreferences.edit()
                editor.putInt("money", money)
                editor.putInt("food2", amount)
                editor.apply()

                //updates money textview
                val textView8: TextView = findViewById(R.id.textView8)
                textView8.text = "$money"
            }
            if (id == 3) {
                var amount = sharedPreferences.getInt("food3", 0)

                //does the buying
                money -= cost
                amount += 1

                //saves the buying
                val editor = sharedPreferences.edit()
                editor.putInt("money", money)
                editor.putInt("food3", amount)
                editor.apply()

                //updates money textview
                val textView8: TextView = findViewById(R.id.textView8)
                textView8.text = "$money"
            }
            if (id == 4) {
                var amount = sharedPreferences.getInt("food4", 0)
                //does the buying
                money -= cost
                amount += 1

                //saves the buying
                val editor = sharedPreferences.edit()
                editor.putInt("money", money)
                editor.putInt("food4", amount)
                editor.apply()

                //updates money textview
                val textView8: TextView = findViewById(R.id.textView8)
                textView8.text = "$money"
            }
            if (id == 5) {
                var amount = sharedPreferences.getInt("toy1", 0)
                if (amount <= 0) {
                    //does the buying
                    money -= cost
                    amount += 1

                    //saves the buying
                    val editor = sharedPreferences.edit()
                    editor.putInt("money", money)
                    editor.putInt("toy1", amount)
                    editor.apply()

                    //updates money textview
                    val textView8: TextView = findViewById(R.id.textView8)
                    textView8.text = "$money"
                }
            }
            if (id == 6) {
                var amount = sharedPreferences.getInt("toy2", 0)
                if (amount <= 0) {
                    //does the buying
                    money -= cost
                    amount += 1

                    //saves the buying
                    val editor = sharedPreferences.edit()
                    editor.putInt("money", money)
                    editor.putInt("toy2", amount)
                    editor.apply()

                    //updates money textview
                    val textView8: TextView = findViewById(R.id.textView8)
                    textView8.text = "$money"
                }
            }
            if (id == 7) {
                var amount = sharedPreferences.getInt("toy3", 0)
                if (amount <= 0) {
                    //does the buying
                    money -= cost
                    amount += 1

                    //saves the buying
                    val editor = sharedPreferences.edit()
                    editor.putInt("money", money)
                    editor.putInt("toy3", amount)
                    editor.apply()

                    //updates money textview
                    val textView8: TextView = findViewById(R.id.textView8)
                    textView8.text = "$money"
                }
            }
            if (id == 8) {
                var amount = sharedPreferences.getInt("toy4", 0)
                if (amount >= 0) {
                    //does the buying
                    money -= cost
                    amount += 1

                    //saves the buying
                    val editor = sharedPreferences.edit()
                    editor.putInt("money", money)
                    editor.putInt("toy4", amount)
                    editor.apply()

                    //updates money textview
                    val textView8: TextView = findViewById(R.id.textView8)
                    textView8.text = "$money"
                }
            }
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
