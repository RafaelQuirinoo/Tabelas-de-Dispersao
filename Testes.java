package com.mycompany.hashtable;

import static com.mycompany.hashtable.TabelaHashLista.carregarDicionario;

public class Testes {
        public static void main(String[] args) {

        int capacidadeTabela = 100000;
            
        TabelaHashLista tabela1 = new TabelaHashLista(capacidadeTabela);
        tabela1.metodo = 1; 
        carregarDicionario(tabela1);
        System.out.println("Colisoes - Metodo de Multiplicacao: " + tabela1.colisoes);
        tabela1.distribuicao();
        System.out.println("");
        TabelaHashLista tabela2 = new TabelaHashLista(capacidadeTabela);
        tabela2.metodo = 2; 
        carregarDicionario(tabela2);
        System.out.println("Colisoes - Algoritmo DJB2: " + tabela2.colisoes);
        tabela2.distribuicao();
        }
        
}

