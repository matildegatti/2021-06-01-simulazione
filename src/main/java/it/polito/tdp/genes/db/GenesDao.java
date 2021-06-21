package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenze;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public void getAllGenes(Map<String,Genes> idMap){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getString("GeneID"))) {
				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				idMap.put(genes.getGeneId(), genes);
				}
			}
			res.close();
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Genes>  getVertici(Map<String,Genes> idMap){
		String sql = "SELECT DISTINCT g.GeneID AS id FROM genes g "
				+ "WHERE g.Essential='Essential'";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getString("id"))) {
					result.add(idMap.get(res.getString("id")));
				}
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenze> getArchi(Map<String,Genes> idMap){
		String sql="SELECT i.GeneID1 AS id1, i.GeneID2 AS id2, i.Expression_Corr AS peso "
				+ "FROM interactions i WHERE i.GeneID1!=i.GeneID2 "
				+ "GROUP BY i.GeneID1, i.GeneID2";
		List<Adiacenze> result = new ArrayList<Adiacenze>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(idMap.containsKey(res.getString("id1")) && idMap.containsKey(res.getString("id2"))) {
					Genes g1=idMap.get(res.getString("id1"));
					Genes g2=idMap.get(res.getString("id2"));
					double peso;
					if(g1.getChromosome()==g2.getChromosome()) {
						peso=Math.abs(res.getDouble("peso"))*2;
						result.add(new Adiacenze(g1,g2,peso));
					}
					else {
						peso=Math.abs(res.getDouble("peso"));
						result.add(new Adiacenze(g1,g2,peso));
					}
		
				}
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
}
