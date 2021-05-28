package inscripcionMaterias.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
public class Atmin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@Cascade(CascadeType.ALL)
	private Account acc;

	private String username;

//	@OneToMany
//	@Cascade(CascadeType.ALL)
//	public List<Account> accounts = new ArrayList<>();
//
//	@OneToMany
//	@Cascade(CascadeType.ALL)
//	public List<Subject> subjects = new ArrayList<>();
//
//	@OneToMany
//	@Cascade(CascadeType.ALL)
//	public List<Student> students = new ArrayList<>();

	public Atmin(String username) {
		this.username = this.acc.getUsername();
	}

}
