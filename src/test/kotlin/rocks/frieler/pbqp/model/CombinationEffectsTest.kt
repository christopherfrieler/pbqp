package rocks.frieler.pbqp.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import java.math.BigDecimal


internal class CombinationEffectsTest {
    private val optionOfFirstDecision = mock<Any>()
    private val firstDecision = mock<Decision<Any>> {
        on { hasOption(optionOfFirstDecision) }.thenReturn(true)
    }
    private val optionOfSecondDecision = mock<Any>()
    private val secondDecision = mock<Decision<Any>> {
        on { hasOption(optionOfSecondDecision) }.thenReturn(true)
    }
    private val combinationEffects = CombinationEffects(firstDecision, secondDecision)

    @Test
    internal fun `CombinationEffects can hold the Effect of choosing two options together`() {
        val effect = Effect.of(BigDecimal.ONE)

        combinationEffects.addEffect(optionOfFirstDecision, optionOfSecondDecision, effect)

        assertThat(combinationEffects.getEffect(optionOfFirstDecision, optionOfSecondDecision)).isEqualTo(effect)
        assertThat(combinationEffects.getEffect(optionOfSecondDecision, optionOfFirstDecision)).isEqualTo(effect)
    }

    @Test
    internal fun `CombinationEffects can take the Effect of options of the two Decisions also when swapped`() {
        val effect = Effect.of(BigDecimal.ONE)

        combinationEffects.addEffect(optionOfSecondDecision, optionOfFirstDecision, effect)

        assertThat(combinationEffects.getEffect(optionOfFirstDecision, optionOfSecondDecision)).isEqualTo(effect)
    }

    @Test
    internal fun `CombinationEffects throws IllegalArgumentException when attempting to get the effect of options not each belonging to one of the decisions`() {
        assertThrows<IllegalArgumentException> {
            combinationEffects.getEffect(optionOfFirstDecision, mock())
        }

        assertThrows<IllegalArgumentException> {
            combinationEffects.getEffect(mock(), optionOfSecondDecision)
        }
    }

    @Test
    internal fun `CombinationEffects throws IllegalArgumentException when attempting to add an effect of options not each belonging to one of the decisions`() {
        assertThrows<IllegalArgumentException> {
            combinationEffects.addEffect(optionOfFirstDecision, mock(), Effect.NONE)
        }

        assertThrows<IllegalArgumentException> {
            combinationEffects.addEffect(mock(), optionOfSecondDecision, Effect.NONE)
        }
    }

    @Test
    internal fun `CombinationEffects can add an additional effect to choosing two options together`() {
        val effect1 = Effect.of(BigDecimal.ONE)
        val effect2 = Effect.of(BigDecimal.TEN)

        combinationEffects.addEffect(optionOfFirstDecision, optionOfSecondDecision, effect1)
        combinationEffects.addEffect(optionOfFirstDecision, optionOfSecondDecision, effect2)

        assertThat(combinationEffects.getEffect(optionOfFirstDecision, optionOfSecondDecision)).isEqualTo(effect1 + effect2)
    }
}
