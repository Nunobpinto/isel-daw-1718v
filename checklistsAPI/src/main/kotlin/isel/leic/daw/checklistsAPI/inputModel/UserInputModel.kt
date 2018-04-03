package isel.leic.daw.checklistsAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class UserInputModel (
      val username: String,
      @JsonProperty("family_name")
      val familyName: String,
      val email: String,
      @JsonProperty("given_name")
      val givenName: String,
      val password: String
)