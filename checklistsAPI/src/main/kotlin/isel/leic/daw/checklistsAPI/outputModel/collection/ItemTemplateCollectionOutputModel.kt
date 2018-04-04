package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ItemTemplateOutputModel

@Siren4JEntity(name = "items")
class ItemTemplateCollectionOutputModel(itemTemplates: Collection<ItemTemplateOutputModel>)
    : CollectionResource<ItemTemplateOutputModel>() {
    init {
        this.addAll(itemTemplates)
    }
}