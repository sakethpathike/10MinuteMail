package sakethh.tenmin.mail.data.remote.api

import retrofit2.Response
import sakethh.tenmin.mail.data.remote.api.model.account.AccountData
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.data.remote.api.model.account.Token
import sakethh.tenmin.mail.data.remote.api.model.domain.Domain
import sakethh.tenmin.mail.data.remote.api.model.mail.Mail

class RemoteMailImpl(private val mailService: MailService) : RemoteMailRepository {
    override suspend fun getTokenAndID(body: AccountInfo): Response<Token> {
        return mailService.getAccountTokenAndID(body)
    }

    override suspend fun getExistingMailAccountData(
        id: String,
        token: String
    ): Response<AccountData> {
        return mailService.getExistingMailAccountData(id, "Bearer ".plus(token))
    }

    override suspend fun getMessages(token: String, pageNo: Int): Mail {
        return mailService.getMessages(
            authorization = "Bearer ".plus(token), pageNo = pageNo.toString()
        )
    }

    override suspend fun deleteAnAccount(id: String, token: String): Response<Unit> {
        return mailService.deleteAnAccount(id, "Bearer ".plus(token))
    }

    override suspend fun createANewAccount(accountInfo: AccountInfo): Response<Unit> {
        return mailService.createANewAccount(accountInfo)
    }

    override suspend fun getDomains(): Response<Domain> {
        return mailService.getDomains()
    }
}