package day07

import utils.Resources.resourceAsListOfString

fun main(){

    val input = resourceAsListOfString("src/day07/Day07.txt").map { it.split(" ") }
    val hands = input.map { line -> Hand.parse(line)}
    val handsWithJokers = input.map {line -> Hand.parseWithJokers(line)}

    fun solve(hands: List<Hand>): Int{
        return  hands.groupBy { hand -> hand.type }
            .toSortedMap()
            .flatMap { hand -> hand.value.sorted() }
            .zip(1..hands.size)
            .sumOf { it -> it.first.bid * it.second }
    }

    println(solve(hands))
    println(solve(handsWithJokers))
}
data class Hand(
    val type: Int,
    val cards: String,
    val bid: Int
): Comparable<Hand>{

    companion object{

        fun parse(line: List<String>) = Hand(
            cards = line.first(),
            bid = line.last().toInt(),
            type = handType(parseHand(line.first()))
        )

        fun parseWithJokers(line: List<String>) = Hand(
            cards = line.first(),
            bid = line.last().toInt(),
            type = handTypeWithJokers(parseHand(line.first()))
        )
        private fun parseHand(cards: String): Map<String, Int>{
            val hand = mutableMapOf<String, Int>()
            cards.forEach { card ->
                hand[card.toString()] = cards.count { it == card }
            }
            return hand
        }
        private fun handType(hand: Map<String, Int>): Int{
            return when{
                hand.containsValue(5) -> 6
                hand.containsValue(4) -> 5
                hand.containsValue(3) and hand.containsValue(2) -> 4
                hand.containsValue(3) -> 3
                hand.filterValues { it == 2 }.count() == 2 -> 2
                hand.containsValue(2) -> 1
                else -> 0

            }
        }

        private fun handTypeWithJokers(hand: Map<String, Int>): Int{
            val jokers = hand.getOrDefault("J",0)
            return when{
                hand.containsValue(5) -> 6
                jokers == 4 -> 6
                hand.containsValue(4) and (jokers == 1) -> 6
                hand.containsValue(4) -> 5
                hand.containsValue(3) and (jokers == 2) -> 6
                hand.containsValue(3) and (jokers == 1) -> 5
                hand.containsValue(2) and (jokers == 3) -> 6
                hand.containsValue(3) and hand.containsValue(2) -> 4
                (hand.filterValues { it == 3 }.count() == 1) and (jokers == 3) -> 5
                hand.containsValue(3) -> 3
                (hand.filterValues { it == 2 }.count() == 2) and (jokers == 2) -> 5
                (hand.filterValues { it == 2 }.count() == 2) and (jokers == 1) -> 4
                (hand.filterValues { it == 2 }.count() == 1) and (jokers == 2) -> 3
                (hand.filterValues { it == 2 }.count() == 2) and (jokers == 0) -> 2
                hand.containsValue(2) and (jokers == 1) -> 3
                hand.containsValue(2) and (jokers == 0) -> 1
                else -> 0 + jokers

            }
        }

    }
    override fun compareTo(other: Hand): Int {
        //val cards = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
        val cards = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
        val strength = cards.associateWith { cards.indexOf(it) }
        this.cards.forEachIndexed { index, card ->
            when{
                strength[card]!! > strength[other.cards[index]]!! -> return 1
                strength[card]!! < strength[other.cards[index]]!! -> return -1
                strength[card]!! == strength[other.cards[index]]!! -> {}
            }
        }
        return 0
    }
}