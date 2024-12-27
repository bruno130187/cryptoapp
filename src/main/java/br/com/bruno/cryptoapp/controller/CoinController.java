package br.com.bruno.cryptoapp.controller;

import br.com.bruno.cryptoapp.dto.CoinDTO;
import br.com.bruno.cryptoapp.entity.CoinEntity;
import br.com.bruno.cryptoapp.repository.CoinRepository;
import br.com.bruno.cryptoapp.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/coin")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private CoinRepository coinRepository;

    /*@Bean
    public CoinEntity init(){
        CoinEntity c1 = new CoinEntity();
        c1.setName("BITCOIN".toLowerCase());
        c1.setPrice(BigDecimal.valueOf(328407.25));
        c1.setQuantity(BigDecimal.valueOf(0.002));
        c1.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
        c1.setUpdateDateTime(new Timestamp(System.currentTimeMillis()));
        coinRepository.save(c1);

        CoinEntity c2 = new CoinEntity();
        c2.setName("DOGECOIN".toLowerCase());
        c2.setPrice(BigDecimal.valueOf(0.78));
        c2.setQuantity(BigDecimal.valueOf(250));
        c2.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
        c2.setUpdateDateTime(new Timestamp(System.currentTimeMillis()));
        coinRepository.save(c2);

        CoinEntity c3 = new CoinEntity();
        c3.setName("ETHEREUM".toLowerCase());
        c3.setPrice(BigDecimal.valueOf(16199.53));
        c3.setQuantity(BigDecimal.valueOf(0.53));
        c3.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
        c3.setUpdateDateTime(new Timestamp(System.currentTimeMillis()));
        coinRepository.save(c3);

        return c1;
    }*/

    @PostMapping
    public ResponseEntity<Object> insert(@RequestBody CoinDTO coinDTO) {
        try {
            return new ResponseEntity<>(coinService.insert(coinDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody CoinDTO coinDTO) {
        try {
            return new ResponseEntity<>(coinService.update(coinDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        try {
            coinService.delete(id);
            return new ResponseEntity<>("Coin deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<CoinDTO> findAll() {
        List<CoinDTO> listCoin = coinService.findAll();
        return listCoin;
    }

    @GetMapping("/{name}")
    public List<CoinDTO> findByName(@PathVariable String name) {
        List<CoinDTO> listCoin = coinService.findByName(name);
        return listCoin;
    }

}
