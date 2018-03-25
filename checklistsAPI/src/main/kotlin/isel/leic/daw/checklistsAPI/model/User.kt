package isel.leic.daw.checklistsAPI.model

import javax.persistence.*

@Entity
@Table(name = "app_user" )
class User (
    @Column(name = "username")
    val  username : String,

    @Column(name = "family_name")
    val familyName: String,

    @Column(name = "given_name")
    val givenName : String,

    @Column(name = "email")
    val email : String) {

    private constructor() : this("", "", "", "")
}