package com.example.a021_tic_tac_toe

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class OfflineGameActivity : AppCompatActivity() {

    lateinit var layout_board: GridLayout
    lateinit var btn_game_restart: Button

    lateinit var txt_player1_wins:TextView
    lateinit var txt_player2_wins:TextView
    lateinit var txt_player1:TextView
    lateinit var txt_player2:TextView

    //Creating a 2D Array of ImageViews
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }

    //Real Player or Ai
    var current_player:Int = 0
    var player2:Int = 1


    //creating the board instance
    var board = Board()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_game)

        layout_board = findViewById(R.id.layout_board)
        btn_game_restart = findViewById(R.id.btn_game_restart)
        txt_player1 = findViewById(R.id.txt_player1)
        txt_player2 = findViewById(R.id.txt_player2)
        txt_player1_wins = findViewById(R.id.txt_player1_wins)
        txt_player2_wins = findViewById(R.id.txt_player2_wins)

        player2 = intent.getIntExtra("player2",1)
        if(player2==1){
            txt_player2.text = "Computer"
        }
        else if(player2==2){
            val player1name = intent.getStringExtra("player1name")
            val player2name = intent.getStringExtra("player2name")
            txt_player1.text = player1name
            txt_player2.text = player2name
        }

        loadBoard()

        //restart functionality
        btn_game_restart.setOnClickListener {
            btn_game_restart.visibility = View.GONE
            //current_player = 0
            //creating a new board instance
            //it will empty every cell
            board = Board()

            //setting the result to empty
            //text_view_result.text = ""

            //this function will map the internal board
            //to the visual board
            mapBoardToUi()
        }
    }

    //function is mapping
    //the internal board to the ImageView array board
    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER1 -> {
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.PLAYER2 -> {
                        boardCells[i][j]?.setImageResource(R.drawable.zero)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.zero)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }


    private fun loadBoard() {
        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))
                layout_board.addView(boardCells[i][j])
            }
        }
    }

    inner class CellClickListener(
        private val i: Int,
        private val j: Int
    ) : View.OnClickListener {

        override fun onClick(p0: View?) {

            //checking if the game is not over
            if (!board.isGameOver) {

                //creating a new cell with the clicked index
                val cell = Cell(i, j)

                //placing the move for player
                if(current_player==0) {
                    board.placeMove(cell, Board.PLAYER1)
                    if(player2==1){
                        current_player=1
                    }
                    else if(player2==2){
                        current_player=2
                    }
                }
                else if(current_player==2){
                    board.placeMove(cell, Board.PLAYER2)
                    current_player=0
                }

                if(current_player==1) {

                    //calling minimax to calculate the computers move
                    board.minimax(0, Board.COMPUTER)

                    //performing the move for computer
                    board.computersMove?.let {
                        board.placeMove(it, Board.COMPUTER)
                    }
                    current_player=0
                }
                //mapping the internal board to visual board
                mapBoardToUi()
            }

            //Displaying the results
            //according to the game status
            when {
                board.hasComputerWon() ->{
                    btn_game_restart.visibility = View.VISIBLE
                    var computerWins:Int = txt_player2_wins.text.toString().toInt()
                    computerWins+=1
                    txt_player2_wins.text = computerWins.toString()
                }
                board.hasPlayer1Won() ->{
                    btn_game_restart.visibility = View.VISIBLE
                    var player1Wins:Int = txt_player1_wins.text.toString().toInt()
                    player1Wins+=1
                    txt_player1_wins.text = player1Wins.toString()
                }
                board.hasPlayer2Won() ->{
                    btn_game_restart.visibility = View.VISIBLE
                    var player2Wins:Int = txt_player2_wins.text.toString().toInt()
                    player2Wins+=1
                    txt_player2_wins.text = player2Wins.toString()
                }
                board.isGameOver ->{
                    btn_game_restart.visibility = View.VISIBLE
                }
            }
        }
    }

}