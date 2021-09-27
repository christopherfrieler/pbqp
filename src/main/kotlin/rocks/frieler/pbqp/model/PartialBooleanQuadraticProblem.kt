package rocks.frieler.pbqp.model

/**
 * An instance of the Partial Boolean Quadratic Problem (PBQP).
 *
 * @param O the type of options to choose from
 */
class PartialBooleanQuadraticProblem<O>() {
    private val decisions: MutableList<Decision<O>> = ArrayList()
    private val combinationEffects: MutableMap<Pair<Decision<O>, Decision<O>>, CombinationEffects<O>> = HashMap()

    /**
     * Adds the given [Decision] to this [PBQP][PartialBooleanQuadraticProblem].
     *
     * @param decision the [Decision] to add
     */
    fun addDecision(decision: Decision<O>) {
        decisions.add(decision)
    }

    /**
     * Returns the decisions to be made in this [PBQP][PartialBooleanQuadraticProblem].
     *
     * Note: The returned list is an immutable copy. Changes must be made through the corresponding methods of
     * [PBQP][PartialBooleanQuadraticProblem] and a re not reflected in the list.
     *
     * @return the [Decision]s in this [PBQP][PartialBooleanQuadraticProblem]
     */
    fun getDecisions(): List<Decision<O>> = listOf(*decisions.toTypedArray())

    /**
     * Adds the given option, optionally with the given [Effect] to the given [Decision] within this
     * [PBQP][PartialBooleanQuadraticProblem].
     *
     * @param decision the [Decision] to add an option to
     * @param option the option to add
     * @param effect the [Effect] when choosing the option, defaults to [NONE][Effect.NONE]
     */
    fun addOption(decision: Decision<O>, option: O, effect: Effect = Effect.NONE) {
        if (!decisions.contains(decision)) {
            decisions.add(decision)
        }
        decision.addOption(option, effect)
    }

    /**
     * Adds the given [Effect] for choosing a combination of two options for two [Decision]s within this
     * [PBQP][PartialBooleanQuadraticProblem].
     *
     * When there is already an [Effect] for these options, the additional [Effect] is added.
     *
     * @param decision1 a [Decision] within this [PBQP][PartialBooleanQuadraticProblem]
     * @param option1 an option for [decision1]
     * @param decision2 another [Decision] within this [PBQP][PartialBooleanQuadraticProblem]
     * @param option2 an option for [decision2]
     * @param additionalEffect additional [Effect] for choosing the two options
     * @see [CombinationEffects]
     */
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

    /**
     * Returns a list of all [CombinationEffects] for choosing two options for two [Decision]s within this
     * [PBQP][PartialBooleanQuadraticProblem].
     *
     * Note: The returned list is an immutable copy. Changes must be made through the corresponding methods of
     * [PBQP][PartialBooleanQuadraticProblem] and a re not reflected in the list.
     */
    fun getCombinationEffects(): List<CombinationEffects<O>> = listOf(*combinationEffects.values.toTypedArray())
}
