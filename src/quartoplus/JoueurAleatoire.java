package quartoplus;

import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////////
//JOUEUR ALEATOIRE
///////////////////////////////////////////////////////////////////////////////////
//classe pour bien comprendre comment communiquer avec l arbitre



public class JoueurAleatoire implements IJoueur {

	static final int BLANC = -1;
    static final int NOIR = 1;
	
    private int maCouleur;
    private String maCouleurString;
    
    private PlateauQuartoplus lePlateau;
    
    private String laPieceAJouer;
    private String laPieceChoisie;
    
    private int etapeJeu; //sert a daterminer so l'on est dans le choix de piece ou pose la piece
    //1 = choix de piece
    //2 = pose de piece
    
    private boolean debut = true;
    private Integer obj = new Integer(1);
    private boolean notend=false;
    
    private Heuristique h;
    
    public JoueurAleatoire() {
    	this.lePlateau=new PlateauQuartoplus();
    }
    
	@Override
	public void initJoueur(int mycolour) {
		this.maCouleur=mycolour;
		if (this.maCouleur==-1) {
			this.maCouleurString="BLANC";
			System.out.println("je suis blanc");
			h = new Heuristique1("BLANC");
			this.etapeJeu=1;
		}
		else {
			this.maCouleurString="NOIR";
			System.out.println("je suis noir");
			h = new Heuristique1("NOIR");
			this.etapeJeu=2;
		}
	}

	@Override
	public int getNumJoueur() {
		return this.maCouleur;
	}

	@Override
	public String choixMouvement() {
		
		
		if(!(this.lePlateau.finDePartie())) {

			if(!this.debut) {
				
				ArrayList<String> mpossible = this.lePlateau.mouvementsPossibles(maCouleurString);
				int indexm = alea(mpossible.size());
				System.out.println("je choisie le placement " + mpossible.get(indexm));
				
				this.lePlateau.play(laPieceAJouer, mpossible.get(indexm), maCouleurString);
				this.lePlateau.removePiece(laPieceAJouer, "");
				
				System.out.println("\nvaleur heuristique = " + this.h.eval(lePlateau, maCouleurString) + "\n");
				
				ArrayList<String> cpossible = this.lePlateau.choixPossibles(maCouleurString);
				int indexc = alea(cpossible.size());
				System.out.println("je choisie la piece " + cpossible.get(indexc));
				
				System.out.println(this.lePlateau.toString()); // pour solo
				this.laPieceChoisie= cpossible.get(indexc);
				return (mpossible.get(indexm) + "-" + cpossible.get(indexc));
				
			}
			else {

				ArrayList<String> cpossible = this.lePlateau.choixPossibles(maCouleurString);
				int indexc = alea(cpossible.size());
				System.out.println("je choisie la piece " + cpossible.get(indexc));
				this.debut=false;
				this.laPieceChoisie= cpossible.get(indexc);
				return ("A1-" + cpossible.get(indexc));

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

		}

	}

	@Override
	public String binoName() {
		return "CHAKHCHOUKH_CHARPENTIER";
	}
	
	private int alea(int i) {
		return (int)(Math.random()*(i));
	}

}
