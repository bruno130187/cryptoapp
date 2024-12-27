package br.com.bruno.cryptoapp.service;

import br.com.bruno.cryptoapp.dto.CoinDTO;
import br.com.bruno.cryptoapp.entity.CoinEntity;
import br.com.bruno.cryptoapp.repository.CoinRepository;
import br.com.bruno.cryptoapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    public List<CoinDTO> findAll() {
        List<CoinEntity> listCoin = coinRepository.findAll();
        return listCoin
                .stream()
                .map(Utils::toDTO)
                .toList();
    }

    public List<CoinDTO> findByName(String name) {
        List<CoinEntity> listCoin = coinRepository.findByNameIsContaining(name);

        if (listCoin.isEmpty()) {
            throw new EntityNotFoundException("Coin with name " + name + " not found");
        }

        return listCoin
                .stream()
                .map(Utils::toDTO)
                .toList();
    }

    public CoinDTO insert(CoinDTO coinDTO) {
        CoinEntity coinEntity = CoinEntity
                .builder()
                .name(coinDTO.getName())
                .price(coinDTO.getPrice())
                .quantity(coinDTO.getQuantity())
                .createDateTime(new Timestamp(System.currentTimeMillis()))
                .updateDateTime(new Timestamp(System.currentTimeMillis()))
                .build();

        CoinDTO dto = Utils.toDTO(coinRepository.save(coinEntity));

        return dto;
    }

    public CoinDTO update(CoinDTO coinDTO) {
        var retorno = coinRepository.findById(coinDTO.getId())
                .map(entity -> {
                    entity.setName(coinDTO.getName());
                    entity.setPrice(coinDTO.getPrice());
                    entity.setQuantity(coinDTO.getQuantity());
                    entity.setUpdateDateTime(new Timestamp(System.currentTimeMillis()));
                    return entity;
                })
                .map(coinRepository::save)
                .orElseThrow(() -> new EntityNotFoundException("Coin with id " + coinDTO.getId() + " not found"));

        return Utils.toDTO(retorno);
    }

    public void delete(int id) {
        if (coinRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Coin with id " + id + " not found");
        }
        coinRepository.deleteById(id);
    }

}
