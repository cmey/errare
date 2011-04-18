package networkEngine.messages;

import gameEngine.GameExperience;

/**
 * A message that the server sends to a client, telling that
 * ~his~ character has had experience values modified.
 * A scenaria can be : he killed a monster and now the server
 * sends the new values for xp, strength, level, ratios, etc.
 * The client receiving this message should update the values
 * in his own representation of the concerned character, so
 * that if the user wants to see the stats of his heros, he
 * already has it ; no need to wait for an ask / receive from
 * the network.
 * Also, by having permanently the correct values of experience,
 * the client can optionnaly make a simulate of the combats,
 * masking lag on the client side.
 * 
 * @author cyberchrist
 *
 */
public class ExperienceChangedDoneEvent extends DoneEvent{

	/**
	 * the complete new values of our character's experience status
	 */
	GameExperience xp;
}
