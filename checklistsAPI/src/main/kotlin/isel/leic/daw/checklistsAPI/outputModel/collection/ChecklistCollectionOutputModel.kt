package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JAction
import com.google.code.siren4j.annotations.Siren4JActionField
import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.component.impl.ActionImpl.*
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistOutputModel

@Siren4JEntity(
        name = "checklists",
        suppressClassProperty = true,
        uri = "/api/checklists",
        links = [],
        actions = [
            Siren4JAction(
                    name = "create-checklist",
                    title = "Create Checklist",
                    method = Method.POST,
                    href = "/api/checklists",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "completion_date", type = "date", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true)
                    ]
            ),
            Siren4JAction(
                    name = "delete-all-checklists",
                    title = "Delete All Checklists",
                    method = Method.DELETE,
                    href = "/api/checklists"
            ),
            Siren4JAction(
                    name = "bulk-update-checklists",
                    title = "Bulk Update Checklists",
                    method = Method.PUT,
                    href = "/api/checklists",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "completion_date", type = "date", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true),
                        Siren4JActionField(name = "id", type = "hidden", required = true)
                    ]
            )
        ]
)
class ChecklistCollectionOutputModel(
        checklists: Collection<ChecklistOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<ChecklistOutputModel>() {
    init {
        addAll(checklists)
        this.offset = offset.toLong()
        this.limit = limit.toLong()

    }
}