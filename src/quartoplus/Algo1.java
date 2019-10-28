package quartoplus;

import java.util.ArrayList;
////////////////////////////////////////////////////////////////////////////////////
//TENTATIVE ALPHA BETA
///////////////////////////////////////////////////////////////////////////////////
//non fonctionnel abandonner pour une autre version: algo2

public class Algo1 implements AlgoJeu{
	 
	private final static int PROFMAXPARDEF=3;
	
	private Heuristique h;
	
	private String joueurMin;
	private String joueurMax;
	
	private int profMax;
	
    private int nbnoeuds;
    private int nbfeuilles;
	
    private String piecePourApres;
	
	public Algo1(Heuristique heur, String joueurMin, String joueurMax) {
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
    	
    	ArrayList<String> mp = new ArrayList<String>();
		for (String a: p.mouvementsPossibles(null)) {mp.add(a);}
    	
    	//ArrayList<String> mp = p.mouvementsPossibles(joueurMax); // position
    	
    	//cp.remove(piece); // bon peut-être un bug ici mais pas sur 
    	
    	
    	String bestCombo= "";
    	String mouvement = "";
    	String choix = "";
    	
    	String bestMouvement = "";
    	String bestChoix = "";
    	
    	if (cp.size()>0) { // normalement c'est cencé s'arrêter avant car si =0 => match nul
    		
    		mouvement = mp.get(0); //position
    		bestMouvement=mouvement;
    		PlateauQuartoplus p2 = p.copy();
    		
    		p2.play(piece, mouvement, null); //on jou
    		p2.removePiece(piece, null);//on enleve la piece
    		System.out.println("je suis a 1");
    		
    		cp = p2.choixPossibles(null);
    		
    		choix = cp.get(0); //on initilialise avec le premier choix
       		//cp.remove(choix);
       		
    		bestChoix = choix;
    		alpha2 = minMax (p2, this.profMax-1, alpha2, beta2, choix);//on initialise le alpha du premier choix
    		
    		cp.remove(choix);
    		System.out.println("je suis a 2");
    		
    		for (String c: cp) { // on regarde pour tout les coups
    			int tmp = minMax(p2, this.profMax-1, alpha2, beta2, c); //on calcule le alpha de l'autre coup
    			//System.out.println(p2.toString());
    			if (alpha2< tmp){ //on met a jour
    				System.out.println("j'ai trouvé un meilleur coup");
    				System.out.println("debug princ1: j'ai un meilleur alpha " + alpha2 + " pour le choix " + c + " et le mouvement " + bestMouvement);
    				alpha2=tmp;
    				bestChoix=c;
    			}
    		}//fin du premier positionnement/initialisation
    		
    		System.out.println("a l'init j'ai " + alpha2 + " pour le choix " + bestChoix + " et le mouvement " + bestMouvement);
    		
    		mp.remove(mouvement);
    		
    		System.out.println("je suis a 3");
    		
    		for (String m : mp) { //pour tout les autres positions
    			p2 = p.copy();
    			
    			//System.out.println(p2.toString());
    			
    			p2.play(piece, m, null);
    			p2.removePiece(piece, null);
    			
    			cp=p2.choixPossibles(null);
    			
    			for (String c : cp) { //pour tout les coup
    				int tmp = minMax(p2, this.profMax-1, alpha2, beta2, c); //on calcule le alpha de l'autre coup
        			if (alpha2< tmp){ //on met a jour
        				System.out.println("debug princ: j'ai un meilleur alpha " + alpha2 + " pour le choix " + c + " et le mouvement " + m);
        				alpha2=tmp;
        				bestChoix=c;
        				bestMouvement=m;
        			}
    			}
    		}
    		
    		
    	}
		
    	bestCombo = (bestMouvement + "-" + bestChoix);
    	
		return bestCombo;
	}
	
	
	
	
	
	private int maxMin(PlateauQuartoplus p, int stop, int alpha, int beta, String choix){

    	ArrayList<String> mp = p.mouvementsPossibles(joueurMax);
    	
    	if(stop<=0 || p.finDePartie()){
    		//this.nbfeuilles++;
    		//this.nbnoeuds--; //var on a déjà incrementer avant le noeuds
    		int res = h.eval(p, joueurMax);
    		//System.out.println("debug maxMin: j'ai trouvé une heuristique de " + res + " avec une prof de " + stop);
    		return res;
    	}
    	else {
    		for (String m: mp) {
    			PlateauQuartoplus p2 = p.copy();
    			p2.play(choix, m, null);
    			p2.removePiece(choix, null);
    			//System.out.println("debug Maxmin: je test en posant la piece en " + m);
    			ArrayList<String> cp = p2.choixPossibles(joueurMin);
    			
    			for (String c : cp) {
	    			//this.nbnoeuds++; 
	    			//PlateauQuartoplus p2 = p.copy();
	    			//p2.play(c, m, joueurMax);
	    			//alpha = Integer.max(alpha, minMax(p,stop-1, alpha, beta));
    				//System.out.println("debug maxMin: je test avec la pice " + c);
	    			alpha = Integer.max(alpha, minMax(p,stop-1, alpha, beta,c));
	    			/*if (alpha >= beta) {
	    				return beta;
	    			}*/
    			}
    			/*if (alpha >= beta) {
    				return beta;
    			}*/
    		}
    	}
    	
    	return alpha;
    }
    
    private int minMax(PlateauQuartoplus p, int stop, int alpha, int beta, String choix){
    	
    	ArrayList<String> mp = p.mouvementsPossibles(joueurMin);
    	
    	if(stop<=0 || p.finDePartie()){//si on atteint la fin du stop ou qu'on a gagné
    		//this.nbfeuilles++;
    		//this.nbnoeuds--;
    		int res = h.eval(p, joueurMin);
    		//System.out.println("debug minMax: j'ai trouvé une heuristique de " + res + " avec une prof de " + stop + " avec choix " + choix);
    		return res;
    	}
    	else {
    		/*for (String c : cj) {
    			this.nbnoeuds++;
    			PlateauQuartoplus p2 = p.copy();
    			p2.play(joueurMin, c);
    			beta = Integer.min(beta, maxMin(p2,stop-1, alpha, beta));
    			if (alpha >= beta) {
    				return alpha;
    			}
    		}*/
    		for (String m: mp) { // pour chaque position
    			PlateauQuartoplus p2 = p.copy();
    			p2.play(choix, m, null);
    			p2.removePiece(choix, null);
    			
    			ArrayList<String> cp = p2.choixPossibles(joueurMin);
    			
    			for (String c : cp) { //pour chaque piece donné
    				//beta = minMax(p2, this.profMax-1, alpha2, beta2, c); //on calcule le alpha de l'autre coup
    				beta = Integer.min(beta, maxMin(p2, stop-1, alpha, beta, c));
        			/*if (alpha>=beta){ //on met a jour
        				return alpha;
        			}*/
    			}
    			/*if (alpha>=beta){ //on met a jour
    				return alpha;
    			}*/
    		}
    		
    	}
    	
    	return beta;
    }
	

}
