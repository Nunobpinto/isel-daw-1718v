package isel.leic.daw.checklistsAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistTemplateOutputModel

@Siren4JEntity(name = "checklist_templates")
class ChecklistTemplateCollectionOutputModel(checklistTemplates: Collection<ChecklistTemplateOutputModel>)
    : CollectionResource<ChecklistTemplateOutputModel>(){
    init {
        this.addAll(checklistTemplates)
    }

}