package com.fmi.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class JsonAnonymisatorTest {

    @InjectMocks private lateinit var jsonAnonymisator: JsonAnonymisator

    @Test
    fun `anonymize when bad json to anonymize should return error string`() {
        // Given
        val originalBody = """
            Bad json
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo("Bad json")
    }

    @Test
    fun `anonymize when empty json to anonymize should return error string`() {
        // Given
        val originalBody = ""

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo("null")
    }

    @Test
    fun `anonymize when nothing to anonymize should return the input`() {
        // Given
        val originalBody = """
            {"nom":"some value"}
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(originalBody)
    }

    @Test
    fun `anonymize when a key to anonymize should return the input with ***`() {
        // Given
        val originalBody = """
            {"nom":"some value","toAnonymized":"value to hide"}
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                {"nom":"some value","toAnonymized":"***"}
            """.trimIndent()
        )
    }

    @Test
    fun `anonymize when an another key to anonymize should return the input with ***`() {
        // Given
        val originalBody = """
            {"nom":"some value","toAnonymized2":"value to hide"}
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                {"nom":"some value","toAnonymized2":"***"}
            """.trimIndent()
        )
    }

    @Test
    fun `anonymize when many keys to anonymize should return the input with ***`() {
        // Given
        val originalBody = """
            {"nom":"some value","toAnonymized":"value to hide","toAnonymized2":"value to hide"}
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                {"nom":"some value","toAnonymized":"***","toAnonymized2":"***"}
            """.trimIndent()
        )
    }

    @Test
    fun `anonymize when sub json object with keys to anonymize should return the input with ***`() {
        // Given
        val originalBody = """
            {"subObject":{"clearKey":"value","toAnonymized3":"value to hide"}}
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                {"subObject":{"clearKey":"value","toAnonymized3":"***"}}
            """.trimIndent()
        )
    }

    @Test
    fun `anonymize should be able to anonymize each json primitive type`() {
        // Given
        val originalBody = """
            {"toAnonymized":"String","toAnonymized2":12,"toAnonymized3":12.43,"toAnonymized4":true}
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                {"toAnonymized":"***","toAnonymized2":"***","toAnonymized3":"***","toAnonymized4":"***"}
            """.trimIndent()
        )
    }

    @Test
    fun `anonymize should be able to anonymize a simple string`() {
        // Given
        val originalBody = """
            "toto"
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo("""
            "toto"
        """.trimIndent()
        )
    }

    @Test
    fun `anonymize should be able to anonymize a simple integer`() {
        // Given
        val originalBody = "12"

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo("12")
    }

    @Test
    fun `anonymize should be able to anonymize a simple array`() {
        // Given
        val originalBody = """
            ["val1","val2","val3"]
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                ["val1","val2","val3"]
            """.trimIndent()
        )
    }

    @Test
    fun `anonymize should be able to anonymize an array of object`() {
        // Given
        val originalBody = """
            [{"clearKey1":"val1","clearKey2":"val2"},{"clearKey11":"val11","toAnonymized":"val2"}]
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                [{"clearKey1":"val1","clearKey2":"val2"},{"clearKey11":"val11","toAnonymized":"***"}]
            """.trimIndent()
        )
    }

    @Test
    fun `anonymize should be able to anonymize complexe response`() {
        // Given
        val originalBody = """
            [{"toAnonymized":"SELLEM","toAnonymized1":"ELISABETH","assurePrincipal":false,"toAnonymized2":"15062152"},{"toAnonymized":"SELLEM","toAnonymized1":"ALEXANDRE","assurePrincipal":true,"toAnonymized2":"12365"},{"toAnonymized":"SELLEM","toAnonymized1":"ERIC","assurePrincipal":true,"toAnonymized2":"15490533"}]
        """.trimIndent()

        // When
        val anonymizedBody = jsonAnonymisator.anonymize(originalBody)

        // Then
        assertThat(anonymizedBody).isEqualTo(
            """
                [{"toAnonymized":"***","toAnonymized1":"***","assurePrincipal":false,"toAnonymized2":"***"},{"toAnonymized":"***","toAnonymized1":"***","assurePrincipal":true,"toAnonymized2":"***"},{"toAnonymized":"***","toAnonymized1":"***","assurePrincipal":true,"toAnonymized2":"***"}]
            """.trimIndent()
        )
    }

}