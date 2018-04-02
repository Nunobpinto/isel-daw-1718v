package isel.leic.daw.checklistsAPI.controller

import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String) = userRepository.findById(userId)

    @PostMapping("/register")
    fun registerUser(user: User) = userRepository.save(user)
}