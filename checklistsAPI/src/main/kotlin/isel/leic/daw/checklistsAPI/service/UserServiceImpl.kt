package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun getUser(username: String) =
            userRepository.findById(username).get()

    override fun saveUser(user: User) =
            userRepository.save(user)

    override fun deleteUser(username: String) =
            userRepository.deleteById(username)

}