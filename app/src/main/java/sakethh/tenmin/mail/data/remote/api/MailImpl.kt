package sakethh.tenmin.mail.data.remote.api

import sakethh.tenmin.mail.data.remote.api.model.account.AccountData
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.account.Token
import sakethh.tenmin.mail.data.remote.api.model.mail.Mail

class MailImpl(private val mailService: MailService) : MailRepository {
    override suspend fun getTokenAndID(body: AccountInfo): Token {
        return mailService.getAccountTokenAndID(body)
    }

    override suspend fun getExistingMailAccountData(id: String, token: String): AccountData {
        return mailService.getExistingMailAccountData(id, "Bearer ".plus(token))
    }

    override suspend fun getMessages(token: String, pageNo: Int): Mail {
        return mailService.getMessages(
            authorization = "Bearer ".plus(token), pageNo = pageNo.toString()
        )
    }
}