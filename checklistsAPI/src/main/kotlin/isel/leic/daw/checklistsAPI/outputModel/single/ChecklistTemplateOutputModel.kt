package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JSubEntity;
import com.google.code.siren4j.resource.BaseResource
import com.google.code.siren4j.resource.CollectionResource

@Siren4JEntity(name = "checklist_template")
data class ChecklistTemplateOutputModel(
        val identifier : Int,
        val name: String,
        val description: String,
        @Siren4JSubEntity(rel = ["item_templates"], embeddedLink = true)
        val itemTemplates : CollectionResource<ItemTemplateOutputModel>
):BaseResource()
