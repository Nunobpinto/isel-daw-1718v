package isel.leic.daw.checklistsAPI.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "app_user" )
data class User (
    @Id
    @Column(name = "username")
    val  username : String,

    @OneToMany(mappedBy = "user")
    val  checklistTemplates:MutableSet<ChecklistTemplate>?=null,

    @OneToMany(mappedBy = "user")
    val checklists:MutableSet<Checklist>?=null,

    @Column(name = "family_name")
    val familyName: String ?= null,

    @Column(name = "given_name")
    val givenName : String ?= null,

    @Column(name = "email")
    val email : String ?= null

) : Serializable
