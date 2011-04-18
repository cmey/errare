/* Errare Humanum Est Project
 * Copyright (C) 2007 Manuel A. Fernandez Montecelo <mafm@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */

package networkEngine.messages;

import java.io.*;
import networkEngine.messages.DamagesDoneEvent;
import networkEngine.messages.HPChangeDoneEvent;
import networkEngine.messages.LoginNetworkMessage;
import networkEngine.messages.MoveDoneEvent;
import networkEngine.messages.NewCharacterInYourAreaDoneEvent;
import networkEngine.messages.RotateDoneEvent;

/**
 * Abstract Network Messages, the superclass for all messages. Concrete messages
 * should extend TryEvent (if Client to Server) or DoneEvent (if Server to Client).
 * 
 * @author mafm@users.sourceforge.net
 */
public abstract class AbstractNetworkMessage implements Serializable {
    private byte MessageClass;
    enum MessageClassChoice {AttackMoveDoneEvent, AttackTryEvent, CharacterLeftYourAreaDoneEvent, DamagesDoneEvent, DoneEvent, ExperienceChangedDoneEvent, HPChangeDoneEvent, LoginNetworkMessage, MoveDoneEvent, NewCharacterInYourAreaDoneEvent, RotateDoneEvent, TryEvent;}
    
    public AbstractNetworkMessage(){
      //dummy
    }
    
    /**
     * Constructor which saves the most specialised class type to identify it at deserialization
     */
    public AbstractNetworkMessage(MessageClassChoice SpecializedClass){
        //setMessageClass(SpecializedClass);
    }

    public byte getMessageClass() {
        return MessageClass;
    }

    public void setMessageClass(byte MessageClass) {
        this.MessageClass = MessageClass;
    }
}
