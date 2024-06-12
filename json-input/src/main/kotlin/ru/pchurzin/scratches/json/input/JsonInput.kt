package ru.pchurzin.scratches.json.input

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.UUID

fun main() {
    val objectMapper = jacksonObjectMapper()
    val s: SomeService = SomeServiceImpl()
    val json = """
        {
            "a": "2a83925d-363b-4ea9-bc35-cecf0fb5bf47",
            "b": 42
        }
    """.trimIndent()
    val i = JsonInput(json, objectMapper)
    println("pass 1")
    s.doWork(i)
    println("pass 2")
    s.doWork(i)
}

interface SomeService {

    fun doWork(input: Input)

    interface Input {
        val a: () -> UUID
        val b: () -> Int
    }
}

class SomeServiceImpl : SomeService {
    override fun doWork(input: SomeService.Input) {
        val a = runCatching { input.a() }
        val b = runCatching { input.b() }

        a.onFailure {
            println("failed to get a: $it")
        }.onSuccess {
            println("a: $it")
        }
        b.onFailure {
            println("failed to get b: $it")
        }.onSuccess {
            println("b: $it")
        }
    }
}

class JsonInput(json: String, private val objectMapper: ObjectMapper) : SomeService.Input {

    private val tree by lazy {
        println("reading tree")
        runCatching { objectMapper.readTree(json) }
    }
    private val _a by lazy {
        println("reading a")
        tree.mapCatching {
            objectMapper.convertValue<UUID>(it["a"])
        }
    }
    private val _b by lazy {
        println("reading b")
        tree.mapCatching {
            objectMapper.convertValue<Int>(it["b"])
        }
    }

    override val a: () -> UUID = { _a.getOrThrow() }
    override val b: () -> Int = { _b.getOrThrow() }
}