package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "app_user")
data class User(
        @Id
        @ApiModelProperty("Username that identifies the User")
        @Column(name = "username")
        val username: String = "",

        @ApiModelProperty("Users Family Name")
        @Column(name = "family_name")
        val familyName: String = "",

        @ApiModelProperty("Users Given Name")
        @Column(name = "given_name")
        val givenName: String = "",

        @ApiModelProperty("Users Email")
        @Column(name = "email")
        val email: String = "",

        @ApiModelProperty("Users Password")
        @Column(name = "password")
        val password: String = "",

        @JsonIgnore
        @ApiModelProperty("Users Templates")
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklistTemplates: MutableSet<ChecklistTemplate> = mutableSetOf(),

        @JsonIgnore
        @ApiModelProperty("Users Checklists")
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklists: MutableSet<Checklist> = mutableSetOf()
) : Serializable
