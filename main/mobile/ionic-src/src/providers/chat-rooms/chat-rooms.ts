import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {ArrayEvent, promiseTimeout, PublishEvent, TypedEvent} from "../../app/app.component";
import {ChatsProvider, UserToken} from "../chats/chats";

declare let Strophe: any;

export class ChatMessage {
  public stamp: Date;
  public from: string;
  public to: string;
  public text: string;

}

export class RoomHandle {
  static readonly TYPE_ROOM_DIRECT: string = "chat";
  static readonly TYPE_ROOM_MUC: string = "muc";
  static readonly TYPE_MESSAGE_CHAT: string = "chat";
  static readonly TYPE_MESSAGE_MUC: string = "groupchat";
  TYPING_TIMER_LENGTH: number = 400;//ms

  private msgs: ArrayEvent<ChatMessage> = new ArrayEvent<ChatMessage>();
  private users: ArrayEvent<string> = new ArrayEvent<string>();
  private unseen: TypedEvent<number> = new TypedEvent<number>();
  private typing: boolean = false;
  private messageUnseenNumber: number = 0;
  private lastTypingTime: number = 0;
  private lastTimeUserSawThisHandle: Date = new Date(2000, 1);//date in the past to tell never seen

  constructor(public roomID: string, public type: string, public chatsServiceAPI: ChatsProvider) {
    if (type !== RoomHandle.TYPE_ROOM_DIRECT && type !== RoomHandle.TYPE_ROOM_MUC) {
      console.error("invalyd room type");
      return;
    }
    if (this.type === RoomHandle.TYPE_ROOM_DIRECT) {
      this.notifyNewUser(this.chatsServiceAPI.getUserToken().username);
      this.notifyNewUser(this.roomID);
    } else {
      this.chatsServiceAPI.connect().then(() => {
        this.joinRoom();
        this.chatsServiceAPI.getMUCService().queryOccupants(this.roomID, (data) => {
          /*
            <iq xmlns="jabber:client" from="debug-announce-5@conference.mobile.smillan.com" to="debug-tpHUjHtKQGAnB1cPHvmvmA7FVwrf3cKCP4bDuPB3PhCFplivzkamUT3bgD6GZsPN@mobile.smillan.com/16698474109275419771167000" id="39dd6b58-c2c9-40d5-8e2b-7676d566f2b8:sendIQ" type="result">
              <query xmlns="http://jabber.org/protocol/disco#items">
                <item jid="debug-announce-5@conference.mobile.smillan.com/debug-e1ODj4TR5iwGLsr9csNkRlezcvFmrNjACzIsG5hvxrH8wJILVZkwJOazTTjUhiZY@mobile.smillan.com" name="debug-e1ODj4TR5iwGLsr9csNkRlezcvFmrNjACzIsG5hvxrH8wJILVZkwJOazTTjUhiZY@mobile.smillan.com"/>
                <item jid="debug-announce-5@conference.mobile.smillan.com/debug-tpHUjHtKQGAnB1cPHvmvmA7FVwrf3cKCP4bDuPB3PhCFplivzkamUT3bgD6GZsPN@mobile.smillan.com" name="debug-tpHUjHtKQGAnB1cPHvmvmA7FVwrf3cKCP4bDuPB3PhCFplivzkamUT3bgD6GZsPN@mobile.smillan.com"/>
                <item jid="debug-announce-5@conference.mobile.smillan.com/debug-e1ODj4TR5iwGLsr9csNkRlezcvFmrNjACzIsG5hvxrH8wJILVZkwJOazTTjUhiZY@mobile.smillan.com" name="debug-e1ODj4TR5iwGLsr9csNkRlezcvFmrNjACzIsG5hvxrH8wJILVZkwJOazTTjUhiZY@mobile.smillan.com"/>
              </query>
            </iq>
           */
          let items = data.getElementsByTagName("item");
          let result = [];
          for (let i = 0; i < items.length; i++) {
            let item = items[i];
            let jid = item.getAttribute("jid");
            let trueJID = Strophe.getResourceFromJid(jid);//it is a muc room so the real user jid is the ressource
            result.push(trueJID);
            this.notifyNewUser(trueJID);
          }
        }, (err) => {
        });
      });
    }
  }

  /*
   * API
   */
  getRoomId(): string {
    return this.roomID;
  }

  getMsgs(): ArrayEvent<ChatMessage> {
    return this.msgs;
  }

  getUnseenEvent(): TypedEvent<number> {
    return this.unseen;
  }

  getUsers(): ArrayEvent<string> {
    return this.users;
  }

  getLastTimeUserSawThisHandle(): Date {
    return this.lastTimeUserSawThisHandle;
  }

  getMessageUnseenNumber(): number {
    return this.messageUnseenNumber;
  }

  notifyNewUser(jid: string): void {
    this.users.push(jid);
  }

  /**
   * MUC ROOM API pART
   */

