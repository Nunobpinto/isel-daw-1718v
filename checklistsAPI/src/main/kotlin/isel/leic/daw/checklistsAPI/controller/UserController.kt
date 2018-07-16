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
import isel.leic.daw.checklistsAPI.configuration.security.UserInfo
import isel.leic.daw.checklistsAPI.exceptions.NotFoundException
import isel.leic.daw.checklistsAPI.exceptions.UnauthenticatedException
import isel.leic.daw.checklistsAPI.mappers.InputMapper
import isel.leic.daw.checklistsAPI.mappers.OutputMapper
import isel.leic.daw.checklistsAPI.service.UserService
import org.aspectj.weaver.ast.Not
import java.security.Principal

@RestController
@RequestMapping("/api/users", produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Operations pertaining to Users")
class UserController {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var inputMapper: InputMapper
    @Autowired
    lateinit var outputMapper: OutputMapper
    @Autowired
    lateinit var userInfo: UserInfo

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
        val user = userService.getUser(username).orElseThrow{ NotFoundException() }
        val output = outputMapper.toUserOutput(user)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @PostMapping()
    fun registerUser(): ResponseEntity<Entity> {
        val user = userService.getUser(userInfo.sub!!).orElseGet {
            userService.saveUser(User(sub=userInfo.sub!!))
        }
        val output = outputMapper.toUserOutput(user)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }
}