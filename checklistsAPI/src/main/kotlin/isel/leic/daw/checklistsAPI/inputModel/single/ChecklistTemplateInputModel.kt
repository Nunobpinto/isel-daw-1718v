package isel.leic.daw.checklistsAPI.inputModel.single

import com.fasterxml.jackson.annotation.JsonProperty

class ChecklistTemplateInputModel (
    @JsonProperty("name")
    val checklistTemplateName: String,
    @JsonProperty("description")
    val checklistTemplateDescription: String,
    @JsonProperty("id")
    var checklistTemplateId: Long = -1
)