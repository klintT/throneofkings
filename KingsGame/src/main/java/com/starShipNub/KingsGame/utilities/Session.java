package com.starShipNub.KingsGame.utilities;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.starShipNub.KingsGame.models.Game;

public class Session {
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> sessions = new ConcurrentHashMap<String, ConcurrentHashMap<String, Object>>();
	private static ConcurrentHashMap<String, String> index = new ConcurrentHashMap<String, String>();

	public synchronized static void put(String sessionId, String key, Object value) {
		if (sessions.get(sessionId) != null) {
			sessions.get(sessionId).put(key, value);
		} else {
			ConcurrentHashMap<String, Object> ha = new ConcurrentHashMap<String, Object>();
			ha.put(key, value);
			sessions.put(sessionId, ha);
		}
	}
	
	public synchronized static void putIndex(String key, String value){
		index.put(key, value);
	}

	public synchronized static ConcurrentHashMap<String, Object> get(String sessionId) {
		return sessions.get(sessionId);
	}

	public synchronized static void remove(String sessionId) {
		sessions.remove(sessionId);
		index.remove(sessionId);
	}

	public synchronized static String getUniqueId(String playerId) {
		// Check to see if our player already has a running session
		Iterator<ConcurrentHashMap.Entry<String, String>> it = index.entrySet().iterator();
		while (it.hasNext()) {
			ConcurrentHashMap.Entry<String, String> es = it.next();
			if (es.getValue().equalsIgnoreCase(playerId)) {
				return es.getKey();
			}
		}

		// If not, create a new one
		Random random = new Random();
		while (true) {
			Integer id = random.nextInt();
			if (!sessions.containsKey(id.toString())) {
				index.put(id.toString(), playerId);
				return id.toString();
			}
		}
	}

	public synchronized static String getUniqueId(Integer playerId) {
		return getUniqueId(playerId.toString());
	}
	
	public synchronized static void removeByGameId(Integer gameId){
		Iterator<ConcurrentHashMap.Entry<String, ConcurrentHashMap<String, Object>>> it = sessions.entrySet().iterator();
		while (it.hasNext()) {
			ConcurrentHashMap.Entry<String, ConcurrentHashMap<String, Object>> es = it.next();
			Iterator<Entry<String, Object>> iit = es.getValue().entrySet().iterator();
			while(iit.hasNext()){
				ConcurrentHashMap.Entry<String, Object> games = iit.next();
				if(games.getKey().equalsIgnoreCase("games")){
					Game g = (Game)games.getValue();
					if(g.getGameId().compareTo(gameId)==0){
						sessions.get(es.getKey()).remove("games", gameId);
					}
				}
			}
		}
	}

}
