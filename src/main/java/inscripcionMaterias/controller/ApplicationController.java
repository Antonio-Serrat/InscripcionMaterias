package inscripcionMaterias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import inscripcionMaterias.Enum.Rol;
import inscripcionMaterias.entities.Account;
import inscripcionMaterias.entities.Atmin;
import inscripcionMaterias.entities.Role;
import inscripcionMaterias.repository.AccountRepository;
import inscripcionMaterias.repository.AtminRepository;
import inscripcionMaterias.repository.RolesRepository;
import inscripcionMaterias.repository.StudentRepository;
import inscripcionMaterias.repository.SubjectRepository;
import inscripcionMaterias.repository.TeacherRepository;

@Controller

@RequestMapping(value = "/api")
public class ApplicationController {

	// private final AccountService userService;

	@Autowired
	PasswordEncoder bcrypt;
	@Autowired
	private SubjectRepository repoSub;
	@Autowired
	private StudentRepository repoStud;
	@Autowired
	private TeacherRepository repoTheach;
	@Autowired
	private AccountRepository repoAc;
	@Autowired
	private AtminRepository repoAd;
	@Autowired
	private RolesRepository repoRol;

	@GetMapping(value = "/")
	public String Welcome(Model model) {

		return "index";
	}

	@RequestMapping(value = "/registerNew")
	public String register(Model model) {

		String pass = bcrypt.encode("password");

		Atmin ad = new Atmin();
		Account acc = new Account();
		Role role = new Role();
		role.setRol(Rol.ADMIN);
		acc.setUsername("root@go");
		acc.setPassword(pass);
		acc.setRol(role);

		role.setAc(acc);
		acc.setAdmin(ad);
		ad.setAcc(acc);
		repoAd.save(ad);
		repoAc.save(acc);
		repoRol.save(role);

		model.addAttribute("account", acc);
		model.addAttribute("admin", ad);
		model.addAttribute("role", role);

		return "/index";
	}

	@RequestMapping(value = "/delete")
	public String delete(Model model) {

		repoAc.deleteAll();
		repoAd.deleteAll();

		return "/index";
	}
}
