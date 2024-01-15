package day08

import utils.Resources.resourceAsListOfString

typealias Nodes = Map<String, Pair<String, String>>
fun main(){

    fun parseInput(input: List<String>): Nodes{
        val nodes = mutableMapOf<String, Pair<String, String>>()
        input.forEach { line ->
            nodes[line.substringBefore(" =")] = Pair(line.substringAfter("(").substringBefore(","),line.substringAfter(", ").substringBefore(")"))
        }
        return nodes
    }

    fun getNextNode(source: String, nodes: Nodes, instruction: Char): String{
        return when(instruction){
            'L' -> nodes[source]?.first!!
            'R' -> nodes[source]?.second!!
            else -> ""
        }
    }

    fun part1(start: String, end: String, instructions: String, nodes: Nodes): Int{
        var steps = 0
        var nextNode = start
        var index = 0
        while(true) {
            steps++
            nextNode = getNextNode(nextNode, nodes, instructions[index])
            when{
                nextNode == end -> return steps
            }
            when{
                index == instructions.length-1 -> index = 0
                else -> index++
            }

        }
    }
    fun part2(start: String, instructions: String, nodes: Nodes): Long{
        var steps = 0
        var nextNode = start
        var index = 0
        while(true) {
            steps++
            nextNode = getNextNode(nextNode, nodes, instructions[index])
            when {
                nextNode.last() == 'Z' -> return steps.toLong()
            }
            when {
                index == instructions.length - 1 -> index = 0
                else -> index++
            }
        }
    }



    val input = resourceAsListOfString("src/day08/Day08.txt")
    val instructions = input.first()
    val nodes = parseInput(input.subList(2,input.size))
    val start = "AAA"
    val end = "ZZZ"
    val starts = nodes.filter { it.key.last() == 'A' }.keys.toList()
    println(part1(start, end, instructions, nodes))
    println(starts.map{ part2(it, instructions, nodes)}.reduce(Long::lcm))

}

tailrec fun Long.gcd(other: Long): Long =
    if (other == 0L) this
    else other.gcd(this % other)

fun Long.lcm(other: Long): Long =
    (this * other) / this.gcd(other)
