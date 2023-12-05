package day04

import utils.Resources.resourceAsListOfString

fun main(){

    tailrec fun calculateCard(points: Int, winnings: Int,  winningNumbers: Int): Int {
        return if (winningNumbers <= 1){
            points
        } else{
            calculateCard(points*winnings, winnings, winningNumbers-1)
        }
    }

    fun cardScore(card: Card): Int{
        return card.numbers.intersect(card.winningNumbers).size
    }

    fun part1(cards: List<Card>): Int{
        return cards.filter { card -> cardScore(card) > 0 }.sumOf { card -> calculateCard(1, 2, cardScore(card)) }
    }

    fun part2(cards: List<Pair<Int, Card>>): Int{
        val winnings = IntArray(cards.size+1){1}
        cards.forEach { card ->
            val score = cardScore(card.second)
            repeat(score){
                winnings[card.first+it+1] += winnings[card.first]
            }

        }
        return winnings.sum()-1
    }

    val input = resourceAsListOfString("src/day04/Day04.txt")
    val cards = input.map{ Card.parse(it) }
    val cards2 = input.mapIndexed { index, s -> Pair(index+1, Card.parse(s))  }
    println(part1(cards))
    println(part2(cards2))

}
data class Card(
    val numbers: Set<Int>,
    val winningNumbers: Set<Int>
){
    companion object{
        fun parse(line: String) = Card(
            numbers = line.substringAfter(" | ")
                .replace("  ", " ")
                .split(" ")
                .mapNotNull { it.toIntOrNull() }
                .toSet(),
            winningNumbers = line.substringAfter(":")
                .substringBefore(" | ")
                .replace("  ", " ")
                .split(" ")
                .mapNotNull { it.toIntOrNull() }
                .toSet()
        )
    }
}