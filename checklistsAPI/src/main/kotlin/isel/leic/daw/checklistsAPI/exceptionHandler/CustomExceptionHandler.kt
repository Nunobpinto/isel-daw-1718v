package isel.leic.daw.checklistsAPI.exceptionHandler

import isel.leic.daw.checklistsAPI.exceptions.BadRequestException
import isel.leic.daw.checklistsAPI.exceptions.NotFoundException
import isel.leic.daw.checklistsAPI.exceptions.UnauthenticatedException
import isel.leic.daw.checklistsAPI.outputModel.error.ErrorOutputModel
import org.hibernate.HibernateException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(NoSuchElementException::class)])
    fun handleNoSuchElementException(
            ex:Exception,
            request:WebRequest
    ) : ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                    ErrorOutputModel(
                            title = "Not Found",
                            detail = "Could not find the resource you wanted",
                            status = 404
                    ),
                httpHeaders,
                HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(value = [(AccessDeniedException::class)])
    fun handleAccessDeniedException(
            ex:Exception,
            request:WebRequest
    ) : ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Forbidden",
                        detail = ex.message ?: "No permission granted to access the resource",
                        status = 403
                ),
                httpHeaders,
                HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [(HibernateException::class)])
    fun handleHibernateException(
            ex:Exception,
            request:WebRequest
    ) : ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Internal Server Error",
                        detail = "Error while processing the request",
                        status = 500
                ),
                httpHeaders,
                HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [(NotFoundException::class)])
    fun handleNotFoundException(
            ex:Exception,
            request:WebRequest
    ) : ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Not Found",
                        detail = "Could not find the resource you wanted",
                        status = 404
                ),
                httpHeaders,
                HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(BadRequestException::class)])
    fun handleBadRequestException(
            ex:Exception,
            request:WebRequest
    ) : ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Invalid Syntax",
                        detail = "Server could not understand the request",
                        status = 400
                ),
                httpHeaders,
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(UnauthenticatedException::class)])
    fun handleUnauthenticatedException(
            ex:Exception,
            request:WebRequest
    ) : ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Not Authenticated",
                        detail = "Authentication Required",
                        status = 401
                ),
                httpHeaders,
                HttpStatus.UNAUTHORIZED)
    }
}