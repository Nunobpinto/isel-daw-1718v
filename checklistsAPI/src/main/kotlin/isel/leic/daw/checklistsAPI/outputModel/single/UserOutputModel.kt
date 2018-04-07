package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JAction
import com.google.code.siren4j.annotations.Siren4JActionField
import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JSubEntity
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.resource.BaseResource
import isel.leic.daw.checklistsAPI.outputModel.collection.ChecklistCollectionOutputModel
import isel.leic.daw.checklistsAPI.outputModel.collection.ChecklistTemplateCollectionOutputModel

@Siren4JEntity(
        name = "user",
        suppressClassProperty = true,
        uri = "/users/{username}",
        links = [],
        actions = [
            Siren4JAction(
                    name = "delete-user",
                    title = "Delete User",
                    method = ActionImpl.Method.DELETE,
                    href = "/users/{username}"
            ),
            Siren4JAction(
                    name = "update-user",
                    title = "Update User",
                    method = ActionImpl.Method.PUT,
                    href = "/users/{username}",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "username", type = "text", required = true),
                        Siren4JActionField(name = "family_name", type = "text", required = true),
                        Siren4JActionField(name = "email", type = "text", required = true),
                        Siren4JActionField(name = "given_name", type = "text", required = true),
                        Siren4JActionField(name = "password", type = "password", required = true)
                    ]
            )
        ]
)
class UserOutputModel(
        val username: String = "",
        val familyName: String = "",
        val givenName: String = "",
        val email: String = "",
        @Siren4JSubEntity(
                uri = "/api/checklists",
                rel = ["collection", "search"],
                embeddedLink = true
        )
        val checklists: ChecklistCollectionOutputModel = ChecklistCollectionOutputModel(),
        @Siren4JSubEntity(
                uri = "/api/templates",
                rel = ["collection", "search"],
                embeddedLink = true
        )
        val templates: ChecklistTemplateCollectionOutputModel = ChecklistTemplateCollectionOutputModel()
        ) : BaseResource()






