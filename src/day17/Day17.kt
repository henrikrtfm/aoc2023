package day17

import utils.Functions.get
import utils.Resources.resourceAsListOfString
import utils.Point2D
import utils.Functions.isSafe
import utils.Point2D.Companion.NORTH
import utils.Point2D.Companion.SOUTH
import utils.Point2D.Companion.EAST
import utils.Point2D.Companion.WEST
import java.util.PriorityQueue

fun main() {
    val input = resourceAsListOfString("src/day17/Day17.txt")
    val grid = input.map { row -> row.map { it.digitToInt() } }
    val directions = mapOf(
        NORTH to setOf(NORTH, EAST, WEST),
        WEST to setOf(WEST, NORTH, SOUTH),
        SOUTH to setOf(SOUTH, EAST, WEST),
        EAST to setOf(EAST, NORTH, SOUTH)
    )

    fun calculateHeatLoss(minSteps: Int = 1, isValidNextMove: (State, Point2D) -> Boolean): Int {
        val goal = Point2D(grid.first().lastIndex, grid.lastIndex)
        val seen = mutableSetOf<State>()
        val queue = PriorityQueue<Work>()

        listOf(EAST, SOUTH).forEach { startDirection ->
            State(Point2D(0, 0), startDirection, 0).apply {
                queue += Work(this, 0)
                seen += this
            }
        }

        while (queue.isNotEmpty()) {
            val (current, heatLoss) = queue.poll()
            if (current.location == goal && current.steps >= minSteps) return heatLoss

            directions
                .getValue(current.direction)
                .filter { direction -> grid.isSafe(current.location + direction) }
                .filter { direction -> isValidNextMove(current, direction) }
                .map { direction -> current.next(direction) }
                .filterNot { state -> state in seen }
                .forEach { state ->
                    queue += Work(state, heatLoss + grid[state.location])
                    seen += state
                }
        }
        throw IllegalStateException("No route to goal")
    }

    fun solvePart1(): Int =
        calculateHeatLoss { state, nextDirection ->
            state.steps < 3 || state.direction != nextDirection
        }

    fun solvePart2(): Int =
        calculateHeatLoss(4) { state, nextDirection ->
            if (state.steps > 9) state.direction != nextDirection
            else if (state.steps < 4) state.direction == nextDirection
            else true
        }

    println(solvePart1())
    println(solvePart2())

}
private data class State(val location: Point2D, val direction: Point2D, val steps: Int) {
        fun next(nextDirection: Point2D): State =
            State(location + nextDirection, nextDirection, if (direction == nextDirection) steps + 1 else 1)
}

private data class Work(val state: State, val heatLoss: Int) : Comparable<Work> {
        override fun compareTo(other: Work): Int =
            heatLoss - other.heatLoss
}