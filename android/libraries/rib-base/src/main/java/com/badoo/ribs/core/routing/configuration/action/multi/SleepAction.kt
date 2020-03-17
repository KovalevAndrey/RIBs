package com.badoo.ribs.core.routing.configuration.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.single.DeactivateAction
import com.badoo.ribs.core.routing.configuration.feature.WorkingState

/**
 * Calls [DeactivateAction] all elements with an [ActivationState] of [ACTIVE].
 */
internal class SleepAction<C : Parcelable> : MultiConfigurationAction<C> {

    /**
     * Filters the pool for [ACTIVE] elements, executes [DeactivateAction] on all of them.
     *
     * @return the map of elements updated by [DeactivateAction]
     */
    override fun execute(
        state: WorkingState<C>,
        params: ActionExecutionParams<C>
    ): Map<ConfigurationKey, ConfigurationContext.Resolved<C>> =
        state.pool.invokeOn(ACTIVE, params) { (foundByFilter, add) ->
            if (add != null) {
                add.onBeforeTransition()
                add.onTransition()
                add.onFinish()
            }

            state.ongoingTransitions.forEach { it.jumpToEnd() }
            val action = DeactivateAction(foundByFilter, params, false)
            action.onBeforeTransition()
            action.onTransition()
            action.onFinish()
            action.result.withActivationState(SLEEPING)
        }
}
