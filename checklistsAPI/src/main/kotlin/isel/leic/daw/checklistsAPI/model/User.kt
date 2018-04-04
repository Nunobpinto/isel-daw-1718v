package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "app_user")
data class User(
        @Id
        @Column(name = "username")
        val username: String,

        @Column(name = "family_name")
        val familyName: String? = null,

        @Column(name = "given_name")
        val givenName: String? = null,

        @Column(name = "email")
        val email: String? = null,

        @Column(name = "password")
        val password: String? = null,

        @JsonIgnore
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklistTemplates: MutableSet<ChecklistTemplate>? = null,

        @JsonIgnore
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklists: MutableSet<Checklist>? = null
) : Serializable
