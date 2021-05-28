package inscripcionMaterias.repository;

import org.springframework.data.repository.CrudRepository;

import inscripcionMaterias.entities.Teacher;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {

	public Teacher findByDni(Long dni);
}