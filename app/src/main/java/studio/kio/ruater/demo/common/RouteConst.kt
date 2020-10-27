package studio.kio.ruater.demo.common

import studio.kio.ruater.api.route.Empty
import studio.kio.ruater.api.route.Route

/**
 * created by KIO on 2020/10/13
 */

object ListRoute : Route<Int, Int>()

object DemoRoute : Route<User, Empty>()

object EmptyRoute : Route<Empty, Empty>()