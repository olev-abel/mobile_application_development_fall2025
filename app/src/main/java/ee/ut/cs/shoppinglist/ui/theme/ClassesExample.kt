package ee.ut.cs.shoppinglist.ui.theme

open class Item(val name: String, var quantity: Int) {

    open fun modifyQuantity(newQuantity: Int) {
        this.quantity = newQuantity
    }

}

class Milk(id: Int, name: String, quantity: Int) : Item(name, quantity) {

}

abstract class Animal {
    abstract fun sound()
    fun eat() = println("Nom nom!")
}

class Dog(): Animal() {
    override fun sound() {
        println("Whoof whoof!")
    }

}

class Cat(): Animal() {
    override fun sound() {
        println("Meow!")
    }

}

sealed class Result
class Success(val data: String) : Result()
class Failure(val message: String, val code: Int): Result()
class Loading() : Result()


fun handle(result: Result) {
    when(result) {
        is Failure -> println("Failure with code ${result.code} and message: ${result.message}")
        is Success -> println("Success with ${result.data} ")
        is Loading -> println("Loading...")
    }
}

