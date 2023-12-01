package day01

import utils.Resources.resourceAsListOfString

fun main(){

    fun part1(input: List<String>): Int{
        val filteredInput = input.map { value -> value.filter { it.isDigit() }}
        return filteredInput.map { it.first().toString() + it.last().toString()  }.sumOf { it.toInt() }
    }

    fun part2(input: List<String>): Int{
        val replacementValues = mapOf("one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5", "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9")
        val regex: Regex = """(?=((one)|(two)|(three)|(four)|(five)|(six)|(seven)|(eight)|(nine)))""".toRegex()

        val replacedInput = input.map {it.replace(regex) { matchResult ->
            val matchedValue = (1..10).firstNotNullOfOrNull { matchResult.groupValues[it] }
            println(matchResult.groupValues)
            replacementValues[matchedValue] ?: matchResult.value
            }
        }
        return(part1(replacedInput))

    }

    val input = resourceAsListOfString("src/day01/Day01.txt")
    println(part1(input))
    println(part2(input))
}