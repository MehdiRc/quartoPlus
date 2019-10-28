package quartoplus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//import iia.jeux.modele.joueur.Joueur;

public class PlateauQuartoplus{

	public final static int dimensionPlateau = 4; //les dim du plateau
	
	private String[][] lePlateau; //le plateau representer par un tableau 2d de String
	
	private ArrayList<String> stockPiece; //reserve des pieces de chaques joueurs
	
	/** Le joueur que joue "bleu" */
	//private static Joueur joueurBlanc;

	/** Le joueur que joue "rouge" */
	//private static Joueur joueurNoir;
	
	//plateau par default
	public PlateauQuartoplus() {
		this.lePlateau = new String[4][4];
		for (int i =0; i<lePlateau.length; i++) {
			for (int j=0; j<lePlateau.length; j++) {
				this.lePlateau[i][j]=null;
			}
		}
		setPieceInit();
	}
	
	/**
	 * variante du constructeur prenant un plateau existant
	 * @param newPlateau le plateau a utiliser
	 * @param pieces la liste des pieces restantes
	 */
	public PlateauQuartoplus(String[][] newPlateau, ArrayList<String> pieces) {
		this.lePlateau = newPlateau;
		this.stockPiece = pieces;
	}
	
	/*public PlateauQuartoplus(String[][] newPlateau, ArrayList<String> pieces) {
		this.lePlateau = new String[4][4];
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				this.lePlateau[i][j] = newPlateau[i][j];
			}
		}
		
	}*/
	
	/**
	 * fonction d'initialisation des pieces
	 */
	private void setPieceInit() {
		this.stockPiece = new ArrayList<String>();
		
		this.stockPiece.add("bppr");
		this.stockPiece.add("bppc");
		this.stockPiece.add("bptr");
		this.stockPiece.add("bptc");
		this.stockPiece.add("bgpr");
		this.stockPiece.add("bgpc");
		this.stockPiece.add("bgtr");
		this.stockPiece.add("bgtc");

		this.stockPiece.add("rppr");
		this.stockPiece.add("rppc");
		this.stockPiece.add("rptr");
		this.stockPiece.add("rptc");
		this.stockPiece.add("rgpr");
		this.stockPiece.add("rgpc");
		this.stockPiece.add("rgtr");
		this.stockPiece.add("rgtc");
	}
	
	/**
	 * fonction testant si la forme d'un piece est valide
	 * @param s une piece sous la forme "bgpc"
	 * @return
	 */
	private boolean checkPiece(String s) {
		if (s.length()!=4) {
			System.out.println(s.length());
			return false;
		}
		else {
			if (!(s.substring(0,1).equals("b") || s.substring(0,1).equals("r"))) {
				return false;
			}
			if (!(s.substring(1,2).equals("p") || s.substring(1,2).equals("g"))) {
				return false;
			}
			if (!(s.substring(2,3).equals("p") || s.substring(2,3).equals("t"))) {
				return false;
			}
			if (!(s.substring(3,4).equals("r") || s.substring(3,4).equals("c"))) {
				return false;
			}
		}
		
		return true;
		
	}
	
