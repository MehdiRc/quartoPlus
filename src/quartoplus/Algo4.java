package quartoplus;

import java.util.ArrayList;


////////////////////////////////////////////////////////////////////////////////////
//MIN MAX
///////////////////////////////////////////////////////////////////////////////////

public class Algo4 implements AlgoJeu{
	 
	private final static int PROFMAXPARDEF=4;
	
	private Heuristique h;
	
	private String joueurMin;
	private String joueurMax;
	
	private int profMax;
	
  private int nbnoeuds;
  private int nbfeuilles;
	
  private String piecePourApres;
	
	public Algo4(Heuristique heur, String joueurMin, String joueurMax) {
		this.joueurMin=joueurMin;
		this.joueurMax=joueurMax;
		this.h=heur;
		this.nbfeuilles=0;
		this.nbnoeuds=0;
		this.profMax = PROFMAXPARDEF;
	}
	
	
	@Override
	public String meilleurCoup(PlateauQuartoplus p, String piece, int prof) { //juste la piece

		
	   	ArrayList<String> cp = p.choixPossibles(joueurMax); //pieces
	   	
	   	/*ArrayList<String> mp = new ArrayList<String>();
			for (String a: p.mouvementsPossibles(null)) {mp.add(a);}*/
	   	ArrayList<String> mp = p.mouvementsPossibles(joueurMax);
	   	
	   	//ArrayList<String> mp = p.mouvementsPossibles(joueurMax); // position
	   	
	   	//cp.remove(piece); // bon peut-être un bug ici mais pas sur 
	   	
	   	
	   	String bestCombo= "";
	   	String mouvement = "";
	   	String choix = "";
	   	
	   	String bestMouvement = "";
	   	String bestChoix = "";
	   	
	   	int max=0;
	   	
	   	if(mp.size()>14) { // cas debut de partie -> random
	   		int indexm = alea(mp.size());
	   		bestMouvement=mp.get(indexm);
	   		
	   		cp.remove(piece);
	   		int indexc = alea(cp.size());
	   		bestChoix = cp.get(indexc);
	   		
	   		return (bestMouvement + "-" + bestChoix);
	   	}
	   	
	   	
	   	if (mp.size()>0) { // normalement c'est cencé s'arrêter avant car si =0 => match nul
	   		
	   		//il faut initialisé le alpha et les valeurs
	   		
	   		bestMouvement = mp.get(0); //la meilleur position ou posé notre piece est par def la premiere
	   		mp.remove(bestMouvement); //on l'enleve
	   		
	   		PlateauQuartoplus p2 = p.copy(); // on fait une copy du tableau avant de faire quoique ce soit
	   		
	   		p2.play(piece, bestMouvement, null); //on jou le coup
	   		p2.removePiece(piece, null); // on l'enleve de la lisete des pieces
	   		
	   		ArrayList<String> cp2 = p2.choixPossibles(joueurMax);
	   		ArrayList<String> mp2 = p2.mouvementsPossibles(joueurMax);
	   		
	   		bestChoix = cp2.get(0);// le meilleur choix par def est le premier
	   		cp2.remove(bestChoix);
	   		
	   		if (p2.finDePartie()) {
	   			return (bestMouvement + "-" + bestChoix);
	   		}
	   		
	   		
	   		for (String m : mp2) { //initialisation du alpha on impose la piece et la position a l'adversaire pour qu'il evalu directement après
	   			
	   			
	   			max = minMax(p2, this.profMax-1, bestChoix, m); 
	   			//initialisation du max
	   			
	   			
	   		}
	   		
	   		
	   		System.out.println("initialisation :max " + max + " choix " + bestChoix + " mouvement " + bestMouvement);
	   		
	   		//fin initialisation des valeurs
	   		//a present il faut faire pareil pour toutes les autres positions ou l'on pose la pieces et tout les placement ou l adversaire peu jouer
	   		
	   		for (String c : cp2 ) {
	   			
	   			boolean update=true;
   	   			int maxtmp=0;
   	   			
	   			for (String m : mp2) {
	   				
	   				int tmp = minMax(p2, this.profMax-1, c, m);
	   				if (tmp<0 || tmp<=max) { //si on a un cas ou pour une position le choix est pire que les enciens meilleurs
	   					update=false; //ce coup est pas bon on n'update pas
	   					break; //on s'arrête la et on passe ou choix suivant
	   				}
	   				else { //sinon on a un choix meilleur que celui de l'initialisation
	   					maxtmp=tmp;
	   				
	   			}
	   			
	   			if (update) {
   					System.out.println("boucle1 :max " + max + " choix " + c + " mouvement " + bestMouvement);
   					max=maxtmp;
   					bestChoix=c;
   	   			}
	   			
	   		}
	   	}
	   		
	   		// la on a fini pour si l'on pose notre piece sue la premiere case
	   		//maintenant il faut faire pour toute les autres case
	   		
	   		for (String m1 : mp) { // pour toute les autres cases
	   			p2 = p.copy();
	   			
	   			p2.play(piece, m1, null); //on jou le coup
	   			p2.removePiece(piece, null);
	   			
	   			cp2 = p2.choixPossibles(joueurMax); // bon la c juste une secu pour etre sur de travailler avec des valeurs safes
	   	   		mp2 = p2.mouvementsPossibles(joueurMax);
	   			
		   		if (p2.finDePartie()) {
	   	   			return (m1 + "-" + bestChoix);
	   	   		}
	   	   		
	   	   		for (String c : cp2) { // pour toutes les pieces restante
	   	   			
	   	   			boolean update=true;
	   	   			int maxtmp=0;
	   	   			
	   	   			for (String m2 : mp2) { // pour toute les positions restantes
	   	   				
		   	   			int tmp = minMax(p2, this.profMax-1, c, m2);
		   	   			
		   	   			if (tmp<0 || tmp<=max) { //si on a un cas ou pour une position le choix est pire que les enciens meilleurs
							break; //on s'arrête la et on passe ou choix suivant
						}
		   	   			else {
		   	   				maxtmp=tmp; // on met a jour le alpha et les reponses
		   	
		   	   			}
	   	   			
	   	   			
	   	   			}
	   	   			
	   	   		if (update) {
   	   				max=maxtmp; // on met a jour le alpha et les reponses
   	   				System.out.println("boucle 2 :max " + max + " choix " + c + " mouvement " + m1);
					bestChoix=c;
					bestMouvement=m1;
   	   			}
	   	   		}
	   	   		
	   		}
	   		
	   		}
			
	   	System.out.println("au final je choisie: max " +max+ " placement " + bestMouvement + " piece " + bestChoix);
	   	bestCombo = (bestMouvement + "-" + bestChoix);
   	
		return bestCombo;
	}
	
	
	
	
	
