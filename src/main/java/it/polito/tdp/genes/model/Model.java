package it.polito.tdp.genes.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private SimpleWeightedGraph<Genes,DefaultWeightedEdge> grafo;
	private Map<String,Genes> idMap;
	private GenesDao dao;
	private List<Genes> geneStudiato;
	private PriorityQueue<Evento> queue;
	private int T;
	
	public Model() {
		dao=new GenesDao();
		idMap=new HashMap<String,Genes>();
		
		this.dao.getAllGenes(idMap);
	}
	
	public void creaGrafo() {
		grafo=new SimpleWeightedGraph<Genes,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(idMap));
		
		for(Adiacenze a:this.dao.getArchi(idMap)) {
			if(grafo.containsVertex(a.getG1()) && grafo.containsVertex(a.getG2()))
				Graphs.addEdge(grafo, a.getG1(), a.getG2(), a.getPeso());
		}
	}
	
	public Set<Genes> listVertici(){
		return this.grafo.vertexSet();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<Adiacenze> geniAdiacenti(Genes gene) {
		List<Adiacenze> result=new LinkedList<Adiacenze>();
		
		for(Genes g:Graphs.neighborListOf(grafo, gene)) {
			DefaultWeightedEdge e=this.grafo.getEdge(gene, g);
			double peso=this.grafo.getEdgeWeight(e);
			result.add(new Adiacenze(gene,g,peso));
		}
		Collections.sort(result);
		return result;
	}
	
	public boolean getGrafo() {
		if(this.grafo==null)
			return false;
		else
			return true;
	}

	public void simula(Genes gene, int n) {
		geneStudiato=new LinkedList<Genes>();
		T=1;
		
		this.queue=new PriorityQueue<Evento>();
		for(int ning=0;ning<n;ning++){
			this.queue.add(new Evento(T,ning));
			this.geneStudiato.add(gene);
		}
		this.run();
	}
	
	public void run() {
		Evento e;
		while((e=this.queue.poll())!=null) {
			if(e.getT()<36) {
				this.T=e.getT();
				int ning=e.getN();
				Genes gene=this.geneStudiato.get(ning);
				
				List<Vicini> vicini=new LinkedList<>();
				
				double random=Math.random()*100;
				if(random<30) {
					this.queue.add(new Evento(T++, ning));
				}
				else {
					double peso=0;
					for(Genes g:Graphs.neighborListOf(grafo, gene)) {
						DefaultWeightedEdge arco=this.grafo.getEdge(gene, g);
						double pesoarco=this.grafo.getEdgeWeight(arco);
						peso+=pesoarco;
						vicini.add(new Vicini(g,pesoarco));
					}
					
					double ran2=Math.random()*peso;
					Genes prossimo=null;
					double somma=0;
					for(Vicini v:vicini) {
						somma+=v.peso;
						if(somma>ran2) {
							prossimo=v.getVicino();
						}
					}
					
					this.queue.add(new Evento(T++,ning));
					this.geneStudiato.set(ning, prossimo);
				}
			}
		}
	}
	
	public Map<Genes,Integer> getGeniStudiati(){
		Map<Genes,Integer> result=new HashMap<>();
		
		for(Genes g:this.geneStudiato) {
			if(!result.containsKey(g)) {
				result.put(g, 1);
			}
			else {
				result.put(g, result.get(g)+1);
			}
		}
		
		return result;
	}
}
