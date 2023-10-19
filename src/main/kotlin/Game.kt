import kotlin.concurrent.fixedRateTimer
import kotlin.math.ceil
import kotlin.system.exitProcess

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
        while (status is Status.Running) {
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
            status = Status.Running
        } catch (e: Throwable) {
            println("Invalid Name.")
        }
    }

    private fun getCall() {
        val input = readlnOrNull()
        try {
            require(value = input != null)
            val cellNumber = input.toInt()
            require(value = cellNumber in 0..8)
            setCell(cellNumber)
        } catch (e: Throwable) {
            println("Invalid Number.")
        }
    }

    private fun setCell(cellNumber: Int) {
        val cell = board[cellNumber]
        if (cell is Cell.Empty) {
            board.set(
                index = cellNumber,
                element = Cell.Filled(player = player)
            )
            generateComputerMove()
            checkTheBoard()
            printBoard()
        } else {
            println("Cell Taken, Choose Another.")
        }
    }

    private fun generateComputerMove() {
        try {
            val availableCell = mutableListOf<Int>()
            board.forEachIndexed { index, cell ->
                if (cell is Cell.Empty) availableCell.add(index)
            }
            if (availableCell.isNotEmpty()) {
                val randomCell = availableCell.random()
                board.set(
                    index = randomCell,
                    element = Cell.Filled(player = Player())
                )
            }
        } catch (e: Throwable) {
            println("Error: ${e.message}")
        }
    }

    private fun checkTheBoard() {
        val winningCombination = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )
        val player1Cells = mutableListOf<Int>()
        val player2Cells = mutableListOf<Int>()
        board.forEachIndexed { index, cell ->
            if (cell.placeHolder == 'X')
                player1Cells.add(element = index)
            if (cell.placeHolder == 'O')
                player2Cells.add(element = index)
        }

        println("Your Moves: $player1Cells")
        println("Computer Moves: $player2Cells")

        run combinationLoop@{
            winningCombination.forEach { combination ->
                if (player1Cells.containsAll(elements = combination)) {
                    won()
                    return@combinationLoop
                }
                if (player2Cells.containsAll(elements = combination)) {
                    lost()
                    return@combinationLoop
                }
            }
        }

        if (board.none { it is Cell.Empty } && status is Status.Running) {
            draw()
        }

        if (status is Status.GameOver) {
            finish()
            playAgain()
        }

    }

    private fun finish() {
        status = Status.Ideal
        board.replaceAll { Cell.Empty }
    }

    private fun playAgain() {
        println("Do you wish to play another one? Y/N: ")
        val input = readlnOrNull()
        try {
            require(value = input != null)
            val capitalizedInput = input.replaceFirstChar(Char::titlecase)
            val positive = capitalizedInput.contains(other = "Y")
            val negative = capitalizedInput.contains(other = "N")
            require(value = positive || negative)
            if (positive)
                start()
            else if (negative)
                exitProcess(status = 0)
        } catch (e: Throwable) {
            println("Wrong option, type either 'Y' of 'N' ")
            playAgain()
        }
    }

    private fun won() {
        status = Status.GameOver
        printBoard()
        println("Congratulations ${player.name}, You Won!")
    }

    private fun lost() {
        status = Status.GameOver
        printBoard()
        println("Sorry ${player.name}, You Lost!")
    }

    private fun draw() {
        status = Status.GameOver
        printBoard()
        println("DRAW!")
    }

    private fun printBoard() {
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
