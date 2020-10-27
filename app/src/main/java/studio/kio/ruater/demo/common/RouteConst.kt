package studio.kio.ruater.demo.common

import studio.kio.ruater.api.route.EmptyInput
import studio.kio.ruater.api.route.EmptyOutput
import studio.kio.ruater.api.route.Route

/**
 * created by KIO on 2020/10/13
 */

object ListRoute : Route<Int, Int>()

object DemoRoute : Route<User, EmptyOutput>()

object EmptyRoute : Route<EmptyInput, EmptyOutput>()