package com.example.linker.feature.home

import android.content.Context
import android.location.Location
import androidx.core.content.FileProvider
import com.example.linker.core.model.TrackPoint
import com.linker.core.domain.authusecase.LogoutUseCase
import com.linker.core.domain.trackingusecase.AddPointUseCase
import com.linker.core.domain.trackingusecase.EndTrackUseCase
import com.linker.core.domain.trackingusecase.GetTrackPointsUseCase
import com.linker.core.domain.trackingusecase.StartTrackUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.osmdroid.util.GeoPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule val mainRule = MainDispatcherRule()

    // موک‌ها
    private lateinit var startTrack: StartTrackUseCase
    private lateinit var addPoint: AddPointUseCase
    private lateinit var endTrack: EndTrackUseCase
    private lateinit var getPoints: GetTrackPointsUseCase
    private lateinit var logout: LogoutUseCase
    private lateinit var locationClient: LocationClient
    private lateinit var vm: HomeViewModel

    @Before
    fun setup() {
        startTrack = mockk()
        addPoint   = mockk()
        endTrack   = mockk()
        getPoints  = mockk()
        logout     = mockk()
        locationClient = mockk()

        every { locationClient.locationUpdates() } returns emptyFlow()

        vm = HomeViewModel(
            startTrack = startTrack,
            addPoint = addPoint,
            endTrack = endTrack,
            getPoints = getPoints,
            logout = logout,
            location = locationClient
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `onStart opens track and stores first point`() = runTest {
        // Arrange
        coEvery { startTrack.invoke() } returns Result.success(123L)
        val loc = mockLocation(36.1, 52.2)
        coEvery { locationClient.currentOnce(any()) } returns loc
        coEvery { addPoint.invoke(123L, 36.1, 52.2) } returns Unit

        // Act
        vm.onStart()
        advanceUntilIdle()

        // Assert
        assertTrue(vm.tracking)
        assertTrue(vm.polyline.isNotEmpty())
        assertGeo(vm.polyline[0], 36.1, 52.2)
        coVerify{ addPoint.invoke(123L, 36.1, 52.2) }
    }

    @Test
    fun `onStop ends track`() = runTest {
        coEvery { startTrack.invoke() } returns Result.success(77L)
        coEvery { locationClient.currentOnce(any()) } returns null
        vm.onStart()
        advanceUntilIdle()

        coEvery { endTrack.invoke(77L) } returns Unit

        // Act
        vm.onStop()
        advanceUntilIdle()

        // Assert
        assertFalse(vm.tracking)
        coVerify(exactly = 1) { endTrack.invoke(77L) }
    }

    @Test
    fun `onLocation when tracking adds point and persists`() = runTest {
        coEvery { startTrack.invoke() } returns Result.success(9L)
        coEvery { locationClient.currentOnce(any()) } returns null
        vm.onStart()
        advanceUntilIdle()

        val l = mockLocation(35.7, 51.4)
        coEvery { addPoint.invoke(9L, 35.7, 51.4) } returns Unit

        // Act
        vm.onLocation(l)
        advanceUntilIdle()

        // Assert
        assertEquals(1, vm.polyline.size)
        assertGeo(vm.polyline.last(), 35.7, 51.4)
        coVerify { addPoint.invoke(9L, 35.7, 51.4) }
    }

    @Test
    fun `onLocation when not tracking does nothing`() = runTest {
        // Arrange: do not call inStart here
        val l = mockLocation(30.0, 60.0)

        // Act
        vm.onLocation(l)
        advanceUntilIdle()

        // Assert
        assertTrue(vm.polyline.isEmpty())
        coVerify(exactly = 0) { addPoint.invoke(any(), any(), any()) }
    }

    @Test
    fun `exportCsv writes human-readable time and returns uri`() = runTest {
        // Arrange
        coEvery { startTrack.invoke() } returns Result.success(111L)
        coEvery { locationClient.currentOnce(any()) } returns null
        vm.onStart()
        advanceUntilIdle()

        val ts = 1_700_000_000_000L
        coEvery { getPoints.invoke(111L) } returns listOf(
            TrackPoint(trackId = 111L, lat = 36.5, lon = 52.5, timestamp = ts)
        )

        val context = mockk<Context>()
        val tempDir = createTempDir(prefix = "csv_test_")
        every { context.cacheDir } returns tempDir
        every { context.packageName } returns "com.example.traveltracker"

        mockkStatic(FileProvider::class)
        val fileSlot = slot<File>()
        every { FileProvider.getUriForFile(context, any(), capture(fileSlot)) } returns mockk(relaxed = true)

        // Act
        val uri = vm.exportCsv(context) // مهم نیست واقعی نباشه؛ فایل را می‌خوانیم
        advanceUntilIdle()

        // محتوا را از روی فایل capture شده بخوان
        val file = fileSlot.captured
        val content = file.readText()

        // Assert
        assertTrue(content.lines().first().trim() == "lat,lon,time")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val expected = df.format(Date(ts))
        assertTrue(content.contains("36.5,52.5,$expected"))

        // Clean
        unmockkStatic(FileProvider::class)
        file.delete()
        tempDir.deleteRecursively()
    }

    @Test
    fun `addCurrentPointOnce when location is null does nothing`() = runTest {
        coEvery { locationClient.currentOnce(any()) } returns null
        vm.addCurrentPointOnce()
        advanceUntilIdle()

        assertTrue(vm.polyline.isEmpty())
        coVerify(exactly = 0) { addPoint.invoke(any(), any(), any()) }
    }

    @Test
    fun `addCurrentPointOnce before start does nothing`() = runTest {
        // Arrange
        val loc = mockLocation(10.0, 20.0)
        coEvery { locationClient.currentOnce(any()) } returns loc

        // Act
        vm.addCurrentPointOnce()
        advanceUntilIdle()

        // Assert
        assertTrue(vm.polyline.isEmpty())
        coVerify(exactly = 0) { addPoint.invoke(any(), any(), any()) }
    }

    @Test(expected = IllegalStateException::class)
    fun `exportCsv without active track throws`() = runTest {
        val ctx = mockk<Context>()
        vm.exportCsv(ctx)
    }

    @Test
    fun `locations flow is passthrough from locationClient`() {
        val f = emptyFlow<Location>()
        every { locationClient.locationUpdates() } returns f
        val localVm = HomeViewModel(startTrack, addPoint, endTrack, getPoints, logout, locationClient)
        assertTrue(localVm.locations === f)
    }



    // ---------- helpers ----------
    private fun mockLocation(lat: Double, lon: Double): Location =
        mockk<Location>().apply {
            every { latitude } returns lat
            every { longitude } returns lon
        }

    private fun assertGeo(p: GeoPoint, lat: Double, lon: Double) {
        assertEquals(lat, p.latitude, 1e-6)
        assertEquals(lon, p.longitude, 1e-6)
    }
}