package maquinavirtual;

import java.util.ArrayList;

public class MaquinaVirtual 
{
    public static void main(String[] args) 
    {
        //MEMÓRIA DE PROGRAMA
        //lista de instruções
        ArrayList<String> instrucao = new ArrayList();
        //instrucao.add("0101001001100001"); //get
        instrucao.add("0110001001101001"); //set
        //instrucao.add("0000001000101000"); //soma tipo c
        instrucao.add("0001001001101001"); //mult tipo c
        instrucao.add("0010001001010001"); //and tipo c
       // instrucao.add("0011001001101011"); //or tipo c
        instrucao.add("1000001001101001"); //soma tipo v
        //instrucao.add("1001001001101001"); //mult tipo v
        instrucao.add("1010001001100100"); //and tipo v
        instrucao.add("1011001001100000"); //or tipo v
        instrucao.add("0100001001100010"); //jump
        instrucao.add("0101001001100001"); //get
        instrucao.add("0110001001101001"); //set
        instrucao.add("1000011000110001"); //soma tipo v
        instrucao.add("0001000001100111"); //mult tipo c
        instrucao.add("1010001001001000"); //and tipo v
       
        
        
        //MEMÓRIA CACHE COM 4 CÉLULAS
        ArrayList <InfoCache> cache = new ArrayList();
        InfoCache c1 = new InfoCache();
        InfoCache c2 = new InfoCache();
        InfoCache c3 = new InfoCache();
        InfoCache c4 = new InfoCache();
       // InfoCache c5 = new InfoCache();
       // InfoCache c6 = new InfoCache();
       // InfoCache c7 = new InfoCache();
       // InfoCache c8 = new InfoCache();
        cache.add(c1);
        cache.add(c2);
        cache.add(c3);
        cache.add(c4);
        //cache.add(c5);
        //cache.add(c6);
        //cache.add(c7);
        //cache.add(c8);
        
        Funcoes f = new Funcoes();
        int operando1,operando2,resultado,endereco;
        int PC = 0;
        String IR,binario1,binario2;
        IR = instrucao.get(PC);
        
        //BANCO DE REGISTRADORES
        //Registradores de uso geral
        f.registradores.add(2); //v0
        f.registradores.add(0); //v1
        f.registradores.add(3); //v2
        f.registradores.add(8); //v3
        f.registradores.add(4); //v4
        f.registradores.add(14); //v5
        f.registradores.add(15); //v6
        f.registradores.add(13); //v7
        f.registradores.add(1); //v8
        f.registradores.add(10); //v9
        
        
        System.out.println("SIMULAÇÃO DE UMA ARQUITETURA DE COMPUTADOR COM MEMÓRIA CACHE:");
        System.out.println("Valores iniciais dos registradores: ");
        for (int i=0;i<f.registradores.size();i++)
        {
            System.out.println("Registrador "+i+" = "+f.registradores.get(i));
        }
        
        //PROCESSAMENTO DAS INSTRUÇÕES (BUSCA NA MEMÓRIA + INTERPRETAÇÃO + EXECUÇÃO)
        for(int j=0;j<8;j++) //Roda apenas 5 vezes para teste
        { //Na teoria aqui seria  um loop infinito!
            boolean achou=false;
            System.out.println("*********************************************");
            System.out.println("Execução da instrução: "+j);
            System.out.println("PC = "+PC);
            for(int i=0;i<4;i++) //Procurar a instrução na memória cache
            {
                if(cache.get(i).valid) //Verifica se existe alguma informação associada a célula i
                {
                   if(cache.get(i).tag==PC) //Verifica se o campo tag faz referência ao endereço da
                   {                      //RAM correspondente ao valor de PC
                       //Em caso afirmativo:
                       IR = cache.get(i).data; //Faz uma cópia dos dados para a memória cache
                       achou=true; //A instrução foi encontrada na cache, logo temos um HIT
                       System.out.println("CACHE HIT");
                       break;
                   }
                }
            }
            if(!achou)//A instrução não foi encontrada na Cache, logo temos um MISS
            {
                System.out.println("CACHE MISS"); 
                IR = instrucao.get(PC); //A instrução atual é buscada na RAM 
                //(arquitetura de leitura LOOK ASIDE)
                int end=PC;
                for(int i=0;i<4;i++) //Carregar os valores próximos a instrução para a Cache
                { //conforme o princípio de Localidade
                    InfoCache aux = new InfoCache();
                    aux.celula=i;
                    aux.valid=true;
                    aux.tag=end;
                    aux.data=instrucao.get(end);
                    cache.add(i, aux);
                    end++;
                }
            }     
            PC = PC + 1; 
            System.out.println("IR="+IR); //instruçãp a ser executada
            if(IR.charAt(0)=='0') //tipo C
            { 
                System.out.println("Instrução tipo C!");
                if(IR.substring(1, 4).equals("000")) //Instrução: Soma
                {
                    System.out.println("SOMA");
                    operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2)); //Busca nos registradores
                    System.out.println("operando1 da soma: "+operando1);
                    operando2 = Integer.parseInt(IR.substring(12, 16),2);
                    System.out.println("operando2 da soma: "+operando2);
                    resultado = f.soma(operando1,operando2);
                    System.out.println("Soma: "+resultado);
                    endereco = Integer.parseInt(IR.substring(8, 12),2); //Armazena no registrador
                    f.funcao_set(endereco, resultado);
                    System.out.println("Resultado armazenado no registrador "+endereco);
                    
                }
                else if(IR.substring(1, 4).equals("001")) //Instrução: Multiplicação
                {
                    System.out.println("MULTIPLICAÇÃO");
                    operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2));
                    System.out.println("operando1 da mult: "+operando1);
                    operando2 = Integer.parseInt(IR.substring(12, 16),2);
                    System.out.println("operando2 da mult: "+operando2);
                    resultado = f.multiplicacao(operando1,operando2);
                    System.out.println("Mult: "+resultado);
                    endereco = Integer.parseInt(IR.substring(8, 12),2);
                    f.funcao_set(endereco, resultado);
                    System.out.println("Resultado armazenado no registrador "+endereco);

                }
                else if(IR.substring(1, 4).equals("010")) //Instrução: And
                {
                    System.out.println("AND");
                    int b1,b2;//variável para receber bit a bit
                    String t = "0"; //"vetor" de char temporario
                    String r = "0"; //resposta
                    
                    operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2)); 
                    binario1 = Integer.toBinaryString(operando1); //Converte para binário
                    System.out.println("binario1: "+binario1);
                    
                    operando2 = Integer.parseInt(IR.substring(12, 16),2);
                    binario2 = Integer.toBinaryString(operando2);
                    System.out.println("binario2: "+binario2);
                    
                    //Se o binario1 for menor que o binario 2, iguala a quantidade de casas
                    if (binario1.length() < binario2.length())
                    {
                        int s = binario2.length() - binario1.length();
                        for(int i=1;i<s;i++)
                        {
                            t = t + "0";
                        }
                        int cont = 0;
                        for(int i=s;i< binario2.length();i++)
                        { 
                            t = t + Character.toString(binario1.charAt(cont));
                            cont++;
                        }     
                        binario1 = t;    
                    }
                    //Se o binario2 for menor que o binario1, iguala a quantidade de casas
                    if(binario1.length() > binario2.length())
                    {
                        int s = binario1.length() - binario2.length();
                        for(int i=1;i<s;i++)
                        {
                            t = t + "0"; 
                        }
                        int cont = 0;
                        for(int i=s;i< binario1.length();i++)
                        {
                            t = t + Character.toString(binario1.charAt(cont));
                            cont++;
                        }
                        binario2 = t;  
                    }
                   //Percorre bit a bit
                    b1 = Character.getNumericValue(binario1.charAt(0));
                    b2 = Character.getNumericValue(binario2.charAt(0));
                    r = Integer.toString(f.and(b1, b2));
                    for (int i=1; i<binario1.length(); i++) 
                    {
                        b1 = Character.getNumericValue(binario1.charAt(i));
                        b2 = Character.getNumericValue(binario2.charAt(i));
                        r = r + Integer.toString(f.and(b1, b2));  
                    }
                    System.out.println("Resposta em binario: "+r);
                    System.out.println("Resposta em decimal: "+Integer.parseInt(r,2));
                    endereco = Integer.parseInt(IR.substring(8, 12),2);
                    f.funcao_set(endereco,Integer.parseInt(r,2));
                    System.out.println("Resultado armazenado no registrador "+endereco);
                }
                else if(IR.substring(1, 4).equals("011")) //Instrução: Or
                {
                    System.out.println("OR");
                    int b1,b2; //recebe bit a bit
                    String t = "0";//"vetor" de char temporario
                    String r = "0";//resposta

                    operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2));
                    binario1 = Integer.toBinaryString(operando1);
                    System.out.println("binario1: "+binario1);

                    operando2 = Integer.parseInt(IR.substring(12, 16),2);
                    binario2 = Integer.toBinaryString(operando2);
                    System.out.println("binario2: "+binario2);

                    //Se o binario1 for menor que o binario2, iguala a quantidade de casas
                    if (binario1.length() < binario2.length())
                    {
                        int s = binario2.length() - binario1.length();
                        for(int i=1;i<s;i++)
                        {
                            t = t + "0";
                        }
                        int cont = 0;
                        for(int i=s;i< binario2.length();i++)
                        { 
                            t = t + Character.toString(binario1.charAt(cont));
                            cont++;
                        }
                        binario1 = t;  
                    }
                    //Se o binario2 for menor que o binario1, iguala a quantidade de casas
                    if(binario1.length() > binario2.length())
                    {
                        int s = binario1.length() - binario2.length();
                        for(int i=1;i<s;i++)
                        {
                            t = t + "0"; 
                        }
                        int cont = 0;
                        for(int i=s;i< binario1.length();i++)
                        {
                            t = t + Character.toString(binario1.charAt(cont));
                            cont++;
                        }
                        binario2 = t;  
                    }
                   //Percorre bit a bit
                    b1 = Character.getNumericValue(binario1.charAt(0));
                    b2 = Character.getNumericValue(binario2.charAt(0));
                    r = Integer.toString(f.or(b1, b2));
                    for (int i=1; i<binario1.length(); i++) 
                    {
                        b1 = Character.getNumericValue(binario1.charAt(i));
                        b2 = Character.getNumericValue(binario2.charAt(i));
                        r = r + Integer.toString(f.or(b1, b2));
                    }
                    System.out.println("Resposta em binario: "+r);
                    System.out.println("Resposta em decimal: "+Integer.parseInt(r,2));
                    endereco = Integer.parseInt(IR.substring(8, 12),2);
                    f.funcao_set(endereco,Integer.parseInt(r,2));    
                    System.out.println("Resultado armazenado no registrador "+endereco);
                }
                else if(IR.substring(1, 4).equals("100")) //Instrução: jump
                {
                   System.out.println("JUMP");
                   PC = Integer.parseInt(IR.substring(12, 16),2); 
                   System.out.println("Novo valor de PC: "+PC);
                }
                else if(IR.substring(1, 4).equals("101")) //Instrução: get
                {
                    System.out.println("GET");
                    endereco = Integer.parseInt(IR.substring(12, 16),2);
                    System.out.print("O valor guardado no registrador de endereco "+endereco);
                    System.out.println(" eh "+f.funcao_get(endereco));
                }
                else if(IR.substring(1, 4).equals("110")) //Instrução: set
                {
                    System.out.println("SET");
                    operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2)); //constante
                    endereco = Integer.parseInt(IR.substring(8, 12),2);
                    f.funcao_set(endereco, operando1);
                    System.out.print("A constante de valor igual a "+operando1);
                    System.out.println(" foi salva no registrador de endereco "+endereco);
                }
        }
        else if(IR.charAt(0)=='1') //tipo V
        {
            System.out.println("Instrução tipo V!");
            if(IR.substring(1, 4).equals("000")) //Instrução: Soma
            {
                System.out.println("SOMA");
                operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2));
                System.out.println("operando1 da soma: "+operando1);
                operando2 = f.funcao_get(Integer.parseInt(IR.substring(8, 12),2));
                System.out.println("operando2 da soma: "+operando2);
                resultado = f.soma(operando1,operando2);
                System.out.println("Soma: "+resultado);

                endereco = Integer.parseInt(IR.substring(12, 16),2);
                f.funcao_set(endereco, resultado);
                System.out.println("Resultado armazenado no registrador "+endereco);
            }
            else if(IR.substring(1, 4).equals("001")) //Instrução: Multiplicação
            {
                System.out.println("MULTIPLICAÇÃO");
                operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2));
                System.out.println("operando1 da mult: "+operando1);
                operando2 = f.funcao_get(Integer.parseInt(IR.substring(8, 12),2));
                System.out.println("operando2 da mult: "+operando2);
                resultado = f.multiplicacao(operando1,operando2);
                System.out.println("Mult: "+resultado);
                endereco = Integer.parseInt(IR.substring(12,16),2);
                f.funcao_set(endereco, resultado);
                System.out.println("Resultado armazenado no registrador "+endereco);
            }
            else if(IR.substring(1, 4).equals("010")) ////Instrução: And
            {
                System.out.println("AND");
                int b1,b2;//recebe bit a bit
                String t = "0"; //"vetor" de char temporario
                String r = "0"; //resposta

                operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2)); 
                binario1 = Integer.toBinaryString(operando1); //Converte para binário
                System.out.println("binario1: "+binario1);

                operando2 = f.funcao_get(Integer.parseInt(IR.substring(8,12),2));
                binario2 = Integer.toBinaryString(operando2);
                System.out.println("binario2: "+binario2);

                //Se o binario1 for menor que o binario 2. Igualo a quantidade de casas
                if (binario1.length() < binario2.length())
                {

                    int s = binario2.length() - binario1.length();
                    for(int i=1;i<s;i++)
                    {
                        t = t + "0";
                    }
                    int cont = 0;
                    for(int i=s;i< binario2.length();i++)
                    { 
                        t = t + Character.toString(binario1.charAt(cont));
                        cont++;
                    }
                    binario1 = t;  
                }
                //Se o binario2 for menor que o binario 1. Igualo a quantidade de casas
                if(binario1.length() > binario2.length())
                {
                    int s = binario1.length() - binario2.length();
                    for(int i=1;i<s;i++)
                    {
                        t = t + "0"; 
                    }
                    int cont = 0;
                    for(int i=s;i< binario1.length();i++)
                    {
                        t = t + Character.toString(binario1.charAt(cont));
                        cont++;
                    }
                    binario2 = t; 
                }
               //Percorre bit a bi
                b1 = Character.getNumericValue(binario1.charAt(0));
                b2 = Character.getNumericValue(binario2.charAt(0));
                r = Integer.toString(f.and(b1, b2));
                for (int i=1; i<binario1.length(); i++) 
                {

                    b1 = Character.getNumericValue(binario1.charAt(i));
                    b2 = Character.getNumericValue(binario2.charAt(i));
                    r = r + Integer.toString(f.and(b1, b2));
                }
                System.out.println("Resposta em binario: "+r);
                System.out.println("Resposta em decimal: "+Integer.parseInt(r,2));
                endereco = Integer.parseInt(IR.substring(12, 16),2);
                f.funcao_set(endereco,Integer.parseInt(r,2));
                System.out.println("Resultado armazenado no registrador "+endereco);
            }
            else if(IR.substring(1, 4).equals("011")) //Instrução: Or
            {
                System.out.println("OR");
                int b1,b2; //recebe bit a bit
                String t = "0";//"vetor" de char temporario
                String r = "0";//resposta
                
                operando1 = f.funcao_get(Integer.parseInt(IR.substring(4, 8),2));
                binario1 = Integer.toBinaryString(operando1);
                System.out.println("binario1: "+binario1);

                operando2 = f.funcao_get(Integer.parseInt(IR.substring(8,12),2));
                binario2 = Integer.toBinaryString(operando2);
                System.out.println("binario2: "+binario2);

                //Se o binario1 for menor que o binario 2. Igualo a quantidade de casas
                if (binario1.length() < binario2.length())
                {

                    int s = binario2.length() - binario1.length();
                    for(int i=1;i<s;i++)
                    {
                        t = t + "0";
                    }
                    int cont = 0;
                    for(int i=s;i< binario2.length();i++)
                    { 
                        t = t + Character.toString(binario1.charAt(cont));
                        cont++;
                    }
                binario1 = t;
                }
                //Se o binario2 for menor que o binario 1. Igualo a quantidade de casas
                if(binario1.length() > binario2.length())
                {
                    int s = binario1.length() - binario2.length();
                    for(int i=1;i<s;i++)
                    {
                        t = t + "0"; 
                    }
                    int cont = 0;
                    for(int i=s;i< binario1.length();i++)
                    {
                        t = t + Character.toString(binario1.charAt(cont));
                        cont++;
                    }
                    binario2 = t;  
                }
               //Percorre bit a bi
                b1 = Character.getNumericValue(binario1.charAt(0));
                b2 = Character.getNumericValue(binario2.charAt(0));
                r = Integer.toString(f.or(b1, b2));
                for (int i=1; i<binario1.length(); i++) {

                    b1 = Character.getNumericValue(binario1.charAt(i));
                    b2 = Character.getNumericValue(binario2.charAt(i));

                    r = r + Integer.toString(f.or(b1, b2));

                }
                System.out.println("Resposta em binario: "+r);
                System.out.println("Resposta em decimal: "+Integer.parseInt(r,2));
                endereco = Integer.parseInt(IR.substring(12, 16),2);
                f.funcao_set(endereco,Integer.parseInt(r,2));  
                System.out.println("Resultado armazenado no registrador "+endereco);
            }
            
        }
        System.out.println("Valor dos registradores após execução da instrução:");
        for (int i=0;i<f.registradores.size();i++)
        {
            System.out.println("Registrador "+i+" = "+f.registradores.get(i));
        }
          
        
    }
        
  }
}

