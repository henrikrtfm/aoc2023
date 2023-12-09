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
}