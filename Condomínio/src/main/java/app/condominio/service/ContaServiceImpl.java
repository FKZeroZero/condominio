package app.condominio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import app.condominio.dao.ContaDao;
import app.condominio.domain.Condominio;
import app.condominio.domain.Conta;

@Service
@Transactional
public class ContaServiceImpl implements ContaService {

	@Autowired
	private ContaDao contaDao;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void salvar(Conta entidade) {
		entidade.setCondominio(usuarioService.lerLogado().getCondominio());
		entidade.setSaldoAtual(entidade.getSaldoInicial());
		contaDao.save(entidade);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Conta ler(Long id) {
		return contaDao.findById(id).get();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Conta> listar() {
		Condominio condominio = usuarioService.lerLogado().getCondominio();
		if (condominio == null) {
			return new ArrayList<>();
		}
		return condominio.getContas();
	}

	@Override
	public void editar(Conta entidade) {
		Conta antiga = ler(entidade.getIdConta());
		if (antiga.getSaldoInicial().compareTo(entidade.getSaldoInicial()) != 0) {
			entidade.setSaldoAtual(
					entidade.getSaldoAtual().subtract(antiga.getSaldoInicial()).add(entidade.getSaldoInicial()));
		}
		contaDao.save(entidade);
	}

	@Override
	public void excluir(Conta entidade) {
		contaDao.delete(entidade);

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void validar(Conta entidade, BindingResult validacao) {
		// TODO validar conta com sigla repetida

	}
}
