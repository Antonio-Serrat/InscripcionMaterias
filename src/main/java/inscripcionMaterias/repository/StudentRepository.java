package inscripcionMaterias.repository;

import org.springframework.data.repository.CrudRepository;

import inscripcionMaterias.entities.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {

	public Student findByid(Long id);

}
