package com.starShipNub.KingsGame.utilities;

import java.util.ArrayList;
import java.util.List;

import com.starShipNub.KingsGame.models.Attack;
import com.starShipNub.KingsGame.models.Champion;
import com.starShipNub.KingsGame.models.Sprite;

public class GenerateDefaults {
	private BaseDAO myDAO = new BaseDAO();

	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0


	public void generateChampions() {

		List<Champion> champs = new ArrayList<Champion>();

		/*
		 * fighter.setMagicDamage(10); fighter.setPhysicalDamage(25);
		 * fighter.setMagicDefense(10); fighter.setPhysicalDefense(10);
		 */
		Champion fighter = new Champion();
		fighter.setName("Fighter");
		fighter.setCurrentMana(100);
		fighter.setId(myDAO.createNewId("champions"));
		fighter.setKing(false);
		fighter.setMaxMana(100);
		fighter.setCurrentHp(125);
		fighter.setMaxHp(100);
		fighter.setDead(false);
		fighter.setMoveDistance(3);
		fighter.setSprite(new Sprite());

		List<Attack> fa = new ArrayList<Attack>();
		Attack fighterBasic = new Attack();
		fighterBasic.setDistance(1);
		fighterBasic.setCooldown(0);
		fighterBasic.setDamage(25);
		fighterBasic.setId(myDAO.createNewId("attacks"));
		fighterBasic.setCooldownRemaining(0);
		fighterBasic.setName("Fighter Basic");
		fa.add(fighterBasic);
		
		Attack megaSlash = new Attack();
		megaSlash.setDistance(1);
		megaSlash.setCooldown(5);
		megaSlash.setDamage(75);
		megaSlash.setId(myDAO.createNewId("attacks"));
		megaSlash.setCooldownRemaining(0);
		megaSlash.setName("Mega-Slash");
		fa.add(megaSlash);
		fighter.setAttacks(fa);

		champs.add(fighter);

		/*
		 * mage.setMagicDamage(20); mage.setPhysicalDamage(5);
		 * mage.setMagicDefense(20); mage.setPhysicalDefense(10);
		 */
		Champion mage = new Champion();
		mage.setCurrentMana(1000);
		mage.setId(myDAO.createNewId("champions"));
		mage.setKing(false);
		mage.setMaxMana(1000);
		mage.setCurrentHp(100);
		mage.setMaxHp(150);
		mage.setDead(false);
		mage.setMoveDistance(4);
		mage.setName("Mage");
		mage.setSprite(new Sprite());
		
		List<Attack> ma = new ArrayList<Attack>();
		
		Attack mageBasic = new Attack();
		mageBasic.setDistance(3);
		mageBasic.setCooldown(0);
		mageBasic.setDamage(10);
		mageBasic.setId(myDAO.createNewId("attacks"));
		mageBasic.setCooldownRemaining(0);
		mageBasic.setName("Mage Basic");
		ma.add(mageBasic);
		mage.setAttacks(ma);
		
		Attack fireball = new Attack();
		fireball.setDistance(2);
		fireball.setCooldown(3);
		fireball.setDamage(50);
		fireball.setId(myDAO.createNewId("attacks"));
		fireball.setCooldownRemaining(0);
		fireball.setName("Fireball");
		ma.add(fireball);
		mage.setAttacks(ma);

		champs.add(mage);

		/*
		 * king.setPhysicalDamage(10); king.setMagicDefense(25);
		 * king.setPhysicalDefense(25); king.setMagicDamage(10);
		 */
		Champion king = new Champion();
		king.setCurrentMana(500);
		king.setId(myDAO.createNewId("champions"));
		king.setKing(true);
		king.setMaxMana(500);
		king.setCurrentHp(500);
		king.setMaxHp(500);
		king.setDead(false);
		king.setMoveDistance(5);
		king.setName("King");
		king.setSprite(new Sprite());
		
		List<Attack> ka = new ArrayList<Attack>();
		Attack kingBasic = new Attack();
		kingBasic.setDistance(2);
		kingBasic.setCooldown(0);
		kingBasic.setDamage(25);
		kingBasic.setId(myDAO.createNewId("attacks"));
		kingBasic.setCooldownRemaining(0);
		kingBasic.setName("King Basic");
		ka.add(kingBasic);
		king.setAttacks(ka);

		champs.add(king);

		/*
		 * tank.setPhysicalDamage(10); tank.setMagicDefense(25);
		 * tank.setPhysicalDefense(25); tank.setMagicDamage(10);
		 */
		Champion tank = new Champion();
		tank.setCurrentMana(500);
		tank.setId(myDAO.createNewId("champions"));
		tank.setKing(false);
		tank.setMaxMana(500);
		tank.setCurrentHp(500);
		tank.setMaxHp(500);
		tank.setDead(false);
		tank.setMoveDistance(3);
		tank.setName("Tank");
		tank.setSprite(new Sprite());
		
		List<Attack> ta = new ArrayList<Attack>();
		
		Attack tankBasic = new Attack();
		tankBasic.setDistance(1);
		tankBasic.setCooldown(0);
		tankBasic.setDamage(10);
		tankBasic.setId(myDAO.createNewId("attacks"));
		tankBasic.setCooldownRemaining(0);
		tankBasic.setName("Tank Basic");
		ta.add(tankBasic);
		
		Attack stomp = new Attack();
		stomp.setDistance(1);
		stomp.setCooldown(3);
		stomp.setDamage(20);
		stomp.setId(myDAO.createNewId("attacks"));
		stomp.setCooldownRemaining(0);
		stomp.setName("Stomp");
		ta.add(stomp);
		
		tank.setAttacks(ta);

		champs.add(tank);

		myDAO.addDefaultChampsToChampionsTable(champs);

	}
}
