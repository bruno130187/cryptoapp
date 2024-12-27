package br.com.bruno.cryptoapp.repository;

import br.com.bruno.cryptoapp.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinRepository extends JpaRepository<CoinEntity, Integer> {

    List<CoinEntity> findByNameIsContaining(String name);

}
