package sakethh.tenmin.mail.data.remote.api

import retrofit2.Response
import sakethh.tenmin.mail.data.remote.api.model.account.AccountData
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.account.Token
import sakethh.tenmin.mail.data.remote.api.model.domain.Domain
import sakethh.tenmin.mail.data.remote.api.model.mail.Mail

interface RemoteMailRepository {
    suspend fun getTokenAndID(body: AccountInfo): Response<Token>

    suspend fun getExistingMailAccountData(id: String, token: String): Response<AccountData>

    suspend fun getMessages(token: String, pageNo: Int): Mail

    suspend fun deleteAnAccount(id: String, token: String): Response<Unit>

    suspend fun createANewAccount(accountInfo: AccountInfo): Response<Unit>
    suspend fun getDomains(): Response<Domain>

}