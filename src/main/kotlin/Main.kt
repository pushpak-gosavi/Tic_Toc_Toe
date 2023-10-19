fun main() {
    val objTicTack = TicTack()
    objTicTack.takeName()
    objTicTack.startGame()
}

class  TicTack{
     lateinit var name:String
     lateinit var array:Array<Int>
    fun takeName(){
        println("Plese Write the name :  ")
        name = readln()
        println("Player name is $name")
    }
    fun startGame(){
        println("Let Start the game $name")
    }
}