package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import io.swagger.annotations.ApiOperation
import isel.leic.daw.checklistsAPI.inputModel.single.UserInputModel
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import com.google.code.siren4j.component.Entity

@RestController
@RequestMapping("/users",produces = [Siren4J.JSON_MEDIATYPE])
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    fun getUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return User(username = auth.name)
    }

    @ApiOperation(value = "Returns a Specific User")
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String) : ResponseEntity<Entity> {
        userRepository.findById(userId).get()
    }

    @ApiOperation(value = "Creates a New User")
    @PostMapping("/register")
    fun registerUser(@RequestBody input: UserInputModel) : ResponseEntity<Entity>{
        val user = User(
                username = input.username,
                familyName = input.familyName,
                givenName = input.givenName,
                email = input.email,
                password = input.password
        )
        return userRepository.save(user)
    }

    @ApiOperation(value = "Updates Specific User")
    @PutMapping
    fun updateUser(@RequestBody input: UserInputModel) : ResponseEntity<Entity>{
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

    @ApiOperation(value = "Deletes Specific User")
    @DeleteMapping
    fun deleteUser(@PathVariable username:String) : String{
        if(username != getUser().username) throw AccessDeniedException("Forbidden")
         userRepository.deleteById(username)
        return "Removed user"
    }


}