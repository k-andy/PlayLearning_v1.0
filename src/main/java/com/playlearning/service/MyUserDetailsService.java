package com.playlearning.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.playlearning.dao.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.playlearning.dao.UsersDao;
import com.playlearning.model.Role;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    //get user from the database, via Hibernate
    @Autowired
    private UsersDao usersDao;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        com.playlearning.model.User user = usersDao.findByUsername(username);
        Set<Role> roles = new HashSet<Role>();
        roles.add(user.getRolesByRoleId());
        List<GrantedAuthority> authorities =
                buildUserAuthority(roles);

        return buildUserForAuthentication(user, authorities);

    }

    // Converts com.playlearning.model.User user to
    // org.springframework.security.core.userdetails.User
    private User buildUserForAuthentication(com.playlearning.model.User user,
                                            List<GrantedAuthority> authorities) {
        return new User(user.getName(), user.getPassword(), true, true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<Role> roles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // Build user's authorities
        for (Role role : roles) {
            setAuths.add(new SimpleGrantedAuthority(role.getName()));
        }

        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);

        return result;
    }
}
