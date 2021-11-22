package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

class Rational(private var n: BigInteger, private var d: BigInteger) : Comparable<Rational> {

    init {
        if (d == BigInteger.ZERO) {
            throw IllegalArgumentException("Denominator could not be zero")
        }
        normalize()
    }

    operator fun plus(other: Rational): Rational {
        val dt = this.d * other.d
        val nt = (this.n * (dt / this.d)) + (other.n * (dt / other.d))
        return Rational(nt, dt)
    }

    operator fun minus(other: Rational): Rational {
        val dt = this.d * other.d
        val nt = (this.n * (dt / this.d)) - (other.n * (dt / other.d))
        return Rational(nt, dt)
    }

    operator fun times(other: Rational): Rational {
        val dt = this.d * other.d
        val nt = this.n * other.n
        return Rational(nt, dt)
    }

    operator fun div(other: Rational): Rational {
        val nt = this.n * other.d
        val dt = this.d * other.n
        return Rational(nt, dt)
    }

    operator fun unaryMinus(): Rational {
        return Rational(-this.n, this.d)
    }

    override operator fun compareTo(other: Rational): Int {
        val left = this.n * other.d
        val right = this.d * other.n
        return left.compareTo(right)
    }

    operator fun rangeTo(max: Rational) = RationalRange(this, max)

    override fun toString(): String {
        return if (n % d == BigInteger.ZERO) {
            "$n"
        } else
            "$n/$d"
    }

    private fun normalize() {
        val g: BigInteger = n.gcd(d);
        if (g.compareTo(BigInteger.ONE) > 0) {
            n = n.divide(g);
            d = d.divide(g);
        }
        if (d.compareTo(BigInteger.ZERO) == -1) {
            n = n.negate();
            d = d.negate();
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        if (n != other.n) return false
        if (d != other.d) return false

        return true
    }

    override fun hashCode(): Int {
        var result = n.hashCode()
        result = 31 * result + d.hashCode()
        return result
    }

}


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}

class RationalRange(
    override val start: Rational,
    override val endInclusive: Rational
) : ClosedRange<Rational>, Iterable<Rational> {

    override fun iterator(): Iterator<Rational> {
        return RationalIterator(start, endInclusive)
    }
}

class RationalIterator(private val start: Rational, private val endInclusive: Rational) : Iterator<Rational> {

    var initValue = start

    override fun hasNext(): Boolean {
        return initValue <= endInclusive
    }

    override fun next(): Rational {
        return endInclusive
    }

}

infix fun Int.divBy(d: Int): Rational = Rational(this.toBigInteger(), d.toBigInteger())

infix fun BigInteger.divBy(d: BigInteger): Rational = Rational(this, d)

infix fun Long.divBy(d: Long): Rational = Rational(this.toBigInteger(), d.toBigInteger())

fun String.toRational(): Rational {
    val members = this.split("/")
    return when (members.size) {
        1 -> Rational(members[0].toBigInteger(), BigInteger.ONE)
        2 -> Rational(members[0].toBigInteger(), members[1].toBigInteger())
        else -> throw IllegalArgumentException("This string does not represent rational number")
    }
}
