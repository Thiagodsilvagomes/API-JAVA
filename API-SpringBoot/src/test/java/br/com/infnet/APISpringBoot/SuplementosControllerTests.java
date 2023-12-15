package br.com.infnet.APISpringBoot;


import br.com.infnet.APISpringBoot.controllers.SuplementosController;
import br.com.infnet.APISpringBoot.model.Suplemento;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class SuplementosControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Teste de filtragem de suplementos com nome e preço")
	public void testFiltrarSuplementos() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/suplementos")
						.param("nome", "Creatina"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Creatina"))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));


		mockMvc.perform(MockMvcRequestBuilders.get("/suplementos")
						.param("preco", "120.99"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].preco").value(120.99))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));


		mockMvc.perform(MockMvcRequestBuilders.get("/suplementos")
						.param("nome", "Creatina")
						.param("preco", "120.99"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Creatina"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].preco").value(120.99))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
	}

	@Test
	public void testFiltrarSuplementosComParametrosInvalidos() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/suplementos")
						.param("nome", "NomeInvalido"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));


		mockMvc.perform(MockMvcRequestBuilders.get("/suplementos")
						.param("preco", "9999"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
	}

	@Test
	@DisplayName("Teste de atualização de suplemento com ID inválido")
	public void testUpdateSuplementoComIdInvalido() {
		// Suponha que o ID 100 não exista na sua lista de suplementos
		Long idInexistente = 100L;

		Suplemento suplementoAtualizado = new Suplemento(idInexistente, "AtualizacaoNome", 99.99, Arrays.asList("Beneficio1", "Beneficio2"));

		// Suponha que você tenha uma instância do SuplementosController injetada ou inicializada
		SuplementosController suplementosController = new SuplementosController();

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			suplementosController.updateSuplemento(idInexistente, suplementoAtualizado);
		});

		// Verifica se a exceção tem o status esperado
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
		// Verifica a mensagem da exceção
		assertEquals("Suplemento não encontrado", exception.getReason());
	}




}
