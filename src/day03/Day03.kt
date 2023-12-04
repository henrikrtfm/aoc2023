package day03

import utils.Resources.resourceAsListOfString

typealias Point = Pair<Int, Int>

fun main(){
    val neighbors = sequenceOf(-1 to 0, 0 to -1, 0 to 1, 1 to 0, -1 to 1, -1 to -1, 1 to 1, 1 to -1)
    val parts = mutableSetOf<Part>()
    val symbols = mutableSetOf<Symbol>()
    val input = resourceAsListOfString("src/day03/Day03_sample.txt")

    fun createPoints(first: Int, start: Int, end: Int): Set<Point>{
        val newSet = mutableSetOf<Point>()
        for (i in start..<end){
            newSet.add(Point(first, i))
        }
        return newSet
    }



    fun parseInput(input: List<String>) {
        val regex: Regex = """(?<=[&*@$#%/\-+=])|(?=[&*@$#%/\-+=])""".toRegex()
        input.forEachIndexed { indexRow, row ->
            val partsOrSymbols = row.split(".").filterNot { it.isEmpty() }
                .map { it.split(regex) }.flatten().filterNot { it.isEmpty() }
            partsOrSymbols.forEachIndexed() { index, it ->
                when {
                    it.toIntOrNull() == null -> {
                        val indices = Regex("""\$it""").findAll(row).map { it.range.first }.toList()
                        indices.forEach { indice -> symbols.add(Symbol(it, Point(indexRow, indice)))}
                    }
                    else -> parts.add(Part(Pair(indexRow, index), it.toInt(), createPoints(indexRow, row.indexOf(it), row.indexOf(it)+it.length)))
                }
            }
        }

    }

    fun part1(): Int{
        val partNumbers = mutableSetOf<Pair<Pair<Int, Int>, Int>>()
        symbols.forEach { symbol ->
            val adjacentPoints = neighbors.map { it + symbol.point }.toSet()
            partNumbers.addAll(parts.map { part -> Part.returnNumberIfIntersecting(part, adjacentPoints) })

        }
        //println(partNumbers.filterNot { it.second == 0 })
        return partNumbers.sumOf { it.second }
    }

    parseInput(input)
    println(parts.filter { it -> it.number == 874 })
    println(symbols.filter { it.point.first ==1 })
    println(part1())
}

private operator fun Point.plus(that: Point): Point =
    Point(this.first +that.first, this.second+that.second)

data class Symbol(
    val value: String,
    val point: Point
)
data class Part(
    val id: Pair<Int,Int>,
    val number: Int,
    val points: Set<Point>
){
    companion object{
        fun returnNumberIfIntersecting(part: Part, points: Set<Point>): Pair<Pair<Int,Int>, Int> {
            return when{
                (part.points intersect points).isNotEmpty()-> Pair(part.id,part.number)
                else -> Pair(Pair(0,0), 0)
            }
        }
    }
}