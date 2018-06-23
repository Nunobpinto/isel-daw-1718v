package isel.leic.daw.checklistsAPI.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
class UnauthenticatedException(val messsage: String) : RuntimeException(messsage)