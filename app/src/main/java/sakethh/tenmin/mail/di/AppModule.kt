package sakethh.tenmin.mail.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import sakethh.tenmin.mail.data.remote.api.MailService
import javax.inject.Singleton

private val json = Json {
    ignoreUnknownKeys = true
}


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMailService(): MailService {
        return Retrofit.Builder().baseUrl("https://api.mail.gw").addConverterFactory(
            json.asConverterFactory(
                "application/json".toMediaType()
            )
        )
            .build().create(MailService::class.java)
    }
}