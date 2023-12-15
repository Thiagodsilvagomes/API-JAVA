package br.com.infnet.APISpringBoot.controllers;

import br.com.infnet.APISpringBoot.model.ProductDetails;
import br.com.infnet.APISpringBoot.model.Suplemento;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("suplementos")
public class SuplementosController {
    Logger logger = LoggerFactory.getLogger(SuplementosController.class);
    private List<Suplemento> suplementos = new CopyOnWriteArrayList<>();

    @GetMapping
    public List<Suplemento> filtrarSuplementos(@RequestParam(required = false) String nome,
                                               @RequestParam(required = false) Double preco) {
        logger.info("Método GET: filtrarSuplementos - Recebendo requisição GET para filtrar suplementos");

        if (suplementos.isEmpty()) {
            List<String> beneficiosCreatina = Arrays.asList("Aumento da massa muscular","Melhora a recuperação muscular",
                    "Prevenir doenças crônicas");
            Suplemento suplemento = new Suplemento(1L,"Creatina",120.99, beneficiosCreatina);

            List<String> beneficiosWheyProtein = Arrays.asList("Aumento da massa muscular","Melhora a recuperação muscular",
                    "Prevenir a perda de massa muscular");
            Suplemento suplemento2 = new Suplemento(2L,"Whey-Protein",159.99,beneficiosWheyProtein);

            List<String> beneficiosCafeina= Arrays.asList("Acelera a queima de gordura e perda de peso",
                    "Combate o cansaço físico e mental, estimulando o desempenho durante exercícios");
            Suplemento suplemento3 = new Suplemento(3L,"Cafeína",40.49,beneficiosCafeina);

            suplementos.addAll(Arrays.asList(suplemento, suplemento2, suplemento3));
        }

        List<Suplemento> filteredSuplementos = suplementos;

        if (nome != null) {
            filteredSuplementos = filteredSuplementos.stream()
                    .filter(s -> s.getNome().equalsIgnoreCase(nome))
                    .collect(Collectors.toList());
        }

        if (preco != null) {
            filteredSuplementos = filteredSuplementos.stream()
                    .filter(s -> s.getPreco() == preco)
                    .collect(Collectors.toList());
        }

        return filteredSuplementos;
    }


    @GetMapping("/produto")
    public ResponseEntity<Object> getDetalhesProduto() {
        try {
            ProductDetails productDetails = fetchProductData();
            logger.info("Método GET: getDetalhesProduto - Requisição GET para detalhes do produto. Status: {}", HttpStatus.OK);
            return new ResponseEntity<>(productDetails, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erro no método GET: getDetalhesProduto - Erro ao buscar detalhes do produto: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ProductDetails fetchProductData() throws Exception {
        String apiUrl = "https://world.openfoodfacts.net/api/v2/product/3017624010701";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new Exception("Failed to fetch product data");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> productInfo = mapper.readValue(response.getBody(), Map.class);
        return extractProductData(productInfo);
    }

    private ProductDetails extractProductData(Map<String, Object> productInfo) {
        ProductDetails productDetails = new ProductDetails();
        if (productInfo.containsKey("product")) {
            Map<String, Object> product = (Map<String, Object>) productInfo.get("product");
            if (product.containsKey("product_name")) {
                productDetails.setProductName((String) product.get("product_name"));
            }
            if (product.containsKey("brands")) {
                productDetails.setBrands((String) product.get("brands"));
            }
            if (product.containsKey("countries")) {
                productDetails.setCountries((String) product.get("countries"));
            }
        }
        return productDetails;
    }


    @PostMapping
    public ResponseEntity<Object> createSuplemento(@RequestBody Suplemento suplemento) {
        try {
            if (suplemento.getNome() == null || suplemento.getNome().isEmpty()) {
                throw new IllegalArgumentException("O campo 'nome' não pode ser vazio ou nulo.");
            }


            if (suplemento.getPreco() < 0) {
                throw new IllegalArgumentException("O campo 'preco' deve ser um número válido e não negativo.");
            }


            if (suplemento.getBeneficios() == null || suplemento.getBeneficios().isEmpty()) {
                throw new IllegalArgumentException("O campo 'beneficios' não pode ser vazio ou nulo.");
            }


            suplementos.add(suplemento);
            return new ResponseEntity<>(suplemento, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erro ao criar suplemento", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar o suplemento: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuplemento(@PathVariable Long id) {
        Suplemento suplemento = suplementos.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);

        if (suplemento == null) {
            logger.warn("Método PUT: updateSuplemento - Suplemento não encontrado para o ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        suplementos.remove(suplemento);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Suplemento> updateSuplemento(@PathVariable Long id, @RequestBody Suplemento novoSuplemento) {
        Suplemento suplementoExistente = suplementos.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);

        if (suplementoExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Suplemento não encontrado");
        }

        suplementoExistente.setNome(novoSuplemento.getNome());
        suplementoExistente.setPreco(novoSuplemento.getPreco());
        suplementoExistente.setBeneficios(novoSuplemento.getBeneficios());

        return new ResponseEntity<>(suplementoExistente, HttpStatus.OK);
    }

}

