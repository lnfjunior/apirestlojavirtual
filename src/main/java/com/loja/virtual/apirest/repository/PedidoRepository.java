package com.loja.virtual.apirest.repository;

import com.loja.virtual.apirest.domain.Cliente;
import com.loja.virtual.apirest.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	
	@Transactional
    Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}
