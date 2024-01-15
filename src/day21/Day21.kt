package day21

import utils.isSafe
import utils.get
import utils.Point2D
import utils.Resources.resourceAsListOfString

fun main(){
    val input = resourceAsListOfString("src/day21/Day21.txt")
    val gardenMap : Array<CharArray> = input.map { it.toCharArray() }.toTypedArray()
    val start: Point2D = gardenMap.mapIndexedNotNull { y, row ->
        if ('S' in row) Point2D(row.indexOf('S'), y) else null
    }.first()
    val rocks = mutableSetOf<Point2D>()

    fun parseInput(input: List<String>){
        input.forEachIndexed { indexrow, row -> row.forEachIndexed { indexcol, char ->
            when(char){
                '#' -> rocks.add(Point2D(indexrow, indexcol))
                else -> {}
            } } }
    }

    fun part1(steps: Int): Int{
        var plots = mutableSetOf<Point2D>()
        val queue = mutableListOf(start)
        for (i in 0 until steps){
            val currentPlots = mutableSetOf<Point2D>()
            while(queue.isNotEmpty()){
                val current = queue.removeFirst()
                val possibleNeighbors = current.cardinalNeighbors().filter { it !in rocks }
                possibleNeighbors.forEach { neighbor ->
                    currentPlots.add(neighbor)
                }
            }
            currentPlots.forEach { plot ->
                queue.add(plot)
            }
            plots = currentPlots
        }
        return plots.size
    }

    fun countSteps(max: Int = gardenMap.size): Map<Point2D, Int> = buildMap {
        val queue = ArrayDeque<Pair<Point2D, Int>>().apply {
            add(start to 0)
        }
        while (queue.isNotEmpty()) {
            queue.removeFirst().let { (location, distance) ->
                if (location !in this && distance <= max) {
                    this[location] = distance
                    queue.addAll(
                        location
                            .cardinalNeighbors()
                            .filter { it !in this }
                            .filter { gardenMap.isSafe(it) }
                            .filter { gardenMap[it] != '#' }
                            .map { it to distance + 1 }
                    )
                }
            }
        }
    }

    fun part2(stepCount: Int): Long{
        val steps = countSteps()
        val oddCorners = steps.count { it.value % 2 == 1 && it.value > 65 }.toLong()
        val evenCorners = steps.count { it.value % 2 == 0 && it.value > 65 }.toLong()
        val evenBlock = steps.values.count { it % 2 == 0 }.toLong()
        val oddBlock = steps.values.count { it % 2 == 1 }.toLong()
        val n: Long = ((stepCount.toLong() - (gardenMap.size / 2)) / gardenMap.size)

        val even: Long = n * n
        val odd: Long = (n + 1) * (n + 1)
        return (odd * oddBlock) + (even * evenBlock) - ((n + 1) * oddCorners) + (n * evenCorners)
    }

    parseInput(input)
    println(part1(64))
    println(part2(26501365))

}