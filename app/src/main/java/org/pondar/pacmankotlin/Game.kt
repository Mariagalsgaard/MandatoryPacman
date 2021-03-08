package org.pondar.pacmankotlin


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sqrt
import kotlin.random.Random


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView) {

    private var pointsView: TextView = view
    private var points: Int = 0
    var running = false
    var direction: Int = 0

    //bitmap of the pacman
    var pacBitmap: Bitmap
    var goldCoin: Bitmap
    var pacx: Int = 0
    var pacy: Int = 0



    //did we initialize the coins?
    var coinsInitialized = false

    //the list of goldcoins - initially empty
    var coins = ArrayList<GoldCoin>()

    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        goldCoin = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins() {

        /* my try on using random() - but not working
        for (coin in 0..4){
            val coin = GoldCoin(0, 0, false)
            coin.goldcoinx = Random().nextInt(950)
            coin.goldcoiny = Random().nextInt(1000)
            coins.add(coin)
        }
        */

        //DO Stuff to initialize the array list with some coins.
        val coin1 = GoldCoin(150, 1250, false)
        val coin2 = GoldCoin(350, 1150, false)
        val coin3 = GoldCoin(50, 1075, false)
        val coin4 = GoldCoin(250, 1175, false)
        val coin5 = GoldCoin(450, 1000, false)
        val coin6 = GoldCoin(550, 1325, false)
        val coin7 = GoldCoin(175, 1260, false)
        val coin8 = GoldCoin(190, 1265, false)
        val coin9 = GoldCoin(90, 1245, false)
        val coin10 = GoldCoin(145, 1235, false)

        coins.add(coin1)
        coins.add(coin2)
        coins.add(coin3)
        coins.add(coin4)
        coins.add(coin5)
        coins.add(coin6)
        coins.add(coin7)
        coins.add(coin8)
        coins.add(coin9)
        coins.add(coin10)

        coinsInitialized = true

    }


    fun newGame() {
        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        gameView?.invalidate() //redraw screen
        coins.clear()
        running = true
        direction = 0
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }

    }

    fun movePacmanLeft(pixels: Int) {
        //still within our boundaries?
        if (pacx - pixels > 0) {
            pacx = pacx - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }
    }

    fun movePacmanDown(pixels: Int) {
        //still within our boundaries?
        if (pacy + pixels + pacBitmap.height < h) {
            pacy = pacy + pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }

    }

    fun movePacmanUp(pixels: Int) {
        //still within our boundaries?
        if (pacy - pixels > 0) {
            pacy = pacy - pixels
            doCollisionCheck()
            gameView!!.invalidate()
        }

    }


    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {

        //pacman
        val R1 = pacBitmap.width / 2
        val X1 = pacx + R1
        val Y1 = pacy + R1

        //coins
        val R2 = goldCoin.width / 2
        for (coin in coins.indices) {
            val X2 = coins[coin].goldcoinx + R2
            val Y2 = coins[coin].goldcoiny + R2
            val dist = distance( X1, Y1, X2, Y2)
            if (calcCircleIntersect(dist, R1, R2) && !coins[coin].taken) {
                coins[coin].taken = true
                points++
                pointsView.text = "${context.resources.getString(R.string.points)} $points"
            }
        }

        val coinsLeft: List<GoldCoin> = coins.filter { c -> c.taken == false}
        if (coinsLeft.size == 0) {
            Toast.makeText(context, "Yay, you've won!!", Toast.LENGTH_LONG).show()
            newGame()
        }
    }

    fun distance (x1: Int, y1: Int, x2: Int, y2: Int): Double {
        val distSqrd = ((x2 - x1) * (x2 - x1) * (y2 - y1) * (y2 - y1)).toDouble()
        return sqrt(distSqrd)
    }

    fun calcCircleIntersect(dist: Double, R1: Int, R2: Int) : Boolean {
        return dist <= R1 + R2
    }

}