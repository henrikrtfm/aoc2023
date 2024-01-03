package day18

import utils.Resources.resourceAsListOfString
import utils.Point2D
import utils.Point2D.Companion.NORTH
import utils.Point2D.Companion.SOUTH
import utils.Point2D.Companion.EAST
import utils.Point2D.Companion.ORIGIN
import utils.Point2D.Companion.WEST

@OptIn(ExperimentalStdlibApi::class)
fun main(){

    fun parseRow(line: String): Pair<Point2D,Int>{
        val command = line.split(" ")
        val direction = command[0]
        val distance = command[1].toInt()
        return when(direction){
            "U" -> Pair(NORTH, distance)
            "D" -> Pair(SOUTH, distance)
            "L" -> Pair(WEST, distance)
            "R" -> Pair(EAST, distance)
            else -> throw IllegalStateException("Bad direction $line")
        }
    }

    fun parseRowHex(line: String): Pair<Point2D,Int>{
        val command = line.substringAfter("#").substringBefore(")")
        val direction = when(command.last()){
            '0' -> "R"
            '1' -> "D"
            '2' -> "L"
            '3' -> "U"
            else -> throw IllegalStateException("Bad direction $line")
        }
        val distance = command.dropLast(1).hexToInt()
        return when(direction){
            "U" -> Pair(NORTH, distance)
            "D" -> Pair(SOUTH, distance)
            "L" -> Pair(WEST, distance)
            "R" -> Pair(EAST, distance)
            else -> throw IllegalStateException("Bad direction $line")
        }
    }

    fun calculateArea(digplan: List<Pair<Point2D, Int>>): Long{
        val area = digplan
            .runningFold(ORIGIN) {acc, (direction, length) ->
                acc + (direction*length)
            }
            .zipWithNext()
            .sumOf { (a,b) ->
                (a.x.toLong() * b.y.toLong()) - (b.x.toLong()*a.y.toLong())
            }/2
        val trench = digplan.sumOf { it.second }
        return area + (trench /2)+1
    }

    fun part1(input: List<String>):Long{
        return calculateArea(input.map { parseRow(it) })
    }

    fun part2(input: List<String>):Long{
        return calculateArea(input.map { parseRowHex(it) })
    }

    val input = resourceAsListOfString("src/day18/Day18.txt")
    println(part1(input))
    println(part2(input))
}