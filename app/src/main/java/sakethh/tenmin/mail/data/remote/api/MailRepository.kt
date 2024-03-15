package sakethh.tenmin.mail.data.remote.api

import sakethh.tenmin.mail.data.remote.api.model.account.AccountData
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.account.Token
import sakethh.tenmin.mail.data.remote.api.model.mail.Mail
import javax.inject.Inject

class MailRepository @Inject constructor(private val mailService: MailService) {
    suspend fun getTokenAndID(body: AccountInfo): Token {
        return mailService.getAccountTokenAndID(body)
    }

    suspend fun getExistingMailAccountData(id: String, token: String): AccountData {
        return mailService.getExistingMailAccountData(id, "Bearer ".plus(token))
    }

    suspend fun getMessages(token: String, pageNo: Int): Mail {
        return mailService.getMessages(
            authorization = "Bearer ".plus(token),
            pageNo = pageNo.toString()
        )
    }
}