package com.masum.devfolio.data.hackathon

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class HackathonApplicationStatusTest {

    @Test
    fun testStatusMappingSubmitted() {
        assertEquals(NativeApplicationStatus.APPLIED_UNDER_REVIEW, HackathonApplicationRepository.mapStatus("submit", null))
        assertEquals(NativeApplicationStatus.APPLIED_UNDER_REVIEW, HackathonApplicationRepository.mapStatus("submitted", null))
        assertEquals(NativeApplicationStatus.APPLIED_UNDER_REVIEW, HackathonApplicationRepository.mapStatus("applied", null))
        assertEquals(NativeApplicationStatus.APPLIED_UNDER_REVIEW, HackathonApplicationRepository.mapStatus("under_review", null))
    }

    @Test
    fun testStatusMappingAccepted() {
        assertEquals(NativeApplicationStatus.ACCEPTED_RSVP_PENDING, HackathonApplicationRepository.mapStatus("accepted", null))
        assertEquals(NativeApplicationStatus.ACCEPTED_RSVP_PENDING, HackathonApplicationRepository.mapStatus("accepted", "pending"))
        assertEquals(NativeApplicationStatus.RSVP_CONFIRMED, HackathonApplicationRepository.mapStatus("accepted", "attending"))
        assertEquals(NativeApplicationStatus.RSVP_CONFIRMED, HackathonApplicationRepository.mapStatus("accepted", "confirmed"))
    }

    @Test
    fun testStatusMappingRsvpDirect() {
        assertEquals(NativeApplicationStatus.RSVP_CONFIRMED, HackathonApplicationRepository.mapStatus("rsvp", null))
        assertEquals(NativeApplicationStatus.RSVP_CONFIRMED, HackathonApplicationRepository.mapStatus("check_in", null))
    }

    @Test
    fun testStatusMappingWaitlistAndWithdrawn() {
        assertEquals(NativeApplicationStatus.WAITLISTED, HackathonApplicationRepository.mapStatus("waitlist", null))
        assertEquals(NativeApplicationStatus.WAITLISTED, HackathonApplicationRepository.mapStatus("waitlisted", null))
        assertEquals(NativeApplicationStatus.WITHDRAWN, HackathonApplicationRepository.mapStatus("withdraw", null))
        assertEquals(NativeApplicationStatus.WITHDRAWN, HackathonApplicationRepository.mapStatus("withdrawn", null))
        assertEquals(NativeApplicationStatus.WITHDRAWN, HackathonApplicationRepository.mapStatus("cancelled", null))
    }

    @Test
    fun testStatusMappingRejectedAndUnknown() {
        assertEquals(NativeApplicationStatus.REJECTED, HackathonApplicationRepository.mapStatus("rejected", null))
        assertEquals(NativeApplicationStatus.REJECTED, HackathonApplicationRepository.mapStatus("reject_sent", null))
        assertEquals(NativeApplicationStatus.NOT_APPLIED, HackathonApplicationRepository.mapStatus(null, null))
        assertEquals(NativeApplicationStatus.NOT_APPLIED, HackathonApplicationRepository.mapStatus("", null))
        assertEquals(NativeApplicationStatus.NOT_APPLIED, HackathonApplicationRepository.mapStatus("random_unknown", null))
    }

    @Test
    fun testInMemCacheRoundtrip() {
        HackathonApplicationRepository.clearCache()
        val dummyDetail = ApplicationStatusDetail(
            status = NativeApplicationStatus.APPLIED_UNDER_REVIEW,
            teamName = "PIKACHU",
            teamCode = "PK123"
        )
        HackathonApplicationRepository.updateCachedStatus("testuser", "testathon", dummyDetail)

        val retrieved = HackathonApplicationRepository.getCachedStatus("testuser", "testathon")
        assertEquals(dummyDetail, retrieved)
        assertEquals("PIKACHU", retrieved?.teamName)

        HackathonApplicationRepository.clearCache()
        assertNull(HackathonApplicationRepository.getCachedStatus("testuser", "testathon"))
    }
}
