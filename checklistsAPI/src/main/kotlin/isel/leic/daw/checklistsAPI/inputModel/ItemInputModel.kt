package isel.leic.daw.checklistsAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ItemInputModel (
        @JsonProperty("name")
        val itemName: String,
        @JsonProperty("description")
        val itemDescription: String,
        @JsonProperty("state")
        val itemState: String
)