package inscripcionMaterias.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import inscripcionMaterias.entities.Account;
import inscripcionMaterias.entities.Role;
import inscripcionMaterias.repository.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountRepository repoAc;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> userDb = repoAc.findByUsername(username);

		if (userDb.isPresent()) {
			Account user = userDb.get();
			List<Role> permis = new ArrayList<>();
			Role permissions = user.getRol();
			permis.add(permissions);
			Set<String> roles = new HashSet<>();
			if (!CollectionUtils.isEmpty(permis)) {
				for (Role p : permis) {
					roles.add(p.getRol().name());
				}
			}
			return User.withUsername(username).roles(roles.toArray(new String[0])).password(user.getPassword()).build();
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");

		}
	}
}