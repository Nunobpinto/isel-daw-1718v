package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel

@Siren4JEntity(name="items")
data class ItemCollectionOutputModel(
        @Siren4JPropertyIgnore
        private val identifier:Int,
        @Siren4JPropertyIgnore
        val name:String,
        @Siren4JPropertyIgnore
        private val description:String,
        @Siren4JPropertyIgnore
        private val state:String
):CollectionResource<ItemOutputModel>(){
    constructor(identifier: Int,name: String,description: String,state: String,items : Collection<ItemOutputModel>)
        :this(identifier=identifier,name = name,description = description,state = state){
        this.addAll(items)
    }
}
