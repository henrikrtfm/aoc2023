package day22

import day22.Brick.Companion.GROUND
import utils.Resources.resourceAsListOfString
import utils.intersects

fun main(){

    val input = resourceAsListOfString("src/day22/Day22.txt")
    val bricks = input.mapIndexed {index, line -> Brick.parse(index, line) }.sorted().settle()

    fun Brick.topple(): Set<Brick> = buildSet {
        add(this@topple)
        val untoppled = (bricks - this).toMutableSet()
        do {
            val willFall = untoppled
                .filter { it.supportedBy.isNotEmpty() }
                .filter { it.supportedBy.all { brick -> brick in this } }
                .also {
                    untoppled.removeAll(it)
                    addAll(it)
                }
        } while (willFall.isNotEmpty())
    }

    fun part1(): Int{
        return bricks.size - bricks.structurallySignificant().size
    }

    fun part2(): Int{
        return bricks.structurallySignificant().sumOf {  it.topple().size -1 }
    }

    println(part1())
    println(part2())


}



fun List<Brick>.settle(): List<Brick> = buildList{
    this@settle.forEach { brick ->
        var current = brick
        do {
            var settled = false
            val supporters = filter { below -> below.canSupport(current) }
            if(supporters.isEmpty() && !current.onGround()){
                val restingPlace = filter {it.z.last < current.z.first - 1 }
                    .maxOfOrNull{ it.z.last }?.let { it+1 }?: GROUND
                current = current.fall(restingPlace)
            }
            else{
                settled = true
                supporters.forEach { below -> below.supports(current) }
                add(current)
            }
        }
        while(!settled)
    }

}

fun List<Brick>.structurallySignificant(): List<Brick> =
    filter { brick -> brick.supporting.any { it.supportedBy.size == 1 } }

data class Brick(val id: Int, val x: IntRange, val y: IntRange, val z: IntRange): Comparable<Brick>{
    val supporting = mutableSetOf<Brick>()
    val supportedBy = mutableSetOf<Brick>()

    override fun compareTo(other: Brick): Int =
        z.first - other.z.first

    fun supports(other: Brick) {
        supporting += other
        other.supportedBy += this
    }

    fun canSupport(other: Brick): Boolean =
        x intersects other.x && y intersects other.y && z.last + 1 == other.z.first

    fun onGround(): Boolean =
        z.first == GROUND

    fun fall(restingPlace: Int): Brick =
        copy(
            z = restingPlace..(restingPlace + (z.last - z.first))
        )

    companion object{

        const val GROUND = 1

        fun parse(index: Int, line: String): Brick =
            line.split("~")
                .map { side -> side.split(",").map { it.toInt() } }
                .let { lists ->
                    val left = lists.first()
                    val right = lists.last()
                    Brick(
                        index,
                        left[0]..right[0],
                        left[1]..right[1],
                        left[2]..right[2]
                    )
                }
    }
}