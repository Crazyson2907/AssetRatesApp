package com.task.assetratesapp


import com.google.common.truth.Truth.assertThat
import com.task.assetratesapp.domain.network.core.ExchangeRatesApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRatesApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ExchangeRatesApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRatesApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getLatestRates returns valid response`() = runBlocking {
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

        // Enqueue the mock response
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(dummyResponse))

        // Call the API method (for example, getting EUR rate)
        val response = apiService.getLatestRates(base = "USD", symbols = "EUR")

        // Verify the result
        assertThat(response.success).isTrue()
        assertThat(response.source).isEqualTo("USD")
        assertThat(response.quotes["USDEUR"]).isEqualTo(0.85)
    }
}