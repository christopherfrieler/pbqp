package rocks.frieler.pbqp.model

/**
 * An instance of the Partial Boolean Quadratic Problem (PBQP).
 *
 * @param O the type of options to choose from
 */
class PartialBooleanQuadraticProblem<O>() {
    private val decisions: MutableList<Decision<O>> = ArrayList()
    private val combinationEffects: MutableMap<Pair<Decision<O>, Decision<O>>, CombinationEffects<O>> = HashMap()

    fun addDecision(decision: Decision<O>) {
        decisions.add(decision)
    }

    fun getDecisions(): List<Decision<O>> = listOf(*decisions.toTypedArray())

    fun addOption(decision: Decision<O>, option: O, effect: Effect = Effect.NONE) {
        if (!decisions.contains(decision)) {
            decisions.add(decision)
        }
        decision.addOption(option, effect)
    }

    fun addCombinationEffect(decision1: Decision<O>, option1: O, decision2: Decision<O>, option2: O, additionalEffect: Effect) {
        require(decisions.contains(decision1))
        require(decision1.hasOption(option1))
        require(decisions.contains(decision2))
        require(decision2.hasOption(option2))

        val indexOfDecision1 = decisions.indexOf(decision1)
        val indexOfDecision2 = decisions.indexOf(decision2)
        if (indexOfDecision1 < indexOfDecision2) {
            combinationEffects.computeIfAbsent(Pair(decision1, decision2)) { CombinationEffects(decision1, decision2) }
                .addEffect(option1, option2, additionalEffect)
        } else {
            combinationEffects.computeIfAbsent(Pair(decision2, decision1)) { CombinationEffects(decision2, decision1) }
                .addEffect(option2, option1, additionalEffect)
        }
    }

    fun getCombinationEffects(): List<CombinationEffects<O>> = listOf(*combinationEffects.values.toTypedArray())
}
