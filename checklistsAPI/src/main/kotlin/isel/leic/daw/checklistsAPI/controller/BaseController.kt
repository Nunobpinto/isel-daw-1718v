package isel.leic.daw.checklistsAPI.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMethod

@RestController
@RequestMapping("/")
class BaseController {

    @RequestMapping(method = [(RequestMethod.OPTIONS)], value = ["/*"])
    @ResponseBody
    fun handleOptions(): ResponseEntity<Any> = ResponseEntity(HttpStatus.NO_CONTENT)
}