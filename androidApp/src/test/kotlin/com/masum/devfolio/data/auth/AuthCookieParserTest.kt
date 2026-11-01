package com.masum.devfolio.data.auth

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class AuthCookieParserTest {

    @Test
    fun testParseDirectAccessToken() {
        val cookie = "access_token=devfolio_secret_token_123; Path=/; Domain=.devfolio.co"
        val (token, uuid, username) = AuthSessionManager.parseCookies(cookie)

        assertEquals("devfolio_secret_token_123", token)
        assertNull(uuid)
        assertNull(username)
    }

    @Test
    fun testParseEmptyCookies() {
        val (token, uuid, username) = AuthSessionManager.parseCookies("")
        assertNull(token)
        assertNull(uuid)
        assertNull(username)
    }

    @Test
    fun testParseMultipleCookies() {
        val cookies = "dummy=123; access_token=tok_abc_456; other=xyz"
        val (token, _, _) = AuthSessionManager.parseCookies(cookies)
        assertEquals("tok_abc_456", token)
    }
}
