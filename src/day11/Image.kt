package day11

typealias Point = Pair<Int, Int>

class Image(val grid: Array<CharArray>) {

    fun lookupValue(point: Point): Char{
        return grid[point.first][point.second]
    }

    operator fun contains(point: Point): Boolean =
        point.first in this.grid.indices && point.second in this.grid.first().indices


}

fun parseInput(lines: List<String>): Image {
    val grid = lines.map {line -> line.chunked(1) }
        .map { it -> it.map { it -> it[0] }.toCharArray()}
        .toTypedArray()

    return Image(grid)
}
fun parseGalaxies(lines: List<String>): List<Point> {
    val galaxies = lines.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { col, char ->
            if (char == '#') Point(row, col)
            else null
        }
    }
    return galaxies
}