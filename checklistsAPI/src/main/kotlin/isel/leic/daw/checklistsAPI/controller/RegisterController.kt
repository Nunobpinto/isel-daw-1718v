package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import io.swagger.annotations.*
import isel.leic.daw.checklistsAPI.exceptions.ConflictException
import isel.leic.daw.checklistsAPI.inputModel.single.UserInputModel
import isel.leic.daw.checklistsAPI.mappers.InputMapper
import isel.leic.daw.checklistsAPI.mappers.OutputMapper
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/register", produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Creating a new User")
class RegisterController {

    @Autowired
    lateinit var userService: UserService

    val inputMapper: InputMapper = InputMapper()
    val outputMapper: OutputMapper = OutputMapper()

    @ApiOperation(value = "Creates a New User")
    @ApiResponses(
            ApiResponse(code = 201, message = "User created successfully"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 409, message = "User already exists")
    )
    @PostMapping
    fun registerUser(
            @ApiParam(value = "Input that represents the User to be created")
            @RequestBody input: UserInputModel
    ): User {
        val user = inputMapper.toUser(input= input)
        try{
            userService.getUser(user.username)
        }catch (e:NoSuchElementException){
            return userService.saveUser(user)
        }
        throw ConflictException()
    }
}