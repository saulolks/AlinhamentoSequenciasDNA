import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AlinhamentoSemiGlobal {
	
	public static void main(String[] args){
		Scanner ler = new Scanner(System.in);
		String linha1 = new String();
		String linha2 = new String();
		String linha3 = new String();
	    try {
	      FileReader arq = new FileReader("sequencias.txt");
	      BufferedReader lerArq = new BufferedReader(arq);
	 
	      linha1 = lerArq.readLine();
	      linha2 = lerArq.readLine();
	      linha3 = lerArq.readLine();
	 
	      arq.close();
	    } catch (IOException e) {
	        System.err.printf("Erro na abertura do arquivo: %s.\n",
	          e.getMessage());
	    }
	    
	    
		String[] sequencias = alinharSequencia(linha1,linha2,linha3);
	    
		
	 
	    System.out.println(sequencias[0]);
	    System.out.println(sequencias[1]);
	}
	
	public static String[] alinharSequencia(String linha1, String linha2, String linha3){
		String sequencias[] = new String[2];
		int custoA = calculaPesos(linha1,1);
		int custoM = calculaPesos(linha1,2);
		int custoG = calculaPesos(linha1,3);
		
		int matriz[][] = gerarMatriz(linha2,linha3,custoA,custoM,custoG);
		
		for(int i = 0; i <= 2*linha2.length()+1; i++){
			for(int j = 0; j <= linha3.length(); j++){
				System.out.print(matriz[i][j] + " ");
			}
			System.out.println();
		}
		
		sequencias = alinha(matriz,linha2,linha3);
		
		return sequencias;
	}
	
	private static String[] alinha(int[][] matriz, String linha2, String linha3) {
		int tamanhoA = linha2.length()+1;
		int tamanhoB = linha3.length()+1;
		
		System.out.println();
		
		int i = 2*tamanhoA-1;
		int j = tamanhoB-1;
		
		int maximo[] = retornaMaximo(matriz, tamanhoA, tamanhoB);
		
		StringBuilder linhaA = new StringBuilder(linha2);
		StringBuilder linhaB = new StringBuilder(linha3);
		
		if(maximo[0] == 1){
			System.out.println(matriz[i-tamanhoA][maximo[1]]);
			int a = maximo[1];
			while(j > a){
				linhaA.insert(i - tamanhoA, '_');
				j--;
			}
		}
		if(maximo[0] == 2){
			int a = maximo[1]+tamanhoA;
			int b = matriz[maximo[1]][j];
			System.out.println(b);
			while(i > a){
				linhaB.insert(linhaB.length(), '_');
				i--;
			}
		}
		
		while(i > linha2.length() && j > 0){
			if(matriz[i][j] == 1){
				linhaB.insert(j, '_'); 
				i--;
			}else if(matriz[i][j] == 2){
				linhaA.insert(i - tamanhoA, '_');
				
				j--;
			}else{
				i--;
				j--;
			}
		}
		while(i > linha2.length()+1){
			linhaB.insert(0, '_');
			i--;
		}
		while(j > 0){
			linhaA.insert(0, '_');
			j--;
		}
		String sequencias[] = new String[2];
		sequencias[0] = linhaA.toString();
		sequencias[1] = linhaB.toString();
		return sequencias;
	}
	
	static int[] retornaMaximo(int[][] matriz, int i, int j){
		int maximoI = matriz[i-1][1], posicaoI = 0;
		int maximoJ = matriz[1][j-1], posicaoJ = 0;
		
		int solucao[] =  new int[2];
		
		for(int a = 1; a < j; a++){
			if(matriz[i-1][a] > maximoI){
				maximoI = matriz[i-1][a];
				posicaoI = a;
			}
		}
		for(int a = 1; a < i; a++){
			if(matriz[a][j-1] > maximoJ){
				maximoJ = matriz[a][j-1];
				posicaoJ = a;
			}
		}
		
		if(maximoI > maximoJ){
			solucao[0] = 1;
			solucao[1] = posicaoI;
		}
		else{
			solucao[0] = 2;
			solucao[1] = posicaoJ;
		}
		return solucao;
	}

	private static int[][] gerarMatriz(String linha2, String linha3, int custoA, int custoM, int custoG) {
		int tamanhoA = linha2.length()+1;
		int tamanhoB = linha3.length()+1;
		int matriz[][] = new int[2*tamanhoA][tamanhoB];
		
		int caso1, caso2, caso3;
		for(int i = 0; i < tamanhoA; i++){
			for(int j = 0; j < tamanhoB; j++){
				if(i == 0){
					matriz[i][j] = 0;
					matriz[tamanhoA+i][j] = 0;
				}
				else if(j == 0){
					matriz[i][j] = 0;
					matriz[tamanhoA+i][j] = 0;
				}
				else{
					caso1 = matriz[i-1][j] + custoG;
					caso2 = matriz[i][j-1] + custoG;
					caso3 = matriz[i-1][j-1] + p(i,j,linha2,linha3,custoA,custoM);
					matriz[i][j] = max(caso1,caso2,caso3);
					if(matriz[i][j] == caso1) matriz[tamanhoA+i][j] = 1;
					else if(matriz[i][j] == caso2) matriz[tamanhoA+i][j] = 2;
					else matriz[tamanhoA+i][j] = 3;
				}
				
			}
		}
		return matriz;
	}
	
	public static int p(int i, int j, String linha2, String linha3, int custoA, int custoM){
		if(linha2.charAt(i-1) == linha3.charAt(j-1)) return custoA;
		else return custoM;
	}
	
	public static int max(int x, int y, int z){
		if(x > y && x > z){
			return x;
		}
		else if(y > x && y > z){
			return y;
		}
		else if(z > x && z > y){
			return z;
		}
		else if(x == y && x > z){
			return x;
		}
		else if(x == z && x > y){
			return x;
		}
		else if(z == y && z > x){
			return z;
		}
		else return x;
	}
	

	public static int calculaPesos(String linha1, int custo){
		int v[] = new int[3];
		int x = 0, y = 0;
		
		for(int i = 0; i < linha1.length();){
			if(Character.isDigit(linha1.charAt(i))){
				y = i;
				while(y <= 6){
					if(Character.isDigit(linha1.charAt(y))) y++;
					else break;
				}
				v[x++] = Integer.parseInt(linha1.substring(i,y));
				i = y;
			}else if(linha1.charAt(i) == ' '){
				i++;
			}else{
				y = i+1;
				while(y <= 6){
					if(Character.isDigit(linha1.charAt(y))) y++;
					else break;
				}
				v[x++] = -1*Integer.parseInt(linha1.substring(i+1,y));
				i = y;
			}
		}
		
		if(custo == 1) return v[0];
		else if(custo == 2) return v[1];
		else return v[2];
		
	}

}
