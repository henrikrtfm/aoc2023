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
}