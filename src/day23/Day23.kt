package day23

import utils.Point2D
import utils.Resources.resourceAsListOfString
import utils.isSafe
import utils.get
import kotlin.math.max

fun main(){
    val input = resourceAsListOfString("src/day23/Day23.txt")
    val grid = input.map { it.toCharArray() }.toTypedArray()
    val start = Point2D(grid.first().indexOf('.'), grid.indices.first)
    val goal = Point2D(grid.last().indexOf('.'), grid.indices.last)

    fun traverse(nextLocations: (Point2D) -> List<Pair<Point2D, Int>>): Int{
        var best = 0
        val visited = mutableSetOf<Point2D>()

        fun traverseWork(location: Point2D, steps: Int): Int{
            if (location == goal){
                best = max(steps, best)
                return best
            }
            visited += location
            nextLocations(location)
                .filter{(place, _) -> place !in visited}
                .forEach{(place, distance) -> traverseWork(place, distance+steps)}
            visited-= location
            return best
        }
        return traverseWork(start,0)
    }

    fun Array<CharArray>.findDecisionPoints() = buildSet {
        add(start)
        add(goal)
        this@findDecisionPoints.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != '#') {
                    Point2D(x, y).apply {
                        if (cardinalNeighbors()
                                .filter { grid.isSafe(it) }
                                .filter { grid[it] != '#' }.size > 2
                        ) {
                            add(this)
                        }
                    }
                }
            }
        }
    }

    fun reduceGridFromPoint(from: Point2D, toAnyOther: Set<Point2D>): Map<Point2D, Int> {
        val queue = ArrayDeque<Pair<Point2D, Int>>().apply {
            add(from to 0)
        }
        val seen = mutableSetOf(from)
        val answer = mutableMapOf<Point2D, Int>()
        while (queue.isNotEmpty()) {
            val (location, distance) = queue.removeFirst()
            if (location != from && location in toAnyOther) {
                answer[location] = distance
            } else {
                location.cardinalNeighbors()
                    .filter { grid.isSafe(it) }
                    .filter { grid[it] != '#' }
                    .filter { it !in seen }
                    .forEach {
                        seen += it
                        queue.add(it to distance + 1)
                    }
            }
        }
        return answer
    }

    fun reduceGrid(): Map<Point2D, Map<Point2D, Int>> =
        grid.findDecisionPoints().let { decisionPoints ->
            decisionPoints.associateWith { point ->
                reduceGridFromPoint(point, decisionPoints)
            }
        }




    fun part1(): Int{
        val answer = traverse { location -> location.cardinalNeighbors()
            .filter { grid.isSafe(it) }
            .filter { newLocation -> grid[newLocation].matchesDirection(newLocation -location) }
            .map { it to 1 }
        }
        return answer
    }

    fun part2(): Int {
        val reducedGrid = reduceGrid()
        return traverse { location ->
            reducedGrid
                .getValue(location)
                .map { it.key to it.value }
        }
    }

    println(part1())
    println(part2())
}

fun Char.matchesDirection(direction: Point2D): Boolean =
    when (this) {
        '^' -> Point2D.NORTH == direction
        '<' -> Point2D.WEST == direction
        'v' -> Point2D.SOUTH == direction
        '>' -> Point2D.EAST == direction
        '.' -> true
        else -> false
    }
