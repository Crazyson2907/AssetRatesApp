package com.task.assetratesapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.task.assetratesapp.data.cache.AssetDatabase
import com.task.assetratesapp.data.network.AssetRepositoryImpl
import com.task.assetratesapp.domain.network.core.ExchangeRatesApiService
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class AssetRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ExchangeRatesApiService
    private lateinit var database: AssetDatabase
    private lateinit var repository: AssetRepositoryImpl

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRatesApiService::class.java)

        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AssetDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = AssetRepositoryImpl(apiService, database)
    }

    @After
    fun teardown() {
        database.close()
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchRate returns correct rate`() = runTest {
        val dummyResponse = """
            {
                "success": true,
                "terms": "https://exchangerate.host/terms",
                "privacy": "https://exchangerate.host/privacy",
                "timestamp": 1430401802,
                "source": "USD",
                "quotes": {
                    "USDEUR": 0.85
                }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(dummyResponse))

        val rate = repository.fetchRate("EUR")
        assertEquals(0.85, rate)
    }

    @Test
    fun `addAsset and getAssets store and return asset`() = runTest {
        repository.addAsset("EUR")
        val result = repository.getAssets()
        assertTrue(result.contains("EUR"))
    }

    @Test
    fun `removeAsset removes the asset`() = runTest {
        repository.addAsset("JPY")
        repository.removeAsset("JPY")
        val result = repository.getAssets()
        assertFalse(result.contains("JPY"))
    }
}