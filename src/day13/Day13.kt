package day13


import utils.Resources.resourceAsListOfString
import kotlin.math.absoluteValue

typealias Pattern = Array<CharArray>

fun main(){
    val input = resourceAsListOfString("src/day13/Day13.txt")


    infix fun String.diff(other: String): Int =
        indices.count { this[it] != other[it] } + (length - other.length).absoluteValue

    fun createMirrorRanges(start: Int, max: Int): List<Pair<Int, Int>> =
        (start downTo 0).zip(start + 1..max)

    fun List<String>.columnToString(column: Int): String =
        this.map { it[column] }.joinToString("")

    fun findHorizontalMirror(pattern: List<String>, goalTotal: Int): Int? =
        (0 until pattern.lastIndex).firstNotNullOfOrNull { start ->
            if (createMirrorRanges(start, pattern.lastIndex)
                    .sumOf { (up, down) ->
                        pattern[up] diff pattern[down]
                    } == goalTotal
            ) (start + 1) * 100
            else null
        }

    fun findVerticalMirror(pattern: List<String>, goalTotal: Int): Int? =
        (0 until pattern.first().lastIndex).firstNotNullOfOrNull { start ->
            if (createMirrorRanges(start, pattern.first().lastIndex)
                    .sumOf { (left, right) ->
                        pattern.columnToString(left) diff pattern.columnToString(right)
                    } == goalTotal
            ) start + 1
            else null
        }

    fun findMirror(pattern: List<String>, goalTotal: Int): Int =
        findHorizontalMirror(pattern, goalTotal) ?:
        findVerticalMirror(pattern, goalTotal) ?:
        throw IllegalStateException("Pattern does not mirror")

    fun parseInput(input: List<String>): List<List<String>> =
        input.joinToString("\n").split("\n\n").map { it.lines() }

    val patterns: List<List<String>> = parseInput(input)

    fun part1(): Int =
        patterns.sumOf { findMirror(it, 0) }

    fun part2(): Int =
        patterns.sumOf { findMirror(it, 1) }

    println(part2())
}
