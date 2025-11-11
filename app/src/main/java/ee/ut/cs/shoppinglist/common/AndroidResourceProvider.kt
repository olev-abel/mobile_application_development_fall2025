package ee.ut.cs.shoppinglist.common

import android.content.Context
import androidx.annotation.StringRes

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(@StringRes resId: Int, vararg args: Any): String =
        context.getString(resId, *args)
}