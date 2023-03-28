package com.example.a021_tic_tac_toe

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class GameActivity : AppCompatActivity() {

    lateinit var txt_player1:TextView
    lateinit var txt_player2:TextView
    lateinit var img1:ImageView
    lateinit var img2:ImageView
    lateinit var img3:ImageView
    lateinit var img4:ImageView
    lateinit var img5:ImageView
    lateinit var img6:ImageView
    lateinit var img7:ImageView
    lateinit var img8:ImageView
    lateinit var img9:ImageView
    lateinit var txt_player1_wins:TextView
    lateinit var txt_player2_wins:TextView
    lateinit var btn_game_restart:Button

    lateinit var layout_player1:LinearLayout
    lateinit var layout_player2:LinearLayout


    lateinit var my_username:String
    lateinit var room_id:String

    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var firebaseReference = firebaseDatabase.reference.child("Rooms")

    var player1:String=""
    var player2:String=""
    lateinit var current_player:String

    var player1Steps = ArrayList<Int>()
    var player2Steps = ArrayList<Int>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        txt_player1 = findViewById(R.id.txt_player1)
        txt_player2 = findViewById(R.id.txt_player2)

        img1 = findViewById(R.id.img1)
        img2 = findViewById(R.id.img2)
        img3 = findViewById(R.id.img3)
        img4 = findViewById(R.id.img4)
        img5 = findViewById(R.id.img5)
        img6 = findViewById(R.id.img6)
        img7 = findViewById(R.id.img7)
        img8 = findViewById(R.id.img8)
        img9 = findViewById(R.id.img9)

        txt_player1_wins = findViewById(R.id.txt_player1_wins)
        txt_player2_wins = findViewById(R.id.txt_player2_wins)

        btn_game_restart = findViewById(R.id.btn_game_restart)
        layout_player1 = findViewById(R.id.layout_player1)
        layout_player1.setBackgroundResource(R.drawable.stroke_background)
        layout_player2 = findViewById(R.id.layout_player2)

        btn_game_restart.setOnClickListener {
            firebaseReference.child("Restart").setValue(1)
        }

        enableActivity(false)

        my_username = intent.getStringExtra("my_username").toString()
        room_id = intent.getStringExtra("room_id").toString()

        firebaseReference =  firebaseReference.child(room_id)

        waitingForPlayer()
        updateCurrentPlayer()
        updatePlayerMove()
        startGame()
        checkForRestart()
        updateScoreBoard()

    }

    private fun updateScoreBoard() {
        var player1WinsReference = firebaseReference.child("Player1Wins")
        var player2WinsReference = firebaseReference.child("Player2Wins")

        player1WinsReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_player1_wins.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        player2WinsReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                txt_player2_wins.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun checkForRestart() {
        firebaseReference.child("Restart").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val restart:Int = snapshot.value.toString().toInt()
                if(restart==1){
                    firebaseReference.child("Restart").setValue(0)
                    clearAll()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun clearAll() {
        firebaseReference.child("Steps").removeValue()
        img1.setImageResource(0)
        img2.setImageResource(0)
        img3.setImageResource(0)
        img4.setImageResource(0)
        img5.setImageResource(0)
        img6.setImageResource(0)
        img7.setImageResource(0)
        img8.setImageResource(0)
        img9.setImageResource(0)

        img1.isEnabled = true
        img2.isEnabled = true
        img3.isEnabled = true
        img4.isEnabled = true
        img5.isEnabled = true
        img6.isEnabled = true
        img7.isEnabled = true
        img8.isEnabled = true
        img9.isEnabled = true

        btn_game_restart.isVisible = false
        player1Steps.clear()
        player2Steps.clear()

    }

    private fun enableActivity(isEnabled: Boolean) {
        if (!isEnabled) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun updatePlayerMove() {

        //Player 1 moves
        firebaseReference.child("Steps").child("Player1Steps").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                player1Steps.clear()
                for(eachSteps in snapshot.children){
                    player1Steps.add(eachSteps.value.toString().toInt())
                }
                for (num in player1Steps){
                    when (num) {
                        1 -> updateImageView(img1,0)
                        2 -> updateImageView(img2,0)
                        3 -> updateImageView(img3,0)
                        4 -> updateImageView(img4,0)
                        5 -> updateImageView(img5,0)
                        6 -> updateImageView(img6,0)
                        7 -> updateImageView(img7,0)
                        8 -> updateImageView(img8,0)
                        9 -> updateImageView(img9,0)

                    }
                }
                checkWinningPossibility(player1Steps,player1,player2Steps)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        //Player 2 moves
        firebaseReference.child("Steps").child("Player2Steps").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                player2Steps.clear()
                for(eachSteps in snapshot.children){
                    player2Steps.add(eachSteps.value.toString().toInt())
                }

                for (num in player2Steps){
                    when (num) {
                        1 -> updateImageView(img1,1)
                        2 -> updateImageView(img2,1)
                        3 -> updateImageView(img3,1)
                        4 -> updateImageView(img4,1)
                        5 -> updateImageView(img5,1)
                        6 -> updateImageView(img6,1)
                        7 -> updateImageView(img7,1)
                        8 -> updateImageView(img8,1)
                        9 -> updateImageView(img9,1)

                    }
                }
                checkWinningPossibility(player2Steps, player2, player1Steps)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })



    }

    private fun checkWinningPossibility(
        player1Steps: ArrayList<Int>,
        player: String,
        player2Steps: ArrayList<Int>
    ) {

        if ((player1Steps.contains(1) && player1Steps.contains(2) && player1Steps.contains(3)) || (player1Steps.contains(
                1
            ) && player1Steps.contains(4) && player1Steps.contains(7)) ||
            (player1Steps.contains(3) && player1Steps.contains(6) && player1Steps.contains(9)) || (player1Steps.contains(
                7
            ) && player1Steps.contains(8) && player1Steps.contains(9)) ||
            (player1Steps.contains(4) && player1Steps.contains(5) && player1Steps.contains(6)) || (player1Steps.contains(
                1
            ) && player1Steps.contains(5) && player1Steps.contains(9)) ||
            player1Steps.contains(3) && player1Steps.contains(5) && player1Steps.contains(7) || (player1Steps.contains(2) && player1Steps.contains(
                5
            ) && player1Steps.contains(8))
        ){
            //pLAYER 1 WINS
            Toast.makeText(this@GameActivity, "$player Wins",Toast.LENGTH_LONG).show()
            btn_game_restart.isVisible = true

            if(player == my_username) {
                updateScoreInDatabase(player)
            }
        }
        else if((player1Steps.size+player2Steps.size)==9){
            Toast.makeText(this@GameActivity, "Draw",Toast.LENGTH_LONG).show()
            btn_game_restart.isVisible = true
        }


    }

    private fun updateScoreInDatabase(player: String) {
        var scoreReference = firebaseReference
        if(player == player1){
            scoreReference = scoreReference.child("Player1Wins")
        }
        else if(player == player2){
            scoreReference = scoreReference.child("Player2Wins")
        }

        scoreReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                scoreReference.setValue(snapshot.value.toString().toInt()+1)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun updateImageView(img1: ImageView, i: Int) {
        if(i==0) {
            img1.setImageResource(R.drawable.cross)
            img1.isEnabled = false
        }
        else{
            img1.setImageResource(R.drawable.zero)
            img1.isEnabled = false
        }
    }

    private fun updateCurrentPlayer() {
        firebaseReference.child("Current_Player").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                current_player = snapshot.value.toString()
                if(current_player==my_username){
                    enableActivity(true)
                }
                if(current_player==player1){
                    layout_player1.setBackgroundResource(R.drawable.stroke_background)
                    layout_player2.setBackgroundResource(R.drawable.background_without_stroke)
                }
                if(current_player==player2){
                    layout_player2.setBackgroundResource(R.drawable.stroke_background)
                    layout_player1.setBackgroundResource(R.drawable.background_without_stroke)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun startGame() {

        img1.setOnClickListener {
            selectedButton(img1)
        }
        img2.setOnClickListener {
            selectedButton(img2)
        }
        img3.setOnClickListener {
            selectedButton(img3)
        }
        img4.setOnClickListener {
            selectedButton(img4)
        }
        img5.setOnClickListener {
            selectedButton(img5)
        }
        img6.setOnClickListener {
            selectedButton(img6)
        }
        img7.setOnClickListener {
            selectedButton(img7)
        }
        img8.setOnClickListener {
            selectedButton(img8)
        }
        img9.setOnClickListener {
            selectedButton(img9)
        }


    }

    private fun selectedButton(view: ImageView) {
        val selectedBtn = view as ImageView
        var  btnNum = 0
        when(selectedBtn.id){
            R.id.img1 -> btnNum = 1
            R.id.img2 -> btnNum = 2
            R.id.img3 -> btnNum = 3
            R.id.img4 -> btnNum = 4
            R.id.img5 -> btnNum = 5
            R.id.img6 -> btnNum = 6
            R.id.img7 -> btnNum = 7
            R.id.img8 -> btnNum = 8
            R.id.img9 -> btnNum = 9
        }
        enableActivity(false)
        game(btnNum,selectedBtn)

    }

    private fun game(btnNum: Int, selectedBtn: ImageView) {
            if(current_player==player1){
                selectedBtn.setImageResource(R.drawable.cross)
                //player1Steps.add(btnNum)
                addStepsToDatabase(current_player,btnNum)
            }
            else if(current_player==player2){
                selectedBtn.setImageResource(R.drawable.zero)
                //player2Steps.add(btnNum)
                addStepsToDatabase(current_player,btnNum)
            }

    }

    private fun addStepsToDatabase(currentPlayer: String, btnNum: Int) {

        var stepsReference = firebaseReference.child("Steps")
        if(currentPlayer==player1){
            stepsReference.child("Player1Steps").push().setValue(btnNum)
            firebaseReference.child("Current_Player").setValue(player2)
        }
        else if(currentPlayer==player2){
            stepsReference.child("Player2Steps").push().setValue(btnNum)
            firebaseReference.child("Current_Player").setValue(player1)
        }

    }

    //--------------------------------------------------------------------------------//
    private fun waitingForPlayer() {
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Waiting For Player 2 : Room id - $room_id")
        mProgressDialog.show()
        firebaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("Players").childrenCount.toInt()==2){
                    player1 = snapshot.child("Players").child("Player1").value.toString()
                    player2 = snapshot.child("Players").child("Player2").value.toString()

                    txt_player1.text = player1
                    txt_player2.text = player2

                    mProgressDialog.dismiss()
                    //Toast.makeText(this@GameActivity,"Start the game",Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    //--------------------------------------------------------------------------------//


}