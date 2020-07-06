package com.loja.virtual.apirest.services;

import com.loja.virtual.apirest.domain.Produto;
import com.loja.virtual.apirest.dto.ProdutoDTO;
import com.loja.virtual.apirest.repository.ProdutoRepository;
import com.loja.virtual.apirest.services.exceptions.DataIntegrityException;
import com.loja.virtual.apirest.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	public Produto findById(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	public Produto insert(Produto obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Produto update(Produto obj) {
		Produto newObj = findById(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	private void updateData(Produto newObj, Produto obj) {
		newObj.setNome(obj.getNome());
	}

	public void delete(Integer id) {
		findById(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}

	public Produto fromDTO(ProdutoDTO objDto) {
		return new Produto(objDto.getId(), objDto.getNome(), objDto.getPreco());
	}

	public Page<Produto> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}


}
