package com.example.linker.feature.home

import android.content.Context
import android.location.Location
import android.net.Uri
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
import io.mockk.spyk
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
        assertEquals(1, vm.polyline.size)
        assertGeo(vm.polyline[0], 36.1, 52.2)
        coVerify(exactly = 1) { addPoint.invoke(123L, 36.1, 52.2) }
    }

    @Test
    fun `onStop ends track`() = runTest {
        // Arrange: با onStart به حالت tracking برسیم و id را کپچر کنیم
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
        // Arrange: استارت کنیم
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
        // Arrange: عمداً onStart را صدا نمی‌زنیم
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
        // currentTrackId private است ⇒ روی public API اعتماد می‌کنیم: از usecase خروجی می‌گیریم برای همان id
        // این ویومادل داخل exportCsv از currentTrackId استفاده می‌کند؛ پس قبلش یک track بسازیم:
        coEvery { startTrack.invoke() } returns Result.success(111L)
        coEvery { locationClient.currentOnce(any()) } returns null
        vm.onStart()
        advanceUntilIdle()

        val ts = 1_700_000_000_000L
        coEvery { getPoints.invoke(111L) } returns listOf(
            TrackPoint(trackId = 111L, lat = 36.5, lon = 52.5, timestamp = ts)
        )

        // Context موک با cacheDir temp
        val context = mockk<Context>()
        val tempDir = createTempDir(prefix = "csv_test_")
        every { context.cacheDir } returns tempDir
        every { context.packageName } returns "com.example.linker"

        // FileProvider را استاتیک موک کن تا URI بسازد
        mockkStatic(FileProvider::class)
        every { FileProvider.getUriForFile(context, any(), any()) } answers {
            val file = thirdArg<File>()
            Uri.fromFile(file)
        }

        // Act
        val uri = vm.exportCsv(context)
        val file = File(uri.path!!)
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

    // ---------- helpers ----------
    private fun mockLocation(lat: Double, lon: Double): Location =
        spyk(Location("mock")).apply {
            latitude = lat
            longitude = lon
        }

    private fun assertGeo(p: GeoPoint, lat: Double, lon: Double) {
        assertEquals(lat, p.latitude, 1e-6)
        assertEquals(lon, p.longitude, 1e-6)
    }
}