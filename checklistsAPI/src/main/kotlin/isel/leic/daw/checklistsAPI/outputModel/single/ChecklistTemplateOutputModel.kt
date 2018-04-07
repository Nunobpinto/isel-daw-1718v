package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.resource.BaseResource
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.collection.ItemTemplateCollectionOutputModel

@Siren4JEntity(
        name = "checklist_template",
        suppressClassProperty = true,
        uri = "/api/templates/{templateId}",
        links = [],
        actions = [
            (Siren4JAction(
                    name = "delete-template",
                    title = "Delete Template",
                    method = ActionImpl.Method.DELETE,
                    href = "/api/templates/{templateId}"
            )),
            Siren4JAction(
                    name = "update-template",
                    title = "Update Template",
                    method = ActionImpl.Method.PUT,
                    href = "/api/templates/{templateId}",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true),
                        Siren4JActionField(name = "id", type = "hidden", required = true)
                    ]
            ),
            Siren4JAction(
                    name = "create-checklist",
                    title = "Create Checklist based on Template",
                    method = ActionImpl.Method.POST,
                    href = "/api/templates/{templateId}",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "completion_date", type = "date", required = true),
                        Siren4JActionField(name = "name", type = "text", required = false, value = "{name}"),
                        Siren4JActionField(name = "description", type = "text", required = true)
                    ]
            )
        ]
)
data class ChecklistTemplateOutputModel(
        val templateId: Long = 0,
        val name: String = "",
        val description: String = "",
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        @Siren4JSubEntity(
                uri = "/api/templates/{parent.templateId}/items",
                rel = ["collection"],
                embeddedLink = true
        )
        val items: ItemTemplateCollectionOutputModel = ItemTemplateCollectionOutputModel(),
        @Siren4JSubEntity(
                uri = "/users/{parent.username}",
                rel = ["author"],
                embeddedLink = true
        )
        val user: UserOutputModel = UserOutputModel()
) : BaseResource()
