package ee.ut.cs.shoppinglist.common

import android.util.Patterns

fun isValidUrl(url: String): Boolean =
    Patterns.WEB_URL.matcher(url).matches()
