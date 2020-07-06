package com.loja.virtual.apirest.services;

import com.loja.virtual.apirest.domain.Cliente;
import com.loja.virtual.apirest.domain.ItemPedido;
import com.loja.virtual.apirest.domain.Pedido;
import com.loja.virtual.apirest.repository.ItemPedidoRepository;
import com.loja.virtual.apirest.repository.PedidoRepository;
import com.loja.virtual.apirest.security.UserSS;
import com.loja.virtual.apirest.services.exceptions.AuthorizationException;
import com.loja.virtual.apirest.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.findById(obj.getCliente().getId()));
		obj = repo.save(obj);

		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.00);
			ip.setProduto(produtoService.findById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		return obj;
	}

	public Page<Pedido> findPage(Integer clienteId, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.findById(clienteId);
		return repo.findByCliente(cliente, pageRequest);
	}
}
