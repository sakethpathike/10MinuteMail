package sakethh.tenmin.mail.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import sakethh.tenmin.mail.data.remote.api.model.account.AccountData
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.account.Token
import sakethh.tenmin.mail.data.remote.api.model.domain.Domain
import sakethh.tenmin.mail.data.remote.api.model.mail.Mail

interface MailService {
    @POST("/token")
    suspend fun getAccountTokenAndID(@Body body: AccountInfo): Response<Token>

    @GET("/accounts/{id}")
    suspend fun getExistingMailAccountData(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): Response<AccountData>

    @GET("/messages")
    suspend fun getMessages(
        @Header("Authorization") authorization: String,
        @Query("page") pageNo: String
    ): Mail

    @DELETE("/accounts/{id}")
    suspend fun deleteAnAccount(
        @Path("id") id: String, @Header("Authorization") authorization: String
    ): Response<Unit>

    @POST("/accounts")
    suspend fun createANewAccount(
        @Body accountInfo: AccountInfo
    ): Response<Unit>

    @GET("/domains?page=1")
    suspend fun getDomains(): Response<Domain>
}