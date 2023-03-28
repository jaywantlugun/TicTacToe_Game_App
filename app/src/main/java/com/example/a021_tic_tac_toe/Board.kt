package com.example.a021_tic_tac_toe

class Board {

    //Strings for PLAYER and COMPUTER
    companion object {
        const val PLAYER1 = "O"
        const val PLAYER2 = "Z"
        const val COMPUTER = "X"
    }

    //This is our internal board
    //and for this we used a 3 by 3 array of Strings
    val board = Array(3) { arrayOfNulls<String>(3) }

    //This property is giving us
    //a list of all the empty cells
    val availableCells: List<Cell>
        get() {
            val cells = mutableListOf<Cell>()
            for (i in board.indices) {
                for (j in board.indices) {
                    if (board[i][j].isNullOrEmpty()) {
                        cells.add(Cell(i, j))
                    }
                }
            }
            return cells
        }

    //this property will tell
    //if the game is over or not
    val isGameOver: Boolean
        get() = hasComputerWon() || hasPlayer1Won() || hasPlayer2Won() || availableCells.isEmpty()


    //These functions are checking
    //Weather the computer or player has won or not
    fun hasComputerWon(): Boolean {
        if (board[0][0] == board[1][1] &&
            board[0][0] == board[2][2] &&
            board[0][0] == COMPUTER ||
            board[0][2] == board[1][1] &&
            board[0][2] == board[2][0] &&
            board[0][2] == COMPUTER
        ) {
            return true
        }

        for (i in board.indices) {
            if (
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2] &&
                board[i][0] == COMPUTER ||
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i] &&
                board[0][i] == COMPUTER
            ) {
                return true
            }
        }

        return false
    }

    fun hasPlayer1Won(): Boolean {

        if (board[0][0] == board[1][1] &&
            board[0][0] == board[2][2] &&
            board[0][0] == PLAYER1 ||
            board[0][2] == board[1][1] &&
            board[0][2] == board[2][0] &&
            board[0][2] == PLAYER1
        ) {
            return true
        }

        for (i in board.indices) {
            if (
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2] &&
                board[i][0] == PLAYER1 ||
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i] &&
                board[0][i] == PLAYER1
            ) {
                return true
            }
        }

        return false
    }

    fun hasPlayer2Won(): Boolean {

        if (board[0][0] == board[1][1] &&
            board[0][0] == board[2][2] &&
            board[0][0] == PLAYER2 ||
            board[0][2] == board[1][1] &&
            board[0][2] == board[2][0] &&
            board[0][2] == PLAYER2
        ) {
            return true
        }

        for (i in board.indices) {
            if (
                board[i][0] == board[i][1] &&
                board[i][0] == board[i][2] &&
                board[i][0] == PLAYER2 ||
                board[0][i] == board[1][i] &&
                board[0][i] == board[2][i] &&
                board[0][i] == PLAYER2
            ) {
                return true
            }
        }

        return false
    }

    //in this var we will store the computersMove
    var computersMove: Cell? = null

    //this is our minimax function to calculate
    //the best move for the computer
    fun minimax(depth: Int, player: String): Int {
        if (hasComputerWon()) return +1
        if (hasPlayer1Won()) return -1

        if (availableCells.isEmpty()) return 0

        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE

        for (i in availableCells.indices) {
            val cell = availableCells[i]
            if (player == COMPUTER) {
                placeMove(cell, COMPUTER)
                val currentScore = minimax(depth + 1, PLAYER1)
                max = Math.max(currentScore, max)

                if (currentScore >= 0) {
                    if (depth == 0) computersMove = cell
                }

                if (currentScore == 1) {
                    board[cell.i][cell.j] = ""
                    break
                }

                if (i == availableCells.size - 1 && max < 0) {
                    if (depth == 0) computersMove = cell
                }

            } else if (player == PLAYER1) {
                placeMove(cell, PLAYER1)
                val currentScore = minimax(depth + 1, COMPUTER)
                min = Math.min(currentScore, min)

                if (min == -1) {
                    board[cell.i][cell.j] = ""
                    break
                }
            }
            board[cell.i][cell.j] = ""
        }

        return if (player == COMPUTER) max else min
    }

    //this function is placing a move in the given cell
    fun placeMove(cell: Cell, player: String) {
        board[cell.i][cell.j] = player
    }

}