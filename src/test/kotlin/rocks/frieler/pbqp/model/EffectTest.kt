package rocks.frieler.pbqp.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import java.math.BigDecimal

internal class EffectTest {
    @Test
    internal fun `Effect has the specified value`() {
        val effect = Effect.of(BigDecimal.ONE)

        assertThat(effect.value).isEqualTo(BigDecimal.ONE)
    }

    @Test
    internal fun `Effect NONE has a value of zero`() {
        assertThat(Effect.NONE.value).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    internal fun `Effect FORBIDDEN has no numerical value`() {
        assertThrows<IllegalStateException> {
            (Effect.FORBIDDEN.value)
        }
    }

    @Test
    internal fun `Effects can be added`() {
        val effect1 = Effect.of(BigDecimal.ONE)
        val effect2 = Effect.of(BigDecimal.valueOf(2))

        val totalEffect = effect1 + effect2

        assertThat(totalEffect.value).isEqualTo(effect1.value + effect2.value)
    }

    @Test
    internal fun `adding FORBIDDEN with any Effect stays FORBIDDEN`() {
        assertThat(Effect.FORBIDDEN + Effect.of(BigDecimal.ONE)).isEqualTo(Effect.FORBIDDEN)
        assertThat(Effect.of(BigDecimal.valueOf(-100)) + Effect.FORBIDDEN).isEqualTo(Effect.FORBIDDEN)
    }

    @Test
    internal fun `Effect provides a hasCode implementation`() {
        assertThat(Effect.of(BigDecimal.ONE).hashCode()).isNotNull()
    }
}
