package com.loja.virtual.apirest.repository;

import com.loja.virtual.apirest.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	
}
