package ru.pchurzin.conversation;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

public class Transactions {
    public static void main(String[] args) {

    }
}

@Component
class SomeService3 {

    @Transactional
    public void method1() {
        // saves some data to database
    }

    public void method2() {
        method1();
    }

    @Transactional
    private void method3() {
        // saves some data to database
    }

    public void method4() {
        method3();
    }

}

@Component
@Transactional
class SomeService4 {

    public void method1() throws InterruptedException {
        Thread.sleep(1000); // throws InterruptedException
    }

}
