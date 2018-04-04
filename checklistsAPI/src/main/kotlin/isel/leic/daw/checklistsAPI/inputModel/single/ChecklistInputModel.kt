package isel.leic.daw.checklistsAPI.inputModel.single

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.*

class ChecklistInputModel (
    @JsonProperty("name")
    val checklistName: String,
    @JsonProperty("completion_date")
    val completionDate: LocalDate
)