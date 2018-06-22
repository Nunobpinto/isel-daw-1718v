package isel.leic.daw.checklistsAPI.configuration.security

import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserConfig : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        try{
            userRepository.findById(username).get()
        } catch (ex: NoSuchElementException){
            userRepository.save(User(sub = username))
        }
        return org.springframework.security.core.userdetails
                .User
                .withUsername(username)
                .roles("USER")
                .build()
    }
}