package umu.tds.apps.persistencia;

import java.util.Hashtable;

public class PoolDAO {
	private static PoolDAO unicaInstancia;
	private Hashtable<Integer, Object> pool;

	private PoolDAO() {
		pool = new Hashtable<Integer, Object>();
	}

	public static synchronized PoolDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new PoolDAO();
		return unicaInstancia;

	}

	public Object getObjeto(int id) {
		return pool.get(id);
	}

	public void addObjeto(int id, Object objeto) {
		pool.putIfAbsent(id, objeto);
	}

	public boolean contiene(int id) {
		return pool.containsKey(id);
	}
}
