package com.rodrigmatrix.weatheryou.data.remote.search

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.Normalizer

class SearchLocalDataSourceMatcherTest {

    @Test
    fun `matches common ASCII names without accent folding`() {
        val query = "london"

        assertTrue("London".matchesSearchQuery(query, query))
        assertFalse("Paris".matchesSearchQuery(query, query))
    }

    @Test
    fun `matches accented city when query omits its accent`() {
        val query = "sao"

        assertTrue("São Paulo".matchesSearchQuery(query, query))
    }

    @Test
    fun `matches ASCII city when query includes an accent`() {
        val query = "São"
        val normalizedQuery = Normalizer.normalize(query, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

        assertTrue("Sao Paulo".matchesSearchQuery(query, normalizedQuery))
    }
}
