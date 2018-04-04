package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistOutputModel

@Siren4JEntity(name = "checklists")
class ChecklistCollectionOutputModel(checklists: Collection<ChecklistOutputModel>)
    : CollectionResource<ChecklistOutputModel>() {
    init {
        this.addAll(checklists)
    }
}