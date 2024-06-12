package ru.pchurzin.scratches.eo

import java.io.BufferedReader
import java.io.BufferedWriter

fun main() {
    val reader = System.`in`.bufferedReader()
    val a1: Value<Int> = BufferedReaderIntValue(reader)
    val a2: Value<Int> = BufferedReaderIntValue(reader)
    val a3: Value<Int> = BufferedReaderIntValue(reader)
    val sum1: Value<Int> = IntSum(a1, a2)
    val sum2: Value<Int> = IntSum(sum1, a3)
    val output = systemOutput()
    output(sum2())
}

interface Value<out T> : () -> T

class BufferedReaderIntValue(
    private val reader: BufferedReader
) : Value<Int> {
    override fun invoke(): Int {
        return reader.readLine().toInt()
    }
}

class IntSum(
    private val a1: Value<Int>,
    private val a2: Value<Int>
) : Value<Int> {
    override fun invoke(): Int {
        return a1() + a2()
    }
}

interface Output<in T> : (T) -> Unit

class BufferedWriterOutput(
    private val writer: BufferedWriter
) : Output<Any> {
    override fun invoke(p1: Any) {
        writer.write(p1.toString())
        writer.flush()
    }
}

fun systemOutput(): Output<Any> = BufferedWriterOutput(System.out.bufferedWriter())
