package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.User

interface UserService {

    fun getUser(username: String): User

    fun saveUser(user: User): User

    fun deleteUser(username: String)

}