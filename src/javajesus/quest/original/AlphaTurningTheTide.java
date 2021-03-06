package javajesus.quest.original;

import javajesus.entities.Mob;
import javajesus.entities.monsters.EvilOrangutan;
import javajesus.entities.npcs.characters.Jesus;
import javajesus.level.Level;
import javajesus.level.LevelFactory;
import javajesus.quest.Quest;

public class AlphaTurningTheTide extends Quest {

	// objective
	private static final int ISDEAD = 0;

	// whether or not jesus/orang is dead
	private boolean killedKen, killedJesus;

	// instance of jesus
	private Jesus jesus;
	
	private Level alpha;

	public AlphaTurningTheTide() {
		super("Turning the Tide", "/WORLD_DATA/QUEST_DATA/ALPHA_TURNING_THE_TIDE.json", 1);
		
		alpha = LevelFactory.get(LevelFactory.ALPHA);
	}

	@Override
	public void update() {
		
		// check if orangutan is dead
		if (!killedKen) {
			for (Mob m : giver.getLevel().getMobs()) {
				if (m instanceof EvilOrangutan && m.isDead()) {
					killedKen = true;
				}
			}
		}

		if (killedKen) {
			// find jesus
			if (jesus == null) {
				for (Mob m : alpha.getMobs()) {
					if (m instanceof Jesus) {
						jesus = (Jesus) m;
						this.giver = jesus;
						jesus.addQuestAndSet(this);
					}
				}
			}
		}

		// check if orangutan is dead
		if (!killedJesus) {
			for (Mob m : alpha.getMobs()) {
				if (m instanceof Jesus && m.isDead()) {
					killedJesus = true;
				}
			}
		}

		// update the objective
		objectives[ISDEAD] = killedJesus || killedKen;
	}

}
