package com.mycompany.hashtable;

public class ListaEncadeada {
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

    private Node cabeca;
    private int tamanho;

    public ListaEncadeada() {
        this.cabeca = null;
        this.tamanho = 0;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void inserir(String chave, String valor) {
        Node atual = cabeca;
        while (atual != null) {
            if (atual.chave.equals(chave)) {
                atual.valor = valor;
                return;
            }
            atual = atual.proximo;
        }
        Node novo = new Node(chave, valor);
        novo.proximo = cabeca;
        cabeca = novo;
        tamanho++;
    }

    public String buscar(String chave) {
        Node atual = cabeca;
        while (atual != null) {
            if (atual.chave.equals(chave)) {
                return atual.valor;
            }
            atual = atual.proximo;
        }
        return null;
    }

    public boolean remover(String chave) {
        Node atual = cabeca;
        Node anterior = null;
        while (atual != null) {
            if (atual.chave.equals(chave)) {
                if (anterior == null) {
                    cabeca = atual.proximo;
                } else {
                    anterior.proximo = atual.proximo;
                }
                tamanho--;
                return true;
            }
            anterior = atual;
            atual = atual.proximo;
        }
        return false;
    }
}
