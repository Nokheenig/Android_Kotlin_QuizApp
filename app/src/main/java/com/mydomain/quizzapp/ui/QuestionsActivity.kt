package com.mydomain.quizzapp.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mydomain.quizzapp.R
import com.mydomain.quizzapp.model.Question
import com.mydomain.quizzapp.utils.Constants

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewProgress: TextView
    private lateinit var textViewQuestion: TextView
    private lateinit var flagImage: ImageView

    private lateinit var textViewOption1: TextView
    private lateinit var textViewOption2: TextView
    private lateinit var textViewOption3: TextView
    private lateinit var textViewOption4: TextView

    private lateinit var checkButton: Button

    private var questionsCounter = 0
    private lateinit var questionsList: MutableList<Question>

    private var selectedAnswer = 0
    private lateinit var currentQuestion: Question
    private var answered = false
    private lateinit var name: String
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        progressBar = findViewById(R.id.progressBar)
        textViewProgress = findViewById(R.id.text_view_progress)
        textViewQuestion = findViewById(R.id.question_text_view)
        flagImage = findViewById(R.id.image_flag)

        textViewOption1 = findViewById(R.id.text_view_option_one)
        textViewOption2 = findViewById(R.id.text_view_option_two)
        textViewOption3 = findViewById(R.id.text_view_option_three)
        textViewOption4 = findViewById(R.id.text_view_option_four)

        checkButton = findViewById(R.id.button_check)

        textViewOption1.setOnClickListener(this)
        textViewOption2.setOnClickListener(this)
        textViewOption3.setOnClickListener(this)
        textViewOption4.setOnClickListener(this)
        checkButton.setOnClickListener(this)

        questionsList = Constants.getQuestions()
        progressBar.max = questionsList.size
        Log.d("QuestionsSize", "${questionsList.size}")

        showNextQuestion()

        if(intent.hasExtra(Constants.USER_NAME)){
            name = intent.getStringExtra(Constants.USER_NAME)!!
        }
    }

    private fun showNextQuestion() {
        Log.d("App Starting", "Hey its me, 'showNextQuestion'")

        if (questionsCounter < questionsList.size) {
            checkButton.text = "CHECK"

            resetOptions()
            val question  = questionsList[questionsCounter]
            flagImage.setImageResource(question.image)
            progressBar.progress = questionsCounter +1
            textViewProgress.text = "${questionsCounter +1}/${progressBar.max}"
            textViewQuestion.text = question.question
            textViewOption1.text = question.optionOne
            textViewOption2.text = question.optionTwo
            textViewOption3.text = question.optionThree
            textViewOption4.text = question.optionFour

            currentQuestion = question
        } else {
            checkButton.text = "FINISH"
            // Start Result activity here
            Intent(this,ResultActivity::class.java).also {
                it.putExtra(Constants.USER_NAME, name)
                it.putExtra(Constants.SCORE, score)
                it.putExtra(Constants.TOTAL_QUESTIONS, questionsList.size)
                startActivity(it)
            }
        }

        questionsCounter++
        answered = false
    }

    private fun resetOptions() {
        Log.d("App Starting", "Hey its me, 'resetOptions'")
        val options = mutableListOf<TextView>()
        options.add(textViewOption1)
        options.add(textViewOption2)
        options.add(textViewOption3)
        options.add(textViewOption4)

        for(option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            ) //This will take car of reseting the background
        }
    }

    private fun selectedOption(textView: TextView, selectedOptionNumber: Int) {
        Log.d("App Starting", "Hey its me, 'selectedOption'")
        resetOptions()

        selectedAnswer = selectedOptionNumber

        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(view: View?) {
        Log.d("App Starting", "Hey its me, 'onClick'")
        when (view?.id) {
            R.id.text_view_option_one -> {
                selectedOption(textViewOption1,1)
            }
            R.id.text_view_option_two -> {
                selectedOption(textViewOption2,2)
            }
            R.id.text_view_option_three -> {
                selectedOption(textViewOption3,3)
            }
            R.id.text_view_option_four -> {
                selectedOption(textViewOption4,4)
            }
            R.id.button_check -> {
                if(!answered){
                    checkAnswer()
                } else {
                    showNextQuestion()
                }
                selectedAnswer = 0
            }
        }
    }

    private fun highlightBg(textViewIndex: Int, state:Boolean) {
        Log.d("App Starting", "Hey its me, 'highlightBg'")
        when (textViewIndex) {
            1 -> {
                textViewOption1.background = ContextCompat.getDrawable(
                    this,
                    if (state) R.drawable.correct_option_border_bg else R.drawable.wrong_option_border_bg
                )
            }
            2 -> {
                textViewOption2.background = ContextCompat.getDrawable(
                    this,
                    if (state) R.drawable.correct_option_border_bg else R.drawable.wrong_option_border_bg
                )
            }
            3 -> {
                textViewOption3.background = ContextCompat.getDrawable(
                    this,
                    if (state) R.drawable.correct_option_border_bg else R.drawable.wrong_option_border_bg
                )
            }
            4 -> {
                textViewOption4.background = ContextCompat.getDrawable(
                    this,
                    if (state) R.drawable.correct_option_border_bg else R.drawable.wrong_option_border_bg
                )
            }
        }
    }

    private fun checkAnswer() {
        Log.d("App Starting", "Hey its me, 'checkAnswer'")
        answered = true
        if(selectedAnswer == currentQuestion.correctAnswer){
            Log.d("Status", "correct answer selected ($selectedAnswer matches with ${currentQuestion.correctAnswer})")
            score++
            highlightBg(selectedAnswer,true)
        } else {
            Log.d("Status", "wrong answer selected ($selectedAnswer instead of ${currentQuestion.correctAnswer})")
            highlightBg(selectedAnswer,false)
        }
        checkButton.text = if (questionsCounter < questionsList.size) "NEXT" else "FINISH"
        showSolution()
    }

    private fun showSolution() {
        selectedAnswer = currentQuestion.correctAnswer
        highlightBg(selectedAnswer, true)
    }
}


