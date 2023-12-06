package day06

import utils.Resources.resourceAsListOfString

fun main(){

    val input = resourceAsListOfString("src/day06/Day06.txt")
    val races = input.first().substringAfter(":").split(" ").filterNot { it.isEmpty() }.map { it.toLong() }
        .zip(input.last().substringAfter(":").split(" ").filterNot { it.isEmpty() }.map { it.toLong() })

    val race = Pair(input.first().substringAfter(":").replace(" ", "").toLong(),
        input.last().substringAfter(":").replace(" ", "").toLong())

    fun getRaceOptions(time: Long): List<Pair<Long,Long>>{
        return ((0..time).toList()).zip((0..time).toList().reversed())

    }

    fun traveledDistance(raceOption: Pair<Long, Long>): Long{
        return raceOption.first*raceOption.second
    }

    fun findWinningOptions(race: Pair<Long,Long>): List<Long>{
        return getRaceOptions(race.first).map{it -> traveledDistance(it)}.filter{it -> it > race.second}
    }

    fun part1(races: List<Pair<Long, Long>>): Long{
        return races.map{ race -> findWinningOptions(race)}
            .map{options -> options.size.toLong()}
            .reduce {acc, i -> acc * i}
    }

    fun part2(race: Pair<Long, Long>): Long {
         return findWinningOptions(race).size.toLong()
    }

    println(part1(races))
    println(part2(race))

}