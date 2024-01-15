package day10

import utils.Point2D
import utils.get
import utils.isSafe
import utils.set
import utils.Point2D.Companion.EAST
import utils.Point2D.Companion.NORTH
import utils.Point2D.Companion.SOUTH
import utils.Point2D.Companion.WEST
import utils.Resources.resourceAsListOfString

fun main(){

    val input = resourceAsListOfString("src/day10/Day10.txt")
    val grid: Array<CharArray> = input.map { it.toCharArray() }.toTypedArray()
    val start: Point2D = grid.indexOfFirst { 'S' in it }.let { y ->
        Point2D(grid[y].indexOf('S'), y)
    }

    val movements: Map<Pair<Char, Point2D>, Point2D> =
        mapOf(
            ('|' to SOUTH) to SOUTH,
            ('|' to NORTH) to NORTH,
            ('-' to EAST) to EAST,
            ('-' to WEST) to WEST,
            ('L' to WEST) to NORTH,
            ('L' to SOUTH) to EAST,
            ('J' to SOUTH) to WEST,
            ('J' to EAST) to NORTH,
            ('7' to EAST) to SOUTH,
            ('7' to NORTH) to WEST,
            ('F' to WEST) to SOUTH,
            ('F' to NORTH) to EAST
        )

    val markingDirection: Map<Point2D, Point2D> =
        mapOf(
            SOUTH to EAST,
            NORTH to WEST,
            WEST to SOUTH,
            EAST to NORTH
        )


    fun traversePipe(preMove: (Point2D, Point2D, Point2D) -> (Unit) = { _, _, _ -> }): Set<Point2D> {
        val pipe = mutableSetOf(start)
        var current = start
            .cardinalNeighbors()
            .filter { grid.isSafe(it) }
            .first {
                val d = it - start
                (grid[it] to d in movements)
            }
        var direction = current - start
        while (current != start) {
            pipe += current
            movements[grid[current] to direction]?.let { nextDirection ->
                preMove(current, direction, nextDirection)
                direction = nextDirection
                current += direction
            } ?: error("Invalid movement detected: $current, $direction")
        }
        return pipe
    }

    fun Array<CharArray>.floodFill(at: Point2D, c: Char) {
        if (!isSafe(at)) return
        val queue = ArrayDeque<Point2D>().apply { add(at) }
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (this[next] == '.') {
                this[next] = c
                queue.addAll(next.cardinalNeighbors().filter { isSafe(it) })
            }
        }
    }

    fun part1(): Int =
        traversePipe().size / 2

    fun part2(): Int {
        val pipe = traversePipe()

        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                val at = Point2D(x, y)
                if (at !in pipe) grid[at] = '.'
            }
        }
        val emptyCorner =
            listOf(
                Point2D(0, 0),
                Point2D(0, grid[0].lastIndex),
                Point2D(grid.lastIndex, 0),
                Point2D(grid.lastIndex, grid[0].lastIndex)
            )
                .first { grid[it] == '.' }

        traversePipe { current, direction, nextDirection ->
            grid.floodFill(current + markingDirection.getValue(direction), 'O')
            if (grid[current] in setOf('7', 'L', 'J', 'F')) {
                grid.floodFill(current + markingDirection.getValue(nextDirection), 'O')
            }
        }

        val find = if (grid[emptyCorner] == 'O') '.' else 'O'
        return grid.sumOf { row -> row.count { it == find } }
    }

    println(part2())

}