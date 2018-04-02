package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistOutputModel

@Siren4JEntity(name = "checklists")
data class ChecklistCollectionOutputModel(
        @Siren4JPropertyIgnore
        private val identifier : Int,
        @Siren4JPropertyIgnore
        val name : String,
        @Siren4JPropertyIgnore
        private val description : String,
        @Siren4JPropertyIgnore
        private val completionDate : String
): CollectionResource<ChecklistOutputModel>(){
    constructor(identifier : Int,name : String,description : String,completionDate : String,checklists : Collection<ChecklistOutputModel>)
            :this(identifier=identifier,name = name,description = description,completionDate = completionDate){
        this.addAll(checklists)
    }
}