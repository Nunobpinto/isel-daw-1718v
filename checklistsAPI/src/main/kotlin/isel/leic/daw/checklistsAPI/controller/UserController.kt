package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import isel.leic.daw.checklistsAPI.inputModel.single.UserInputModel
import isel.leic.daw.checklistsAPI.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.*
import isel.leic.daw.checklistsAPI.exceptions.UnauthenticatedException
import isel.leic.daw.checklistsAPI.mappers.InputMapper
import isel.leic.daw.checklistsAPI.mappers.OutputMapper
import isel.leic.daw.checklistsAPI.service.UserService
import java.security.Principal

@RestController
@RequestMapping("/api/users", produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Operations pertaining to Users")
class UserController {

    @Autowired
    lateinit var userService: UserService

    val inputMapper: InputMapper = InputMapper()
    val outputMapper: OutputMapper = OutputMapper()

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
        val user = userService.getUser(username)
        val output = outputMapper.toUserOutput(user)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
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
            @PathVariable username: String,
            @ApiParam(value = "Input that represents the User to be updated")
            @RequestBody input: UserInputModel,
            principal: Principal
    ): User {
        val currentUser = userService.getUser(input.username)
        if (currentUser.sub != principal.name) throw UnauthenticatedException("Forbidden")
        val user = inputMapper.toUser(input, currentUser.checklists, currentUser.checklistTemplates)
        return userService.saveUser(user)
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
            @PathVariable username: String,
            principal: Principal
    ) {
        if (username != principal.name) throw UnauthenticatedException("Forbidden")
        return userService.deleteUser(username)
    }

}