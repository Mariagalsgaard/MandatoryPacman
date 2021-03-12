package org.pondar.pacmankotlin


import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnClickListener
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnClickListener {

    //reference to the game class.
    private var game: Game? = null

    var myTimer: Timer = Timer() //set fra martins timeropgave
    var gameTimer: Timer = Timer()
    var counter : Int = 60 //set fra martins timeropgave

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

        //make a new timer  - fra Martins timeropgaver
        game?.running = true
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }

        }, 0, 100)


        //my new game timer
        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                timerTimeLeft()
            }

        }, 0, 1000)
    }

    //set fra martins timeropgave
    override fun onStop() {
        super.onStop()
        myTimer.cancel()
        gameTimer.cancel()
    }


    //til min nye timer
    private fun timerTimeLeft() {
        this.runOnUiThread(timerSeconds)
    }

    //set fra martins timeropgave
    private fun timerMethod() {
        this.runOnUiThread(timerTick)
    }


    //til min nye timer
    private val timerSeconds = Runnable {

        if (game!!.running) {
            timeLeft.text = resources.getString(R.string.timeLeft, counter)
            counter--
            if (game?.direction == 1) {
                counter--
                timeLeft.text = resources.getString(R.string.timeLeft, counter)
            }
            if (game?.direction == 2) {
                counter--
                timeLeft.text = resources.getString(R.string.timeLeft, counter)
            }
            if (game?.direction == 3) {
                counter--
                timeLeft.text = resources.getString(R.string.timeLeft, counter)
            }
            if (game?.direction == 4) {
                counter--
                timeLeft.text = resources.getString(R.string.timeLeft, counter)
            }
        }
    }


    //set fra martins timeropgave
    private val timerTick = Runnable {
        if (game!!.running) {
            counter++
            timerValue.text = resources.getString(R.string.timerValue, counter)

            if (game?.direction==1)
            {
                game?.movePacmanRight(20)
            }
            else if (game?.direction == 2)
            {
                game?.movePacmanLeft(20)
            }
            else if (game!!.direction == 3)
            {
                game?.movePacmanUp(20)
            }
            else if (game?.direction == 4)
            {
                game?.movePacmanDown(20)
            }
        }
    }

    //set fra martins timeropgave
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
            counter = 60
            timerValue.text = getString(R.string.timerValue, counter)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

