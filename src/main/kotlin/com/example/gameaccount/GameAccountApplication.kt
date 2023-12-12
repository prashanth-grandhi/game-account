package com.example.gameaccount

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan
class GameAccountApplication

fun main(args: Array<String>) {
	runApplication<GameAccountApplication>(*args)
}
