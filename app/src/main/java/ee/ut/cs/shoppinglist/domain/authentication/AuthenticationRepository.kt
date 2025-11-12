package ee.ut.cs.shoppinglist.domain.authentication

import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult

interface AuthenticationRepository {

    fun isUserLoggedIn(): Boolean
    suspend fun login(email: String, password: String): NetworkResult<Unit>

    suspend fun logout(): NetworkResult<Unit>
}