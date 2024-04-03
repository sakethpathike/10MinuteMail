package sakethh.tenmin.mail.di

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import sakethh.tenmin.mail.data.local.LocalDatabase
import sakethh.tenmin.mail.data.local.repo.accounts.AccountsImpl
import sakethh.tenmin.mail.data.local.repo.accounts.AccountsRepo
import sakethh.tenmin.mail.data.local.repo.mail.LocalMailRepo
import sakethh.tenmin.mail.data.remote.api.MailService
import sakethh.tenmin.mail.data.remote.api.RemoteMailImpl
import sakethh.tenmin.mail.data.remote.api.RemoteMailRepository
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

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application): LocalDatabase {
        return Room.databaseBuilder(app, LocalDatabase::class.java, "tenMinuteMailDB").build()
    }

    @Provides
    @Singleton
    fun provideAccountsRepository(db: LocalDatabase): AccountsRepo {
        return AccountsImpl(db.accountsDao)
    }

    @Provides
    @Singleton
    fun provideMailRepository(mailService: MailService): RemoteMailRepository {
        return RemoteMailImpl(mailService)
    }

    @Provides
    @Singleton
    fun provideInboxRepo(db: LocalDatabase): LocalMailRepo {
        return sakethh.tenmin.mail.data.local.repo.mail.LocalMailImpl(db.inboxDao)
    }


}