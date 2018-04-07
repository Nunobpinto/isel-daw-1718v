package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import isel.leic.daw.checklistsAPI.inputModel.single.UserInputModel
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users",produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Operations pertaining to Users")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    fun getUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return User(username = auth.name)
    }

    @ApiOperation(value = "Returns a Specific User")
    @GetMapping("/{username}")
    fun getUser(
            @ApiParam(value = "The username of the User", required = true)
            @PathVariable username: String
    ) = userRepository.findById(username).get()

    @ApiOperation(value = "Creates a New User")
    @PostMapping("/register")
    fun registerUser(
            @ApiParam(value = "Input that represents the User to be created")
            @RequestBody input: UserInputModel
    ) : User{
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
    @PutMapping("/{username}")
    fun updateUser(
            @ApiParam(value = "The username of the User to be updated")
            @PathVariable username : String,
            @ApiParam(value = "Input that represents the User to be updated")
            @RequestBody input: UserInputModel
    ) : User{
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
    @DeleteMapping("/{username}")
    fun deleteUser(
            @ApiParam(value = "The username of the User to be deleted")
            @PathVariable username:String
    ) {
        if(username != getUser().username) throw AccessDeniedException("Forbidden")
        return userRepository.deleteById(username)
    }

}