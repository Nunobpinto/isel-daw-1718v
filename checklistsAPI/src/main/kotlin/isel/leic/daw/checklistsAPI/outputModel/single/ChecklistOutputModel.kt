package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.resource.BaseResource
import isel.leic.daw.checklistsAPI.outputModel.collection.ItemCollectionOutputModel
import java.time.LocalDate

@Siren4JEntity(
        name = "checklist",
        suppressClassProperty = true,
        uri = "/api/checklists/{checklistId}",
        links = [],
        actions = [
            Siren4JAction(
                    name = "delete-checklist",
                    title = "Delete Checklist",
                    method = ActionImpl.Method.DELETE,
                    href = "/api/checklists/{checklistId}"
            ),
            Siren4JAction(
                    name = "update-checklist",
                    title = "Update Checklist",
                    method = ActionImpl.Method.PUT,
                    href = "/api/checklists/{checklistId}",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true),
                        Siren4JActionField(name = "completion_date", type = "date", required = true),
                        Siren4JActionField(name = "id", type = "hidden", required = true)
                    ]
            )
        ]
)
data class ChecklistOutputModel(
        val checklistId: Long = 0,
        val name: String = "",
        val description: String = "",
        val completionDate: String = "",
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        @Siren4JSubEntity(
                uri = "/api/checklists/{parent.checklistId}/items",
                rel = ["collection"],
                embeddedLink = true
        )
        val items: ItemCollectionOutputModel = ItemCollectionOutputModel(),
        @Siren4JSubEntity(
                uri = "/users/{parent.username}",
                rel = ["author"],
                embeddedLink = true
        )
        val user: UserOutputModel = UserOutputModel()

) : BaseResource()
