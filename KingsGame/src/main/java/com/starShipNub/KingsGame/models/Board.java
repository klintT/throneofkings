package com.starShipNub.KingsGame.models;

import java.util.ArrayList;
import java.util.List;

import com.starShipNub.KingsGame.utilities.BaseDAO;

public class Board {

	private final static int COUNT_X = 18;
	private final static int COUNT_Y = 18;
	private Integer[][] tiles;
	private List<Landscape> land;
	private List<Champion> champs;
	private List<Obstacle> obs;
	private BaseDAO myDAO;

	public Board() {
		myDAO = new BaseDAO();
		this.champs = new ArrayList<Champion>();
	}

	public void defaultBoard(String player1Id, String player2Id) {
		tiles = new Integer[COUNT_X][COUNT_Y];
		setDefaultLand();
		setDefaultChamps(player1Id, player2Id);
		setDefaultObs();
	}

	public void move(int[] from, int[] to) {
		Integer item = tiles[from[0]][from[1]];
		tiles[to[0]][to[1]] = item;
		tiles[from[0]][from[1]] = null;
	}

	public void destroy(int[] tile) {
		tiles[tile[0]][tile[1]] = null;
	}

	public void spawn(int[] tile, Integer itemId) {
		tiles[tile[0]][tile[1]] = itemId;
	}

	public boolean isEmpty(int[] tile) {
		if (tiles[tile[0]][tile[1]] == null || tiles[tile[0]][tile[1]] == -1) {
			return true;
		}
		return false;
	}

	public List<Landscape> getLand() {
		return land;
	}

	public void setLand(final List<Landscape> land) {
		this.land = land;
	}

	public void setDefaultLand() {
		// this.land = myDAO.getDefaultLand();
	}

	public List<Champion> getChamps() {
		return champs;
	}

	public void setChamps(final List<Champion> champs) {
		this.champs = champs;
	}

	public void setDefaultChamps(String player1Id, String player2Id) {
		this.champs = new ArrayList<Champion>();

		Champion tank1 = myDAO.getDefaultChampByName("Tank");
		tank1.setPlayerId(player1Id);
		tank1.setPosition(37);
		tank1.setCurrentHp(tank1.getMaxHp());
		tank1.setCurrentMana(tank1.getMaxMana());
		champs.add(tank1);

		Champion mage1 = myDAO.getDefaultChampByName("Mage");
		mage1.setPlayerId(player1Id);
		mage1.setPosition(57);
		mage1.setCurrentHp(mage1.getMaxHp());
		mage1.setCurrentMana(mage1.getMaxMana());
		champs.add(mage1);

		Champion king1 = myDAO.getDefaultChampByName("King");
		king1.setPlayerId(player1Id);
		king1.setPosition(38);
		king1.setCurrentHp(king1.getMaxHp());
		king1.setCurrentMana(king1.getMaxMana());
		champs.add(king1);

		Champion fighter1 = myDAO.getDefaultChampByName("Fighter");
		fighter1.setPlayerId(player1Id);
		fighter1.setPosition(58);
		fighter1.setCurrentHp(fighter1.getMaxHp());
		fighter1.setCurrentMana(fighter1.getMaxMana());
		champs.add(fighter1);

		Champion tank2 = myDAO.getDefaultChampByName("Tank");
		tank2.setPlayerId(player2Id);
		tank2.setPosition(362);
		tank2.setCurrentHp(tank2.getMaxHp());
		tank2.setCurrentMana(tank2.getMaxMana());
		champs.add(tank2);

		Champion mage2 = myDAO.getDefaultChampByName("Mage");
		mage2.setPlayerId(player2Id);
		mage2.setPosition(342);
		mage2.setCurrentHp(mage2.getMaxHp());
		mage2.setCurrentMana(mage2.getMaxMana());
		champs.add(mage2);

		Champion king2 = myDAO.getDefaultChampByName("King");
		king2.setPlayerId(player2Id);
		king2.setPosition(361);
		king2.setCurrentHp(king2.getMaxHp());
		king2.setCurrentMana(king2.getMaxMana());
		champs.add(king2);

		Champion fighter2 = myDAO.getDefaultChampByName("Fighter");
		fighter2.setPlayerId(player2Id);
		fighter2.setPosition(341);
		fighter2.setCurrentHp(fighter2.getMaxHp());
		fighter2.setCurrentMana(fighter2.getMaxMana());
		champs.add(fighter2);

	}

	public List<Obstacle> getObs() {
		return obs;
	}

	public void setObs(final List<Obstacle> obs) {
		this.obs = obs;
	}

	public void setDefaultObs() {
		// this.obs = myDAO.getDefaultObs();
	}

	public Champion getChampionById(String id) {
		for (Champion champ : champs) {
			if (champ.getId().toString().equalsIgnoreCase(id)) {
				return champ;
			}
		}
		return null;
	}

	public boolean endTurn(Integer activePlayerId) {
		boolean endSuccess = true;
		for (Champion c : champs) {
			if(!c.endTurn(activePlayerId)){
				endSuccess = false;
			}
		}
		return endSuccess;
	}
	
	public boolean inactiveView(){
		for(Champion c : champs){
			c.setMoveDistance(0);
			for(Attack a : c.getAttacks()){
				a.setDistance(0);
			}
		}
		return true;
	}
	
	public boolean checkWinCondition(){
		int player1DownCount=0;
		int player2DownCount=0;
		for(int i=0; i<champs.size(); i++){
			if(champs.get(i).getCurrentHp()<=0){
				if(champs.get(i).isKing()){
					return true;
				} else {
					if(i<4){
						player1DownCount++;
						if(player1DownCount>=3){
							return true;
						}
					} else {
						player2DownCount++;
						if(player2DownCount>=3){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
