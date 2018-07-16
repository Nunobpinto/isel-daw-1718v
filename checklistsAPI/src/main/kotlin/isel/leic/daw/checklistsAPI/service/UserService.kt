package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.User
import java.util.*

interface UserService {

    fun getUser(username: String): Optional<User>

    fun saveUser(user: User): User

    fun deleteUser(username: String)

}