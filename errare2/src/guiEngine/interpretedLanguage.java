 package guiEngine;

import java.util.StringTokenizer;

public class interpretedLanguage {

	// COMMANDS
 public static final int AFK = 0 ;
	public static final int CAST = 1 ;
	public static final int DUEL = 2 ;
	public static final int YIELD = 3 ;
	public static final int EMOTE = 4 ;
	public static final int FOLLOW = 5 ;
	public static final int IGNORE = 6 ;
	public static final int INVITE = 7 ;
	public static final int LOGOUT = 8 ;
	public static final int PLAYED = 9 ;
	public static final int RANDOM = 10 ;
	public static final int SAY = 11 ;
	public static final int WISPER = 12 ;
	public static final int WHO = 13 ;
	// EMOTES (Movement)
 public static final int AGREE = 14 ;
	public static final int ANGRY = 15 ;
	public static final int BORED = 16 ;
	public static final int BOW = 17 ;
	public static final int BYE = 18 ;
	public static final int CLAP = 19 ;
	public static final int CRY = 20 ;
	public static final int DANCE = 21 ;
	public static final int DRINK = 22 ;
	public static final int DUCK = 23 ;
	public static final int EAT = 24 ;
	public static final int FART = 25 ;
	public static final int GASP = 26 ;
	public static final int HAPPY = 27 ;
	public static final int HELLO = 28 ;
	public static final int HUNGRY = 29 ;
	public static final int INSULT = 30 ;
	public static final int KISS = 31 ;
	public static final int LAUGH = 32 ;
	public static final int LISTEN = 33 ;
	public static final int LOVE = 34 ;
	public static final int NO = 35 ;
	public static final int POINT = 36 ;
	public static final int PRAY = 37 ;
	public static final int READY = 38 ;
	public static final int SHOUT = 39 ;
	public static final int SIT = 40 ;
	public static final int SLAP = 41 ;
	public static final int SLEEP = 42 ;
	public static final int STANT = 43 ;
	public static final int THIRSTY = 44 ;
	public static final int WHISTKE = 45 ;
	public static final int YES = 46 ;
	// EMOTES	
 public static final int ICOSMILE = 47 ;
	// :) or :-)
 public static final int ICOHAPPY = 48 ;
	// :D or :-D
 public static final int ICOSAD = 49 ;
	// :( or:-(
 public static final int ICOSUPRISED = 50 ;
	// :O or :-O
 public static final int ICOCONFUSED = 51 ;
	// :-/ or :-\ or :/ or :\
 public static final int ICOCOOL = 52 ;
	// 8) or 8-)
 public static final int ICOCRYING = 53 ;
	// :'(
 public static final int ICORAZZ = 53 ;
	// :-P or :P
 public static final int ICOWINK = 54 ;
	// ;-) or ;)
 public static final int ICOMAD = 55 ;
	// :-X or :X
 public static final int ICOKISS = 56 ;
	// :-* or :*
 // MODERATION
 public static final int ADMIN = 47 ;
	public static final int ACCEPTINGROUP = 48 ;
	public static final int ACCEPTINGUILD = 49 ;
	public static final int ANNOUNCEMENT = 50 ;
	public static final int BAN = 51 ;
	public static final int LEADGROUP = 52 ;
	public static final int LEADGUILD = 53 ;
	public static final int KICKFROMGAME = 54 ;
	public static final int KICKFROMGROUP = 55 ;
	public static final int KICKFROMGUILD = 56 ;
	public static final int MOD = 57 ;
	public static final int MUTE = 58 ;
	public static final int UNADMIN = 59 ;
	public static final int UNBAN = 60 ;
	public static final int UNLEADGROUP = 61 ;
	public static final int UNLEADGUILD = 62 ;
	public static final int UNMOD = 63 ;
	public static final int UNMUTE = 64 ;

	private String errorMessage;
	private String infoMessage;
	
