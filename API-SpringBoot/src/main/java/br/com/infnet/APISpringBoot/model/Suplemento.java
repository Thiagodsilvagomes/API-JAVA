package br.com.infnet.APISpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data@AllArgsConstructor@NoArgsConstructor
public class Suplemento {
    private long id;
    private String nome;
    private double preco;
    private List<String> beneficios;
}
