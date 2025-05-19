package manager.ecommerce.security.principle;

import manager.ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import manager.ecommerce.repository.IUserRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService
{
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));


        return MyUserDetails.builder()
                .user(user)
                .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName().toString())).toList())
                .build();
    }
}
