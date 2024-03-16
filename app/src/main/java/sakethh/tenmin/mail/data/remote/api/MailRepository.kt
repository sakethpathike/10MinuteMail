package sakethh.tenmin.mail.data.remote.api

import retrofit2.Response
import sakethh.tenmin.mail.data.remote.api.model.account.AccountData
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.account.Token
import sakethh.tenmin.mail.data.remote.api.model.mail.Mail

interface MailRepository {
    suspend fun getTokenAndID(body: AccountInfo): Token

    suspend fun getExistingMailAccountData(id: String, token: String): AccountData

    suspend fun getMessages(token: String, pageNo: Int): Mail

    suspend fun deleteAnAccount(id: String, token: String): Response<Unit>
}