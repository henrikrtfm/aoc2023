package day16

import utils.Resources.resourceAsListOfString

typealias Point = Pair<Int, Int>

private val UP = Point(-1,0)
private val RIGHT = Point(0,1)
private val DOWN = Point(1,0)
private val LEFT = Point(0,-1)
private val movements: Map<Pair<Char, String>, List<String>> =
    mapOf(
        ('.' to "DOWN") to listOf("DOWN"),
        ('.' to "UP") to listOf("UP"),
        ('.' to "LEFT") to listOf("LEFT"),
        ('.' to "RIGHT") to listOf("RIGHT"),
        ('|' to "DOWN") to listOf("DOWN"),
        ('|' to "UP") to listOf("UP"),
        ('|' to "LEFT") to listOf("UP", "DOWN"),
        ('|' to "RIGHT") to listOf("UP", "DOWN"),
        ('-' to "RIGHT") to listOf("RIGHT"),
        ('-' to "LEFT") to listOf("LEFT"),
        ('-' to "DOWN") to listOf("LEFT","RIGHT"),
        ('-' to "UP") to listOf("LEFT","RIGHT"),
        ('/' to "UP") to listOf("RIGHT"),
        ('/' to "DOWN") to listOf("LEFT"),
        ('/' to "RIGHT") to listOf("UP"),
        ('/' to "LEFT") to listOf("DOWN"),
        ('\\' to "UP") to listOf("LEFT"),
        ('\\' to "DOWN") to listOf("RIGHT"),
        ('\\' to "RIGHT") to listOf("DOWN"),
        ('\\' to "LEFT") to listOf("UP")
    )
fun main(){
    val input = resourceAsListOfString("src/day16/Day16.txt")
    val grid = input.map { it.toCharArray() }.toTypedArray()
    val start = Point(0,0)
    val minX = -1
    val minY = -1
    val maxX = input.size
    val maxY = input[0].length

    fun moveUp(self: Point): Boolean{
        return (self+UP).first > minX
    }
    fun moveDown(self: Point): Boolean{
        return (self+DOWN).first < maxX
    }
    fun moveLeft(self: Point): Boolean{
        return (self+ LEFT).second > minY
    }
    fun moveRight(self: Point): Boolean{
        return (self+ RIGHT).second < maxY
    }

    fun traverse(start: Point, direction: String): Int{
        val energized = mutableMapOf<Point, Int>()
        val seen = mutableSetOf<Pair<Point, String>>()
        val queue = mutableListOf(Pair(start, direction))
        while(queue.isNotEmpty()){
            val current = queue.removeFirst()
            when(energized[current.first]){
                null -> energized[current.first] = 1
                else -> energized[current.first] = energized[current.first]!! + 1
            }
            movements[grid[current.first] to current.second]?.let { nextDirections ->
                nextDirections.forEach { nextDirection ->
                    when (nextDirection) {
                        "UP"    -> if (moveUp(current.first) && Pair(current.first + UP, nextDirection) !in seen) {
                            queue.add(Pair(current.first + UP, nextDirection))
                            seen+=Pair(current.first + UP, nextDirection)
                        }
                        "DOWN"  -> if (moveDown(current.first) && Pair(current.first + DOWN, nextDirection) !in seen) {
                            queue.add(Pair(current.first + DOWN, nextDirection))
                            seen+=Pair(current.first + DOWN, nextDirection)
                        }
                        "LEFT"  -> if (moveLeft(current.first) && Pair(current.first + LEFT, nextDirection) !in seen) {
                            queue.add(Pair(current.first + LEFT, nextDirection))
                            seen+=Pair(current.first + LEFT, nextDirection)
                        }
                        "RIGHT" -> if (moveRight(current.first) && Pair(current.first + RIGHT, nextDirection) !in seen) {
                            queue.add(Pair(current.first + RIGHT, nextDirection))
                            seen+=Pair(current.first + RIGHT, nextDirection)
                        }
                    }
                }
            }?: error("Invalid movement")

        }
        return energized.size
    }

    fun part1(): Int{
        return traverse(start, "RIGHT")
    }

    fun part2(): Int{
        return listOf(
            grid.first().indices.map { Pair(Point(it, 0),"RIGHT") },
            grid.first().indices.map { Pair(Point(it, grid.lastIndex), "LEFT") },
            grid.indices.map { Pair(Point(0, it),"DOWN") },
            grid.indices.map { Pair(Point(grid.first().lastIndex, it), "UP") }
        )
            .flatten()
            .maxOf { traverse(it.first, it.second) }
    }
    println(part1())
    println(part2())


}

private operator fun Point.plus(that: Point): Point =
    Point(this.first + that.first, this.second + that.second)

private operator fun Array<CharArray>.get(at: Point): Char =
    this[at.first][at.second]