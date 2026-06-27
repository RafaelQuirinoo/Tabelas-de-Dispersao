package com.mycompany.hashtable;
    
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TabelaHashLista {

    // 1. Definição do Nó da Lista Encadeada
    private static class Node {
        String chave;
        String valor;
        Node proximo;

        public Node(String chave, String valor) {
            this.chave = chave;
            this.valor = valor;
            this.proximo = null;
        }
    }

    // Atributos da Tabela Hash
    private Node[] tabela;
    private int capacidade;
    protected int colisoes = 0;
    protected int metodo = 1;

    // Construtor
    public TabelaHashLista(int capacidade) {
        this.capacidade = capacidade;
        this.tabela = new Node[capacidade]; // Inicializa o array com posições vazias (null)
    }
    
    // 2. A Função Hash (Função de Dispersão)
    //Redireciona para um dos metodos
    private int funcaoHash(String chave) {
        if (this.metodo == 1) {
            return multiplicacao(chave);
        } else {
            return algoritmoDJB2(chave);
        }
    }

    // 3. Operação de Inserção (Put)
    public void inserir(String chave, String valor) {
        int indice = funcaoHash(chave);
        Node noAtual = tabela[indice];

        // Caso 1: A posição está vazia (Sem colisão)
        if (noAtual == null) {
            tabela[indice] = new Node(chave, valor);
            return;
        }
        
        colisoes++;
        
        // Caso 2: Há elementos na posição (Colisão!)
        // Vamos percorrer a lista encadeada naquela posição
        while (noAtual != null) {
            // Se a chave já existir, atualiza o valor (evita duplicatas)
            if (noAtual.chave.equals(chave)) {
                noAtual.valor = valor;
                return;
            }
            // Se chegou ao último nó, para a execução
            if (noAtual.proximo == null) {
                break;
            }
            noAtual = noAtual.proximo;
        }

        // Insere o novo nó no final da lista encadeada existente
        noAtual.proximo = new Node(chave, valor);
    }

    // 4. Operação de Busca (Get)
    public String buscar(String chave) {
        int indice = funcaoHash(chave);
        Node noAtual = tabela[indice];

        // Percorre a lista encadeada no índice gerado
        while (noAtual != null) {
            if (noAtual.chave.equals(chave)) {
                return noAtual.valor; // Encontrou!
            }
            noAtual = noAtual.proximo;
        }
        return null; // Não encontrou
    }

    // 5. Operação de Remoção (Delete)
    public boolean remover(String chave) {
        int indice = funcaoHash(chave);
        Node noAtual = tabela[indice];
        Node noAnterior = null;

        while (noAtual != null) {
            if (noAtual.chave.equals(chave)) {
                // Se for o primeiro nó da lista daquela posição
                if (noAnterior == null) {
                    tabela[indice] = noAtual.proximo;
                } else {
                    // Se estiver no meio ou fim, "pula" o nó atual
                    noAnterior.proximo = noAtual.proximo;
                }
                return true; // Removido com sucesso
            }
            noAnterior = noAtual;
            noAtual = noAtual.proximo;
        }
        return false; // Chave não encontrada
    }

    public static void carregarDicionario(TabelaHashLista minhaTabela) {
        try {
        BufferedReader br = new BufferedReader(new FileReader("english Dictionary.csv"));

        // Pula o cabeçalho ("word,pos,definition")
        br.readLine(); 

        
        String linha = br.readLine(); 

        while (linha != null) {
            boolean entreAspas = false;
            StringBuilder valorAtual = new StringBuilder();
            ArrayList<String> colunas = new ArrayList<>();

            // Algoritmo de Varredura
            for (int i = 0; i < linha.length(); i++) {
                char letra = linha.charAt(i);            
                if (letra == '"') {
                    entreAspas = !entreAspas; 
                } else if (letra == ',') {
                    if (entreAspas) {
                        valorAtual.append(letra); // Vírgula de dentro do texto
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
                String palavra = colunas.get(0);
                String definicao = colunas.get(2);
                minhaTabela.inserir(palavra, definicao);
            }
            linha = br.readLine();
        } 
        br.close();
        System.out.println("Dicionário carregado.");
    } catch (IOException e) {
        System.out.println("Erro ao ler o arquivo: " + e.getMessage());
    }
}
    
     //Metodo da multiplicação ( h(k) = ⌊ m * ( (k * A) % 1 ) ⌋ )
    private int multiplicacao(String chave) {
        int k = Math.abs(chave.hashCode());
        double A = 0.6180339887; 
        double multiplicacao = k * A;
        double parteFracionaria = multiplicacao % 1; 
        int posicao = (int) (parteFracionaria * capacidade); 
    return posicao;
}
    
    //Algoritmo DJB2
    private int algoritmoDJB2(String chave) {
    long hash = 5381; 
    for (int i = 0; i < chave.length(); i++) {
        char letra = chave.charAt(i);
        hash = (hash * 33) + letra; 
    }
    int posicao = (int) (Math.abs(hash) % capacidade);
    return posicao;
}
}
