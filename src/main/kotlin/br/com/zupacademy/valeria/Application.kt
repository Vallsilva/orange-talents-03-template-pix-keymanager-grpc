package br.com.zupacademy.valeria

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zupacademy.valeria")
		.start()
}

