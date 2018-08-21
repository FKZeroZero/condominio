package app.condominio.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import app.condominio.domain.Bloco;
import app.condominio.domain.Moradia;
import app.condominio.domain.Pessoa;
import app.condominio.domain.enums.TipoMoradia;
import app.condominio.domain.enums.TipoRelacao;
import app.condominio.service.BlocoService;
import app.condominio.service.MoradiaService;
import app.condominio.service.PessoaService;

@Controller
@RequestMapping("sindico/moradias")
public class MoradiaController {

	@Autowired
	private MoradiaService moradiaService;

	@Autowired
	private BlocoService blocoService;

	@Autowired
	private PessoaService pessoaService;

	@ModelAttribute("ativo")
	public String[] ativo() {
		return new String[] { "condominio", "moradias" };
	}

	@ModelAttribute("tipos")
	public TipoMoradia[] tipos() {
		return TipoMoradia.values();
	}

	@ModelAttribute("tiposRelacao")
	public TipoRelacao[] tiposRelacao() {
		return TipoRelacao.values();
	}

	@ModelAttribute("blocos")
	public List<Bloco> blocos() {
		return blocoService.listar();
	}

	@ModelAttribute("pessoas")
	public List<Pessoa> pessoas() {
		return pessoaService.listar();
	}

	@GetMapping({ "", "/", "/lista", "/todos" })
	public ModelAndView getMoradias(ModelMap model) {
		model.addAttribute("moradias", moradiaService.listar());
		model.addAttribute("conteudo", "moradiaLista");
		return new ModelAndView("fragmentos/layoutSindico", model);
	}

	@GetMapping("/cadastro")
	public ModelAndView getMoradiaCadastro(@ModelAttribute("moradia") Moradia moradia) {
		return new ModelAndView("fragmentos/layoutSindico", "conteudo", "moradiaCadastro");
	}

	@GetMapping("/{idMoradia}/cadastro")
	public ModelAndView getMoradiaEditar(@PathVariable("idMoradia") Long idMoradia, ModelMap model) {
		model.addAttribute("moradia", moradiaService.ler(idMoradia));
		model.addAttribute("conteudo", "moradiaCadastro");
		return new ModelAndView("fragmentos/layoutSindico", model);
	}

	@PostMapping("/cadastro")
	public ModelAndView postMoradiaCadastro(@Valid @ModelAttribute("moradia") Moradia moradia,
			BindingResult validacao) {
		moradiaService.setRelacaoMoradia(moradia);
		moradiaService.validar(moradia, validacao);
		if (validacao.hasErrors()) {
			return new ModelAndView("fragmentos/layoutSindico", "conteudo", "moradiaCadastro");
		}
		moradiaService.salvar(moradia);
		return new ModelAndView("redirect:/sindico/moradias");
	}

	@PutMapping("/cadastro")
	public ModelAndView putMoradiaCadastro(@Valid @ModelAttribute("moradia") Moradia moradia, BindingResult validacao,
			ModelMap model) {
		moradiaService.validar(moradia, validacao);
		if (validacao.hasErrors()) {
			model.addAttribute("conteudo", "moradiaCadastro");
			return new ModelAndView("fragmentos/layoutSindico", model);
		}
		moradiaService.editar(moradia);
		return new ModelAndView("redirect:/sindico/moradias");
	}

	@DeleteMapping("/excluir")
	public ModelAndView deleteMoradiaCadastro(@RequestParam("idObj") Long idObj) {
		moradiaService.excluir(moradiaService.ler(idObj));
		return new ModelAndView("redirect:/sindico/moradias");
	}
}
