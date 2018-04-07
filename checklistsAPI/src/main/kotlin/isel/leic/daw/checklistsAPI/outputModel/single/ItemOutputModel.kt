package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.resource.BaseResource
import com.google.code.siren4j.component.impl.ActionImpl.Method
import isel.leic.daw.checklistsAPI.model.State
import isel.leic.daw.checklistsAPI.outputModel.collection.ItemCollectionOutputModel

@Siren4JEntity(
        name = "item",
        suppressClassProperty = true,
        uri = "/api/checklists/{checklistId}/items/{itemId}",
        links = [
                Siren4JLink(rel = ["next"], href = "/api/checklists/{checklistId}/items/{itemId}"),
                Siren4JLink(rel = ["previous"], href = "/api/checklists/{checklistId}/items/{itemId}")
        ],
        actions = [
                Siren4JAction(
                        name = "delete-item",
                        title = "Delete Item",
                        method = Method.DELETE,
                        href = "/api/checklists/{checklistId}/items/{itemId}"
                ),
                Siren4JAction(
                        name = "update-item",
                        title = "Update Item",
                        method = Method.PUT,
                        href = "/api/checklists/{checklistId}/items/{itemId}",
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
data class ItemOutputModel(
        val checklistId: Long = 0,
        val itemId: Long = 0,
        val name: String = "",
        val description: String = "",
        val state: String = State.Uncompleted.toString(),
        @Siren4JSubEntity(
                uri = "/api/checklists/{parent.checklistId}/items",
                rel = ["collection"],
                embeddedLink = true
        )
        val items: ItemCollectionOutputModel = ItemCollectionOutputModel(),
        @Siren4JSubEntity(
                uri = "/api/checklists/{parent.checklistId}",
                rel=["about", "collection"],
                embeddedLink = true
        )
        val checklist: ChecklistOutputModel = ChecklistOutputModel()
) : BaseResource()