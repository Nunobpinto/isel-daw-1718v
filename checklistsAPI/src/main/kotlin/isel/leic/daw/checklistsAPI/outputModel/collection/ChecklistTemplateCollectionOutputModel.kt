package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistTemplateOutputModel

@Siren4JEntity(name = "checklist_templates")
data class ChecklistTemplateCollectionOutputModel(
        @Siren4JPropertyIgnore
        private val identifier : Int,
        @Siren4JPropertyIgnore
        val name : String,
        @Siren4JPropertyIgnore
        private val description : String
): CollectionResource<ChecklistTemplateOutputModel>(){
    constructor(identifier : Int,name : String,description : String,checklistTemplates : Collection<ChecklistTemplateOutputModel>)
            :this(identifier=identifier,name = name,description = description){
        this.addAll(checklistTemplates)
    }
}