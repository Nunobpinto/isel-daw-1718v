package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(name="item_template")
data class ItemTemplateOutputModel(
    val name:String,
    val description:String
) : BaseResource()
