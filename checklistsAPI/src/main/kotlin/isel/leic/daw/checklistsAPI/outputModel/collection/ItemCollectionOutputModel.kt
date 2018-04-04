package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel

@Siren4JEntity(name="items")
class ItemCollectionOutputModel(items: Collection<ItemOutputModel>):CollectionResource<ItemOutputModel>(){
   init {
       this.addAll(items)
   }
}
