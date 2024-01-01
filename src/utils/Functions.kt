package utils

internal object Functions {

    val lcmLong = {x: Long, y: Long ->
        var gcd = 1L
        var index = 1L
        while (index <= x && index <= y) {
            if (x % index == 0L && y % index == 0L)
                gcd = index
            index++
        }
        x * y / gcd
    }

    val lcm = {x: Int, y: Int ->
        var gcd = 1
        var index = 1
        while (index <= x && index <= y) {
            if (x % index == 0 && y % index == 0)
                gcd = index
            index++
        }
        x * y / gcd
    }

    fun Array<CharArray>.rotateLeft(): Array<CharArray> {
        val rows = this.size
        val cols = this[0].size

        return (0 ..<cols).map { x ->
            (0 ..<rows).map { y ->
                this[y][cols - 1 - x]
            }.toCharArray()
        }.toTypedArray()
    }

    fun Array<CharArray>.rotateRight(): Array<CharArray> {
        val rows = this.size
        val cols = this[0].size

        return (0..<cols).map { x ->
            (0..<rows).map { y ->
                this[rows - 1 - y][x]
            }.toCharArray()
        }.toTypedArray()
    }

    fun List<Int>.generateDifference(): List<Int> {
        return this.windowed(2, 1).map { (it[1] - it[0])}
    }
    tailrec fun findNextInSequence(list: List<Int>, acc: Int = 0): Int {
        val nextSeq = list.generateDifference()
        val result = acc + list.last()
        return if (nextSeq.all { it == 0 }) {
            result
        } else {
            findNextInSequence(nextSeq, result)
        }
    }

    fun <E> List<E>.cartesianPairs(): List<Pair<E, E>> =
        this.flatMapIndexed { index, left ->
            this.indices.drop(index).map { right -> left to this[right] }
        }

    tailrec fun Long.gcd(other: Long): Long =
        if (other == 0L) this
        else other.gcd(this % other)

    fun Long.lcm(other: Long): Long =
        (this * other) / this.gcd(other)


    fun Array<CharArray>.isSafe(at: Point2D) =
        at.y in this.indices && at.x in this[at.y].indices

    operator fun Array<CharArray>.set(at: Point2D, c: Char) {
        this[at.y][at.x] = c
    }

    operator fun Array<CharArray>.get(at: Point2D): Char =
        this[at.y][at.x]

    fun Array<CharArray>.swap(a: Point2D, b: Point2D) {
        val tmp = this[a]
        this[a] = this[b]
        this[b] = tmp
    }

    fun List<List<*>>.isSafe(at: Point2D) =
        at.y in this.indices && at.x in this[at.y].indices

    operator fun <T> List<List<T>>.get(at: Point2D): T =
        this[at.y][at.x]

    infix fun IntRange.intersects(other: IntRange): Boolean =
        first <= other.last && last >= other.first

    infix fun IntRange.intersectRange(other: IntRange): IntRange =
        maxOf(first, other.first)..minOf(last, other.last)

    fun IntRange.length(): Int =
        last - first + 1
}