	public interpretedLanguage () {
		
		this.errorMessage="";
		this.infoMessage="";
		
	}
	
	
	public boolean useCommand(String command, int rights) {
		
		if (correctSyntax(command)) {
			
			int commandID = new Integer(command).intValue();
			if (canUse(commandID,rights)) {
				
			}else {
				this.errorMessage="You don't have the permission to do this command.";
				return false;
			}
		}else {
			this.errorMessage="The syntax of this command is not correct.";
			return false;
		}
		return true;
	}
	
	
	
	private boolean correctSyntax(String command) {
		
		StringTokenizer tokenizer = new StringTokenizer(command);
		String [] tabCommand = new String[5];
		int i=0;
		
		// On split le string en mot
		while (tokenizer.hasMoreTokens()){
			tabCommand[i] = tokenizer.nextToken();
			i++;
		}
		
		// On suppr le premier char : /
		tabCommand[0]=tabCommand[0].substring(1);
		
		if (tabCommand[0].compareTo("sqdsqd")==0) {
			
		}
		return false;
		
	}


	public boolean canUse(int command, int rights) {
		
		switch (rights) {
		
		/* A Administrator can :
		 * - Administrate someone
		 * - Ban / Unban someone
		 * - Unlead someone from a guild
		 * - Unlead someone from a group
		 * - Change the annoncement of a channel
		 * - Kick someone from the game
		 * - Kick someone from a guild
		 * - Kick someone from a group
		 * - Moderate / Unmoderate someone
		 * - Mute / Unmute someone
		 * 
		 *  A Administrator can't :
		 *  - Accept someone in a guild
		 *  - Accept someone in a group
		 */
		
		case ADMIN :
			if (command==ADMIN || command==BAN || command==UNBAN || command==UNLEADGUILD
					|| command==UNLEADGROUP || command==ANNOUNCEMENT || command==KICKFROMGAME 
					|| command==KICKFROMGUILD || command==KICKFROMGROUP || command==MOD 
					|| command==UNMOD || command==MUTE || command==UNMUTE) {
				return true;
			}else {
				return false;
			}
			
		/* A Moderator can :
		 * - Change the annoncement of a channel
		 * - Kick someone from the game
		 * - Kick someone from a guild
		 * - Kick someone from a group
		 * - Moderate someone
		 * - Mute / Unmute someone
		 * 
		 * A Moderator can't :
		 * - Administrate someone
		 * - Ban / Unban someone
		 * - Unlead someone from a guild
		 * - Unlead someone from a group
		 * - Unmoderate someaone
		 */
			
		case MOD : 
			if (command==ANNOUNCEMENT || command==KICKFROMGAME || command==KICKFROMGUILD 
					|| command==KICKFROMGROUP	|| command==MOD || command==MUTE || command==UNMUTE) {
				return true;
			}else {
				return false;
			}
			
		/* A Guild Leader can :
		 * - Accept someone in the guild
		 * - Kick someone in the guild
		 * - Unlead he from the guild
		 */	
			
		case LEADGUILD :
			if (command==ACCEPTINGUILD || command==KICKFROMGUILD	|| command==UNLEADGUILD) {
				return true;
			}else {
				return false;
			}
			
		/* A Group Leader can :
		 * - Accept someone in the group
		 * - Kick someone in the group
		 * - Unlead he from the group
		 */		
			
		case LEADGROUP :
			if (command==ACCEPTINGROUP || command==KICKFROMGROUP	|| command==UNLEADGROUP) {
				return true;
			}else {
				return false;
			}
		
		/* A classique player */
			
		default :
			if (command==ADMIN || command==BAN || command==UNBAN || command==UNLEADGUILD
					|| command==ACCEPTINGROUP || command==ACCEPTINGUILD ||command==UNLEADGROUP || command==ANNOUNCEMENT || command==KICKFROMGAME 
					|| command==KICKFROMGUILD || command==KICKFROMGROUP || command==MOD 
					|| command==UNMOD || command==MUTE || command==UNMUTE) {
				return false;
			}else {
				return true;
			}
			}	
	}

}
