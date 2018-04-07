package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(
        name = "user",
        suppressClassProperty = true,
        uri = "/users/{username}",
        links = [],
        actions = []
)
class UserOutputModel(
        val username: String = "",
        val familyName: String = "",
        val givenName: String = "",
        val email: String = ""
) : BaseResource()