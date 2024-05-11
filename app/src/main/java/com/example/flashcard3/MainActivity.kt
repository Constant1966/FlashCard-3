package com.example.flashcard3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    var currentCardDisplayedIndex = 0
    private lateinit var flashcardDatabase: FlashcardDatabase
    private var allFlashcards = mutableListOf<Flashcard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = this.findViewById<TextView>(R.id.flashcard_answer)
        val flashcardAnswer1 = findViewById<TextView>(R.id.flashcard_answer1)
        val flashcardAnswer2 = findViewById<TextView>(R.id.flashcard_answer2)

        flashcardDatabase = FlashcardDatabase(this)
        flashcardDatabase.initFirstCard()
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        if(allFlashcards.size > 0){

            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
            flashcardAnswer1.text = allFlashcards[0].wrongAnswer1
            flashcardAnswer2.text = allFlashcards[0].wrongAnswer2

        }

        val crossMain = findViewById<View>(R.id.imageButton3)
        val editbtn   = findViewById<View>(R.id.imageButton)
        val deletebtn   = findViewById<View>(R.id.imageButton4)
        val nextBtn    = findViewById<View>(R.id.imageButton1)


        nextBtn.setOnClickListener {
            currentCardDisplayedIndex = getRandomNumber(0,allFlashcards.size-1)

            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }

            currentCardDisplayedIndex++

            if (currentCardDisplayedIndex >= allFlashcards.size) {

                currentCardDisplayedIndex = 0
                Snackbar.make(findViewById(R.id.imageButton4), "No more cards!!", Snackbar.LENGTH_SHORT).show()

            }

            val (question, answer,wrongAnswer1,wrongAnswer2) = allFlashcards[currentCardDisplayedIndex]

            flashcardQuestion.text = question
            flashcardAnswer.text = answer
            flashcardAnswer1.text = wrongAnswer1
            flashcardAnswer2.text = wrongAnswer2
        }


        deletebtn.setOnClickListener {
            val currentQuestion = flashcardQuestion.text.toString()
            flashcardDatabase.deleteCard(currentQuestion)

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()

            // Vérifier s'il reste des cartes
            if (allFlashcards.isNotEmpty()) {
                // Afficher la carte précédente (si disponible)
                currentCardDisplayedIndex = max(0, currentCardDisplayedIndex - 1)
                val (question, answer,wrongAnswer1,wrongAnswer2) = allFlashcards[currentCardDisplayedIndex]
                flashcardQuestion.text = question
                flashcardAnswer.text = answer
                flashcardAnswer1.text = wrongAnswer1
                flashcardAnswer2.text = wrongAnswer2

            } else {
                // S'il n'y a plus de cartes, afficher un état vide
                flashcardQuestion.text = ""
                flashcardAnswer.text = ""
                flashcardAnswer1.text = ""
                flashcardAnswer2.text = ""

            }
        }


        flashcardAnswer.setOnClickListener {
            flashcardQuestion.visibility = View.VISIBLE
            findViewById<View>(R.id.flashcard_answer).setBackgroundColor(getResources().getColor(R.color.my_green, null))
        }

        // Click listener for the second answer option
        flashcardAnswer1.setOnClickListener {
            findViewById<View>(R.id.flashcard_answer1).setBackgroundColor(getResources().getColor(R.color.my_red_color, null))
            findViewById<View>(R.id.flashcard_answer).setBackgroundColor(getResources().getColor(R.color.my_green, null))
        }


        // Click listener for the third answer option
        flashcardAnswer2.setOnClickListener {
            findViewById<View>(R.id.flashcard_answer).setBackgroundColor(getResources().getColor(R.color.my_green, null))
            findViewById<View>(R.id.flashcard_answer2).setBackgroundColor(getResources().getColor(R.color.my_red_color, null))
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data

            if (result.resultCode == Activity.RESULT_OK && data != null) {
                val question = data.getStringExtra("question")
                val answer = data.getStringExtra("answer")
                val wrongAnswer1 = data.getStringExtra("wrongAnswer1")
                val wrongAnswer2 = data.getStringExtra("wrongAnswer2")

                // Mettre à jour les TextView dans MainActivity avec les nouvelles données

                if (question != null && answer != null) {
                    flashcardDatabase.insertCard(Flashcard(question,answer,wrongAnswer1,wrongAnswer2))

                    flashcardQuestion.text = question
                    flashcardAnswer.text = answer
                    flashcardAnswer1.text = wrongAnswer1
                    flashcardAnswer2.text = wrongAnswer2
                    // Update set of flashcards to include new card
                    //allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                } else {
                    Log.e("TAG", "Missing question or answer to input into database. Question is $question and answer is $answer")
                }

            } else {
                Log.i("AddCardActivity", "Save operation cancelled or no data returned")
            }
        }
        editbtn.setOnClickListener {
            val question = this.findViewById<TextView>(R.id.flashcard_question).text.toString()
            val answer = findViewById<TextView>(R.id.flashcard_answer).text.toString()
            val wrongAnswer1 = this.findViewById<TextView>(R.id.flashcard_answer1).text.toString()
            val wrongAnswer2 = this.findViewById<TextView>(R.id.flashcard_answer2).text.toString()

            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("question", question)
            intent.putExtra("answer", answer)
            intent.putExtra("wrongAnswer1", wrongAnswer1)
            intent.putExtra("wrongAnswer2", wrongAnswer2)
            resultLauncher.launch(intent)
        }

        crossMain.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

    }
    private fun getRandomNumber(minNumber: Int, maxNumber: Int): Int {
        return (minNumber..maxNumber).random()
    }

}