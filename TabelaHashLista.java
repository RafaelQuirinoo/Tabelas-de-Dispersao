package com.mycompany.hashtable;

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

    // Construtor
    public TabelaHashLista(int capacidade) {
        this.capacidade = capacidade;
        this.tabela = new Node[capacidade]; // Inicializa o array com posições vazias (null)
    }

    // 2. A Função Hash (Função de Dispersão)
    // Usa o hashCode nativo do Java e aplica o operador resto (%) para caber no array
    private int funcaoHash(String chave) {
        int hash = chave.hashCode();
        return Math.abs(hash) % capacidade;
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

    // Método auxiliar para exibir a tabela na aula
    public void imprimirTabela() {
        System.out.println("\n=== ESTRUTURA ATUAL DA TABELA HASH ===");
        for (int i = 0; i < capacidade; i++) {
            System.out.print("Posição [" + i + "]: ");
            Node noAtual = tabela[i];
            if (noAtual == null) {
                System.out.print("NULL");
            } else {
                while (noAtual != null) {
                    System.out.print("{" + noAtual.chave + " => " + noAtual.valor + "}");
                    if (noAtual.proximo != null) {
                        System.out.print(" -> ");
                    }
                    noAtual = noAtual.proximo;
                }
            }
            System.out.println();
        }
        System.out.println("======================================\n");
    }
    
    //Metodo da multiplicação ( h(k) = ⌊ m * ( (k * A) % 1 ) ⌋ )
    private int hashMultiplicacao(String chave) {
        int k = Math.abs(chave.hashCode());
        double a = 0.6180339887; 
        double multiplicacao = k * a;
        double aposVirgula = multiplicacao % 1; 
        int indice = (int) (aposVirgula * capacidade); 
    return indice;
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