  /**
   *
   * @param stanza
   * @param room
   * @returns {boolean}
   */
  private onMessage(stanza: any, room: any): boolean {
    //Room message are already notified by the global message system
    /*
     <message xmlns="jabber:client"
     from="debug-announce-3@conference.mobile.smillan.com/debug-gZIIJAKXkul1nwvRPIO1N5lmuOiMDPGVcF9ev1EdMtq5k1YxCzSxpSKe6sPDKmsN"
     to="debug-3E570rYJoID6jmHtJbIXB6cCYHCrYlbvjX3RZF4nx5SAfbLsHIrw6gXCVvcM59tV@mobile.smillan.com/11935830219011212284165958"
     xml:lang="en"
     type="groupchat">
     <body>hello</body>
     </message>
     */

    /*let from = stanza.getAttribute("from");
    let type = stanza.getAttribute("type");
    //let to = stanza.getAttribute("to");
    let message = stanza.getElementsByTagName("body")[0].innerHTML;
    let ressourceFrom = Strophe.getResourceFromJid(from);
    this.notifyNewUser(ressourceFrom);//to load associated profile
    let ressourceTo = Strophe.getBareJidFromJid(from);
    let date = new Date();
    this.notifyMessage(ressourceFrom, ressourceTo, type, message, date);*/
    return true;
  }

  private onPresence(stanza: any, room: any): boolean {
    /*
     <presence xmlns="jabber:client" from="debug-announce-6@conference.mobile.smillan.com/debug-e1ODj4TR5iwGLsr9csNkRlezcvFmrNjACzIsG5hvxrH8wJILVZkwJOazTTjUhiZY@mobile.smillan.com" to="debug-tpHUjHtKQGAnB1cPHvmvmA7FVwrf3cKCP4bDuPB3PhCFplivzkamUT3bgD6GZsPN@mobile.smillan.com/11999475592418999156167448">
     <x xmlns="http://jabber.org/protocol/muc#user">
      <item affiliation="none" role="participant"/>
     </x>
     </presence>
      getRoomsHandles(): any {
        throw new Error("Method not implemented.");
    }
   */

    let jid = stanza.getAttribute("from");
    let trueJID = Strophe.getResourceFromJid(jid);//it is a muc room so the real user jid is the ressource
    this.notifyNewUser(trueJID);
    return true;
  }

  /**
   *
   * @param {type} jid of the room
   * @returns {roomHandle} room handle
   */
  joinRoom(): Promise<string> {
    if (!this.chatsServiceAPI.isConnected()) {
      throw new Error("not connected");
    }
    let jid = this.roomID;
    return new Promise((resolve, fail) => {
      this.chatsServiceAPI.getMUCService().join(jid, this.chatsServiceAPI.getUserToken().username, this.onMessage, this.onPresence);
      console.info("enter room:" + jid);
      this.updateUnseenMessage();
      resolve(jid);
    });
  }

  /**
   *
   * @returns {roomHandle} room handle
   */
  leaveRoom(): Promise<string> {
    if (!this.chatsServiceAPI.isConnected()) {
      throw new Error("not connected");
    }
    return new Promise((resolve, fail) => {
      //TODO we never leave room for the moment
      //this.chatsServiceAPI.getMUCService().leave(this.roomID);
      console.info("leave room:" + this.roomID);
      resolve(this.roomID);
    });
  }

  /**
   * END MUC ROOM API pART
   */

  updateUnseenMessage(): void {
    this.lastTimeUserSawThisHandle = new Date();
    this.messageUnseenNumber = 0;
    this.msgs.get().forEach((msg: ChatMessage) => {
      if (msg.stamp.valueOf() > this.lastTimeUserSawThisHandle.valueOf()) {
        this.messageUnseenNumber++;
      }
    });
    this.unseen.emit(this.messageUnseenNumber);
  }

  sendMsg(text: string): void {
    let chatType = ChatsProvider.TYPE_CHAT_MESSAGE;//default
    if(this.type==RoomHandle.TYPE_ROOM_MUC){
      chatType = ChatsProvider.TYPE_MUC_MESSAGE;
    }
    this.chatsServiceAPI.sendXMPPMessage(this.roomID, text, chatType);
    this.typing = false;
    this.updateUnseenMessage();
  }

  // Updates the typing event
  sendUpdateTyping() {
    if (!this.typing) {
      this.chatsServiceAPI.sendXMPPTypingNotification(this.roomID, this.typing);
    }
    this.lastTypingTime = (new Date()).getTime();
    setTimeout(() => {
      let typingTimer = (new Date()).getTime();
      let timeDiff = typingTimer - this.lastTypingTime;
      if (timeDiff >= this.TYPING_TIMER_LENGTH && this.typing) {
        this.typing = false;
        this.chatsServiceAPI.sendXMPPTypingNotification(this.roomID, this.typing);
      }
    }, this.TYPING_TIMER_LENGTH);
  }

