package day12

import utils.Resources.resourceAsListOfString

fun main(){

    fun parseRow(input: String): Pair<String, List<Int>> {
        return input.split(" ").run {
            first() to last().split(",").map { it.toInt() }
        }
    }

    fun countArrangements( springs: String, damage: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long> = HashMap()): Long {
        val key = springs to damage
        cache[key]?.let {
            return it
        }
        if (springs.isEmpty()) return if (damage.isEmpty()) 1 else 0

        return when (springs.first()) {
            '.' -> countArrangements(springs.dropWhile { it == '.' }, damage, cache)
            '?' -> countArrangements(springs.substring(1), damage, cache) +
                        countArrangements("#${springs.substring(1)}", damage, cache)
            '#' -> when {
                damage.isEmpty() -> 0
                else -> {
                    val thisDamage = damage.first()
                    val remainingDamage = damage.drop(1)
                    if (thisDamage <= springs.length && springs.take(thisDamage).none { it == '.' }) {
                        when {
                            thisDamage == springs.length -> if (remainingDamage.isEmpty()) 1 else 0
                            springs[thisDamage] == '#' -> 0
                            else -> countArrangements(springs.drop(thisDamage + 1), remainingDamage, cache)
                        }
                    } else 0
                }
            }

            else -> throw IllegalStateException("Invalid springs: $springs")
        }.apply {
            cache[key] = this
        }
    }
    fun part1(input: List<String>): Long{
        return input.map { parseRow(it) }
            .sumOf { (springs, damage) -> countArrangements(springs, damage) }
    }
    fun part2(input: List<String>): Long {
        return input.map { parseRow(it) }
                .map { unfold(it) }
                .sumOf { (springs, damage) -> countArrangements(springs, damage) }
    }
    val input = resourceAsListOfString("src/day12/Day12.txt")
    println(part1(input))
    println(part2(input))

}

private fun unfold(input: Pair<String, List<Int>>): Pair<String, List<Int>> =
    (0..4).joinToString("?") { input.first } to (0..4).flatMap { input.second }