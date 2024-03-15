package sakethh.tenmin.mail.data.remote.api

import sakethh.tenmin.mail.data.remote.api.model.AccountData
import sakethh.tenmin.mail.data.remote.api.model.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.Token
import javax.inject.Inject

class MailRepository @Inject constructor(private val mailService: MailService) {
    suspend fun getTokenAndID(body: AccountInfo): Token {
        return mailService.getAccountTokenAndID(body)
    }

    suspend fun getExistingMailAccountData(id: String, token: String): AccountData {
        return mailService.getExistingMailAccountData(id, "Bearer ".plus(token))
    }
}