package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ItemTemplateOutputModel

@Siren4JEntity(name="items")
data class ItemTemplateCollectionOutputModel(
        @Siren4JPropertyIgnore
        val identifier : Int,
        @Siren4JPropertyIgnore
        val name : String,
        @Siren4JPropertyIgnore
        val description : String
): CollectionResource<ItemTemplateOutputModel>(){
    constructor(identifier : Int,name : String,description : String,itemTemplates : Collection<ItemTemplateOutputModel>)
            :this(identifier=identifier,name = name,description = description){
        this.addAll(itemTemplates)
    }
}