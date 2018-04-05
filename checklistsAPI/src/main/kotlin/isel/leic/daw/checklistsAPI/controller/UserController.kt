package isel.leic.daw.checklistsAPI.controller

import isel.leic.daw.checklistsAPI.inputModel.single.UserInputModel
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    fun getUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return User(username = auth.name)
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String) = userRepository.findById(userId)

    @PostMapping("/register")
    fun registerUser(@RequestBody input: UserInputModel) : User{
        val user = User(
                username = input.username,
                familyName = input.familyName,
                givenName = input.givenName,
                email = input.email,
                password = input.password
        )
        return userRepository.save(user)
    }

    //TODO check if is correct to throw AccessDenied in this case
    @PutMapping
    fun updateUser(@RequestBody input: UserInputModel) : User{
        val currentUser = userRepository.findById(input.username)
        if(currentUser.get().username != getUser().username) throw AccessDeniedException("Forbidden")
        val user = User(
                username = currentUser.get().username,
                email = input.email,
                givenName = input.givenName,
                familyName = input.familyName,
                password = input.password,
                checklists = currentUser.get().checklists,
                checklistTemplates = currentUser.get().checklistTemplates
        )
        return userRepository.save(user)
    }
}