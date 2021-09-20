package rocks.frieler.pbqp.model

import java.math.BigDecimal

class Effect private constructor(
        private val internalValue: BigDecimal?
) {
    companion object {
        val NONE = of(BigDecimal.ZERO)
        val FORBIDDEN = Effect(null)

        fun of(value: BigDecimal) = Effect(value)
    }

    val value: BigDecimal
    get() {
        if (this == FORBIDDEN) {
            throw IllegalStateException("Effect FORBIDDEN has no numerical value.")
        }

        return this.internalValue!!
    }

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
