package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import isel.leic.daw.checklistsAPI.inputModel.single.UserInputModel
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.*
import isel.leic.daw.checklistsAPI.outputModel.single.UserOutputModel

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
    @ApiResponses(
            ApiResponse(code = 200, message = "User successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "User Not Found")
    )
    @GetMapping("/{username}")
    fun getUser(
            @ApiParam(value = "The username of the User", required = true)
            @PathVariable username: String
    ): ResponseEntity<Entity> {
        val user = userRepository.findById(username).get()
        val output = UserOutputModel(
                username = user.username,
                familyName = user.familyName,
                givenName = user.givenName,
                email = user.email
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a New User")
    @ApiResponses(
            ApiResponse(code = 201, message = "User created successfully"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 409, message = "User already exists")
    )
    @PostMapping("/register")
    fun registerUser(
            @ApiParam(value = "Input that represents the User to be created")
            @RequestBody input: UserInputModel
    ) : User {
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
    @ApiResponses(
            ApiResponse(code = 200, message = "User updated successfully"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "User Not Found")
    )
    @PutMapping("/{username}")
    fun updateUser(
            @ApiParam(value = "The username of the User to be updated")
            @PathVariable username : String,
            @ApiParam(value = "Input that represents the User to be updated")
            @RequestBody input: UserInputModel
    ) : User {
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
    @ApiResponses(
            ApiResponse(code = 200, message = "User deleted successfully"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "User Not Found")
    )
    @DeleteMapping("/{username}")
    fun deleteUser(
            @ApiParam(value = "The username of the User to be deleted")
            @PathVariable username:String
    ) {
        if(username != getUser().username) throw AccessDeniedException("Forbidden")
        return userRepository.deleteById(username)
    }

}