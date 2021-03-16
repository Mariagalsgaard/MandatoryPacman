package org.pondar.pacmankotlin


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), OnClickListener {

    //reference to the game class.
    private var game: Game? = null

    var myTimer: Timer = Timer()
    var gameTimer: Timer = Timer()
    var counter : Int = 0
    var countDown : Int = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        continueButton.setOnClickListener(this) //set fra martins timeropgave
        pauseButton.setOnClickListener(this) //set fra martins timeropgave

        game = Game(this, pointsView)

        //intialize the game view class and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()


        moveRight.setOnClickListener {
            game?.direction = 1
        }

        moveLeft.setOnClickListener {
            game?.direction = 2
        }

        moveDown.setOnClickListener {
            game?.direction = 4
        }

        moveUp.setOnClickListener {
            game?.direction = 3
        }


        //new game timer
        game?.running = true
        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                timerTimeLeft()
            }

        }, 0, 1000)


        //make a new timer
        game?.running = true
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }

        }, 0, 100)

    }


    override fun onStop() {
        super.onStop()
        myTimer.cancel()
        gameTimer.cancel()

        //save highscore
        //setting preferences
        val prefs = getSharedPreferences("pacman", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt("points", counter)
        editor.commit()
    }


    //til min nye timer
    private fun timerTimeLeft() {
        this.runOnUiThread(timerSeconds)
    }


    private fun timerMethod() {
        this.runOnUiThread(timerTick)
    }


    //til den nye timer
    private val timerSeconds = Runnable {

        if (game!!.running) {
            timeLeft.text = resources.getString(R.string.timeLeft, countDown)
            countDown--
            when (game?.direction) {
                1 -> timeLeft.text = resources.getString(R.string.timeLeft, countDown)
                2 -> timeLeft.text = resources.getString(R.string.timeLeft, countDown)
                3 -> timeLeft.text = resources.getString(R.string.timeLeft, countDown)
                4 -> timeLeft.text = resources.getString(R.string.timeLeft, countDown)
            }
             if (countDown <= 0){
                game?.direction = 0
                game?.running = false
                Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show()
            }

        }
    }



    private val timerTick = Runnable {
        if (game!!.running) {
            counter++
            timerValue.text = resources.getString(R.string.timerValue, counter)

            when (game?.direction)
            {
                1 -> {
                    game?.movePacmanRight(20)
                    game?.enemyUp(10)
                }

                2 -> {
                    game?.movePacmanLeft(20)
                    game?.enemyDown(10)
                }

                3 -> {
                    game?.movePacmanUp(20)
                    game?.enemyRight(10)
                }

                4 -> {
                    game?.movePacmanDown(20)
                    game?.enemyLeft(10)
                }
            }
        }
    }


    override fun onClick(v: View) {
        if (v.id == R.id.continueButton) {
            game?.running = true
        } else if (v.id == R.id.pauseButton) {
            game?.running = false
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            counter = 0
            countDown = 60
            timerValue.text = getString(R.string.timerValue, counter)
            return true
        } else if (id == R.id.action_shareContent){
            //Share content
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Your highscore is: ${game?.points}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share highscore to..")
            startActivity(shareIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

