package ru.pchurzin.conversation

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SomeService {

    @Transactional
    fun method1() {
        // saves some data to database
    }

    fun method2() {
        method1()
    }

    fun method3() {
        method4()
    }

    @Transactional
    private fun method4() {
        // saves some data to database
    }
}

// =============================================================================

@Component
@Transactional
class SomeService2 {
    fun method1() {
        // saves some data to database
        Thread.sleep(1000) // throws InterruptedException
    }
}

inline fun <reified T : Any> getBean(): T = TODO()