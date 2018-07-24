package app.condominio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import app.condominio.dao.PessoaDao;
import app.condominio.domain.Condominio;
import app.condominio.domain.Pessoa;

@Service
@Transactional
public class PessoaServiceImpl implements PessoaService {

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void salvar(Pessoa entidade) {
		entidade.setCondominio(usuarioService.lerLogado().getCondominio());
		pessoaDao.save(entidade);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Pessoa ler(Long id) {
		return pessoaDao.findById(id).get();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Pessoa> listar() {
		Condominio condominio = usuarioService.lerLogado().getCondominio();
		if (condominio == null) {
			return new ArrayList<Pessoa>();
		}
		return condominio.getPessoas();
	}

	@Override
	public void editar(Pessoa entidade) {
		pessoaDao.save(entidade);
	}

	@Override
	public void excluir(Pessoa entidade) {
		pessoaDao.delete(entidade);
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public boolean haCondominio() {
		return usuarioService.lerLogado().getCondominio() != null;
	}

	@Override
	public void validar(Pessoa entidade, BindingResult validacao) {
		// TODO Auto-generated method stub
		
	}

}
