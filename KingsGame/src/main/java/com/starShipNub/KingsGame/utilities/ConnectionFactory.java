package com.starShipNub.KingsGame.utilities;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class ConnectionFactory {
	private static MongoClient mongo;
	private static DB db;
	
	public synchronized static boolean checkConnection(){
		createConnection();
		if(db==null){
			return false;
		} else {
			return true;
		}
	}

	public synchronized static DB getConnection() {
		if (mongo == null || db == null) {
			createConnection();
		}
		return db;
	}

	private synchronized static void createConnection() {
		try {
			mongo = new MongoClient("localhost", 27017);
			db = mongo.getDB("KingsGame");
		} catch (Exception e) {
		}
	}
}
