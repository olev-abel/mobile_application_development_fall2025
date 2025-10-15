package ee.ut.cs.shoppinglist

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavEvent {
    data class ToDetailScreen(val id: String) : NavEvent
    data object Back : NavEvent
}

class NavCoordinator {

    private val _channel = Channel<NavEvent>(capacity = Channel.BUFFERED)
    val events = _channel.receiveAsFlow()

    fun toDetailScreen(id: String) {
        _channel.trySend(NavEvent.ToDetailScreen(id))
    }

    fun back() {
        _channel.trySend(NavEvent.Back)
    }
}