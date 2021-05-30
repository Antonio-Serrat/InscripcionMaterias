package inscripcionMaterias.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import inscripcionMaterias.Enum.Rol;
import inscripcionMaterias.entities.Account;
import inscripcionMaterias.entities.Atmin;
import inscripcionMaterias.entities.Role;
import inscripcionMaterias.entities.Student;
import inscripcionMaterias.repository.AccountRepository;
import inscripcionMaterias.repository.AtminRepository;
import inscripcionMaterias.repository.RolesRepository;
import inscripcionMaterias.repository.StudentRepository;

@Controller
@RequestMapping(value = "/api/admin")
public class AdminController {

	@Autowired
	PasswordEncoder bcrypt;
	@Autowired
	private StudentRepository repoStudent;
	@Autowired
	private AccountRepository repoAccount;
	@Autowired
	private AtminRepository repoAdmin;

	@Autowired
	private RolesRepository repoRol;

	@GetMapping(value = "/homeAd")
	public String home(Model model) {

		return "/homeAd";
	}

//***********************************	STUDENT	********************************************************

	@RequestMapping(value = "/studentForm")
	public String studentForm(Model model) {
		return "studentForm";
	}

	@RequestMapping(value = "/newStudent", method = { RequestMethod.PUT, RequestMethod.POST })
	public String newStudent(@ModelAttribute Student stud, @RequestParam(value = "surname") String surname,
			@RequestParam(value = "dni") String dni, @RequestParam(value = "file") String file,
			@RequestParam(value = "name") String name, Model model) {

		String pass = bcrypt.encode(dni);

		Role rol = new Role();
		rol.setRol(Rol.STUDENT);
		Account acc = new Account();
		acc.setUsername(file);
		acc.setPassword(pass);
		acc.setRol(rol);
		rol.setAc(acc);

		stud.setName(name);
		stud.setFile(file);
		stud.setSurname(surname);
		stud.setDni(dni);

		stud.setAcc(acc);
		acc.setStudent(stud);

		repoStudent.save(stud);
		repoAccount.save(acc);
		repoRol.save(rol);

		model.addAttribute("accountSt", acc);
		model.addAttribute("student", stud);
		model.addAttribute("Rol", rol);

		return "redirect:/api/admin/students";
	}

	@RequestMapping(value = "/student", method = { RequestMethod.POST })
	public String student(@RequestParam(value = "id") Long id, @ModelAttribute Student stud, Model model) {

		stud = repoStudent.findByid(id);

		model.addAttribute("student", stud);

		return "editStud";

	}

	@RequestMapping(value = "/editStud/{id}", method = { RequestMethod.POST })
	public String editStud(@ModelAttribute Student stud, @PathVariable(value = "id") Long id,
			@RequestParam(value = "name") String name, @RequestParam(value = "file") String file,
			@RequestParam(value = "dni") String dni, @RequestParam(value = "surname") String surname, Model model)
			throws ParseException {

		stud = repoStudent.findByid(id);

		stud.setName(name);
		stud.setSurname(surname);
		stud.setDni(dni);
		stud.setFile(file);

		repoStudent.save(stud);

		model.addAttribute("student", stud);

		return "redirect:/api/admin/students";

	}

	@RequestMapping(value = "/deleteStud/{id}", method = { RequestMethod.DELETE, RequestMethod.POST })
	public String deleteStud(@ModelAttribute Student stud, @ModelAttribute Account acc,
			@PathVariable(value = "id") Long id, Model model) {

		stud = repoStudent.findByid(id);
		acc = stud.getAcc();

		repoAccount.delete(acc);
		return "redirect:/api/admin/students";

	}

	@RequestMapping(value = "/students", method = RequestMethod.GET)
	public String students(@ModelAttribute Atmin admin, Model model) {

		repoStudent.findAll();

		model.addAttribute("students", repoStudent.findAll());

		return "students";
	}
}
