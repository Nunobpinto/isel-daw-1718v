package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JAction
import com.google.code.siren4j.annotations.Siren4JActionField
import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.component.impl.ActionImpl.*
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistTemplateOutputModel

@Siren4JEntity(
        name = "checklist_templates",
        suppressClassProperty = true,
        uri = "/api/templates",
        links = [],
        actions = [
            Siren4JAction(
                    name = "create-template",
                    title = "Create Template",
                    method = Method.POST,
                    href = "/api/templates",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true)
                    ]
            ),
            Siren4JAction(
                    name = "delete-all-templates",
                    title = "Delete All Templates",
                    method = Method.DELETE,
                    href = "/api/templates"
            ),
            Siren4JAction(
                    name = "bulk-update-template",
                    title = "Bulk Update Templates",
                    method = Method.PUT,
                    href = "/api/templates",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true),
                        Siren4JActionField(name = "id", type = "hidden", required = true)
                    ]
            )
        ]
)
class ChecklistTemplateCollectionOutputModel(
        checklistTemplates: Collection<ChecklistTemplateOutputModel> = CollectionResource()
) : CollectionResource<ChecklistTemplateOutputModel>() {
    init {
        this.addAll(checklistTemplates)
    }

}