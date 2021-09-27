package rocks.frieler.pbqp.model

import assertk.Assert
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal

internal class PartialBooleanQuadraticProblemTest {
    private val pbqp = PartialBooleanQuadraticProblem<Any>()

    @Test
    internal fun `can create PBQP instance`() {
        assertThat(pbqp).isNotNull()
    }

    @Test
    internal fun `PBQP can contain a Decision`() {
        val decision = mock<Decision<Any>>()

        pbqp.addDecision(decision)

        assertThat(pbqp.getDecisions()).containsExactly(decision)
    }

    @Test
    internal fun `PBQP exposes a copy of its Decisions`() {
        val anotherDecision = mock<Decision<Any>>()

        val decisionsSoFar = pbqp.getDecisions()
        pbqp.addDecision(anotherDecision)

        assertThat(decisionsSoFar).doesNotContain(anotherDecision)
    }

    @Test
    internal fun `PBQP can add an Option to an existing Decision`() {
        val decision = mock<Decision<Any>>()
        val option = mock<Any>()

        pbqp.addDecision(decision)
        pbqp.addOption(decision, option)

        verify(decision).addOption(option)
    }

    @Test
    internal fun `PBQP can add a new Decision and an Option`() {
        val decision = mock<Decision<Any>>()
        val option = mock<Any>()

        pbqp.addOption(decision, option)

        assertThat(pbqp.getDecisions()).contains(decision)
        verify(decision).addOption(option)
    }

    @Test
    internal fun `PBQP can add anOption with an Effect to a Decision`() {
        val decision = mock<Decision<Any>>()
        val option = mock<Any>()

        pbqp.addDecision(decision)
        pbqp.addOption(decision, option, Effect.of(BigDecimal.ONE))

        verify(decision).addOption(option, Effect.of(BigDecimal.ONE))
    }

    @Test
    internal fun `PBQP can hold the CombinationEffect of two Options`() {
        val decision1 = mock<Decision<Any>>()
        val option1 = mock<Any>()
        pbqp.addDecision(decision1)
        whenever(decision1.hasOption(option1)).thenReturn(true)
        val decision2 = mock<Decision<Any>>()
        val option2 = mock<Any>()
        pbqp.addDecision(decision2)
        whenever(decision2.hasOption(option2)).thenReturn(true)
        val effect = Effect.of(BigDecimal.ONE)

        pbqp.addCombinationEffect(decision1, option1, decision2, option2, effect)

        assertThat(pbqp.getCombinationEffects()).containsCombinationEffect(decision1, option1, decision2, option2, effect)
    }

    @Test
    internal fun `PBQP can add additional CombinationEffect to swapped Decisions`() {
        val decision1 = mock<Decision<Any>>()
        val option1 = mock<Any>()
        pbqp.addDecision(decision1)
        whenever(decision1.hasOption(option1)).thenReturn(true)
        val decision2 = mock<Decision<Any>>()
        val option2 = mock<Any>()
        pbqp.addDecision(decision2)
        whenever(decision2.hasOption(option2)).thenReturn(true)

        pbqp.addCombinationEffect(decision1, option1, decision2, option2, Effect.of(BigDecimal.ONE))
        pbqp.addCombinationEffect(decision2, option2, decision1, option1, Effect.of(BigDecimal.ONE))

        assertThat(pbqp.getCombinationEffects()).containsCombinationEffect(decision1, option1, decision2, option2, Effect.of(BigDecimal.valueOf(2)))
    }

    @Test
    internal fun `PBQP exposes only a copy of the CombinationEffects`() {
        val combinationEffectsSoFar = pbqp.getCombinationEffects()

        val decision1 = mock<Decision<Any>>()
        val option1 = mock<Any>()
        pbqp.addDecision(decision1)
        whenever(decision1.hasOption(option1)).thenReturn(true)
        val decision2 = mock<Decision<Any>>()
        val option2 = mock<Any>()
        pbqp.addDecision(decision2)
        whenever(decision2.hasOption(option2)).thenReturn(true)
        val effect = Effect.of(BigDecimal.ONE)

        pbqp.addCombinationEffect(decision1, option1, decision2, option2, effect)

        assertThat(combinationEffectsSoFar).containsNoCombinationEffect(decision1, decision2)
    }

    private fun Assert<List<CombinationEffects<Any>>>.containsCombinationEffect(
            decision1: Decision<Any>, option1: Any,
            decision2: Decision<Any>, option2: Any,
            effect: Effect,
    ) = given { actual ->
        val combinationEffects = actual.find {
            (it.firstDecision == decision1 && it.secondDecision == decision2)
            || (it.firstDecision == decision2 && it.secondDecision == decision1)
        }
        assertThat(combinationEffects).isNotNull()
        assertThat(combinationEffects!!.getEffect(option1, option2)).isEqualTo(effect)
    }

    private fun Assert<List<CombinationEffects<Any>>>.containsNoCombinationEffect(
            decision1: Decision<Any>, decision2: Decision<Any>
    ) = given { actual ->
        assertThat(actual.find {
            (it.firstDecision == decision1 && it.secondDecision == decision2)
            || (it.firstDecision == decision2 && it.secondDecision == decision1)
        }).isNull()
    }
}
