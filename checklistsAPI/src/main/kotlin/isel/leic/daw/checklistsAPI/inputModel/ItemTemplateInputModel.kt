package isel.leic.daw.checklistsAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ItemTemplateInputModel (
        @JsonProperty("name")
        val itemTemplateName: String,
        @JsonProperty("description")
        val itemTemplateDescription: String,
        @JsonProperty("state")
        val itemTemplateState: String
)