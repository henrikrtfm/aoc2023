package day10


typealias Point = Pair<Int, Int>

class Network(val grid: Array<CharArray>, val start: Point) {

    fun lookupValue(point: Point): Char{
        return grid[point.first][point.second]
    }

    operator fun contains(point: Point): Boolean =
        point.first in this.grid.indices && point.second in this.grid.first().indices

}

fun parseInput(lines: List<String>): Network {
    val grid = lines.map {line -> line.chunked(1) }
        .map { it -> it.map { it -> it[0] }.toCharArray()}
        .toTypedArray()
    val start = grid.flatMapIndexed{indexrow, row -> row.mapIndexed{indexcol, _ -> when(grid[indexrow][indexcol]){
        'S' -> Point(indexrow,indexcol)
        else -> null
    } }}.filterNotNull().first()

    return Network(grid,start)
}