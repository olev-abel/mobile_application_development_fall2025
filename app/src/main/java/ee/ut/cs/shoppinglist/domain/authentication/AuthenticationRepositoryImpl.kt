package ee.ut.cs.shoppinglist.domain.authentication

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult
import kotlinx.coroutines.tasks.await

private const val TAG = "AuthenticationRepoImpl"

class AuthenticationRepositoryImpl(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth
) : AuthenticationRepository {
    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun login(email: String, password: String): NetworkResult<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            Log.w(TAG, "Login failed", e)
            NetworkResult.Error(e.localizedMessage ?: "Login failed")
        }

    }

    override suspend fun logout(): NetworkResult<Unit> {
        return try {
            firebaseAuth.signOut()
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            Log.w(TAG, "logout failed", e)
            NetworkResult.Error(e.localizedMessage ?: "Logout failed")
        }
    }
}