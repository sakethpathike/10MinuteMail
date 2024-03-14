package sakethh.tenmin.mail.ui.info

import androidx.lifecycle.ViewModel
import sakethh.tenmin.mail.domain.InfoFAQEntity

class InfoScreenVM : ViewModel() {
    val faqData = listOf(
        InfoFAQEntity.FAQ(
            "What is a temporary / disposable / anonymous mail?",
            "A temporary, completely anonymous email address that does not require any registration. Delete your mail as fast as you receive it and never worry about spam. It's that simple!"
        ),
        InfoFAQEntity.FAQ(
            "Why do you need a temporary email address?",
            "Whether you're signing up for a new online account and don't want the email address you have associated with your profile, or you're sharing some private thoughts with someone online. It's that easy."
        ),
        InfoFAQEntity.FAQ(
            "How is it different from usual mail?",
            "• Does not require registration.\n• It is completely anonymous: personal data, address itself and emails are deleted after 10 minutes.\n• Messages are delivered instantly.\n• Email address is generated automatically.\n• The mailbox is protected from spam, hacking, exploits."
        ),
        InfoFAQEntity.FAQ(
            "How do I send an email?",
            "Unfortunately, we do not provide this feature."
        ),
        InfoFAQEntity.FAQ(
            "How to extend the life time of temporary mail?",
            "Unfortunately, we do not provide this feature."
        ),
        InfoFAQEntity.FAQ(
            "How long do you store incoming messages?",
            "We do store messages for only 10 minutes."
        ),
        InfoFAQEntity.FAQ(
            "How can I change the password?",
            "Unfortunately, we do not provide this feature."
        ),
        InfoFAQEntity.FAQ(
            "Do you keep and renew your domains used by this service?",
            "No, we aren't renewing domains that were used by this service. If you need a constant account just use Mail.tm."
        ),
        InfoFAQEntity.FAQ(
            "I forgot my password, is there a way you can reset it?",
            "No, there is no way we could reset your password."
        ),
    )
}