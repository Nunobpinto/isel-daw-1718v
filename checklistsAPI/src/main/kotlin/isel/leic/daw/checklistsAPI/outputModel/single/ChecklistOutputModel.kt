package isel.leic.daw.checklistsAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JSubEntity
import com.google.code.siren4j.resource.BaseResource
import com.google.code.siren4j.resource.CollectionResource

@Siren4JEntity(name = "checklist")
data class ChecklistOutputModel(
        val checklistId : Long = 0,
        val name : String = "",
        val description : String = "",
        val completionDate : String = ""
) : BaseResource()
