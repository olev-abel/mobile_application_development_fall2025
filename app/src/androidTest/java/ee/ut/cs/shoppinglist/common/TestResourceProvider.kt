package ee.ut.cs.shoppinglist.common

import androidx.test.platform.app.InstrumentationRegistry

class TestResourceProvider(
    private val overrides: Map<Int, String> = emptyMap()
) : ResourceProvider {
    override fun getString(resId: Int, vararg args: Any): String {
        val override = overrides[resId]
        if (override != null) {
            return if (args.isEmpty()) override else String.format(override, *args)
        }
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        return if (args.isEmpty()) ctx.getString(resId) else ctx.getString(resId, *args)
    }
}