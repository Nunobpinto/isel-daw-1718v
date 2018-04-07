package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.resource.BaseResource
import isel.leic.daw.checklistsAPI.model.State
import isel.leic.daw.checklistsAPI.outputModel.collection.ItemTemplateCollectionOutputModel

@Siren4JEntity(
        name = "item_template",
        suppressClassProperty = true,
        uri = "/api/templates/{templateId}/items/{itemTemplateId}",
        links = [
            Siren4JLink(rel = ["next"], href = "/api/templates/{templateId}/items/{itemTemplateId}"),
            Siren4JLink(rel = ["previous"], href = "/api/templates/{templateId}/items/{itemTemplateId}")
        ],
        actions = [
            Siren4JAction(
                    name = "delete-item-template",
                    title = "Delete Item Template",
                    method = ActionImpl.Method.DELETE,
                    href = "/api/templates/{templateId}/items/{itemTemplateId}"
            ),
            Siren4JAction(
                    name = "update-item-template",
                    title = "Update Item Template",
                    method = ActionImpl.Method.PUT,
                    href = "/api/templates/{templateId}/items/{itemTemplateId}",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true),
                        Siren4JActionField(name = "state", type = "text", required = true),
                        Siren4JActionField(name = "id", type = "hidden", required = true)
                    ]
            )
        ]
)
data class ItemTemplateOutputModel(
        val name: String = "",
        val description: String = "",
        val state: String = State.Uncompleted.toString(),
        val itemTemplateId: Long = 0,
        val templateId: Long = 0,
        @Siren4JSubEntity(
                uri = "/api/templates/{parent.templateId}/items",
                rel = ["collection"],
                embeddedLink = true
        )
        val itemTemplates: ItemTemplateCollectionOutputModel = ItemTemplateCollectionOutputModel(),
        @Siren4JSubEntity(
                uri = "/api/templates/{parent.templateId}",
                rel = ["about", "collection"],
                embeddedLink = true
        )
        val template: ChecklistTemplateOutputModel = ChecklistTemplateOutputModel()
) : BaseResource()
