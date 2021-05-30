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
import inscripcionMaterias.entities.Subject;
import inscripcionMaterias.repository.AccountRepository;
import inscripcionMaterias.repository.AtminRepository;
import inscripcionMaterias.repository.StudentRepository;
import inscripcionMaterias.repository.SubjectRepository;
import inscripcionMaterias.repository.TeacherRepository;

@Controller
@RequestMapping(value = "/api/admin/sub")
public class SubjectController {
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

	@RequestMapping(value = "/subjectForm")
	public String subjectForm(Account acc, Atmin admin, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) auth.getPrincipal();
		acc = repoAccount.findByusername(user.getUsername());
		admin = acc.getAdmin();

		repoAdmin.save(admin);
		model.addAttribute("admin", admin);

		return "subjectForm";
	}

	@RequestMapping(value = "/newSubject", method = { RequestMethod.PUT, RequestMethod.POST })
	public String newSubject(@ModelAttribute Atmin admin, @ModelAttribute Subject sub,
			@RequestParam(value = "time") String time, @RequestParam(value = "places") Long places,
			@RequestParam(value = "active") String active, @RequestParam(value = "name") String name, Model model) {

		sub.setName(name);
		sub.setPlaces(places);
		sub.setActive(active);
		sub.setTime(time);

		repoSubject.save(sub);

		model.addAttribute("subject", sub);

		return "redirect:/api/admin/sub/subjects";
	}

	@RequestMapping(value = "/subject", method = { RequestMethod.POST, RequestMethod.PUT })
	public String subject(@RequestParam(value = "id") Long id, Model model) {

		Subject sub = repoSubject.findById(id).get();

		model.addAttribute("subject", sub);

		return "editSub";

	}

	@RequestMapping(value = "/editSub/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public String editSub(@ModelAttribute Subject sub, @PathVariable(value = "id") Long id,
			@RequestParam(value = "name") String name, @RequestParam(value = "time") String time,
			@RequestParam(value = "places") Long places, @RequestParam(value = "active") String active, Model model)
			throws ParseException {

		sub = repoSubject.findById(id).get();

		sub.setName(name);
		sub.setTime(time);
		sub.setPlaces(places);
		sub.setActive(active);

		repoSubject.save(sub);
		model.addAttribute("subject", sub);

		return "redirect:/api/admin/sub/subjects";

	}

	@PostMapping(value = "/deleteSub/{id}")
	public String deleteSub(@ModelAttribute Subject sub, @PathVariable(value = "id") Long id, Model model) {

		sub = repoSubject.findByid(id);
		sub.getStudents().clear();
		repoSubject.deleteById(id);

		return "redirect:/api/admin/sub/subjects";

	}

	@RequestMapping(value = "/subjects", method = RequestMethod.GET)
	public String subjects(@ModelAttribute Atmin admin, Model model) {

		repoSubject.findAll();

		model.addAttribute("subjects", repoSubject.findAll());

		return "subjects";
	}

}
