package day12

import utils.Resources.resourceAsListOfString

fun main(){

    val chars = listOf('.', '#')

    fun parseInput(input: List<String>): List<Pair<String, List<Int>>>{
        return input.map { Pair(it.substringBefore(" "), it.substringAfter(" ").split(",").map { it.toInt() }) }
    }

    fun generateCombinations(n: Int): List<String> {
        val result = mutableListOf<String>()
        val queue = mutableListOf<String>()
        queue.add("")
        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            if (current.length == n) {
                result.add(current)
            } else {
                for (char in chars) {
                    queue.add(current + char)
                }
            }
        }
        return result
    }

    tailrec fun replaceChars(springs: String, combination: String): String{
        val newString = springs.replaceFirst('?', combination.first())
        val newCombination = combination.drop(1)
        return if (newCombination.isEmpty()) {
            newString
        } else {
            replaceChars(newString, newCombination )
        }
    }

    fun findArrangements(row: Pair<String, List<Int>>):Int{
        val springs = row.first
        val sizes = row.second
        val combinations = generateCombinations(springs.count { it == '?' })
        val possibleArrangements = combinations.map{combination -> replaceChars(springs,combination)}
            .map{it.split(".").filter { it.isNotEmpty() }.map{it.length}}.filter { it == sizes }
        return possibleArrangements.count()

    }

    fun part1(rows: List<Pair<String, List<Int>>>): Int{
        return rows.sumOf { row -> findArrangements(row) }

    }

    val input = resourceAsListOfString("src/day12/Day12.txt")
    val rows = parseInput(input)
    println(part1(rows))

}