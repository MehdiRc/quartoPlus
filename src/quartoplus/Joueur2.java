package quartoplus;

import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////////
//CLASSE DU JOUEUR UTILISANT JUSTE ALGO3
///////////////////////////////////////////////////////////////////////////////////

public class Joueur2 implements IJoueur {

	static final int BLANC = -1;
    static final int NOIR = 1;
	
    private int maCouleur;
    private String maCouleurString;
    
    private PlateauQuartoplus lePlateau;
    
    private String laPieceAJouer;
    private String laPieceChoisie;
    
    private boolean debut = true;
    
    private Heuristique h;
    private AlgoJeu algo;
    
    
    public Joueur2() {
    	this.lePlateau=new PlateauQuartoplus();
    }
    
	@Override
	public void initJoueur(int mycolour) {
		this.maCouleur=mycolour;
		if (this.maCouleur==-1) {
			this.maCouleurString="BLANC";
			System.out.println("je suis blanc");
			h = new Heuristique1("BLANC");
			algo = new Algo3(h, "NOIR", "BLANC");
		}
		else {
			this.maCouleurString="NOIR";
			System.out.println("je suis noir");
			h = new Heuristique1("NOIR");
			algo = new Algo3(h, "BLANC", "NOIR");
		}
	}

	@Override
	public int getNumJoueur() {
		return this.maCouleur;
	}

	@Override
	public String choixMouvement() {
		
		if(!(this.lePlateau.finDePartie())) {
			
			//synchronized(obj) {
			if(!this.debut) {
				
				String choixIA = algo.meilleurCoup(this.lePlateau, this.laPieceAJouer, 4);
				
				
				
				//ArrayList<String> mpossible = this.lePlateau.mouvementsPossibles(maCouleurString);
				//int indexm = alea(mpossible.size());
				System.out.println("je choisie le placement " + choixIA.substring(0, 2));
				
				this.lePlateau.play(laPieceAJouer, choixIA.substring(0, 2), maCouleurString);
				this.lePlateau.removePiece(laPieceAJouer, "");
				
				//System.out.println("\nvaleur heuristique = " + this.h.eval(lePlateau, maCouleurString) + "\n");
				
				//ArrayList<String> cpossible = this.lePlateau.choixPossibles(maCouleurString);
				//int indexc = alea(cpossible.size());
				System.out.println("je choisie la piece " + choixIA.substring(choixIA.length()-4, choixIA.length()));
				this.laPieceChoisie=(choixIA.substring(choixIA.length()-4, choixIA.length()));
				
				System.out.println(this.lePlateau.toString()); // pour solo
				//this.laPieceChoisie= cpossible.get(indexc);
				return choixIA;
				
			}
			else {
				//synchronized(this.obj) {
					//this.debut=false;
				//}
				//this.obj = 2;
				ArrayList<String> cpossible = this.lePlateau.choixPossibles(maCouleurString);
				int indexc = alea(cpossible.size());
				System.out.println("je choisie la piece " + cpossible.get(indexc));
				this.debut=false;
				this.laPieceChoisie= cpossible.get(indexc);
				return ("A1-" + cpossible.get(indexc));
			//}
			}
		}
		else {
			return "xxxxx";
		}
		
	}

	@Override
	public void declareLeVainqueur(int colour) {
		System.out.println(colour + " a gagn�");

	}

	@Override
	public void mouvementEnnemi(String coup) {
		
		/*System.out.println(this.etapeJeu);
		if(this.etapeJeu==3) { // l'adversaire choisi une piece pour nous
			this.laPieceAJouer = coup.substring(coup.length()-4, coup.length());
			System.out.println("le coup qu'on me donne" +coup);
			//this.laPieceAJouer = coup;
			this.etapeJeu=2;
		}
		else if (this.etapeJeu==4){ //sinon il l'a poser
			//System.out.println(coup);
			this.lePlateau.play(this.laPieceAJouer, coup.substring(0, 2), maCouleurString);
			System.out.println(this.laPieceAJouer+ "     " + coup.substring(0, 2));
			System.out.println("le coup qu a jouoer le mechant" +coup);
			System.out.println(this.lePlateau.toString()); // pour solo
			this.etapeJeu=3;
			//System.out.println(this.lePlateau.toString());
		}
		else {
			System.out.println("erreur mvm adversaire");
		}*/
		//synchronized(obj) {
		if(!this.debut) {
			System.out.println("le coup qu'on me donne " +coup);
			this.laPieceAJouer = coup.substring(coup.length()-4, coup.length());
			System.out.println("je dois jouer  " + this.laPieceAJouer);
			System.out.println("il a jouer en " + coup.substring(0,2));
			
			this.lePlateau.play(this.laPieceChoisie, coup.substring(0,2), "");
			this.lePlateau.removePiece(laPieceChoisie, "");
			
			System.out.println(this.lePlateau.toString()); // pour solo
			
			System.out.println("\nvaleur heuristique = " + this.h.eval(lePlateau, maCouleurString) + "\n");
		}
		else {
			System.out.println("le coup qu'on me donne " +coup);
			this.laPieceAJouer = coup.substring(coup.length()-4, coup.length());
			System.out.println("je dois jouer " + this.laPieceAJouer);
			this.debut=false;
		//}
		}

	}

	@Override
	public String binoName() {
		return "Joueur2";
	}
	
	private int alea(int i) {
		return (int)(Math.random()*(i));
	}
}
