package quartoplus;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;


////////////////////////////////////////////////////////////////////////////////////
//CLASSE DE NOTRE JOUEUR
///////////////////////////////////////////////////////////////////////////////////

public class Joueur1 implements IJoueur {

	static final int BLANC = -1;
    static final int NOIR = 1;
	
    private int maCouleur;
    private String maCouleurString;
    
    private PlateauQuartoplus lePlateau;
    
    private String laPieceAJouer;
    private String laPieceChoisie;
    
    private boolean debut = true;
    
    private Heuristique h;
    private AlgoJeu algo; //algo principale
    private AlgoJeu algo2; //algo de debut de partie
    
    int countTour=0;
    
    long tpsMax = 300000000; 
    
    public Joueur1() {
    	this.lePlateau=new PlateauQuartoplus();
    }
    
	@Override
	public void initJoueur(int mycolour) {
		this.maCouleur=mycolour;
		if (this.maCouleur==-1) {
			this.maCouleurString="BLANC";
			System.out.println("je suis blanc");
			h = new Heuristique1("BLANC");
			algo = new Algo2(h, "NOIR", "BLANC");
			algo2 = new Algo3(h, "NOIR", "BLANC");
		}
		else {
			this.maCouleurString="NOIR";
			System.out.println("je suis noir");
			h = new Heuristique1("NOIR");
			algo = new Algo2(h, "BLANC", "NOIR");
			algo2 = new Algo3(h, "BLANC", "NOIR");
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
				
				String choixIA;
				
				ThreadMXBean thread = ManagementFactory.getThreadMXBean();
				
				if(countTour<4) {
					choixIA = algo2.meilleurCoup(this.lePlateau, this.laPieceAJouer, 4);
				}
				else if (countTour < 5){
					
					long nanos = thread.getCurrentThreadCpuTime();
					
					choixIA = algo.meilleurCoup(this.lePlateau, this.laPieceAJouer, 5);
					System.out.println("j'ai fini en " + (thread.getCurrentThreadCpuTime()-nanos)/100000000 + " s ");
				}
				else {
					long nanos = thread.getCurrentThreadCpuTime();
					
					choixIA = algo.meilleurCoup(this.lePlateau, this.laPieceAJouer, 6);
					System.out.println("j'ai fini en " + (thread.getCurrentThreadCpuTime()-nanos)/100000000 + " s ");
				}

				this.countTour++;

				System.out.println("je choisie le placement " + choixIA.substring(0, 2));
				
				this.lePlateau.play(laPieceAJouer, choixIA.substring(0, 2), maCouleurString);
				this.lePlateau.removePiece(laPieceAJouer, "");
				

				System.out.println("je choisie la piece " + choixIA.substring(choixIA.length()-4, choixIA.length()));
				this.laPieceChoisie=(choixIA.substring(choixIA.length()-4, choixIA.length()));
				
				System.out.println(this.lePlateau.toString()); // pour solo

				return choixIA;
				
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
			
			this.countTour++;
			
			System.out.println(this.lePlateau.toString()); // pour solo
			
			//System.out.println("\nvaleur heuristique = " + this.h.eval(lePlateau, maCouleurString) + "\n");
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
		return "Joueur1";
	}
	
	private int alea(int i) {
		return (int)(Math.random()*(i));
	}

}

