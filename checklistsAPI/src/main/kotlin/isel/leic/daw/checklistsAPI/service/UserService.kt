package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Service
class UserService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findById(username).get()
        val bCryptPasswordEncoder = BCryptPasswordEncoder()

        return org.springframework.security.core.userdetails
                .User
                .withUsername(user.username)
                .password(bCryptPasswordEncoder.encode(user.password))
                .roles("USER")
                .build()
    }
}