package br.com.bruno.cryptoapp.utils;

import br.com.bruno.cryptoapp.dto.CoinDTO;
import br.com.bruno.cryptoapp.entity.CoinEntity;

public class Utils {

    public static CoinDTO toDTO(CoinEntity coinEntity) {
        return new CoinDTO(
                coinEntity.getId(),
                coinEntity.getName(),
                coinEntity.getPrice(),
                coinEntity.getQuantity(),
                coinEntity.getCreateDateTime(),
                coinEntity.getUpdateDateTime()
        );
    }

    public static CoinEntity toEntity(CoinDTO coinDTO) {
        return new CoinEntity(
                coinDTO.getId(),
                coinDTO.getName(),
                coinDTO.getPrice(),
                coinDTO.getQuantity(),
                coinDTO.getCreateDateTime(),
                coinDTO.getUpdateDateTime()
        );
    }
}
