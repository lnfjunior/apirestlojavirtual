package com.loja.virtual.apirest.resources;

import com.loja.virtual.apirest.domain.Produto;
import com.loja.virtual.apirest.dto.ProdutoDTO;
import com.loja.virtual.apirest.services.ProdutoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value="/{id}", method= RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@ApiOperation(value = "Insere produto")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ProdutoDTO objDto) {
		Produto obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@ApiOperation(value = "Atualiza produto")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ProdutoDTO objDto, @PathVariable Integer id) {
		Produto obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Remove produto")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir um produto"),
			@ApiResponse(code = 404, message = "Código inexistente") })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Retorna todos os produtos com paginação")
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24")Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome")String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC")String direction) {
		Page<Produto> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj)) ;
		return ResponseEntity.ok().body(listDto);
	}
}
