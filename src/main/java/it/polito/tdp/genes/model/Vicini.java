package it.polito.tdp.genes.model;

public class Vicini {

	Genes vicino;
	double peso;
	public Vicini(Genes vicino, double peso) {
		super();
		this.vicino = vicino;
		this.peso = peso;
	}
	public Genes getVicino() {
		return vicino;
	}
	public void setVicino(Genes vicino) {
		this.vicino = vicino;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
}
