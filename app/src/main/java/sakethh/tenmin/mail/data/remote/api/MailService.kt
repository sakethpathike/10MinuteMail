package sakethh.tenmin.mail.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import sakethh.tenmin.mail.data.remote.api.model.AccountData
import sakethh.tenmin.mail.data.remote.api.model.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.Token

interface MailService {
    @POST("/token")
    suspend fun getAccountTokenAndID(@Body body: AccountInfo): Token

    @GET("/accounts/{id}")
    suspend fun getExistingMailAccountData(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): AccountData
}