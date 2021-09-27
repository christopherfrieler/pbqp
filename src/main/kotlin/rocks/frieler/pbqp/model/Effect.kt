package rocks.frieler.pbqp.model

import java.math.BigDecimal

/**
 * The effect of deciding for certain options.
 *
 * The effect can be a numerical value, that is interpreted as cost or benefit of that decision, or [FORBIDDEN] to
 * prevent it.
 */
class Effect private constructor(
        private val internalValue: BigDecimal?
) {
    companion object {
        /**
         * [Effect] to indicate "no effect", i.e. no cost or benefit.
         */
        val NONE = of(BigDecimal.ZERO)

        /**
         * [Effect] that indicates, that a certain decision is not allowed.
         */
        val FORBIDDEN = Effect(null)

        /**
         * Creates a new [Effect] of the given value.
         *
         * @param value the [Effect]'s value
         */
        fun of(value: BigDecimal) = Effect(value)
    }

    /**
     * The value of the [Effect].
     */
    val value: BigDecimal
    get() {
        if (this == FORBIDDEN) {
            throw IllegalStateException("Effect FORBIDDEN has no numerical value.")
        }

        return this.internalValue!!
    }

    /**
     * Adds this [Effect] and the [other], i.e. adds their values. This results into a new [Effect] instance.
     *
     * Adding [FORBIDDEN] and any other [Effect] always results in [FORBIDDEN].
     *
     * @param other the [Effect] to add
     */
    operator fun plus(other: Effect) : Effect {
        return if (this === FORBIDDEN || other === FORBIDDEN) {
            FORBIDDEN
        } else {
            of(this.internalValue!!.add(other.internalValue))
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Effect && internalValue == other.internalValue
    }

    override fun hashCode() = internalValue?.hashCode() ?: 0
}
