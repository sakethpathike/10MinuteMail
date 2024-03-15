package sakethh.tenmin.mail.ui.accounts.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.remote.api.MailRepository
import sakethh.tenmin.mail.data.remote.api.model.account.AccountInfo
import sakethh.tenmin.mail.ui.accounts.screens.AccountsUiEvent
import javax.inject.Inject

@HiltViewModel
class AccountsVM @Inject constructor(private val mailRepository: MailRepository) : ViewModel() {
    fun onUiClickEvent(accountsUiEvent: AccountsUiEvent) {
        when (accountsUiEvent) {
            is AccountsUiEvent.SignIn -> {
                viewModelScope.launch {
                    val requestedEmailTokenAndID = mailRepository.getTokenAndID(
                        body = AccountInfo(
                            address = accountsUiEvent.emailAddress,
                            password = accountsUiEvent.emailPassword
                        )
                    )
                    Log.d("10MinMail", requestedEmailTokenAndID.toString())
                    val accountData = mailRepository.getExistingMailAccountData(
                        requestedEmailTokenAndID.id,
                        requestedEmailTokenAndID.token
                    )
                    Log.d("10MinMail", accountData.toString())
                    val messages = mailRepository.getMessages(requestedEmailTokenAndID.token, 1)
                    Log.d("10MinMail", messages.toString())
                }
            }
        }
    }
}