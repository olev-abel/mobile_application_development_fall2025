package ee.ut.cs.shoppinglist.data.local.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
// 1) Add a temporary TEXT column to hold converted image values
        db.execSQL("ALTER TABLE shopping_items ADD COLUMN image_text TEXT")

        // 2) Populate the temporary column from the old integer image column,
        //    casting integers to text and preserving NULLs
        db.execSQL(
            """
            UPDATE shopping_items
            SET image_text = CASE
              WHEN image IS NULL THEN NULL
              ELSE CAST(image AS TEXT)
            END
            """.trimIndent()
        )

        // 3) Create the new table with image as TEXT (final schema)
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `shopping_items_new` (
              `id` TEXT NOT NULL PRIMARY KEY,
              `name` TEXT NOT NULL,
              `quantity` INTEGER NOT NULL,
              `isBought` INTEGER NOT NULL,
              `image` TEXT,
              `category` TEXT NOT NULL
            )
            """.trimIndent()
        )

        // 4) Copy data from old table, using converted image_text as image
        db.execSQL(
            """
            INSERT INTO `shopping_items_new` (id, name, quantity, isBought, image, category)
            SELECT id, name, quantity, isBought, image_text, category
            FROM `shopping_items`
            """.trimIndent()
        )

        // 5) Drop old table and rename new one
        db.execSQL("DROP TABLE `shopping_items`")
        db.execSQL("ALTER TABLE `shopping_items_new` RENAME TO `shopping_items`")
    }
}