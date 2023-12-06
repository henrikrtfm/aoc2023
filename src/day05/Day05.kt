package day05

import utils.Resources.resourceAsText
import kotlin.math.min

typealias Range = List<Long>
typealias Ranges = List<Range>
fun main(){

    fun parseInput(line: String): Ranges {
        val ranges = line.substringAfter(":\n").split("\n")
        return ranges.map { it.split(" ").map { it.toLong() } }
    }

    val input = resourceAsText("src/day05/Day05.txt").split("\n\n")
    val seedsPart1 = input.first().substringAfter(": ").split(" ").map{it.toLong()}
    val seedsPart2 = seedsPart1.windowed(2, 2)
    val ranges = input.subList(1,8).map { parseInput(it)  }


    fun lookupDestination(ranges: Ranges, seed: Long): Long{
        val range = ranges.firstOrNull{ it -> it.isPresent(seed)}
        return when{
            range != null -> range.lookup(seed)
            else-> seed
        }
    }

    fun findLocation(seed: Long): Long{
        return ranges.fold(seed) { acc, ranges -> lookupDestination(ranges, acc) }
    }

    fun part1(seeds: List<Long>): Long{
        return seeds.minOf { seed -> findLocation(seed) }
    }

    fun part2(seeds: List<Range>): Long{
        val locations = mutableSetOf<Long>()
        seeds.forEach { range ->
            var minLocation: Long = 0
            for(i in range.first()..range.last()+range.first()){
                val newLocation = findLocation(i)
                if((minLocation == 0.toLong()) or (newLocation < minLocation) ){
                    minLocation = newLocation
                }
            }
            locations.add(minLocation)
        }
        return locations.min()
    }

    println(part1(seedsPart1))
    println(part2(seedsPart2))
}

private fun Range.isPresent(seed: Long): Boolean{
    return seed in this[1]..<this[1]+this[2]
}

private fun Range.lookup(seed: Long): Long{
    return this[0]+seed-this[1]
}