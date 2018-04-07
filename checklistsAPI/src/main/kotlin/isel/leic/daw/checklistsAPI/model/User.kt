package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "app_user")
data class User(
        @Id
        @Column(name = "username")
        val username: String = "",

        @Column(name = "family_name")
        val familyName: String = "",

        @Column(name = "given_name")
        val givenName: String = "",

        @Column(name = "email")
        val email: String = "",

        @Column(name = "password")
        val password: String = "",

        @JsonIgnore
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklistTemplates: MutableSet<ChecklistTemplate> = mutableSetOf(),

        @JsonIgnore
        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        val checklists: MutableSet<Checklist> = mutableSetOf()
) : Serializable
