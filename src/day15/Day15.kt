package day15

import utils.Resources.resourceAsText
fun main(){

    fun calculateHash(step: String): Int {
        return step.fold(0){acc, char -> ((char.code+acc)*17).rem(256) }
    }


    fun part1(initSequence: List<String>): Int{
        return initSequence.sumOf { calculateHash(it) }
    }

    fun part2(initSequence: List<String>): Int{
        val boxes = (0 until 256).map{ mutableMapOf<String, Int>() }
        initSequence.forEach { step ->
            when(step.last()){
                '-' -> {
                    val label = step.substringBefore("-")
                    val box = calculateHash(label)
                    boxes[box].remove(label)
                }
                else -> {
                    val label = step.substringBefore("=")
                    val box = calculateHash(label)
                    val focalLength = step.substringAfter("=").toInt()
                    boxes[box][label] = focalLength
                }
            }
        }
        val result = boxes.withIndex().sumOf { (box, lenses) ->
            lenses.values.withIndex().sumOf { (lens, focalLength )  ->
                (box+1)*(lens+1)*focalLength
            }
        }
        return result
    }

    val initSequence = resourceAsText("src/day15/Day15.txt").split(",")

    println(part1(initSequence))
    println(part2(initSequence))


}