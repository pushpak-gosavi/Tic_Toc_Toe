class Game {

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
