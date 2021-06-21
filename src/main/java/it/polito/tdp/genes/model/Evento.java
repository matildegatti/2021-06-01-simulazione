package it.polito.tdp.genes.model;

public class Evento implements Comparable<Evento>{
	
	private int t;
	private int nIng;
	
	
	public Evento(int t, int n) {
		super();
		this.t = t;
		this.nIng = n;
	}
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public int getN() {
		return nIng;
	}
	public void setN(int n) {
		this.nIng = n;
	}


	@Override
	public int compareTo(Evento e) {
		return this.t-e.t;
	}
	
	

}
