package isel.leic.daw.checklistsAPI.inputModel.single

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class ChecklistTemplateInputModel (
    @JsonProperty("name")
    val checklistTemplateName: String,
    @JsonProperty("description")
    val checklistTemplateDescription: String,
    @JsonProperty("id")
    val checklistTemplateId: Long


)