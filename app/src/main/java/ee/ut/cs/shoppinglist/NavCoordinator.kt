package ee.ut.cs.shoppinglist

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavEvent {
    data class ToDetailScreen(val id: String) : NavEvent
    data object ToListScreen : NavEvent
    data object Logout: NavEvent
    data object Back : NavEvent
}

open class NavCoordinator {

    private val _channel = Channel<NavEvent>(capacity = Channel.BUFFERED)
    val events = _channel.receiveAsFlow()

    open fun toDetailScreen(id: String) {
        _channel.trySend(NavEvent.ToDetailScreen(id))
    }

    open fun toListScreen() {
        _channel.trySend(NavEvent.ToListScreen)
    }

    open fun back() {
        _channel.trySend(NavEvent.Back)
    }

    open fun logout() {
        _channel.trySend(NavEvent.Logout)
    }
}