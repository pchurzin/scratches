package ru.pchurzin.scratches.poc

data class User(val name: String, val age: Int)

fun main() {
    val user = User("John", 25)
    println(user)
    val user2 = user.with {
        age = 30
    }
    println(user2)
}

fun User.with(block: UserWithersScope.() -> Unit): User = UserWithers(this).apply(block).build()

interface UserWithersScope {
    var name: String
    var age: Int
}

private class UserWithers(user: User) : UserWithersScope {
    override var name = user.name
    override var age = user.age

    fun build(): User = User(name, age)
}

