import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AlinhamentoLocal {
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

	private static String[] alinharSequencia(String linha1, String linha2, String linha3) {
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
		
		System.out.println();
		
		sequencias = alinha(matriz,linha2,linha3);
		
		return sequencias;
	}
	
	private static String[] alinha(int[][] matriz, String linha2, String linha3) {
		int tamanhoA = linha2.length()+1;
		int tamanhoB = linha3.length()+1;
		
		StringBuilder linhaA = new StringBuilder(linha2);
		StringBuilder linhaB = new StringBuilder(linha3);
		
		int maximo[] = encontrarMaximo(matriz,tamanhoA,tamanhoB);
		
		System.out.println(matriz[maximo[0]][maximo[1]]);
		
		int i = maximo[0], j = maximo[1];
		
		
		
		while(matriz[i+tamanhoA][j] > 0){
			if(matriz[i+tamanhoA][j] == 1){
				linhaA.insert(i, '_');
				i--;
			}else if(matriz[i+tamanhoA][j] == 2){
				linhaB.insert(j, '_');
				
				j--;
			}else{
				i--;
				j--;
			}
		}
		
		while(i > 0){
			linhaB.insert(0, '_');
			i--;
		} 
		
		String sequencias[] = new String[2];
		sequencias[0] = linhaA.toString();
		sequencias[1] = linhaB.toString();
		return sequencias;
	}

	private static int[] encontrarMaximo(int[][] matriz, int tamanhoA, int tamanhoB) {
		int maximo[] = new int[2];
		int max = matriz[1][1];
		
		for(int i = 1; i < tamanhoA; i++){
			for(int j = 1; j < tamanhoB; j++){
				if(matriz[i][j] > max){
					max = matriz[i][j];
					maximo[0] = i;
					maximo[1] = j;
				}
			}
		}
		
		return maximo;
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
		int max;
		if(x > y && x > z){
			max = x;
		}
		else if(y > x && y > z){
			max = y;
		}
		else if(z > x && z > y){
			max = z;
		}
		else if(x == y && x > z){
			max = x;
		}
		else if(x == z && x > y){
			max = x;
		}
		else if(z == y && z > x){
			max = z;
		}
		else max = x;
		
		if(max > 0) return max;
		else return 0;
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
