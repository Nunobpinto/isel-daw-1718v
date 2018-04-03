package isel.leic.daw.checklistsAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class ChecklistTemplateModel (
    @JsonProperty("name")
    val checklistTemplateName: String,
    @JsonProperty("description")
    val checklistTemplateDescription: String
)