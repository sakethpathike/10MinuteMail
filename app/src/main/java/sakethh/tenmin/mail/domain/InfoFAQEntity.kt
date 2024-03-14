package sakethh.tenmin.mail.domain

sealed class InfoFAQEntity {
    data class FAQ(val question: String, val description: String) : InfoFAQEntity()
}