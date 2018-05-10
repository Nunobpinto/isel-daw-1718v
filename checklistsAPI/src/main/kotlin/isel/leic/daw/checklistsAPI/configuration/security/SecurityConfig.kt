package isel.leic.daw.checklistsAPI.configuration.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.WebSecurity

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    companion object {
        val REALM: String = "ChecklistsApiRealm"
        private val PROTECTED_URI_PATTERN: String = "/api/**"
    }

    @Autowired
    lateinit var userConfig: UserConfig

    override fun configure(http: HttpSecurity) {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(PROTECTED_URI_PATTERN).hasRole("USER")
                .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val auth = DaoAuthenticationProvider()
        auth.setUserDetailsService(userConfig)
        auth.setPasswordEncoder(passwordEncoder())
        return auth
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun getBasicAuthEntryPoint(): AuthenticationEntryPoint? {
        return CustomBasicAuthenticationEntryPoint()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
                .antMatchers("/swagger-ui.html")
                .antMatchers( "/swagger-resources")
                .antMatchers( "/webjars/**")
                .antMatchers("/users/register/")
    }

}