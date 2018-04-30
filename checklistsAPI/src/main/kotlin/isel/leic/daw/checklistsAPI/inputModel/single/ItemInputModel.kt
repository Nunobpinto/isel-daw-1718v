package isel.leic.daw.checklistsAPI.inputModel.single

import com.fasterxml.jackson.annotation.JsonProperty

class ItemInputModel (
        @JsonProperty("name")
        val itemName: String,
        @JsonProperty("description")
        val itemDescription: String,
        @JsonProperty("state")
        val itemState: String,
        @JsonProperty("id")
        var itemId: Long = -1
)