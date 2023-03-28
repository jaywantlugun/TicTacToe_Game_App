package com.example.a021_tic_tac_toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var btn_single_player:Button
    lateinit var btn_two_player:Button
    lateinit var two_player_layout:LinearLayout
    lateinit var edt_player1_name:EditText
    lateinit var edt_player2_name:EditText
    lateinit var btn_join_room:Button
    lateinit var btn_create_room:Button
    lateinit var join_room_layout:LinearLayout
    lateinit var create_room_layout:LinearLayout
    lateinit var edt_join_name:EditText
    lateinit var edt_join_room_id:EditText
    lateinit var edt_create_name:EditText

    val firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    val firebaseReference = firebaseDatabase.reference.child("Rooms")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()


        //Buttons
        btn_single_player = findViewById(R.id.btn_single_player)
        btn_two_player = findViewById(R.id.btn_two_player)
        btn_join_room = findViewById(R.id.btn_join_room)
        btn_create_room = findViewById(R.id.btn_create_room)

        //EditText
        edt_player1_name = findViewById(R.id.edt_player1_name)
        edt_player2_name = findViewById(R.id.edt_player2_name)
        edt_join_name = findViewById(R.id.edt_join_name)
        edt_join_room_id = findViewById(R.id.edt_join_room_id)
        edt_create_name = findViewById(R.id.edt_create_name)

        //Linear Layouts
        two_player_layout = findViewById(R.id.two_player_layout)
        join_room_layout = findViewById(R.id.join_room_layout)
        create_room_layout = findViewById(R.id.create_room_layout)

        btn_single_player.setOnClickListener {
            val intent = Intent(this,OfflineGameActivity::class.java)
            intent.putExtra("player2",1)
            startActivity(intent)
        }

        btn_two_player.setOnClickListener {
            join_room_layout.visibility = View.GONE
            create_room_layout.visibility = View.GONE
            two_player_layout.visibility = View.VISIBLE

            startTwoPlayerGame()
        }

        btn_join_room.setOnClickListener {
            join_room_layout.visibility = View.VISIBLE
            create_room_layout.visibility = View.GONE
            two_player_layout.visibility = View.GONE
            join_room()
        }

        btn_create_room.setOnClickListener {
            create_room_layout.visibility = View.VISIBLE
            join_room_layout.visibility = View.GONE
            two_player_layout.visibility = View.GONE
            create_room()
        }
    }

    private fun startTwoPlayerGame() {

        if(edt_player1_name.text.toString() == "" || edt_player2_name.text.toString()==""){

            Toast.makeText(this,"Enter player name",Toast.LENGTH_LONG).show()
            return
        }

        val player1name = edt_player1_name.text.toString()
        val player2name = edt_player2_name.text.toString()
        edt_player1_name.setText("")
        edt_player2_name.setText("")

        val intent = Intent(this,OfflineGameActivity::class.java)
        intent.putExtra("player2",2)
        intent.putExtra("player1name",player1name)
        intent.putExtra("player2name",player2name)
        startActivity(intent)
    }

    private fun create_room() {
        var username:String = edt_create_name.text.toString()
        if(username==""){
            Toast.makeText(this,"Enter Username",Toast.LENGTH_LONG).show()
        }
        else{
            edt_create_name.setText("")
            val room_id:Int = (1000..9999).random()
            //var player = Player1(username)
            firebaseReference.child(room_id.toString()).child("Players").child("Player1").setValue(username)
            firebaseReference.child(room_id.toString()).child("Current_Player").setValue(username)
            firebaseReference.child(room_id.toString()).child("Restart").setValue(0)
            firebaseReference.child(room_id.toString()).child("Player1Wins").setValue(0)
            firebaseReference.child(room_id.toString()).child("Player2Wins").setValue(0)

            val intent = Intent(this,OnlineGameActivity::class.java)
            intent.putExtra("my_username",username)
            intent.putExtra("room_id",room_id.toString())
            startActivity(intent)
        }
    }

    private fun join_room() {
        var username:String = edt_join_name.text.toString()
        var room_id:String=edt_join_room_id.text.toString()

        if(username==""){
            Toast.makeText(this,"Enter Username",Toast.LENGTH_LONG).show()
        }
        else if(room_id==""){
            Toast.makeText(this,"Enter room id",Toast.LENGTH_LONG).show()
        }
        else{
            firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var foundRoom = false
                    for(eachRoom in snapshot.children){
                        if(eachRoom.key == room_id){
                            foundRoom = true
                            if(eachRoom.child("Players").childrenCount.toInt()==2){
                                Toast.makeText(this@MainActivity,"Room is full",Toast.LENGTH_LONG).show()
                            }
                            else{
                                //var player = Player2(username)
                                firebaseReference.child(room_id.toString()).child("Players").child("Player2").setValue(username)
                                edt_join_name.setText("")
                                edt_join_room_id.setText("")
                                val intent = Intent(this@MainActivity,OnlineGameActivity::class.java)
                                intent.putExtra("my_username",username)
                                intent.putExtra("room_id",room_id.toString())
                                startActivity(intent)
                            }
                        }
                    }
                    if(foundRoom==false){
                        Toast.makeText(this@MainActivity,"Room not found",Toast.LENGTH_LONG).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }

    }
}