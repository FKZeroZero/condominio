package app.condominio.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.condominio.domain.Usuario;
import app.condominio.service.UsuarioService;

@Controller
@RequestMapping("conta")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/cadastrar/sindico")
	public ModelAndView preCadastro(@ModelAttribute("usuario") Usuario usuario, ModelMap model) {
		model.addAttribute("conteudo", "cadastrarSindico");
		return new ModelAndView("site/layout", model);
	}

	@PostMapping("/cadastrar/sindico")
	public ModelAndView posCadastro(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult validacao,
			ModelMap model) {
		if (usuario.getUsername() != null && usuarioService.existe(usuario.getUsername())) {
			validacao.rejectValue("username", "Unique");
		}
		if (validacao.hasErrors()) {
			return preCadastro(usuario, model);
		}
		usuarioService.salvarSindico(usuario);
		model.addAttribute(usuario);
		model.addAttribute("conteudo", "cadastrarCondominio");
		return new ModelAndView("site/layout", model);
	}

	@GetMapping("/redefinir")
	public ModelAndView preRedefinir(ModelMap model) {
		model.addAttribute("conteudo", "redefinir");
		return new ModelAndView("site/layout", model);
	}

	@PostMapping("/redefinir")
	public String posRedefinir(@ModelAttribute("username") String username, ModelMap model) {
		if (usuarioService.redefinirSenha(username)) {
			return "redirect:/conta/redefinir?ok";
		} else
			return "redirect:/conta/redefinir?erro";
	}

	@PostMapping("/redefinir/alterar")
	public String fimRedefinir(@ModelAttribute("password") String password, @ModelAttribute("token") String token,
			ModelMap model) {
		if (usuarioService.redefinirSenha(token, password)) {
			return "redirect:/login?ok";
		} else
			return "redirect:/conta/redefinir?invalido";
	}
}
