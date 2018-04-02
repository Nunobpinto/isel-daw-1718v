package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, String>