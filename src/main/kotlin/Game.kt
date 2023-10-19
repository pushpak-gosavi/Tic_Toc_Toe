class Game {
    private val board = MutableList<Cell>(size = 9) { Cell.Empty }
    private var status: Status = Status.Ideal
    private lateinit var player: Player

    fun start() {
        println("----------------------------------")
        println("|  Welcome to TIC-TAC-TOE Game!    |")
        println("   Pick a number from 0-8  ")
        println("----------------------------------")
        getName()
        while (status is Status.Running){
            getCall()
        }
    }

    private fun getName() {
        print("Please enter your Name: ")
        val name = readlnOrNull()
        try {
            require(name != null)
            player = Player(name = name, symbol = 'X')
            println("It's your move, $name")
            printBoard()
        } catch (e: Throwable) {
            println("Invalid Name.")
        }
    }

    private fun getCall(){

    }

    private fun printBoard(){
        println()
        println("-------------")
        println("|  ${board[0].placeHolder}  ${board[1].placeHolder}  ${board[2].placeHolder}  |")
        println("|  ${board[3].placeHolder}  ${board[4].placeHolder}  ${board[5].placeHolder}  |")
        println("|  ${board[6].placeHolder}  ${board[7].placeHolder}  ${board[8].placeHolder}  |")
        println("-------------")
        println()
    }
}

data class Player(
    val name: String = "Computer",
    val symbol: Char = 'O'
)

sealed class Status {
    object Ideal : Status()
    object Running : Status()
    object GameOver : Status()
}

sealed class Cell(val placeHolder: Char) {
    object Empty : Cell(placeHolder = '_')
    data class Filled(val player: Player) : Cell(placeHolder = player.symbol)
}