  notifyMessage(from: string, to: string, type: string, text: string, stamp: Date): void {
    if (stamp == null) {
      stamp = new Date();
    }
    if (type === RoomHandle.TYPE_MESSAGE_CHAT || type === RoomHandle.TYPE_MESSAGE_MUC) {
      if (stamp.valueOf() > this.lastTimeUserSawThisHandle.valueOf()) {
        this.messageUnseenNumber++;
      }
      let msg: ChatMessage = {
        from: from,
        to: to,
        text: text,
        stamp: stamp,
      };
      this.msgs.push(msg);
    } else {
      throw new Error("invalid message type");
    }
  }

}

@Injectable()
export class ChatRoomsProvider {
  private onConnectEventEmiter: PublishEvent<ChatRoomsProvider> = new PublishEvent<ChatRoomsProvider>();
  private onDisconectEventEmiter: PublishEvent<string> = new PublishEvent<string>();
  private connectInProgress: boolean = false;
  private connected: boolean = false;
  private roomsHandles: ArrayEvent<RoomHandle> = new ArrayEvent<RoomHandle>();

  constructor(private chatsServiceAPI: ChatsProvider) {
  }

  private privateGetRoomHandle(jid: string): RoomHandle {
    let filtered = this.roomsHandles.get().filter((value) => {
      return value.roomID === jid;
    });
    if (filtered.length === 0) {
      return null;
    } else {
      return filtered[0];
    }
  }

  getRoomHandle(id: string): RoomHandle {
    if (id == null) {
      console.error("null");
      return null;
    }
    let chatHandle = this.privateGetRoomHandle(id);
    if (chatHandle === null) {//nop
      let newHandle = null;
      if (id.indexOf("conference") !== -1) {
        newHandle = new RoomHandle(id, RoomHandle.TYPE_ROOM_MUC, this.chatsServiceAPI);
      } else {
        newHandle = new RoomHandle(id, RoomHandle.TYPE_ROOM_DIRECT, this.chatsServiceAPI);
      }
      this.roomsHandles.push(newHandle);
      chatHandle = newHandle;
    }
    return chatHandle;
  }

  getRoomsHandles(): ArrayEvent<RoomHandle> {
    return this.roomsHandles;
  }

  /**
   * @returns {Promise} promise on jid of the room
   */
  createMUCRoom(name: string): Promise<string> {
    if (!this.connected) {
      throw new Error("not connected");
    }
    return new Promise((resolve, fail) => {
      let jid: string = this.chatsServiceAPI.getMUCService().createConfiguredRoom(
        name,
        {
          "muc#roomconfig_publicroom": "1",
          "muc#roomconfig_persistentroom": "1"
        },
        (success) => {
          console.info("room created:" + jid);
          resolve(jid);
        },
        (err) => {
          fail(err);
        }
      );
    });
  };

  getUserToken(): UserToken {
    return this.chatsServiceAPI.getUserToken();
  };

  private chatServiceConnect(): Promise<ChatsProvider> {
    return new Promise<ChatsProvider>((resolve, reject) => {
      this.chatsServiceAPI.connect();
      this.chatsServiceAPI.getDisconectionEvent().onPublish(() => {
        this.connected = false;
        console.log("chat service lost try recover");
        setTimeout(() => {
          this.connect();
        }, 30000);
      });
      this.chatsServiceAPI.getConnectionEvent().onPublishOnce(() => {
        this.chatsServiceAPI.registerXMPPIncomingDataHandler((from, to, roomId, type, text, stamp) => {
          let roomHandle = this.getRoomHandle(roomId);
          roomHandle.notifyMessage(from, to, type, text, stamp);
        });
        resolve(this.chatsServiceAPI);
      });
    });
  }

  private retreiveHistory(): Promise<boolean> {
    //history will be sent to incoming data handler
    this.chatsServiceAPI.retreiveXMPPHistory();
    return Promise.resolve(true);
  }

  getConnectionEvent(): PublishEvent<ChatRoomsProvider> {
    return this.onConnectEventEmiter;
  }

  getDisconectionEvent(): PublishEvent<string> {
    return this.onDisconectEventEmiter;
  }

  connect(): void {
    if (this.connected || this.connectInProgress) {
      console.log("already connected");
      return;
    }
    this.connected = false;
    this.connectInProgress = true;
    promiseTimeout(120000, Promise.resolve(true)
      .then(() => {
        return this.chatServiceConnect()
      })
      .then(() => {
        return this.retreiveHistory()
      })
      .then(() => {
        //publish api
        console.log("chat service online");
        this.connected = true;
        this.onConnectEventEmiter.publish(this);
        this.connectInProgress = false;
      }))
      .catch((error) => {
        this.connected = false;
        this.connectInProgress = false;
      });
  }

  disconnect(): void {

  }

  isConnected(): boolean {
    return this.connected;
  }

  setUserTokens(userTokens: UserToken[]) {
    this.chatsServiceAPI.setUserToken(userTokens[0]);
  }
}
