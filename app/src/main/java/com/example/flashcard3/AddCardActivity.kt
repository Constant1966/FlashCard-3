package com.example.flashcard3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class AddCardActivity : AppCompatActivity() {
    private lateinit var flashcardDatabase: FlashcardDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display
        setContentView(R.layout.activity_add_card) // Set the layout for this activity
        flashcardDatabase = FlashcardDatabase(this) // Initialize the database

        // Find the views in the layout
        val crossAddCard = findViewById<ImageView>(R.id.imageView4)
        val savebtn = findViewById<ImageView>(R.id.imageView5)
        val editTextField =findViewById<EditText>(R.id.editTextText)
        val editTextField1 =findViewById<EditText>(R.id.editTextText2)
        val editTextField2 =findViewById<EditText>(R.id.editTextText3)
        val editTextField3 =findViewById<EditText>(R.id.editTextText4)

        // Set click listener for the cross button to go back to the main activity
        crossAddCard.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        // Retrieve data passed from intent and set it in the EditText fields
        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")
        val wrongAnswer1 = intent.getStringExtra("wrongAnswer1")
        val wrongAnswer2 = intent.getStringExtra("wrongAnswer2")
        editTextField.setText(question)
        editTextField1.setText(answer)
        editTextField2.setText(wrongAnswer1)
        editTextField3.setText(wrongAnswer2)

        // Set click listener for the save button
        savebtn.setOnClickListener {
            val question1 = editTextField.text.toString()
            val answer0 = editTextField1.text.toString()
            val wrongAnswer = editTextField2.text.toString()
            val wrongAnswer0 = editTextField3.text.toString()

            // Check if question and answer fields are not blank
            if(question1.isBlank() || answer0.isBlank()){
                Snackbar.make(findViewById(R.id.imageView5), "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            } else {
                // Show success message
                Snackbar.make(findViewById(R.id.imageView5), "Card successfully created", Snackbar.LENGTH_SHORT).show()

                // Create intent to pass data back to main activity
                val data = Intent()
                data.putExtra("question", question1)
                data.putExtra("answer", answer0)
                data.putExtra("wrongAnswer1", wrongAnswer)
                data.putExtra("wrongAnswer2", wrongAnswer0)

                // Insert card into the database
                flashcardDatabase.insertCard(Flashcard(question1, answer0, wrongAnswer, wrongAnswer0))

                // Set result and finish the activity
                setResult(Activity.RESULT_OK, intent)
                val intent1 = Intent(this, MainActivity::class.java)
                startActivity(intent1)
                finish()
            }
        }
    }
}
