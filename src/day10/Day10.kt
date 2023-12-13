package day10

import utils.Resources.resourceAsListOfString

fun main(){

    val input = resourceAsListOfString("src/day10/Day10.txt")
    val neighbors = sequenceOf(-1 to 0, 0 to -1, 0 to 1, 1 to 0)
    val network = parseInput(input)

    fun getNext(point: Point, direction: String): Pair<Point, String>{
        when(direction){
            "NORTH" -> {
                val nextPoint = point + Point(-1,0)
                when(network.lookupValue(nextPoint)){
                    '|' -> return Pair(nextPoint, "NORTH")
                    '7' -> return Pair(nextPoint, "WEST")
                    'F' -> return Pair(nextPoint, "EAST")
                    'S' -> return Pair(nextPoint, "DONE")
                }
            }
            "SOUTH" -> {
                val nextPoint = point + Point(1,0)
                when(network.lookupValue(nextPoint)){
                    '|' -> return Pair(nextPoint, "SOUTH")
                    'L' -> return Pair(nextPoint, "EAST")
                    'J' -> return Pair(nextPoint, "WEST")
                    'S' -> return Pair(nextPoint, "DONE")
                }
            }
            "EAST" -> {
                val nextPoint = point + Point(0,1)
                when(network.lookupValue(nextPoint)){
                    '-' -> return Pair(nextPoint, "EAST")
                    '7' -> return Pair(nextPoint, "SOUTH")
                    'J' -> return Pair(nextPoint, "NORTH")
                    'S' -> return Pair(nextPoint, "DONE")
                }
            }
            "WEST" -> {
                val nextPoint = point + Point(0,-1)
                when(network.lookupValue(nextPoint)){
                    '-' -> return Pair(nextPoint, "WEST")
                    'L' -> return Pair(nextPoint, "NORTH")
                    'F' -> return Pair(nextPoint, "SOUTH")
                    'S' -> return Pair(nextPoint, "DONE")
                }
            }
        }
        return Pair(Point(0,0), "NULL")
    }

    fun traverse(start: Point): Set<Point>{
        val visited = mutableSetOf(start)
        val queue = mutableListOf(Pair(start+Pair(1,0), "EAST"))
        while(queue.isNotEmpty()){
            val current = queue.removeFirst()
            when(current.first){
                start -> return visited
                else -> {
                    visited.add(current.first)
                    val nextTile = getNext(current.first, current.second)
                    queue.add(nextTile)
                }
            }
        }
        error("Can't find loop")
    }

    fun part1(): Int{
        return traverse(network.start).size/2
    }

    println(part1())

}
private operator fun Point.plus(that: Point): Point =
    Point(this.first + that.first, this.second + that.second)