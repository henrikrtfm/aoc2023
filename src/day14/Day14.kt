package day14

import utils.Resources.resourceAsListOfString

typealias Point = Pair<Int, Int>

private val NORTH =  Point(-1,0)
private val SOUTH =  Point(1,0)
private val EAST = Point(0,1)
private val WEST = Point(0,-1)

fun main(){
    var roundRocks = mutableListOf<Point>()
    val cubeRocks = mutableSetOf<Point>()
    val resultSeries = mutableMapOf<Int, MutableList<Int>>()
    val input = resourceAsListOfString("src/day14/Day14_sample.txt")
    val minX = 0
    val minY = 0
    val maxX = input.size
    val maxY = input[0].length+1
    val load = (0..input.size).reversed().mapIndexed { index, value -> value to index }.toMap()

    fun parseInput(input: List<String>){
        input.forEachIndexed { indexrow, row -> row.forEachIndexed { indexcol, char ->
            when(char){
                'O' -> roundRocks.add(Point(indexrow, indexcol))
                '#' -> cubeRocks.add(Point(indexrow, indexcol))
                else -> {}
            } } }
    }

    fun moveNorth(self: Point): Boolean{
        return !cubeRocks.contains(self+NORTH) && !roundRocks.contains(self+NORTH) && (self+NORTH).first >= minX
    }
    fun moveSouth(self: Point): Boolean{
        return !cubeRocks.contains(self+SOUTH) && !roundRocks.contains(self+SOUTH) && (self+SOUTH).first < maxX
    }
    fun moveEast(self: Point): Boolean{
        return !cubeRocks.contains(self+EAST) && !roundRocks.contains(self+EAST) && (self+EAST).second < maxY
    }
    fun moveWest(self: Point): Boolean{
        return !cubeRocks.contains(self+WEST) && !roundRocks.contains(self+WEST) && (self+WEST).second >= minY
    }

    fun tiltNorth(self: Point): Point{
        return when{
            moveNorth(self) -> self+NORTH
            else -> self
        }
    }
    fun tiltSouth(self: Point): Point{
        return when{
            moveSouth(self) -> self+SOUTH
            else -> self
        }
    }
    fun tiltEast(self: Point): Point{
        return when{
            moveEast(self) -> self+EAST
            else -> self
        }
    }
    fun tiltWest(self: Point): Point{
        return when{
            moveWest(self) -> self+WEST
            else -> self
        }
    }
    fun tiltCycle(direction: String){
        var done = false
        when(direction){
            "NORTH" -> {
                roundRocks.sortedBy { it.first }
                while (!done) {
                    val newState = roundRocks.map { tiltNorth(it) }.toMutableList()
                    when {
                        newState == roundRocks -> done = true
                        else -> roundRocks = newState
                    }
                }
            }
            "EAST" -> {
                roundRocks.sortedByDescending { it.second }
                while (!done) {
                    val newState = roundRocks.map { tiltEast(it) }.toMutableList()
                    when {
                        newState == roundRocks -> done = true
                        else -> roundRocks = newState
                    }
                }
            }
            "SOUTH" -> {
                roundRocks.sortedByDescending { it.first }
                while (!done) {
                    val newState = roundRocks.map { tiltSouth(it) }.toMutableList()
                    when {
                        newState == roundRocks -> done = true
                        else -> roundRocks = newState
                    }
                }
            }
            "WEST" -> {
                roundRocks.sortedBy { it.second }
                while (!done) {
                    val newState = roundRocks.map { tiltWest(it) }.toMutableList()
                    when {
                        newState == roundRocks -> done = true
                        else -> roundRocks = newState
                    }
                }
            }
        }
    }

    fun spinCycle(){
        tiltCycle("NORTH")
        tiltCycle("WEST")
        tiltCycle("SOUTH")
        tiltCycle("EAST")

    }

    fun part1() {
        var done = false
        while (!done) {
            val newState = roundRocks.map { tiltNorth(it) }.toMutableList()
            when {
                newState == roundRocks -> done = true
                else -> roundRocks = newState
            }
        }
    }

    fun part2(cycles: Int){
        var cycle = 1
        while(cycle<=cycles) {
            spinCycle()
            val loadSum = roundRocks.sumOf { load.getValue(it.first) }
            when{
                resultSeries.containsKey(loadSum) -> resultSeries[loadSum]?.add(cycle)
                else -> resultSeries[loadSum] = listOf(cycle).toMutableList()
            }
            cycle++
        }
    }

    parseInput(input)
    //part1()
    part2(1000000)
    resultSeries.forEach {
        val key = it.key
        val size = it.value.size
        println("Key: $key Values: $size") }


}

private operator fun Point.plus(that: Point) = Point(
    this.first+that.first, this.second+that.second)