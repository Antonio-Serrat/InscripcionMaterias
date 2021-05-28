package inscripcionMaterias.repository;

import org.springframework.data.repository.CrudRepository;

import inscripcionMaterias.entities.Subject;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

	public Subject findByid(Long id);
}
