package com.wilderBackend.security.userDetails;

import com.wilderBackend.entity.MasterUser;
import com.wilderBackend.repository.MasterUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MasterUserRepository masterUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MasterUser user = masterUserRepository.findByEmailAndStatus(username, "ACTIVE")
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone no. : " + username));

        String role = masterUserRepository.findRoleSlugByUserId(user.getId());
        return UserDetailsImpl.build(user, role);

    }

}

