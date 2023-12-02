package day01

import utils.Resources.resourceAsListOfString

fun main(){

    fun findFirstAndLast(line: String): Pair<String,String>{
        val regex: Regex = """((one|eno)|(two|owt)|(three|eerht)|(four|ruof)|(five|evif)|(six|xis)|(seven|neves)|(eight|thgie)|(nine|enin)|1|2|3|4|5|6|7|8|9)""".toRegex()
        return Pair(regex.find(line)!!.value, regex.find(line.reversed())!!.value.reversed())
    }

    fun calibrationValue(pair: Pair<String,String>): Int{
        val replacementValues = mapOf("one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5", "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9")
        val first = when{
            pair.first.length == 1 -> pair.first
            else -> replacementValues.getValue(pair.first)
        }
        val last = when{
            pair.second.length == 1 -> pair.second
            else -> replacementValues.getValue(pair.second)
        }
        return (first+last).toInt()
    }

    fun part1(input: List<String>): Int{
        val filteredInput = input.map { value -> value.filter { it.isDigit() }}
        return filteredInput.map { it.first().toString() + it.last().toString()  }.sumOf { it.toInt() }
    }

    fun part2(input: List<String>): Int{
        return input.map { line -> findFirstAndLast(line) }.map { code -> calibrationValue(code) }.sumOf { it }
    }

    val input = resourceAsListOfString("src/day01/Day01.txt")
    println(part1(input))
    println(part2(input))
}