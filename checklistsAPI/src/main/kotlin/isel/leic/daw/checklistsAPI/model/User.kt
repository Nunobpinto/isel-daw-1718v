package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "app_user")
data class User(
        @Id
        @ApiModelProperty("User Subject that identifies the User")
        @Column(name = "sub")
        val sub: String = "",

        @JsonIgnore
        @ApiModelProperty("Users Templates")
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklistTemplates: MutableSet<ChecklistTemplate> = mutableSetOf(),

        @JsonIgnore
        @ApiModelProperty("Users Checklists")
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklists: MutableSet<Checklist> = mutableSetOf()
) : Serializable
