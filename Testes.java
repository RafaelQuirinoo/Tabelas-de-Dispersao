package com.mycompany.hashtable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import static com.mycompany.hashtable.TabelaHashLista.carregarDicionario;

public class Testes {

    private static class Par {
        String chave;
        String valor;
        Par(String c, String v) {
            chave = c;
            valor = v;
        }
    }

    private static ArrayList<Par> carregarDicionarioEmMemoria() {
        ArrayList<Par> lista = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("english Dictionary.csv"));
            // Pula cabeçalho ("word,pos,definition")
            br.readLine(); 
            String linha = br.readLine();
            while (linha != null) {
                boolean entreAspas = false;
                StringBuilder valorAtual = new StringBuilder();
                ArrayList<String> colunas = new ArrayList<>();

                for (int i = 0; i < linha.length(); i++) {
                    char letra = linha.charAt(i);            
                    if (letra == '"') {
                        entreAspas = !entreAspas; 
                    } else if (letra == ',') {
                        if (entreAspas) {
                            valorAtual.append(letra);
                        } else {
                            colunas.add(valorAtual.toString());
                            valorAtual.setLength(0); 
                        }
                    } else {
                        valorAtual.append(letra);
                    }
                }
                colunas.add(valorAtual.toString());
                if (colunas.size() >= 3) {
                    lista.add(new Par(colunas.get(0), colunas.get(2)));
                }
                linha = br.readLine();
            } 
            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return lista;
    }

    public static void main(String[] args) {
        // 1. Testes Originais da Dupla (Para manter o comportamento original)
        System.out.println("=== TESTES ORIGINAIS DA DUPLA ===");
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
        System.out.println("");

        // 2. Benchmarks Avançados e Comparativos (Trabalho do Allan / Usuário)
        System.out.println("=== BENCHMARKS E RESOLUÇÃO DE COLISÕES (TRABALHO DO ALLAN) ===");
        ArrayList<Par> dados = carregarDicionarioEmMemoria();
        if (dados.isEmpty()) {
            System.out.println("Erro: Não foi possível carregar os dados para o benchmark.");
            return;
        }
        System.out.println("Total de palavras carregadas em memoria para o teste: " + dados.size());

        // Inserção Completa
        System.out.println("\n[1] Tempo de Inserção (Dicionário Completo - 176k+ palavras):");

        // Encadeamento Separado Sem Rehash
        TabelaHashLista thListaSemRehash = new TabelaHashLista(100000);
        thListaSemRehash.usarRehash = false;
        long tIni = System.nanoTime();
        for (Par p : dados) {
            thListaSemRehash.inserir(p.chave, p.valor);
        }
        long tFim = System.nanoTime();
        System.out.printf("- Encadeamento Separado (Sem Rehash, cap=100.000): %.3f ms (Colisões: %d)\n", 
            (tFim - tIni) / 1e6, thListaSemRehash.colisoes);

        // Encadeamento Separado Com Rehash
        TabelaHashLista thListaComRehash = new TabelaHashLista(100000);
        thListaComRehash.usarRehash = true;
        tIni = System.nanoTime();
        for (Par p : dados) {
            thListaComRehash.inserir(p.chave, p.valor);
        }
        tFim = System.nanoTime();
        System.out.printf("- Encadeamento Separado (Com Rehash, cap_ini=100.000, cap_fim=%d): %.3f ms (Colisões: %d)\n", 
            thListaComRehash.getCapacidade(), (tFim - tIni) / 1e6, thListaComRehash.colisoes);

        // Endereçamento Aberto (Linear Probing) Com Rehash
        TabelaHashEnderecamentoAberto thAberto = new TabelaHashEnderecamentoAberto(100000);
        tIni = System.nanoTime();
        for (Par p : dados) {
            thAberto.inserir(p.chave, p.valor);
        }
        tFim = System.nanoTime();
        System.out.printf("- Endereçamento Aberto (Linear Probing com Rehash, cap_ini=100.000, cap_fim=%d): %.3f ms (Colisões: %d)\n", 
            thAberto.getCapacidade(), (tFim - tIni) / 1e6, thAberto.colisoes);

        // Comparação de Tempos com Lista Encadeada Comum (Dataset Reduzido)
        int limiteTamanho = 5000;
        System.out.println("\n[2] Comparação com Lista Encadeada Comum (Dataset de " + limiteTamanho + " chaves):");

        // Lista Encadeada Comum
        ListaEncadeada listaComum = new ListaEncadeada();
        tIni = System.nanoTime();
        for (int i = 0; i < limiteTamanho; i++) {
            listaComum.inserir(dados.get(i).chave, dados.get(i).valor);
        }
        long tInsLista = System.nanoTime() - tIni;

        tIni = System.nanoTime();
        for (int i = 0; i < limiteTamanho; i++) {
            listaComum.buscar(dados.get(i).chave);
        }
        long tBuscaLista = System.nanoTime() - tIni;

        tIni = System.nanoTime();
        for (int i = 0; i < limiteTamanho; i++) {
            listaComum.remover(dados.get(i).chave);
        }
        long tRemLista = System.nanoTime() - tIni;

        // Tabela Hash (Usando Encadeamento Separado para Comparação)
        TabelaHashLista thListaBench = new TabelaHashLista(1000);
        thListaBench.usarRehash = true;
        
        tIni = System.nanoTime();
        for (int i = 0; i < limiteTamanho; i++) {
            thListaBench.inserir(dados.get(i).chave, dados.get(i).valor);
        }
        long tInsHash = System.nanoTime() - tIni;

        tIni = System.nanoTime();
        for (int i = 0; i < limiteTamanho; i++) {
            thListaBench.buscar(dados.get(i).chave);
        }
        long tBuscaHash = System.nanoTime() - tIni;

        tIni = System.nanoTime();
        for (int i = 0; i < limiteTamanho; i++) {
            thListaBench.remover(dados.get(i).chave);
        }
        long tRemHash = System.nanoTime() - tIni;

        System.out.printf("- INSERÇÃO: Lista Encadeada: %.3f ms | Tabela Hash (Lista): %.3f ms (Fator de aceleração: %.1fx)\n", 
            tInsLista / 1e6, tInsHash / 1e6, (double) tInsLista / tInsHash);
        System.out.printf("- BUSCA:    Lista Encadeada: %.3f ms | Tabela Hash (Lista): %.3f ms (Fator de aceleração: %.1fx)\n", 
            tBuscaLista / 1e6, tBuscaHash / 1e6, (double) tBuscaLista / tBuscaHash);
        System.out.printf("- REMOÇÃO:  Lista Encadeada: %.3f ms | Tabela Hash (Lista): %.3f ms (Fator de aceleração: %.1fx)\n", 
            tRemLista / 1e6, tRemHash / 1e6, (double) tRemLista / tRemHash);
    }
}
