package com.badoo.ribs.routing.resolver

import android.os.Parcelable
import com.badoo.ribs.routing.action.Resolution
import com.badoo.ribs.routing.Routing

interface RoutingResolver<C : Parcelable> {

    fun resolve(routing: Routing<C>): Resolution
}
