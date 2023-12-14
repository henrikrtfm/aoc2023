package day13

import utils.Functions.rotateLeft
import utils.Resources.resourceAsText

typealias Pattern = Array<CharArray>

fun main(){

    fun parsePattern(lines: List<String>): Pattern {
        return lines.map { line -> line.chunked(1) }
            .map { it -> it.map { it -> it[0] }.toCharArray() }
            .toTypedArray<CharArray>()
    }

    fun List<CharArray>.isPerfect(): Boolean{
        return when {
            this.groupingBy { it.joinToString("") }.eachCount().all { it.value%2 == 0 } -> true
            else -> false
        }
    }

    fun printPattern(pattern: Array<CharArray>) {
        println(pattern.joinToString(System.lineSeparator()) { row -> row.joinToString(" ") })
    }

    fun checkForPerfection(line: Int, pattern: Pattern): Pair<Boolean, Int>{
        return when{
            line == 0 -> Pair(pattern.toList().isPerfect(), pattern.size)
            line <= pattern.size/2 -> Pair(pattern.dropLast(pattern.size-line*2).isPerfect(),pattern.dropLast(pattern.size-line*2).size)
            else -> Pair(pattern.drop(line*2-pattern.size).isPerfect(), pattern.drop(line*2-pattern.size).size)
        }
    }

    fun findReflectionLines(pattern: Pattern): List<Int>{
        val possibleLines = (0 until pattern.size - 1).map { rowIndex ->
            pattern[rowIndex].contentEquals(pattern[rowIndex + 1]) }
        return possibleLines.indices.filter { possibleLines[it] }.map { it+1 }
    }

    fun findPerfectReflection(pattern: Pattern): Int{
        val horizontalLines = findReflectionLines(pattern)

        val horizontalLine = horizontalLines.map { horizontalLine -> Pair(horizontalLine, checkForPerfection(horizontalLine, pattern)) }.filter{it.second.first}.maxByOrNull { it.second.second }
        if(horizontalLine != null){
            println("Horizontal perfect: $horizontalLine")
            return horizontalLine.first*100
        }
        val verticalLines = findReflectionLines(pattern.rotateLeft())

        val verticalLine = verticalLines.map { verticalLine -> Pair(verticalLine, checkForPerfection(verticalLine, pattern.rotateLeft())) }.filter{it.second.first}.maxByOrNull { it.second.second }
        if(verticalLine != null){
            println("Vertical perfect: $verticalLine")
            return pattern.rotateLeft().size-verticalLine.first
        }

        return 0
    }

    fun part1(patterns: List<Pattern>): Int{
        return patterns.sumOf { pattern -> findPerfectReflection(pattern) }
    }

    val input = resourceAsText("src/day13/Day13.txt").split("\n\n").map { it.split("\n") }
    val patterns = input.map { parsePattern(it) }

    println(part1(patterns))

}