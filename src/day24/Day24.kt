package day24

import utils.Resources.resourceAsListOfString

typealias Point = Pair<Double,Double>
typealias Line = Pair<Point, Point>

fun main(){
    val input = resourceAsListOfString("src/day24/Day24.txt")

    val areaX = 200000000000000
    val areaY = 400000000000000

    val hailstones = input.map { Hailstone.parse(it) }
    val lines = hailstones.map { it.toXYLine()  }

    fun calculateIntersect(pair: Pair<Line, Line>): Point?{
        val line1 = pair.first
        val line2 = pair.second
        val a = (line1.second.second-line1.first.second)/(line1.second.first-line1.first.first)
        val b = (line2.second.second-line2.first.second)/(line2.second.first-line2.first.first)
        if(a==b){
            return null
        }
        val c = line1.first.second-(a*line1.first.first)
        val d = line2.first.second-(b*line2.first.first)
        val pX = (d-c)/(a-b)
        val pY = (a*((d-c)/(a-b)))+c
        return Point(pX,pY)
    }

    fun Point.inArea(): Boolean{
        return (this.first.toLong() in (areaX..areaY)) && (this.second.toLong() in (areaX..areaY))
    }

    fun Line.inFuture(point: Point):Boolean{
        val velX = this.second.first-this.first.first
        val velY = this.second.second-this.first.second

        if(velX > 0 && point.first.compareTo(this.first.first) == 1 && velY > 0 && point.second.compareTo(this.first.second) == 1) return true
        if(velX < 0 && point.first.compareTo(this.first.first) == -1 && velY > 0 && point.second.compareTo(this.first.second) == 1) return true
        if(velX > 0 && point.first.compareTo(this.first.first) == 1 && velY < 0 && point.second.compareTo(this.first.second) == -1) return true
        if(velX < 0 && point.first.compareTo(this.first.first) == -1 && velY < 0 && point.second.compareTo(this.first.second) == -1) return true
        return false
    }


    fun part1(): Int{
        val linePairs = mutableSetOf<Pair<Line, Line>>()
        val intersects = mutableListOf<Point>()
        lines.forEach { line ->
            val pairs = lines.filterNot { it == line }.map { line to it }.filter { pair -> pair !in linePairs }
            pairs.forEach { pair ->
                val intersect = calculateIntersect(pair)
                if(intersect != null) {
                    if (pair.first.inFuture(intersect) && pair.second.inFuture(intersect)) {
                        intersects.add(intersect)
                        linePairs.add(pair)
                        linePairs.add(pair.flip())
                    }
                }
            }
        }
        return intersects.filter { it.inArea() }.size
    }

    println(part1())
}

data class Hailstone(
    val x: Long,
    val y: Long,
    val z: Long,
    val velocityX: Long,
    val velocityY: Long,
    val velocityZ: Long
){
    fun toXYLine(): Line =
        Pair(Point(x.toDouble(), y.toDouble()), (Point((x+velocityX).toDouble(), (y+velocityY).toDouble())))

    companion object{
        fun parse(line: String): Hailstone{
            val coords = line.substringBefore(" @").split(",").map { it.trim() }.map { it.toLong() }
            val velocity = line.substringAfter("@ ").split(",").map { it.trim() }.map { it.toLong() }
            return Hailstone(coords[0],coords[1],coords[2],velocity[0], velocity[1], velocity[2])
        }
    }
}

fun <A, B> Pair<A, B>.flip(): Pair<B, A> {
    return second to first
}