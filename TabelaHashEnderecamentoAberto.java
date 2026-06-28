package com.mycompany.hashtable;

public class TabelaHashEnderecamentoAberto {

    private static class Entry {
        String chave;
        String valor;

        public Entry(String chave, String valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }

    private static final Entry DELETADO = new Entry(null, null);

    private Entry[] tabela;
    private int capacidade;
    private int tamanho;
    protected int colisoes = 0;
    protected int metodo = 1; // 1: Multiplicacao, 2: DJB2

    public TabelaHashEnderecamentoAberto(int capacidade) {
        this.capacidade = capacidade;
        this.tabela = new Entry[capacidade];
        this.tamanho = 0;
    }

    private int funcaoHash(String chave) {
        if (this.metodo == 1) {
            return multiplicacao(chave);
        } else {
            return algoritmoDJB2(chave);
        }
    }

    private int multiplicacao(String chave) {
        int k = Math.abs(chave.hashCode());
        double A = 0.6180339887;
        double multiplicacao = k * A;
        double parteFracionaria = multiplicacao % 1;
        return (int) (parteFracionaria * capacidade);
    }

    private int algoritmoDJB2(String chave) {
        long hash = 5381;
        for (int i = 0; i < chave.length(); i++) {
            char letra = chave.charAt(i);
            hash = (hash * 33) + letra;
        }
        return (int) (Math.abs(hash) % capacidade);
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void inserir(String chave, String valor) {
        // Redimensiona se o fator de carga for >= 0.75
        if ((double) tamanho / capacidade >= 0.75) {
            redimensionar();
        }

        int index = funcaoHash(chave);
        
        // Verifica se há colisão na posição inicial
        if (tabela[index] != null && tabela[index] != DELETADO && !tabela[index].chave.equals(chave)) {
            colisoes++;
        }

        int primeiroDeletado = -1;
        for (int i = 0; i < capacidade; i++) {
            int pos = (index + i) % capacidade;
            Entry atual = tabela[pos];

            if (atual == null) {
                int posInsercao = (primeiroDeletado != -1) ? primeiroDeletado : pos;
                tabela[posInsercao] = new Entry(chave, valor);
                tamanho++;
                return;
            } else if (atual == DELETADO) {
                if (primeiroDeletado == -1) {
                    primeiroDeletado = pos;
                }
            } else if (atual.chave.equals(chave)) {
                atual.valor = valor; // Atualiza o valor se a chave já existir
                return;
            }
        }

        // Caso a tabela esteja cheia por algum motivo extraordinário e haja um slot deletado
        if (primeiroDeletado != -1) {
            tabela[primeiroDeletado] = new Entry(chave, valor);
            tamanho++;
        }
    }

    public String buscar(String chave) {
        int index = funcaoHash(chave);
        for (int i = 0; i < capacidade; i++) {
            int pos = (index + i) % capacidade;
            Entry atual = tabela[pos];
            if (atual == null) {
                return null; // Encontrou slot vazio, a chave não existe
            }
            if (atual != DELETADO && atual.chave.equals(chave)) {
                return atual.valor; // Encontrou!
            }
        }
        return null;
    }

    public boolean remover(String chave) {
        int index = funcaoHash(chave);
        for (int i = 0; i < capacidade; i++) {
            int pos = (index + i) % capacidade;
            Entry atual = tabela[pos];
            if (atual == null) {
                return false; // Encontrou slot vazio, chave não existe
            }
            if (atual != DELETADO && atual.chave.equals(chave)) {
                tabela[pos] = DELETADO; // Exclusão lógica (lazy deletion)
                tamanho--;
                return true;
            }
        }
        return false;
    }

    private void redimensionar() {
        int novaCapacidade = capacidade * 2;
        Entry[] tabelaAntiga = tabela;
        tabela = new Entry[novaCapacidade];
        capacidade = novaCapacidade;
        tamanho = 0;

        for (Entry entry : tabelaAntiga) {
            if (entry != null && entry != DELETADO) {
                inserirSemRehash(entry.chave, entry.valor);
            }
        }
    }

    private void inserirSemRehash(String chave, String valor) {
        int index = funcaoHash(chave);
        for (int i = 0; i < capacidade; i++) {
            int pos = (index + i) % capacidade;
            Entry atual = tabela[pos];
            if (atual == null || atual == DELETADO) {
                tabela[pos] = new Entry(chave, valor);
                tamanho++;
                return;
            }
        }
    }

    public void distribuicao() {
        int[] contagem = new int[2]; // 0: posições vazias, 1: posições ocupadas
        for (int i = 0; i < capacidade; i++) {
            if (tabela[i] != null && tabela[i] != DELETADO) {
                contagem[1]++;
            } else {
                contagem[0]++;
            }
        }
        System.out.println("Tabela de Endereçamento Aberto (Sondagem Linear):");
        System.out.println("Posições ocupadas: " + contagem[1] + " / " + capacidade);
        System.out.println("Posições vazias: " + contagem[0] + " / " + capacidade);
    }
}