	private int maxMin(PlateauQuartoplus p, int stop, String choix, String mouvement){

			PlateauQuartoplus p2 = p.copy(); //on pose la piece
		   p2.play(choix, mouvement, null);
		   p2.removePiece(choix, null);
		   
		   int max=0;
		   
		   	if(stop<=0 || p2.finDePartie()){//si on atteint la fin du stop ou qu'on a gagné
		   		//this.nbfeuilles++;
		   		//this.nbnoeuds--;
		   		int res = h.eval(p, joueurMax);
		   		//System.out.println("debug minMax: j'ai trouvé une heuristique de " + res + " avec une prof de " + stop + " avec choix " + choix);
		   		return res;
		   	}
		   	else {
		   		
		   		ArrayList<String> mp = p2.mouvementsPossibles(null);
		   		ArrayList<String> cp = p2.choixPossibles(null);
		   		
		   		max = -100000000;
		   		
		   		for (String c : cp) { // pour tout les choix possibles
		   			for(String m : mp) { //pour toute les positions possible
		   				
		   				max = Integer.max(max, minMax(p2, stop-1, c, m));

		   				
		   			}
		   		}
		   	}
		   		
		   	
		   	return max;
   }
   
   private int minMax(PlateauQuartoplus p, int stop, String choix, String mouvement){
   	
	   PlateauQuartoplus p2 = p.copy(); //on pose la piece
	   p2.play(choix, mouvement, null);
	   p2.removePiece(choix, null);

	   int max = 0;
   	
	   	if(stop<=0 || p2.finDePartie()){//si on atteint la fin du stop ou qu'on a gagné
	   		//this.nbfeuilles++;
	   		//this.nbnoeuds--;
	   		int res = h.eval(p, joueurMin);
	   		//System.out.println("debug minMax: j'ai trouvé une heuristique de " + res + " avec une prof de " + stop + " avec choix " + choix);
	   		return res;
	   	}
	   	else {
	   		
	   		ArrayList<String> mp = p2.mouvementsPossibles(null);
	   		ArrayList<String> cp = p2.choixPossibles(null);
	   		max = 1000000000;
	   		
	   		for (String c : cp) { // pour tout les choix possibles
	   			for(String m : mp) { //pour toute les positions possible
	   				
	   				max = Integer.min(max, maxMin(p2, stop-1, c, m));

	   			}
	   		}
	   	}
	   	
	   	return max;
	   }
	
	private int alea(int i) {
		return (int)(Math.random()*(i));
	}
}

