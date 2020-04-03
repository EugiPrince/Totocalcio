package it.polito.tdp.totocalcio.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Il livello della ricorsione indica il numero di partita che sto considerando
 * le partite da a livello-1 sono gia' state decise
 * la partita di indice livello la devo decidere io
 * le partite da livello+1 in poi le decideranno le procedure ricorsive sottostanti
 * 
 * Una soluzione parziale e' un sottoinsieme di RisultatoPartita di lunghezza pari al livello (a livello 0 
 * non ho nessuna partita quindi 0.. a livello 1 avro' gia' messo nella prima posizione un numero, quindi 1...)
 * 
 * Una soluzione totale e' quella in cui ho gia' gli N risultati
 * 
 * Condizione di terminazione: quella in cui il livello e' uguale a N
 * 
 * Generazione delle soluzioni del livello: provando tutti i pronostici definiti per quel livello 
 * 
 * [ "2X", "1", "1X2", "12" ] Lo scompongo in 2 sottoproblemi, per esempio:
 * 
 * [ "2X" ] + [ "1", "1X2", "12" ] liv 0 (ci sta 2 o X... provero' entrambe e vado a liv 1)
 * 
 * Signifca che faro' 2 chiamate ricorsive in questo caso, una per vedere cosa succede con il 2 e un'altra
 * per vedere cosa succedere con la X
 * 
 * Al primo livello di ricorsione terremo conto solo di 2X, quindi ci chiederemo se mettere 2 o X, ai livelli
 * successivi di ricorsione gli passo l'altro sottoproblema.. al primo livello non lo vedo!
 * 
 * Dopodiche' avro':
 *             [ "1", "1X2", "12" ]
 *             [ "1" ] + [ "1X2", "12" ] liv 1
 *             
 * C'e' anche il QUINTO livello di ricorsione, ovvero quello dove il problema non esiste [], questo e'
 * il caso terminale... ho gia' quattro posizioni messe, non rimane altro che prendere questi 4 valori
 */

public class Ricerca {
	
	private Pronostico pronostico;
	private int N;
	private List<Risultato> soluzione;

	//Il metodo cerca e' una sorta di interfaccia verso l'esterno, non fa la ricorsione, non sa cosa sia
	//un livello...
	
	public List<Risultato> cerca(Pronostico pronostico) {
		this.pronostico = pronostico;
		this.N = pronostico.size();
		
		List<RisultatoPartita> parziale = new ArrayList<>(); //creo una lista vuota con zero elementi
		int livello = 0;
		
		this.soluzione = new ArrayList<>();
		
		ricorsiva(parziale, livello); // Sto riempiendo la lista di soluzione
		
		return this.soluzione;
	}
	
	private void ricorsiva(List<RisultatoPartita> parziale,int livello) { //hanno gia' deciso le prime 3 partite, tu decidi la quarta
		
		//caso terminale?
		if(livello==N) {
			//System.out.println(parziale); //Significa che questa soluzione parziale e' una soluzione completa
			this.soluzione.add(new Risultato(parziale));
		}
		else {
			//Caso generale
			//la parziale contiene gli elementi da 0 a liv-1 gia' deciso + io (ho liv) + elementi di liv succesivi
			// i primi non li guardo perche' sono gia' decisi e anche quelli dopo li fara' qualcun altro
			
			PronosticoPartita pp = this.pronostico.get(livello); // come inizio quindi ricevo 2X
			//pp sono i sotto-problemi da provare: quello in cui il risultato comincia con 2 e quello con X
			//allora proviamoli tutti:
			
			for(RisultatoPartita ris : pp.getRisultati()) {
				//provo a mettere ris nella posizione livello della soluzione parziale
				
				//costruzione soluzione parziale (sottoproblema)
				parziale.add(ris); //aggiungo il primo elemento, ovvero ho provato a mettere il primo risultato
				//possibile nella prima posizione della soluzione parziale.. ho risolto il mio problema, da qui
				//in poi devono gli altri mettere i livelli successivi
				
				//chiamata ricorsiva
				ricorsiva(parziale, livello+1); //Cioe' prendi questa soluzione parziale a cui ho aggiunto il mio
				//pezzo e spostati al livello successivo
				
				//backtracking (rimettere le cose a posto)... sono in un ciclo for.. ho fatto add(ris) ma nessuno
				//mi dice che sia quella giusta, all'iterazione successiva deve mettere X ma 2 deve essere tolto!
				parziale.remove(parziale.size()-1);
			}
		}
	}

}
