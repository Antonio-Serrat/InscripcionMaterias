package inscripcionMaterias.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import inscripcionMaterias.entities.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

	UserDetails findAllByUsername(String username);

	public Optional<Account> findByUsername(String username);

	public Account findByusername(String username);
}
