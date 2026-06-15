package com.dgsw.butja_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ButjaServerApplication

fun main(args: Array<String>) {
	runApplication<ButjaServerApplication>(*args)
}
