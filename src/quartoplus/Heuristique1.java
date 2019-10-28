package quartoplus;

import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////////
//HEURISTIQUE
///////////////////////////////////////////////////////////////////////////////////

public class Heuristique1 implements Heuristique{

	private String joueurAmi;
	
	public Heuristique1(String joueurAmi) {
		this.joueurAmi=joueurAmi;
	}
	
	public int eval(PlateauQuartoplus p, String j) {
		int res = 0;
		res = win(p,j);
		res += (1.5)*similitude(p);
		return res;
	}
	
	private int win(PlateauQuartoplus p,String j) {
		int valeur=100;
		if (p.finDePartie()) {
			if (j.equals(joueurAmi)) {
				valeur=200;
			}
			else {
				valeur = -200;
			}
		}
		return (int)(0.5*valeur);
	}
	
	public static ArrayList <String> checkSimilitudes(String [] ligne, int i) {
		String couleur = "v" ; // v = vide 
		String hauteur= "v" ;
		String sommet = "v" ;
		String forme = "v" ;
		
		ArrayList <String> similitudes = new ArrayList <String> ();
		
		for (int j =0; j<4; j++) {
			if(ligne[j] != null) {
				if (couleur.equals("v"))
					couleur = ligne[j].substring(0, 1);
				else if(!couleur.equals("v") && !ligne[j].substring(0, 1).equals(couleur))
					couleur = "n"; // n = none c.a.d PAS DE POINT COMMUN SUR CETTE CARACTERISTIQUE
				
				if (hauteur == null)
					hauteur =ligne[j].substring(1, 2);
				else if(!couleur.equals("v") && !ligne[j].substring(1, 2).equals(hauteur))
					hauteur = "n";
				
			
				if (sommet == null)
					sommet = ligne[j].substring(2, 3);
				else if(!couleur.equals("v")  && !ligne[j].substring(2, 3).equals(sommet))
					sommet = "n";
			
				if (forme == null)
					forme = ligne[j].substring(3, 4);
				else if(!couleur.equals("v")  && !ligne[j].substring(3, 4).equals(forme))
					forme = "n";
			}
						
		}
		
		
		
		if (!couleur.equals("n") && !couleur.equals("v")) {
			//System.out.println(" test 1");
			similitudes.add("color-"+couleur);
		}
		
		if (!hauteur.equals("n") && !couleur.equals("v")) {
			//System.out.println(" test 2");
			similitudes.add("hauteur-"+hauteur);
		}
		
		if (!sommet.equals("n") && !couleur.equals("v")) {
			//System.out.println(" test 3");
			similitudes.add("sommet-"+sommet);
		}
		
		if (!forme.equals("n") && !couleur.equals("v")) {
			//System.out.println(" test 4");
			similitudes.add("sommet-"+forme);
		}
			
		return similitudes;
	}
	
	private int similitude(PlateauQuartoplus p) {
		int acc = 0;
		String[][] lePlateau = p.getPlateau();
		
		for (int i=0; i<4; i++) { //cas ligne
			String[] tmp = {lePlateau[i][0], lePlateau[i][1], lePlateau[i][2], lePlateau[i][3]};
			acc += checkSimilitudes(tmp, i).size();
		}
		for (int i=0; i<4; i++) { //cas ligne
			String[] tmp = {lePlateau[0][i], lePlateau[1][i], lePlateau[2][i], lePlateau[3][i]};
			acc += checkSimilitudes(tmp,i).size();
		}
		
		for (int i=0; i<3; i++) { //cas carre
			for (int j=0; j<3; j++) {
				String[] tmp = {lePlateau[i][j], lePlateau[i][j+1], lePlateau[i+1][j], lePlateau[i+1][j+1]};
				acc += checkSimilitudes(tmp,0).size();
			}
		}
		String[] tmp = {lePlateau[0][0], lePlateau[1][1], lePlateau[2][2], lePlateau[3][3]};
		acc += checkSimilitudes(tmp, 0).size();
		String[] tmp2 = {lePlateau[0][3], lePlateau[1][2], lePlateau[2][1], lePlateau[3][0]};
		acc += checkSimilitudes(tmp2,0).size();
		
		
		return acc;
	}
	
	
	
}
