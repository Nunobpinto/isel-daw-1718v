package isel.leic.daw.checklistsAPI.inputModel.single

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class ChecklistInputModel(
        @JsonProperty("name")
        val checklistName: String,
        @JsonProperty("description")
        val checklistDescription: String,
        @JsonProperty("completion_date")
        val completionDate: LocalDate,
        @JsonProperty("id")
        internal val checklistId: Long
)