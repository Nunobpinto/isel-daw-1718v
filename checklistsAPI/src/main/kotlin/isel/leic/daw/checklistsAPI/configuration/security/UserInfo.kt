package isel.leic.daw.checklistsAPI.configuration.security

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
data class UserInfo(
        var sub: String? = null,
        var user_id: String? = null
)