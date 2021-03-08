package org.pondar.pacmankotlin


import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnClickListener
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnClickListener {

    //reference to the game class.
    private var game: Game? = null
    private var myTimer: Timer = Timer()
    private var gameTimer: Timer = Timer()
    private var counter: Int = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        game = Game(this, pointsView)

        //intialize the game view class and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        continueButton.setOnClickListener(this)
        pauseButton.setOnClickListener(this)


        //make a new timer
        game?.running = true //should the game be running?
        //We will call the timer 5 times each second
        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }

        }, 0, 100) //0 indicates we start now, 200
        //is the number of miliseconds between each call


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


        // my game timer
        game?.running = true
        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                timerSomething()
            }
        }, 0, 1000)

    }


    override fun onStop() {
        super.onStop()
        //just to make sure if the app is killed, that we stop the timer.
        myTimer.cancel()
    }

    private fun timerMethod() {
        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(timerTick)
    }

    private fun timerSomething() {
        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(timerSeconds)
    }

    private val timerSeconds = Runnable {
        if (game!!.running) {
            counter--
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            textView.text = getString(R.string.timerLeftText, counter)

            if (game!!.direction==1)
            { // move right
                game!!.movePacmanRight(20)
            }
            else if (game!!.direction==2)
            { //move left
                game!!.movePacmanLeft(20)
            }
            else if (game!!.direction==3)
            { //move up
                game!!.movePacmanUp(20)
            }
            else if (game!!.direction==4)
            { //move down
                game!!.movePacmanDown(20)
            }
        } else if (game!!.running && counter >= 0) {
            Toast.makeText(this, "You've lost!!", Toast.LENGTH_LONG).show()
            myTimer.cancel()
            gameTimer.cancel()
        }
    }


    private val timerTick = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game!!.running) {
            counter++
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            textView.text = getString(R.string.timerValue, counter)

            if (game!!.direction==1)
            { // move right
                game!!.movePacmanRight(20)
            }
            else if (game!!.direction==2)
            { //move left
                game!!.movePacmanLeft(20)
            }
            else if (game!!.direction==3)
            { //move up
                game!!.movePacmanUp(20)
            }
            else if (game!!.direction==4)
            { //move down
                game!!.movePacmanDown(20)
            }
        }
    }

    //if anything is pressed - we do the checks here
    override fun onClick(v: View) {
        if (v.id == R.id.continueButton) {
            game!!.running = true
        } else if (v.id == R.id.pauseButton) {
            game!!.running = false
        } else if (v.id == R.id.action_newGame) {
            game!!.newGame()
            counter = 0
            game!!.running = false
            textView.text = getString(R.string.timerValue, counter)
            textView.text = getString(R.string.timerLeftText, counter)
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
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

