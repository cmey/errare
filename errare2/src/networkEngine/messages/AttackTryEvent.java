package networkEngine.messages;

/**
 * A message that a client sends to the server, telling that he
 * wants to launch an attack. It's only a demand.
 * The server manages the rights, calculates damages and experience
 * and sends informations to surounding characters.
 * See attack_sequence.jpg .
 * 
 * @author cyberchrist
 */
public class AttackTryEvent extends TryEvent {

}
