package quartoplus;

import java.util.ArrayList;


////////////////////////////////////////////////////////////////////////////////////
//ALPHA BETA
///////////////////////////////////////////////////////////////////////////////////


public class Algo2 implements AlgoJeu{
	 
	private final static int PROFMAXPARDEF=5;
	
	private Heuristique h;
	
	private String joueurMin;
	private String joueurMax;
	
	private int profMax;
	
   private int nbnoeuds;
   private int nbfeuilles;
	
   private String piecePourApres;
	
	public Algo2(Heuristique heur, String joueurMin, String joueurMax) {
		this.joueurMin=joueurMin;
		this.joueurMax=joueurMax;
		this.h=heur;
		this.nbfeuilles=0;
		this.nbnoeuds=0;
		this.profMax = PROFMAXPARDEF;
	}
	
	
	@Override
	public String meilleurCoup(PlateauQuartoplus p, String piece, int prof) { //juste la piece
		
		this.profMax=prof;
		
		int alpha = -10000000;
		int beta = 10000000;
		
		int alpha2 = 1000;
		int beta2 = -1000;
		
	   	ArrayList<String> cp = p.choixPossibles(joueurMax); //pieces
	   	

	   	ArrayList<String> mp = p.mouvementsPossibles(joueurMax);

	   	
	   	String bestCombo= "";
	   	String mouvement = "";
	   	String choix = "";
	   	
	   	String bestMouvement = "";
	   	String bestChoix = "";
	   	
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
	   			
	   			
	   			alpha2=Integer.min(alpha2, minMax(p2, this.profMax-1, alpha, beta, bestChoix, m)); //revoir param
	   			//min ici avec un 2eme alpha car on cherche le pire cas que l'aderversaire peux jouer
	   			
	   			
	   		}
	   		
	   		alpha = alpha2; //on initilialise du coup le alpha avec le alpha2, 
	   		
	   		System.out.println("initialisation :alpha " + alpha + " choix " + bestChoix + " mouvement " + bestMouvement);
	   		
	   		//fin initialisation des valeurs
	   		//a present il faut faire pareil pour toutes les autres positions ou l'on pose la pieces et tout les placement ou l adversaire peu jouer
	   		
	   		for (String c : cp2 ) {
	   			
	   			boolean update=true;
   	   			int alphatmp=0;
	   			
	   			for (String m : mp2) {
	   				
	   				int tmp = minMax(p2, this.profMax-1, alpha, beta, c, m);
	   				if (tmp<0 || tmp<=alpha) { //si on a un cas ou pour une position le choix est pire que les enciens meilleurs
	   	   				update=false; //ce coup est pas bon on n'update pas
						break; //on s'arrête la et on passe ou choix suivant
					}
	   	   			else {
	   	   				
	   	   				alphatmp=tmp; // on stack la vlaeur du nouveau alpha potentiel
	   	   				
	   	   			}
	   				
	   			}
	   			
   	   			if (update) {
	   	   			alpha=alphatmp; // on met a jour le alpha et les reponses
   	   				System.out.println("boucle 1 :alpha " + alpha + " choix " + c + " mouvement " + bestMouvement);
					bestChoix=c;
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
	   	   			int alphatmp=0;
	   	   			
	   	   			for (String m2 : mp2) { // pour toute les positions restantes
	   	   				
		   	   			int tmp = minMax(p2, this.profMax-1, alpha, beta, c, m2);
		   	   			
		   	   			if (tmp<0 || tmp<=alpha) { //si on a un cas ou pour une position le choix est pire que les enciens meilleurs
		   	   				update=false; //ce coup est pas bon on n'update pas
							break; //on s'arrête la et on passe ou choix suivant
						}
		   	   			else {
		   	   				
		   	   				alphatmp=tmp; // on stack la vlaeur du nouveau alpha potentiel
		   	   				
		   	   			}
	   	   			
	   	   			}
	   	   			
	   	   			if (update) {
		   	   			alpha=alphatmp; // on met a jour le alpha et les reponses
	   	   				System.out.println("boucle 2 :alpha " + alpha + " choix " + c + " mouvement " + m1);
						bestChoix=c;
						bestMouvement=m1;
	   	   			}
	   	   		}
	   	   		
	   		}
	   		
	   		}
			
	   	System.out.println("au final je choisie: alpha " +alpha+ " placement " + bestMouvement + " piece " + bestChoix);
	   	bestCombo = (bestMouvement + "-" + bestChoix);
   	
		return bestCombo;
	}
	
	
	private int maxMin(PlateauQuartoplus p, int stop, int alpha, int beta, String choix, String mouvement){

		PlateauQuartoplus p2 = p.copy(); //on pose la piece
	   p2.play(choix, mouvement, null);
	   p2.removePiece(choix, null);
	   
	   
   	
	   	if(stop<=0 || p2.finDePartie()){//si on atteint la fin du stop ou qu'on a gagné
	   		//this.nbfeuilles++;
	   		//this.nbnoeuds--;
	   		int res = h.eval(p2, joueurMax);
	   		//System.out.println("debug minMax: j'ai trouvé une heuristique de " + res + " avec une prof de " + stop + " avec choix " + choix);
	   		return res;
	   	}
	   	else {
	   		
	   		ArrayList<String> mp = p2.mouvementsPossibles(null);
	   		ArrayList<String> cp = p2.choixPossibles(null);
	   		
	   		for (String c : cp) { // pour tout les choix possibles
	   			for(String m : mp) { //pour toute les positions possible
	   				
	   				alpha = Integer.max(alpha, minMax(p2, stop-1, alpha, beta, c, m));
	   				if (alpha>=beta){ // si on a trouver un coup vraiment pas bon
	   					
		   				return beta;
		   				
		   			}
	   			}
	   		}
	   	}
	   		
	
	   	return alpha;
	}
	
	private int minMax(PlateauQuartoplus p, int stop, int alpha, int beta, String choix, String mouvement){
		
	   PlateauQuartoplus p2 = p.copy(); //on pose la piece
	   p2.play(choix, mouvement, null);
	   p2.removePiece(choix, null);

		
	   	if(stop<=0 || p2.finDePartie()){//si on atteint la fin du stop ou qu'on a gagné
	   		//this.nbfeuilles++;
	   		//this.nbnoeuds--;
	   		int res = h.eval(p2, joueurMin);
	   		//System.out.println("debug minMax: j'ai trouvé une heuristique de " + res + " avec une prof de " + stop + " avec choix " + choix);
	   		return res;
	   	}
	   	else {
	   		
	   		ArrayList<String> mp = p2.mouvementsPossibles(null);
	   		ArrayList<String> cp = p2.choixPossibles(null);
	   		
	   		for (String c : cp) { // pour tout les choix possibles
	   			for(String m : mp) { //pour toute les positions possible
	   				
	   				beta = Integer.min(beta, maxMin(p2, stop-1, alpha, beta, c, m));
	   				if (alpha>=beta){ // si on a trouver un coup vraiment pas bon
		   				return alpha;
		   			}
	   				
	   			}
	   		}
	   	}
	   	
	   	return beta;
   }
	
	
	private int alea(int i) {
		return (int)(Math.random()*(i));
	}
   
   
}
