package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.component.impl.ActionImpl.*
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ItemTemplateOutputModel

@Siren4JEntity(
        name = "item-templates",
        suppressClassProperty = true,
        uri = "/templates/{templateId}/items",
        links = [],
        actions = [
            Siren4JAction(
                    name = "add-item-template",
                    title = "Add Item to Template",
                    method = Method.POST,
                    href = "/templates/{templateId}/items",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true),
                        Siren4JActionField(name = "state", type = "text", required = true, options = [
                            Siren4JFieldOption(value = "Uncompleted", optionDefault = true),
                            Siren4JFieldOption(value = "Completed")
                        ])
                    ]
            ),
            Siren4JAction(
                    name = "delete-all-item-templates",
                    title = "Delete All Items of Template",
                    method = Method.DELETE,
                    href = "/templates/{templateId}/items"
            ),
            Siren4JAction(
                    name = "bulk-update-item-templates",
                    title = "Bulk Update Items of Template",
                    method = Method.PUT,
                    href = "/templates/{templateId}/items",
                    type = "application/json",
                    fields = [
                        Siren4JActionField(name = "name", type = "text", required = true),
                        Siren4JActionField(name = "description", type = "text", required = true),
                        Siren4JActionField(name = "state", type = "text", required = true, options = [
                            Siren4JFieldOption(value = "Uncompleted", optionDefault = true),
                            Siren4JFieldOption(value = "Completed")
                        ]),
                        Siren4JActionField(name = "id", type = "hidden", required = true)
                    ]
            )
        ]
)
class ItemTemplateCollectionOutputModel(
        val templateId: Long = 0,
        itemTemplates: Collection<ItemTemplateOutputModel> = CollectionResource(),
        offset : String = "0",
        limit : String = "0"
) : CollectionResource<ItemTemplateOutputModel>() {
    init {
        this.addAll(itemTemplates)
        this.offset = offset.toLong()
        this.limit = limit.toLong()
    }
}