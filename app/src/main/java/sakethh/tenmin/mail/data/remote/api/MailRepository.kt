package sakethh.tenmin.mail.data.remote.api

import retrofit2.Response
import sakethh.tenmin.mail.data.remote.api.model.account.AccountData
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.account.Token
import sakethh.tenmin.mail.data.remote.api.model.mail.Mail

interface MailRepository {
    suspend fun getTokenAndID(body: AccountInfo): Response<Token>

    suspend fun getExistingMailAccountData(id: String, token: String): Response<AccountData>

    suspend fun getMessages(token: String, pageNo: Int): Response<Mail>

    suspend fun deleteAnAccount(id: String, token: String): Response<Unit>
}