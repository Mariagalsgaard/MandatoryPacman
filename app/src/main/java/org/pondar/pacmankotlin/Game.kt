package org.pondar.pacmankotlin


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList
import kotlin.math.sqrt
import kotlin.random.Random


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView) {

    private var pointsView: TextView = view
    var points: Int = 0
    var running = false //set fra martins timeropgave
    var direction = 1 //set fra martins timeropgave

    //bitmap of the pacman
    var pacBitmap: Bitmap
    var goldCoin: Bitmap
    var ghost: Bitmap
    var pacx: Int = 0
    var pacy: Int = 0



    //did we initialize the coins?
    var coinsInitialized = false
    var enemiesInitialised = false

    //the list of goldcoins and enemies
    var coins = ArrayList<GoldCoin>()
    var enemies = ArrayList<Enemy>()

    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        goldCoin = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin)
        ghost = BitmapFactory.decodeResource(context.resources, R.drawable.ghost)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }


    //initialize enemies
    fun initializeEnemies() {

            val enemy = Enemy(0, 0, false)
            enemy.enemyx = Random.nextInt(900)
            enemy.enemyy = Random.nextInt(1000)
            enemies.add(enemy)
    }


    //TODO initialize goldcoins also here
    fun initializeGoldcoins() {

        for (coin in 0..10){
            val coin = GoldCoin(0, 0, false)
            coin.goldcoinx = Random.nextInt( 950)
            coin.goldcoiny = Random.nextInt(150 , 1500)
            coins.add(coin)
        }
        coinsInitialized = true

    }


    fun newGame() {
        pacx = 400
        pacy = 700 //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        coins.clear()
        enemies.clear()
        initializeEnemies()
        running = true
        direction = 1
        enemiesInitialised = false
        gameView?.invalidate() //redraw screen
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


    //moving the enemies
    fun enemyRight(pixels: Int) {
        //still within our boundaries?
        for(enemy in enemies) {
            if (enemy.enemyx + pixels + ghost.width < w) {
                enemy.enemyx += pixels
                gameView!!.invalidate()
            }
        }
    }

    fun enemyLeft(pixels: Int) {
        //still within our boundaries?
        for(enemy in enemies) {
            if (enemy.enemyx - pixels > 0) {
                enemy.enemyx -= pixels
                gameView!!.invalidate()
            }
        }
    }

    fun enemyDown(pixels: Int) {
        //still within our boundaries?
        for(enemy in enemies) {
            if (enemy.enemyy + pixels + ghost.height < h) {
                enemy.enemyy += pixels
                gameView!!.invalidate()
            }
        }
    }

    fun enemyUp(pixels: Int) {
        //still within our boundaries?
        for(enemy in enemies) {
            if (enemy.enemyy - pixels > 0) {
                enemy.enemyy -= pixels
                gameView!!.invalidate()
            }
        }
    }



    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {

        //pacman
        val r1 = pacBitmap.width / 2
        val x1 = pacx + r1
        val y1 = pacy + r1

        //enemies
        val r3 = ghost.width / 2
        for (enemy in enemies.indices){
            val x3 = enemies[enemy].enemyx + r3
            val y3 = enemies[enemy].enemyy + r3
            val dist = distance (x1, y1, x3, y3)
            if (calcCircle(dist, r1, r3) && !enemies[enemy].isAlive) {
                enemies[enemy].isAlive = true
                Toast.makeText(context, "Game over", Toast.LENGTH_LONG).show()
                newGame()
            }
        }

        //coins
        val r2 = goldCoin.width / 2
        for (coin in coins.indices) {
            val x2 = coins[coin].goldcoinx + r2
            val y2 = coins[coin].goldcoiny + r2
            val dist = distance( x1, y1, x2, y2)
            if (calcCircle(dist, r1, r2) && !coins[coin].taken) {
                coins[coin].taken = true
                points++
                pointsView.text = "${context.resources.getString(R.string.points)} $points"
            }
        }

        val coinsLeft: List<GoldCoin> = coins.filter { c -> !c.taken }
        if (coinsLeft.isEmpty()) {
            Toast.makeText(context, "Yay, you've won!!", Toast.LENGTH_LONG).show()
            newGame()
        }
    }

    fun distance (x1: Int, y1: Int, x2: Int, y2: Int): Double {
        val distSqrd = ((x2 - x1) * (x2 - x1) * (y2 - y1) * (y2 - y1)).toDouble()
        return sqrt(distSqrd)
    }

    fun calcCircle(dist: Double, R1: Int, R2: Int) : Boolean {
        return dist <= R1 + R2
    }

}


/*
    fun winGame (): Boolean {
        for (coin in coins){
            if(!coin.taken){
                return false
            }
        }
        return true
    }
    */