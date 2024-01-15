package day09

import utils.findNextInSequence
import utils.Resources.resourceAsListOfString
fun main() {
    val input = resourceAsListOfString("src/day09/Day09.txt").map { it.split(" ").map { it.toInt() } }

    println(input.sumOf { findNextInSequence(it) })
    println(input.sumOf { findNextInSequence(it.reversed())})
}