package inscripcionMaterias.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import inscripcionMaterias.repository.SubjectRepository;
import inscripcionMaterias.repository.TeacherRepository;

@Controller
@RequestMapping(value = "/api/admin")
public class AdminController {

	@Autowired
	PasswordEncoder bcrypt;
	@Autowired
	private SubjectRepository repoSubject;
	@Autowired
	private StudentRepository repoStudent;
	@Autowired
	private TeacherRepository repoTeach;
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

//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//		User user = (User) auth.getPrincipal();
//		acc = repoAccount.findByusername(user.getUsername());
//		admin = acc.getAtmin();
//
//		repoAdmin.save(admin);
//		model.addAttribute("admin", admin);

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

		model.addAttribute("accountSt", acc);
		model.addAttribute("student", stud);

		return "redirect:/api/admin/students";
	}

	@RequestMapping(value = "/student", method = { RequestMethod.POST, RequestMethod.PUT })
	public String student(@RequestParam(value = "id") Long id, Model model) {

		Student stud = repoStudent.findById(id).get();

		model.addAttribute("student", stud);

		return "editStud";

	}

	@RequestMapping(value = "/editStud/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public String editStud(@ModelAttribute Student stud, @ModelAttribute Account acc,
			@PathVariable(value = "id") Long id, @RequestParam(value = "name") String name,
			@RequestParam(value = "file") String file, @RequestParam(value = "dni") String dni,
			@RequestParam(value = "surname") String surname, Model model) throws ParseException {

		stud = repoStudent.findByid(id);
		acc = stud.getAcc();
//		stud = acc.getStudent();

		acc.getStudent().setName(name);
		acc.getStudent().setSurname(surname);
		acc.getStudent().setDni(dni);
		acc.getStudent().setFile(file);
		acc.setStudent(stud);

//		repoAccount.save(acc);
		repoStudent.save(stud);

		model.addAttribute("account", acc);
		model.addAttribute("student", stud);

		return "redirect:/api/admin/students";

	}

	@PostMapping(value = "/deleteStud/{id}")
	public String deleteStud(@ModelAttribute Student stud, @ModelAttribute Atmin admin,
			@PathVariable(value = "id") Long id, Model model) {

		stud = repoStudent.findByid(id);
		repoStudent.delete(stud);

		return "redirect:/api/admin/students";

	}

	@RequestMapping(value = "/students", method = RequestMethod.GET)
	public String students(@ModelAttribute Atmin admin, Model model) {

		repoStudent.findAll();

		model.addAttribute("students", repoStudent.findAll());

		return "students";
	}
}
