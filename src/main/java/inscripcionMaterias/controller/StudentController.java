package inscripcionMaterias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import inscripcionMaterias.entities.Account;
import inscripcionMaterias.entities.Student;
import inscripcionMaterias.entities.Subject;
import inscripcionMaterias.repository.AccountRepository;
import inscripcionMaterias.repository.StudentRepository;
import inscripcionMaterias.repository.SubjectRepository;

@Controller
@RequestMapping(value = "/api/student")
public class StudentController {

	@Autowired
	private SubjectRepository repoSubject;
	@Autowired
	private StudentRepository repoStudent;
	@Autowired
	private AccountRepository repoAccount;

	@GetMapping(value = "/homeStud")
	public String home(Model model) {

		return "/homeStud";
	}

	@RequestMapping(value = "/mySubjects", method = RequestMethod.GET)
	public String mySubjects(@ModelAttribute Student stud, @ModelAttribute Account acc, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) auth.getPrincipal();
		acc = repoAccount.findByusername(user.getUsername());

		stud = acc.getStudent();

		stud.getMySubjects();

		model.addAttribute("mySubjects", stud.getMySubjects());

		return "/mySubjects";
	}

	@RequestMapping(value = "/subjects", method = RequestMethod.GET)
	public String subjects(Model model) {

		repoSubject.findAll();

		model.addAttribute("subjects", repoSubject.findAll());

		return "activeSubjects";
	}

	@RequestMapping(value = "/newSubject/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
	public String newSubjects(@ModelAttribute Account acc, @ModelAttribute Student stud, @ModelAttribute Subject sub,
			@PathVariable(value = "id") Long id, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = (User) auth.getPrincipal();
		acc = repoAccount.findByusername(user.getUsername());

		sub = repoSubject.findById(id).get();
		String time = sub.getTime();

		if (sub.getPlaces() <= 0) {
			sub.setActive("No");
			repoSubject.save(sub);
			model.addAttribute("subject", sub);
			model.addAttribute("You cannot register because there aren't more places", model);

			return "redirect:/api/student/subjects";
		}
		if (time.equals(stud.mySubjects.contains(time))) {
			model.addAttribute("You cannot register because you are register on a same time in other subject", model);

			return "redirect:/api/student/subjects";
		} else {
		}

		stud = acc.getStudent();
		stud.mySubjects.add(sub);

		sub.students.add(stud);
		sub.setPlaces(sub.getPlaces() - 1);

		repoSubject.save(sub);
		repoStudent.save(stud);

		model.addAttribute("mySubjects", sub);

		model.addAttribute("student", stud);

		return "redirect:/api/student/mySubjects";
	}

}
