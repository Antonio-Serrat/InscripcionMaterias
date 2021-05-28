package inscripcionMaterias.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import inscripcionMaterias.entities.Account;
import inscripcionMaterias.entities.Atmin;
import inscripcionMaterias.entities.Teacher;
import inscripcionMaterias.repository.AccountRepository;
import inscripcionMaterias.repository.AtminRepository;
import inscripcionMaterias.repository.StudentRepository;
import inscripcionMaterias.repository.SubjectRepository;
import inscripcionMaterias.repository.TeacherRepository;

@Controller
@RequestMapping(value = "/api/admin/teach")
public class TeacherController {

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

	@RequestMapping(value = "/teacherForm")
	public String teacherForm(Account acc, Atmin admin, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) auth.getPrincipal();
		acc = repoAccount.findByusername(user.getUsername());
		admin = acc.getAdmin();

		repoAdmin.save(admin);
		model.addAttribute("admin", admin);

		return "teacherForm";
	}

	@RequestMapping(value = "/newTeacher", method = { RequestMethod.PUT, RequestMethod.POST })
	public String newTeacher(@ModelAttribute Account acc, @ModelAttribute Atmin admin, @ModelAttribute Teacher teach,
			@RequestParam(value = "name") String name, @RequestParam(value = "surname") String surname,
			@RequestParam(value = "active") String active, @RequestParam(value = "dni") Long dni, Model model)
			throws ParseException {

		teach.setActive(active);
		teach.setDni(dni);
		teach.setName(name);
		teach.setSurname(surname);

		repoTeach.save(teach);

		model.addAttribute("teacher", teach);

		return "redirect:/api/admin/teach/teachers";
	}

	@PostMapping(value = "/deleteTeach/{dni}")
	public String deleteTeach(@ModelAttribute Teacher teach, @ModelAttribute Atmin admin,
			@PathVariable(value = "dni") Long dni, Model model) {

		teach = repoTeach.findByDni(dni);
		repoTeach.delete(teach);

		return "redirect:/api/admin/teach/teachers";

	}

	@RequestMapping(value = "/teacher", method = { RequestMethod.POST, RequestMethod.PUT })
	public String teach(@RequestParam(value = "id") Long id, Model model) {

		Teacher teach = repoTeach.findById(id).get();

		model.addAttribute("teacher", teach);

		return "editTeach";

	}

	@RequestMapping(value = "/editTeach/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public String editTeach(@ModelAttribute Teacher teach, @PathVariable(value = "id") Long id,
			@RequestParam(value = "name") String name, @RequestParam(value = "surname") String surname,
			@RequestParam(value = "dni") Long dni, @RequestParam(value = "active") String active, Model model)
			throws ParseException {

		teach = repoTeach.findById(id).get();

		teach.setName(name);
		teach.setSurname(surname);
		teach.setDni(dni);
		teach.setActive(active);

		repoTeach.save(teach);
		model.addAttribute("teacher", teach);

		return "redirect:/api/admin/teach/teachers";

	}

	@RequestMapping(value = "/teachers", method = RequestMethod.GET)
	public String teachers(@ModelAttribute Atmin admin, Model model) {

		repoTeach.findAll();

		model.addAttribute("teachers", repoTeach.findAll());

		return "teachers";
	}
}