	/**
	 * fonction permettant de remplir une ligne de tableau a partir d'une ligne texte
	 * @param s une ligne correspondant au format du fichier save
	 * @param i la ligne du tableau à remplir
	 */
	private void stackLineInTab(String s, int i) {
		int nextChar= 1; //position du char considerer 
		int positionJ=0; //position
		
		//String leChar= s.substring(nextChar, nextChar+1);
		
		while (nextChar<s.length()) {
			String leChar= s.substring(nextChar, nextChar+1);
			if (leChar.equals("b") || leChar.equals("r")) {
				if (checkPiece(s.substring(nextChar, nextChar+4))) {
					if (positionJ >=4) { throw new RuntimeException("le fichier remplit trop de case");}
					this.lePlateau[i][positionJ]=s.substring(nextChar, nextChar+4); //ajoute la piece dans le tableau
					this.stockPiece.remove(s.substring(nextChar, nextChar+4)); //et l'enleve de la liste des pieces pouvant être jouer
					nextChar= nextChar+4;
					positionJ++;
				}
				else {
					throw new RuntimeException("Erreur piece : " + s.substring(nextChar, nextChar+4));
				}
			}
			else if (leChar.equals("+")) {
				if (positionJ >=4) { throw new RuntimeException("le fichier remplit trop de case");}
				this.lePlateau[i][positionJ] = null;
				nextChar++;
				positionJ++;
			}
			else {
				nextChar++;
			}
			
		}
		if(positionJ!=4) {
			throw new RuntimeException("remplissage anormal");
		}
		
		
	}
	
	
	public void setFromFile(String fileName) {

		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			try {
				String line = br.readLine();
				this.setPieceInit();
				
				int laLigne = 0; //ligne considerer
				while (line != null) {
					String firstChar = line.substring(0, 1);
					
					if (firstChar.equals("1") ||firstChar.equals("2")  ||firstChar.equals("3")  ||firstChar.equals("4") ) {
						stackLineInTab(line, laLigne);
						laLigne++;
						line = br.readLine();
					}
					else if (firstChar.equals("%")) {
						line = br.readLine();
					}
					else {
						throw new RuntimeException("Premier caratere invalide : " + firstChar);
					}
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			fr.close();
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	public void saveToFile(String fileName) {

		String res = new String("");
		res += "% Etat du plateau \n"; //commentaire pour la forme
		res += "% ABCD \n";
		for (int i=0; i<this.lePlateau.length; i++) {
			res += Integer.toString(i+1) + " ";
			for (int j=0; j<this.lePlateau[0].length; j++) { // on ajoute les piece à la chaine de caractere
				if (lePlateau[i][j]==null) {
					res += "+";
				}
				else {
					res += lePlateau[i][j];
				}
				
			}
			res += " " + Integer.toString(i+1) + "\n";
		}
		res += "% ABCD \n";
		
		try {
			FileWriter file = new FileWriter(fileName); //on ecri dans le fichier
			BufferedWriter out = new BufferedWriter(file);
			out.write(res);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public boolean estchoixValide(String choose, String player) {
		return stockPiece.contains(choose);
	}
	
	/**
	 * foction convertissant un caractere en entier
	 * @param letter
	 * @return
	 */
	private int letterToX(char letter){
		int i=0;
		if(letter>='A' && letter<='D')
			i = ((int)letter-'A'+1);
		else if(letter>='a' && letter<= 'd')
	        i = ((int)letter - 'a'+1);
		else throw new RuntimeException("Charactere non valide pour Coordonees ") ;
		
		return i-1;
	}
	
	/**
	 * fonction convertissante un entier en lettre pour les coordonnées
	 * @param coord
	 * @return
	 */
	private char xtoLetter(int coord){
		if (coord <0 || coord>3)
			 throw new RuntimeException("Coordonees non valides") ;
		else return (char)((int)'A'+ coord);
	}
	
	/**
	 *  fonction permettant de recupérer la coordonées "x" d'un coup "B3"
	 * @param s
	 * @return
	 */
	private int getX(String s){
		return letterToX(s.charAt(0));
	}
	
	/**
	 * fonction permettant de recupérer la coordonées "y" d'un coup "B3"
	 * @param s
	 * @return
	 */
	private int getY(String s){
		return (Character.getNumericValue(s.charAt(1))-1) ;
	}
	
	public boolean estmoveValide(String move, String player) {
	if(move.length()>=2){
		
		int i = getX(move);	
		int j = getY(move);
		if (j <0 || j>3)
			return false;
		
		if(lePlateau[i][j]==null)
			return true;
		else return false;
		}
		else return false;
	}

	//encienne classe l utilisation d array est plus commode que de tableau de string
	
	/*public String[] mouvementsPossibles(String player) {
		ArrayList<String> res = new ArrayList<String> ();
		for (int i =0; i<lePlateau.length; i++){
			for (int j =0; j<lePlateau[0].length; j++){
				String move = xtoLetter(i)+Integer.toString(j+1);
				if(estmoveValide(move ,player)){
					res.add(move);
				}
			}
		}
		
		String [] resultat = new String[res.size()];
		resultat = res.toArray(resultat);
		return resultat;
	}

	public String[] choixPossibles(String player) {
		
		String [] res = new String[stockPiece.size()];
		res = stockPiece.toArray(res);
		return res;
	}*/
	
	public ArrayList<String> mouvementsPossibles(String player) {
		ArrayList<String> res = new ArrayList<String> ();
		for (int i =0; i<lePlateau.length; i++){
			for (int j =0; j<lePlateau[0].length; j++){
				String move = xtoLetter(i)+Integer.toString(j+1);
				if(estmoveValide(move ,player)){
					res.add(move);
				}
			}
		}
		return res;
	}

	public ArrayList<String> choixPossibles(String player) {
		ArrayList<String> res = new ArrayList<String> ();
		for (String a: stockPiece) { res.add(a);}
		return res;
	}

	public void play(String choose, String move, String player) {
		if(estmoveValide(move, player) && estchoixValide(choose, player) ){
			int i = getX(move);	
			int j = getY(move);
		
			lePlateau[i][j]= choose;
		}
		
	}
	
	public void removePiece(String choose, String player) {
		stockPiece.remove(choose);
	}

	/**
	 * fonction auxiliaire permettant de voir si 4 pieces posseden des points communs
	 * @param a1 piece 1
	 * @param a2 piece 2
	 * @param a3 piece 3
	 * @param a4 piece 4
	 * @return si les 4 pieces ont des poins communs
	 */
	private boolean checkCommun(String a1, String a2, String a3, String a4) {

		if (a1==null || a2==null|| a3==null || a4==null) { //cas ou il y a un + -> pas la peine
			return false;
		}
		
		for (int i=0; i<4; i++) { //les 4 caracteristiques possible
			
			if ((a1.substring(i, i+1).equals(a2.substring(i, i+1))) 
					&& (a1.substring(i, i+1).equals(a3.substring(i, i+1)))
					&& (a1.substring(i, i+1).equals(a4.substring(i, i+1)))) {
				return true;
			}
			
		}
		
		return false;
	}
	
	public boolean finDePartie() {
		
		for (int i=0; i<4; i++) { //cas ligne
			if (checkCommun(lePlateau[i][0], lePlateau[i][1], lePlateau[i][2], lePlateau[i][3])) {
				return true;
			}
		}
		for (int i=0; i<4; i++) { //cas ligne
			if (checkCommun(lePlateau[0][i], lePlateau[1][i], lePlateau[2][i], lePlateau[3][i])) {
				return true;
			}
		}
		
		for (int i=0; i<3; i++) { //cas carre
			for (int j=0; j<3; j++) {
				if (checkCommun(lePlateau[i][j], lePlateau[i][j+1], lePlateau[i+1][j], lePlateau[i+1][j+1])) {
					return true;
				}
			}
		}
		
		if (checkCommun(lePlateau[0][0], lePlateau[1][1], lePlateau[2][2], lePlateau[3][3])) { //diagonal
			return true;
		}
		if (checkCommun(lePlateau[0][3], lePlateau[1][2], lePlateau[2][1], lePlateau[3][0])) {
			return true;
		}
		
		
		return false;
	}
	
	/**
	 * fonction convertissant le talbeau en un string
	 */
	public String toString() {
		String retstr = new String("");
		retstr += "%   A     B     C     D \n";
		for(int i=0; i < this.lePlateau.length; i++) {
			retstr += Integer.toString(i+1) + " ";
			for (int j=0; j < this.lePlateau[0].length; j++)
				if (lePlateau[j][i]== null)
					retstr += "[++++]";
				else 	// damier[i][j] == NOIR
					retstr += "[" + lePlateau[j][i] + "]";
			retstr += " " + Integer.toString(i+1) + "\n";
		}
		retstr += "%   A     B     C     D \n";
		return retstr;
	}
	
	/**
	 * fonction permettan d'afficher les pieces restantes
	 */
	public void affichePieceRestante() {
		for (String a: this.stockPiece) {
			System.out.print(a +" ");
		}
		System.out.println("");
	}
	
	
	public void afficheTableauStrings(String [] s) {
		for (String a: s) {
			System.out.print(a +" ");
		}
		System.out.println();
	}
	
	public void afficheArrayListStrings(ArrayList <String> s) {
		for (String a: s) {
			System.out.print(a +" ");
		}
		System.out.println();
	}
	
	public String[][] getPlateau(){
		return this.lePlateau;
	}
	
	
	public PlateauQuartoplus copy() {
		
		ArrayList<String> clone = new ArrayList<String>();
		for (String a: stockPiece) {clone.add(a);}
		
		String[][] clo = new String[4][4];
		
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				clo[i][j]=lePlateau[i][j];
			}
		}
		
		
		PlateauQuartoplus p2 = new PlateauQuartoplus(clo, clone);
		return p2;
	}
	
   /* public static void main(String[] args) {
    	
    	PlateauQuartoplus p = new PlateauQuartoplus();
    	//p.saveToFile("patate");
    	//p.setFromFile("patate");
    	System.out.println(p.toString());
    	//p.stockPiece.remove("bgpc");
    	p.affichePieceRestante();

    	System.out.println(p.finDePartie());
    	
    	p.play("bppr", "D4", "noir");
    	p.removePiece("bppr", "noir");
    	
    	System.out.println(p.toString());
    	p.afficheArrayListStrings(p.stockPiece);
    	p.afficheTableauStrings(p.choixPossibles("noir"));
    	p.afficheTableauStrings(p.mouvementsPossibles("noir"));
    	
    }*/
	
	

}
