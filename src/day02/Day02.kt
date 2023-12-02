package day02

import utils.Resources.resourceAsListOfString
fun main() {

    fun parseInput(line: String): Game{
        val sets = line.substringAfter(": ").split("; ").map { CubeSet.parse(it) }.toSet()
        return Game(sets)
    }

    fun part1(red: Int, blue: Int, green: Int, games: List<Game>): Int{
        return games.mapIndexed { index, game -> if (Game.isPossible(game, red, blue, green)) index+1 else 0 }.sumOf{ it }
    }

    fun part2(games: List<Game>): Int{
        return games.map { game -> Game.powerCubes(game) }.sumOf { it }
    }

    val input = resourceAsListOfString("src/day02/Day02.txt")
    val games = input.map {line -> parseInput(line.substringAfter(": ")) }
    println(part1(12,14,13, games))
    println(part2(games))

}

data class Game(
    val cubesets: Set<CubeSet>
){
    companion object{
        fun isPossible(game: Game, red: Int, blue: Int, green: Int): Boolean{
            return !(game.cubesets.any{ cubeset -> (cubeset.blue > blue) or (cubeset.red > red) or (cubeset.green > green) })
        }

        fun powerCubes(game: Game): Int{
            return game.cubesets.maxOf{ cubeset -> cubeset.blue} * game.cubesets.maxOf { cubeset -> cubeset.red } * game.cubesets.maxOf { cubeset -> cubeset.green }
        }
    }
}
data class CubeSet(
    val red: Int,
    val blue: Int,
    val green: Int
){
    companion object{
        fun parse(line: String) : CubeSet{
            var red = 0
            var blue = 0
            var green = 0
            line.split(", ").forEach {
                when{
                    it.contains("blue") -> blue = it.substringBefore( " blue").toInt()
                    it.contains("red") -> red = it.substringBefore( " red").toInt()
                    it.contains("green") -> green = it.substringBefore( " green").toInt()
                }
            }
            return CubeSet(red, blue, green)
        }
    }
}



