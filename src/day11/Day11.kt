package day11

import utils.Functions.cartesianPairs
import utils.Resources.resourceAsListOfString
import utils.Functions.rotateLeft
import kotlin.math.absoluteValue

fun main(){
    val input = resourceAsListOfString("src/day11/Day11.txt")
    val initialImage = parseInput(input)
    val galaxies = parseGalaxies(input)

    fun findEmptyRows(): List<Int>{
        return initialImage.grid.mapIndexedNotNull {row, line ->
            when{
                line.all { it == '.' } -> row
                else -> null
            }
        }
    }

    fun findEmptyCols(): List<Int>{
        return initialImage.grid.rotateLeft().mapIndexedNotNull {col, line ->
            when{
                line.all { it == '.' } -> line.size-1-col
                else -> null
            }
        }.reversed()
    }

    fun spreadGalaxies(spreadFactor: Int = 2): List<Point>{
        val rows = findEmptyRows()
        val cols = findEmptyCols()
        val test = galaxies.map { point ->
            Point(point.first+(rows.count { x -> x <= point.first }*(spreadFactor-1)), point.second+(cols.count { y -> y <= point.second }*(spreadFactor-1)))
            }
        return test
    }

    fun part1(): Int{
        return spreadGalaxies().cartesianPairs().sumOf { it.first.distanceTo(it.second) }
    }
    fun part2(): Long{
        return spreadGalaxies(1_000_000).cartesianPairs().sumOf { it.first.distanceTo(it.second).toLong() }
    }

    //println(part1())
    println(part2())

}

private fun Point.distanceTo(that: Point): Int =
    (this.first - that.first).absoluteValue + (this.second - that.second).absoluteValue

private operator fun Point.plus(that: Point): Point =
    Point(this.first + that.first, this.second + that.second)

