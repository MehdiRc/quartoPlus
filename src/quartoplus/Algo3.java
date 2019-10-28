package quartoplus;

import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////////
//ALGO DEFENSIF
///////////////////////////////////////////////////////////////////////////////////
//bon c est une ia aleatoire qui gagne quand elle peu et evite de donner un truc qui fai perdre

public class Algo3 implements AlgoJeu{
	 
	private final static int PROFMAXPARDEF=3;
	
	private Heuristique h;
	
	private String joueurMin;
	private String joueurMax;
	
	private int profMax;
	
  private int nbnoeuds;
  private int nbfeuilles;
	
  private String piecePourApres;
	
	public Algo3(Heuristique heur, String joueurMin, String joueurMax) {
		this.joueurMin=joueurMin;
		this.joueurMax=joueurMax;
		this.h=heur;
		this.nbfeuilles=0;
		this.nbnoeuds=0;
		this.profMax = PROFMAXPARDEF;
	}
	
	
	@Override
	public String meilleurCoup(PlateauQuartoplus p, String piece, int prof) { //juste la piece
		
		int alpha = -10000000;
		int beta = 10000000;
		
		int alpha2 = -1000;
		int beta2 = 1000;
		
  	ArrayList<String> cp = p.choixPossibles(joueurMax); //pieces
  	
  	
  	ArrayList<String> mp = p.mouvementsPossibles(joueurMax); // position
  	
  	//cp.remove(piece); // bon peut-être un bug ici mais pas sur 
  	
  	
  	String bestCombo= "";
  	String mouvement = "";
  	String choix = "";
  	
  	String bestMouvement = "";
  	String bestChoix = "";
  	
  	if (mp.size()>0) { // normalement c'est cencé s'arrêter avant car si =0 => match nul
  		
  		for (String m :mp) { //on regarde s il y a un placement qui fait gagner
  			PlateauQuartoplus p2 = p.copy();
  			p2.play(piece, m, null);
  			p2.removePiece(piece, null);
  			
  			if (p2.finDePartie()) {
  				bestMouvement=m;
  				break;
  			}
  		}
  		if (bestMouvement.equals("")) { //sinon on en prend un aleatoire
				int indexm = alea(mp.size());
				bestMouvement = mp.get(indexm);
		}
  		
		PlateauQuartoplus p2 = p.copy();
		p2.play(piece, bestMouvement, null);
		p2.removePiece(piece, null);
  		
		ArrayList<String> jePeuPasJouer = new ArrayList<String>();
		
		cp = p2.choixPossibles(null);
		
  		for (String c : cp) {
  			
  			mp = p2.mouvementsPossibles(null);

  	  		for (String m :mp) { // on regarde si les coup font gagner
  	  			PlateauQuartoplus p3 = p2.copy();
  	  			p3.play(c, m, null);
  	  			p3.removePiece(c, null);
  	  			
  	  			if (p3.finDePartie()) { //si on pert
  	  				
  	  				jePeuPasJouer.add(c); // on l ajoute a une liste temporaire
  	  				break; //on passe au coup suivant
  	  				
  	  			}
  	  		}
  		
  		}
  		
  		if (jePeuPasJouer.size()==cp.size()) { // si tout les coup nous font perdre
  			bestChoix=cp.get(0);
  		}
  		else {
  		
	  		for (String c : jePeuPasJouer) {
	  			cp.remove(c);
	  		}
	  		if(cp.size() >0) {
	  			int indexm = alea(cp.size());
				bestChoix = cp.get(indexm);
	  		}
  		}
  		
  	}
		
  	bestCombo = (bestMouvement + "-" + bestChoix);
  	
	return bestCombo;
	}
	
	private int alea(int i) {
		return (int)(Math.random()*(i));
	}
	
}
