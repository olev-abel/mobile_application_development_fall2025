package ee.ut.cs.shoppinglist.domain.authentication

import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun isUserLoggedIn(): Boolean
    suspend fun login(email: String, password: String): NetworkResult<Unit>

    suspend fun logout(): NetworkResult<Unit>
}