package ee.ut.cs.shoppinglist.data

import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult
import ee.ut.cs.shoppinglist.domain.authentication.AuthenticationRepository

class TestAuthenticationRepository(
    var shouldLoginSucceed: Boolean,
    var shouldUserBeLoggedIn: Boolean
) : AuthenticationRepository {
    override fun isUserLoggedIn(): Boolean {
        return shouldUserBeLoggedIn
    }

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResult<Unit> {
        if (shouldLoginSucceed) {
            return NetworkResult.Success(Unit)
        } else {
            return NetworkResult.Error("Login failed")
        }
    }

    override suspend fun logout(): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }
}