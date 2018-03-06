import {Inject, Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {NetworkProvider} from "../network/network";
import {PublishEvent} from "../../app/app.component";

declare let Strophe: any;
declare let $msg: any;
declare let $pres: any;

export class UserToken {
  password: string;
  username: string;

}

export interface MAMService {
  query(myBareJid: any, option: { onMessage: ((message) => boolean); onComplete: ((response) => void) }): void;
}

export interface StropheService {
  muc: MUCService;
  jid: string;
  mam: MAMService;
  rawOutput: (data) => any;
  rawInput: (data) => any;

  send(tree: any): void;

  addHandler(param: (msg) => boolean, any: any, s: string, any2: any, any3: any, any4: any): void;

  connect(username: string, password: string, param3: (status) => any): void;
}

export interface MUCService {
  createConfiguredRoom: any;
  groupchat: any;
  queryOccupants: any;
  leave: any;
  join: any;
}

@Injectable()
export class ChatsProvider {

  static readonly TYPE_CHAT_MESSAGE = "chat";
  static readonly TYPE_MUC_MESSAGE = "muc";

  private verbose: boolean = false;
  private onConnectEventEmiter: PublishEvent<ChatsProvider> = new PublishEvent<ChatsProvider>();
  private onDisconectEventEmiter: PublishEvent<string> = new PublishEvent<string>();
  private connectInProgress: Promise<boolean> = null;
  private connected: boolean = false;
  private usersToken: UserToken;
  private connection: StropheService;
  private incomingDataHandler: ((bareFrom: string, bareTo: string, roomId: string, type: string, textMessage: string, stamp: Date) => void)[] = null;

  constructor(@Inject('SmilanHostService') public  smilanHostService: any,
              public network: NetworkProvider) {
    this.connection = new Strophe.Connection(this.smilanHostService.getChatServerHost());
    this.connection.rawInput = (data) => {
      if (this.verbose) {
        console.info('RECV: ' + data);
      }
    };
    this.connection.rawOutput = (data) => {
      if (this.verbose) {
        console.info('SENT: ' + data);
      }
    };
  }

  sendXMPPMessage(to1, message, type) {
    if (!this.connected) {
      throw new Error("not connected");
    }

    if (type === ChatsProvider.TYPE_CHAT_MESSAGE) {
      let reply = $msg({to: to1, from: this.getUserToken().username, type: 'chat'}).c("body").t(message);
      this.connection.send(reply.tree());
      this.incomingDataHandler.forEach((callback) => {
        callback(this.getUserToken().username, to1, to1, type, message, new Date());
      });
    }else if (type === ChatsProvider.TYPE_MUC_MESSAGE) {
      this.getMUCService().groupchat(to1, message);
      //this.private.notifyExternal(this.chatsServiceAPI.userAuthToken.username, this.jid, 'chat', text, new Date());
      //will be notified with on message
    }else{
      console.error("unknown chat message type");
    }

  };

  sendXMPPTypingNotification(to: string, typing: boolean): void {

  }

  getJid(): string {
    return this.connection.jid;
  }

  getMUCService(): MUCService {
    return this.connection.muc;
  }

  getMAMService(): MAMService {
    return this.connection.mam;
  }

  registerXMPPIncomingDataHandler(callback: (bareFrom: string, bareTo: string, roomId: string, type: string, textMessage: string, stamp: Date) => void): void {
    if (!this.connected) {
      throw new Error("not connected");
    }
    if (this.incomingDataHandler == null) {
      this.incomingDataHandler = [];
      this.connection.addHandler((msg) => {
        try {
          let to = msg.getAttribute('to');
          let from = msg.getAttribute('from');
          let type = msg.getAttribute('type');
          let bodyElem = msg.getElementsByTagName('body');
          if ((type === "chat" || type === "groupchat") && bodyElem.length > 0) {
            let bareFrom;
            let bareTo;
            let roomId;
            if (type === "groupchat") {
              //it is a muc room so the real user jid is the ressource
              bareFrom = Strophe.getResourceFromJid(from);
              bareTo = Strophe.getBareJidFromJid(to);
              roomId = Strophe.getBareJidFromJid(from);
            } else {
              bareFrom = Strophe.getBareJidFromJid(from);
              bareTo = Strophe.getBareJidFromJid(to);
              //in case of a chat the room id is the id of the other guy
              roomId = bareFrom === this.getUserToken().username ? bareTo : bareFrom;
            }
            let body = bodyElem[0];
            let textMessage = Strophe.getText(body);
            let stamp = new Date();
            if (msg.getElementsByTagName("delay").length != 0) {
              stamp = new Date(msg.getElementsByTagName("delay")[0].getAttribute("stamp"));
            }
            this.incomingDataHandler.forEach((callback) => {
              callback(bareFrom, bareTo, roomId, type, textMessage, stamp);
            });
          }
          // we must return true to keep the handler alive.
          // returning false would remove it after it finishes.
        } catch (e) {
          console.info(e);
        }
        return true;
      }, null, 'message', null, null, null);
      this.connection.send($pres().tree());
    }
    this.incomingDataHandler.push(callback);
  };

  retreiveXMPPHistory(): void {
    if (!this.connected) {
      throw new Error("not connected");
    }
    let mamService = this.getMAMService();
    let myBareJid = Strophe.getBareJidFromJid(this.getJid());
    let option = {
      /*with:"",
       start:"",
       end:"",*/
      onMessage: (message) => {
        /*<message xmlns="jabber:client" from="sit-1360544799@mobile.smillan.com" to="sit-1360544799@mobile.smillan.com/12758579001806242583368">
         <result xmlns="urn:xmpp:mam:0" id="1486216679638601">
         <forwarded xmlns="urn:xmpp:forward:0">
         <message xmlns="jabber:client" from="sit-1360544799@mobile.smillan.com/6905129997150231887336" to="consetetur-1255373459@mobile.smillan.com" type="chat">
         <body>hello</body>
         </message>
         <delay xmlns="urn:xmpp:delay" from="mobile.smillan.com" stamp="2017-02-04T13:57:59.639Z"/>
         </forwarded>
         </result>
         </message>*/
        let messageXMLArray: any[] = message.getElementsByTagName("message");
        if (messageXMLArray.length == 0) {
          console.warn("invalid history message");
          return true;
        }
        let messageXML = messageXMLArray[0];
        let from = Strophe.getBareJidFromJid(messageXML.getAttribute("from"));
        let to = Strophe.getBareJidFromJid(messageXML.getAttribute("to"));
        let type = messageXML.getAttribute("type");
        let text = messageXML.getElementsByTagName("body")[0].innerHTML;
        let stamp = message.getElementsByTagName("delay")[0].getAttribute("stamp");
        let distantRoomId = from == myBareJid ? to : from;//the room id is the identifier of the destinataire
        this.incomingDataHandler.forEach((callback) => {
          callback(from, to, distantRoomId, type, text, new Date(stamp));
        });
        return true;
      },
      onComplete: (response) => {
      }
    };
    mamService.query(myBareJid, option);
  }

  getConnectionEvent(): PublishEvent<ChatsProvider> {
    return this.onConnectEventEmiter;
  }

  getDisconectionEvent(): PublishEvent<string> {
    return this.onDisconectEventEmiter;
  }

  connect(): Promise<boolean> {

    if (this.connected) {
      console.log("already connected");
      return Promise.resolve(true);
    }
    if (this.connectInProgress != null) {
      return this.connectInProgress;
    }
    this.connected = false;
    this.connectInProgress = new Promise<boolean>((resolve, reject) => {
      console.info('Connecting to chat server as ' + this.usersToken.username);
      this.connection.connect(this.usersToken.username, this.usersToken.password, (status) => {
        if (status === Strophe.Status.ERROR) {
          console.info('Strophe is ERROR.');
          reject(status);
        } else if (status === Strophe.Status.CONNECTING) {
          //console.info('Strophe is connecting.');
          //ok
        } else if (status === Strophe.Status.CONNFAIL) {
          console.info('Strophe failed to connect.');
          reject(status);
        } else if (status === Strophe.Status.AUTHENTICATING) {
          //console.info('Strophe is AUTHENTICATING.');
          //ok
        } else if (status === Strophe.Status.AUTHFAIL) {
          console.info('Strophe is AUTHFAIL.');
          reject(status);
        } else if (status === Strophe.Status.CONNECTED) {
          console.info('Strophe is connected.');
          this.connected = true;
          this.connectInProgress = null;
          this.onConnectEventEmiter.publish(this);
          resolve(true);
          return;
        } else if (status === Strophe.Status.DISCONNECTED) {
          console.info('Strophe is disconnected.');
          this.connected = false;
          this.onDisconectEventEmiter.publish("DISCONECTED");
        } else if (status === Strophe.Status.DISCONNECTING) {
          console.info('Strophe is disconnecting.');
          this.connected = false;
        } else if (status === Strophe.Status.ATTACHED) {
          console.info('Strophe is ATTACHED.');
        } else if (status === Strophe.Status.REDIRECT) {
          console.info('Strophe is REDIRECT.');
          reject(status);
        } else if (status === Strophe.Status.CONNTIMEOUT) {
          console.info('Strophe is CONNTIMEOUT.');
          reject(status);
        }
      });
    });
    this.connectInProgress.catch(() => {
      this.onDisconectEventEmiter.publish("disconected");
      this.connectInProgress = null;
      this.connected = false;
    });
    return this.connectInProgress;
  }

  disconnect() {

  }

  setUserToken(data: UserToken) {
    this.usersToken = data;
  }

  getUserToken() {
    return this.usersToken;
  }

  isConnected(): boolean {
    return this.connected;
  }
}
