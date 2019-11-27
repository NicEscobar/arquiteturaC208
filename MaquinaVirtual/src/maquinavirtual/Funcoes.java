package maquinavirtual;

import java.util.ArrayList;

public class Funcoes 
{
    ArrayList<Integer> registradores = new ArrayList();

    
    //OPERAÇÕES ARITMÉTICAS
    public int soma(int valor, int constante)
    {
        return valor+constante;
    }
    public int multiplicacao(int valor, int constante)
    {
        return valor*constante;
    }
    
    //OPERAÇÕES LÓGICAS
    public int and(int valor1, int valor2)
    {
        return valor1 & valor2;
    }
    public int or(int valor1, int valor2)
    {
        return valor1 | valor2;
    }
    
    //OPERAÇÃO GET/SET
    public int funcao_get(int address)
    {
        return registradores.get(address);
    }
    public void funcao_set(int address, int valor)
    {
        registradores.add(address, valor);
    }
}
