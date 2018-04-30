package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl
import com.google.code.siren4j.component.impl.ActionImpl.*
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel

@Siren4JEntity(
        name = "items",
        suppressClassProperty = true,
        uri = "/checklists/{checklistId}/items",
        links = [],
        actions = [
            Siren4JAction(
                    name = "add-item",
                    title = "Add Item to Checklist",
                    method = Method.POST,
                    href = "/checklists/{checklistId}/items",
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
                    name = "delete-all-items",
                    title = "Delete All Items of Checklist",
                    method = Method.DELETE,
                    href = "/checklists/{checklistId}/items"
            ),
            Siren4JAction(
                    name = "bulk-update-items",
                    title = "Bulk Update Items of Checklist",
                    method = Method.PUT,
                    href = "/checklists/{checklistId}/items",
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
class ItemCollectionOutputModel(
        val checklistId: Long = 0,
        items: Collection<ItemOutputModel> = CollectionResource(),
         limit : String = "0",
         offset:String ="0"
) : CollectionResource<ItemOutputModel>() {
    init {
        this.addAll(items)
        this.offset = offset.toLong()
        this.limit = limit.toLong()
    }
}
