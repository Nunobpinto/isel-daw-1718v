package isel.leic.daw.checklistsAPI.configuration.security

data class IntrospectionResponse(
        val active: Boolean,
        val scope: String? = null,
        val sub: String? = null,
        val user_id: String? = null,
        val client_id: String? = null
)