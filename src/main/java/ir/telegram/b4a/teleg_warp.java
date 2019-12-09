package ir.telegram.b4a;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Switch;
import android.widget.TableRow;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper;


/**
 * Created by owner on 01/11/2017.
 */

@BA.ShortName("telegramB4a")
@BA.Version(1.1f)
//@BA.DependsOn(values = {"android-support-v4"})
//@BA.ActivityObject
@BA.Events(values = {"_errormsg(value as string)","_loginok(value as string)","_accpass(value as string)","_getme(value as string)","_sendsms(value as string)","_chatinvitelinkinfo(value as string)","_joingroup(value as string)","_joingroup(value as string)","_getchat(value as string)","_getchats(value as string)","_getuserinfo(value as string)","_joinchannel(value as string)","_viewdon(value as string)"})
@SuppressWarnings("all")
//@TargetApi(23)

public class teleg_warp extends AbsObjectWrapper<TdApi> {

    @BA.Hide
    static BA ba;
    static String EventName;
    public String phonenumber;
    public String code_num;
    private int offsetOrder = 0;
    public String chat_link;
    private int chatloop = 0;
    static Object mTag;
    public static String accname;

    ///

    ArrayList<TdApi.Chat> mListAdminChats = new ArrayList<>();
    private static final ConcurrentMap<Long, TdApi.Chat> chatss = new ConcurrentHashMap<Long, TdApi.Chat>();
    ArrayList<JSONObject> chatsCache;

    private int mCountAdminsGroup = 0;
    private int totalChats = 0;
    //    private TdApi.ChatInfo chatInfo;
    ArrayList<JSONObject> chatslist;
    JSONArray arr = new JSONArray();
    static JSONArray arrmsg = new JSONArray();
    JSONArray inline_msg = new JSONArray();

    JSONObject channel_msginf = new JSONObject();

    private static TdApi.AuthorizationState authorizationState = null;
    private static Client client = null;
    private static volatile boolean haveAuthorization = false;
    //private static final Client.ResultHandler defaultHandler = new DefaultHandler();

    private static final Lock authorizationLock = new ReentrantLock();
    private static final Condition gotAuthorization = authorizationLock.newCondition();
    private static volatile boolean quiting = false;
    private static final String newLine = System.getProperty("line.separator");
    private static final Client.ResultHandler defaultHandler = new DefaultHandler();
    ///
    private static final ConcurrentMap<Integer, TdApi.User> users = new ConcurrentHashMap<Integer, TdApi.User>();
    private static final ConcurrentMap<Integer, TdApi.BasicGroup> basicGroups = new ConcurrentHashMap<Integer, TdApi.BasicGroup>();
    private static final ConcurrentMap<Integer, TdApi.Supergroup> supergroups = new ConcurrentHashMap<Integer, TdApi.Supergroup>();
    private static final ConcurrentMap<Integer, TdApi.SecretChat> secretChats = new ConcurrentHashMap<Integer, TdApi.SecretChat>();
    private static final ConcurrentMap<Long, TdApi.Chat> chats = new ConcurrentHashMap<Long, TdApi.Chat>();
    private static final ConcurrentMap<Long, TdApi.ChatMember> chatmembersa = new ConcurrentHashMap<Long, TdApi.ChatMember>();

    private static final NavigableSet<OrderedChat> chatList = new TreeSet<OrderedChat>();
    private static boolean haveFullChatList = false;

    private static final ConcurrentMap<Integer, TdApi.UserFullInfo> usersFullInfo = new ConcurrentHashMap<Integer, TdApi.UserFullInfo>();
    private static final ConcurrentMap<Integer, TdApi.BasicGroupFullInfo> basicGroupsFullInfo = new ConcurrentHashMap<Integer, TdApi.BasicGroupFullInfo>();
    private static final ConcurrentMap<Integer, TdApi.SupergroupFullInfo> supergroupsFullInfo = new ConcurrentHashMap<Integer, TdApi.SupergroupFullInfo>();
    public TdApi.ProxyType typep;

    static JSONObject user_infojs = new JSONObject();
    // static JSONArray arrmsg = new JSONArray();
    private TdApi.ConnectionState connectionState;

    public void Initialize(BA ba, String EventName) {

        _Initialize(ba, null, EventName);

    }

    private void _Initialize(BA ba, Object o, String eventName) {

        this.ba = ba;
        this.mTag = o;
        this.EventName = eventName.toLowerCase(ba.cul);
        this.setObject(new TdApi());
        BA.Log("TdApi is intialize  ");
        // TgH.init(ba.context);
        // initClient();
    }

    public void Setphone(final String phoneN) {
        //client.send(new TdApi.SetAuthenticationPhoneNumber(phoneN, false, false), new AuthorizationRequestHandler());
        client.send(new TdApi.SetAuthenticationPhoneNumber(phoneN, false, false), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msg :" + object.toString());
                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR:
                        TdApi.AuthorizationStateWaitCode users = (TdApi.AuthorizationStateWaitCode) object;
                        BA.Log("is sendddddddddddddddddd");
                        //   TdApi.SearchChats users = (TdApi.SearchChats) object;
                        // BA.Log(users.query.toString());


                        JSONObject obj = new JSONObject();
                        try {
                            //TdApi.AuthenticationCodeType authenticationCodeType =(TdApi.AuthenticationCodeType) users.codeInfo.type;
                            obj.put("codeType", users.codeInfo.type);
                            obj.put("isRegistered", users.isRegistered);
                            obj.put("nextCodeType", users.codeInfo.nextType);
                            obj.put("timeout", users.codeInfo.timeout);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BA.Log(obj.toString());
                        testsms(obj.toString());

                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void testsms(String Value) {

                if (ba.subExists(EventName + "_sendsms")) {
                    BA.Log("lib:Raising.. " + EventName + "_sendsms " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_sendsms", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_sendsms");
                }
            }

        });

    }

    public void checkCode(final String code) {
        //  client.send(new TdApi.CheckAuthenticationCode(code, "", ""), new AuthorizationRequestHandler());
        client.send(new TdApi.CheckAuthenticationCode(code, "", ""), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("code :" + object.toString());
                ba.Log("code :" + object.getConstructor());

                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                        okRaiseEvent("online");
                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:
                        TdApi.AuthorizationStateWaitPassword authStateWaitPassword = (TdApi.AuthorizationStateWaitPassword) object;
                        BA.Log("password : " + authStateWaitPassword);

                        JSONObject channel_pass = new JSONObject();
                        ;
                        try {
                            BA.Log("photo");

                            channel_pass.put("pass_hint", authStateWaitPassword.passwordHint);
                            channel_pass.put("pass_recovery", authStateWaitPassword.hasRecoveryEmailAddress);
                            channel_pass.put("pass_email", authStateWaitPassword.recoveryEmailAddressPattern);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        passRaiseEvent(channel_pass.toString());
                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void okRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_loginok")) {
                    BA.Log("lib:Raising.. " + EventName + "_loginok " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_loginok", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_loginok");
                }
            }

            public void passRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_accpass")) {
                    BA.Log("lib:Raising.. " + EventName + "_accpass " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_accpass", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_accpass");
                }
            }

        });


    }

    public void checkPassword(final String password) {
        //client.send(new TdApi.CheckAuthenticationPassword(password), new AuthorizationRequestHandler());
        client.send(new TdApi.CheckAuthenticationPassword(password), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("pass :" + object.toString());

                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("photo");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                        okRaiseEvent("online");
                        ba.Log("USER IS LOGINS");
                        break;


                }


            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void okRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_loginok")) {
                    BA.Log("lib:Raising.. " + EventName + "_loginok " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_loginok", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_loginok");
                }
            }

        });

    }

    public void logOut() {
        client.send(new TdApi.LogOut(), new AuthorizationRequestHandler());

    }

    public void test(int a, int b) {
        TdApi.CallProtocol getChannel = new TdApi.CallProtocol();
        getChannel.maxLayer = 100;
        getChannel.minLayer = 150;
        getChannel.udpP2p = true;
        getChannel.udpReflector = true;

        //    client.send(new TdApi.CreateCall(453361837, getChannel), new AuthorizationRequestHandler());
        //   client.send(new TdApi.GetMe(), new AuthorizationRequestHandler());
        //   client.send(new TdApi.SetBio("........."), new AuthorizationRequestHandler());
        //    client.send(new TdApi.SearchPublicChat("milad"), new AuthorizationRequestHandler());
        //   client.send(new TdApi.GetImportedContactCount(), new AuthorizationRequestHandler());
        //   client.send(new TdApi.SearchContacts("",100), new AuthorizationRequestHandler());
        client.send(new TdApi.DownloadFile(a, b), new AuthorizationRequestHandler());

    }

    public void getUserGap(int chatid, int limits, int offsets) {
        TdApi.GetSupergroupMembers getChannel = new TdApi.GetSupergroupMembers();
        TdApi.SupergroupMembersFilterBanned filterban = new TdApi.SupergroupMembersFilterBanned();
        getChannel.filter = filterban;
        getChannel.limit = limits;
        getChannel.offset = offsets;
        getChannel.supergroupId = chatid;

        client.send(getChannel, new AuthorizationRequestHandler());

    }

    public void getUserinfo(int userid) {
        TdApi.GetUser getChannel = new TdApi.GetUser();
        getChannel.userId = userid;
        client.send(getChannel, new AuthorizationRequestHandler());
    }

    public void invieUserContact(int userid, long chatid, int forwardlimit) {
        TdApi.AddChatMember getChannel = new TdApi.AddChatMember();
        getChannel.userId = userid;
        getChannel.chatId = chatid;
        getChannel.forwardLimit = forwardlimit;
        client.send(getChannel, new AuthorizationRequestHandler());
    }

    public void invieUser(int userid, long chatid, int forwardlimit) {
        //    TdApi.TLFunction f = new TdApi.ChangeChatMemberStatus(chat_id, user.id, new TdApi.ChatMemberStatusLeft());
        //  TdApi.Function f = new TdApi.SetChatMemberStatus(chatid, userid, new TdApi.ChatMemberStatusLeft());
        TdApi.SetChatMemberStatus getChannel = new TdApi.SetChatMemberStatus();
        getChannel.chatId = chatid;
        TdApi.ChatMemberStatusMember filterchatst = new TdApi.ChatMemberStatusMember();
        getChannel.status = filterchatst;
        getChannel.userId = userid;
        client.send(getChannel, new AuthorizationRequestHandler());

        //   client.send(f, new AuthorizationRequestHandler());
    }

    public void invieUsers(int userid, long chatid, int forwardlimit) {
        //    TdApi.TLFunction f = new TdApi.ChangeChatMemberStatus(chat_id, user.id, new TdApi.ChatMemberStatusLeft());
        //  TdApi.Function f = new TdApi.SetChatMemberStatus(chatid, userid, new TdApi.ChatMemberStatusLeft());
        TdApi.SetChatMemberStatus getChannel = new TdApi.SetChatMemberStatus(chatid, userid, new TdApi.ChatMemberStatusLeft());

        client.send(getChannel, new AuthorizationRequestHandler());
        //   client.send(f, new AuthorizationRequestHandler());
    }

    public void sendMessage(long chatId, String message) {
        // initialize reply markup just for testing
        TdApi.InlineKeyboardButton[] row = {new TdApi.InlineKeyboardButton("https://telegram.org?1", new TdApi.InlineKeyboardButtonTypeUrl()), new TdApi.InlineKeyboardButton("https://telegram.org?2", new TdApi.InlineKeyboardButtonTypeUrl()), new TdApi.InlineKeyboardButton("https://telegram.org?3", new TdApi.InlineKeyboardButtonTypeUrl())};
        TdApi.ReplyMarkup replyMarkup = new TdApi.ReplyMarkupInlineKeyboard(new TdApi.InlineKeyboardButton[][]{row, row, row});

        TdApi.InputMessageContent content = new TdApi.InputMessageText(new TdApi.FormattedText(message, null), false, true);
        client.send(new TdApi.SendMessage(chatId, 0, false, false, replyMarkup, content), defaultHandler);
    }

    public void getChats(long offsetOrder, long offsetChatID, int limit) {
        TdApi.GetChats getChats = new TdApi.GetChats(offsetOrder, offsetChatID, limit);
        client.send(getChats, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                switch (object.getConstructor()) {
                    case TdApi.Error.CONSTRUCTOR:
                        System.err.println("Receive an error for GetChats:" + newLine + object);
                        break;
                    case TdApi.Chats.CONSTRUCTOR: {
                        TdApi.Chats chata = (TdApi.Chats) object;
                        final long[] chats2 = chata.chatIds;


                        long[] chatIds = ((TdApi.Chats) object).chatIds;

                        java.util.Iterator<OrderedChat> iter = chatList.iterator();
                        for (int i = 0; i < 10; i++) {
                            long chatId = iter.next().chatId;
                            TdApi.Chat chat = chats.get(chatId);
                            synchronized (chat) {
                                BA.Log("chats : " + chat.title);
                                System.out.println(chatId + ": " + chat.title);
                                //totalChats++;
                                int role = 0;

                                JSONObject obj = new JSONObject();
                                try {


                                    obj.put("chat_id", chat.id);
                                    obj.put("title", chat.title);
                                    obj.put("lastReadInboxMessageId", chat.lastReadInboxMessageId);
                                    obj.put("lastReadOutboxMessageId", chat.lastReadOutboxMessageId);
                                    obj.put("replyMarkupMessageId", chat.replyMarkupMessageId);
                                    obj.put("unreadCount", chat.unreadCount);
                                    obj.put("topdate", chat.lastMessage.date);
                                    obj.put("ChatConstructor", chat.type.getConstructor());

                                    if (chat.photo != null) {
                                        //photo
                                        obj.put("photo_expectedSize", chat.photo.small.expectedSize);
                                        obj.put("photo_id", chat.photo.small.id);
                                        obj.put("photo_size", chat.photo.small.size);
                                        obj.put("photo_l_canBeDeleted", chat.photo.small.local.canBeDeleted);
                                        obj.put("photo_l_canBeDownloaded", chat.photo.small.local.canBeDownloaded);
                                        obj.put("photo_l_downloadedPrefixSize", chat.photo.small.local.downloadedPrefixSize);
                                        obj.put("photo_l_downloadedSize", chat.photo.small.local.downloadedSize);
                                        obj.put("photo_l_isDownloadingActive", chat.photo.small.local.isDownloadingActive);
                                        obj.put("photo_l_isDownloadingCompleted", chat.photo.small.local.isDownloadingCompleted);
                                        obj.put("photo_l_path", chat.photo.small.local.path);


                                        obj.put("photo_r_isUploadingActive", chat.photo.small.remote.isUploadingActive);
                                        obj.put("photo_r_isUploadingCompleted", chat.photo.small.remote.isUploadingCompleted);
                                        obj.put("photo_r_uploadedSize", chat.photo.small.remote.uploadedSize);
                                        obj.put("photo_r_id", chat.photo.small.remote.id);
                                    }

                                    obj.put("content", chat.lastMessage.content.getConstructor());


                                    long chat_content;
                                    chat_content = chat.lastMessage.content.getConstructor();
                                    switch (chat.lastMessage.content.getConstructor()) {
                                        case 1469704153:   //msg photo

                                            obj.put("topmasg_type", "photo");

                                            JSONObject channel_topmsg = new JSONObject();
                                            try {
                                                BA.Log("photo");
                                                TdApi.MessagePhoto top_msgs = (TdApi.MessagePhoto) chat.lastMessage.content;
                                                channel_topmsg.put("caption_topmsg", top_msgs.caption);
                                                channel_topmsg.put("photoid_topmsg", top_msgs.photo.id);


                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

                                            obj.put("topmsg_info", channel_topmsg.toString());


                                            break;

                                        case 1630748077:   //msg document

                                            obj.put("topmasg_type", "document");

                                            JSONObject topmsg_ducument = new JSONObject();
                                            try {
                                                BA.Log("document");
                                                TdApi.MessageDocument top_msgs = (TdApi.MessageDocument) chat.lastMessage.content;
                                                if (top_msgs.caption != null)
                                                    topmsg_ducument.put("msgdoc_caption", top_msgs.caption);

                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

                                            obj.put("topmsg_info", topmsg_ducument.toString());


                                            break;

                                        case 1989037971:   //msg message

                                            obj.put("topmasg_type", "message");

                                            JSONObject topmsg_message = new JSONObject();
                                            try {
                                                BA.Log("message");
                                                TdApi.MessageText top_msgs = (TdApi.MessageText) chat.lastMessage.content;
                                                topmsg_message.put("msg_web", top_msgs.webPage);
                                                topmsg_message.put("msg_text", top_msgs.text.text);


                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            obj.put("topmsg_info", topmsg_message.toString());
                                            break;

                                        case -631462405:   //msg message

                                            obj.put("topmasg_type", "voice");

                                            JSONObject topmsg_voice = new JSONObject();
                                            try {
                                                BA.Log("voice");
                                                TdApi.MessageVoiceNote top_msgs = (TdApi.MessageVoiceNote) chat.lastMessage.content;
                                                topmsg_voice.put("msgVoice_caption", top_msgs.caption);
                                                topmsg_voice.put("msgVoice_islistened", top_msgs.isListened);
                                                topmsg_voice.put("msgVoice_duration", top_msgs.voiceNote.duration);
                                                topmsg_voice.put("msgVoice_mimetype", top_msgs.voiceNote.mimeType);
                                                topmsg_voice.put("msgVoice_voiceId", top_msgs.voiceNote.voice.id);
                                                topmsg_voice.put("msgVoice_path", top_msgs.voiceNote.voice.local.path);
                                                topmsg_voice.put("msgVoice_voiceSize", top_msgs.voiceNote.voice.size);
                                                topmsg_voice.put("msgVoice_waweForm", top_msgs.voiceNote.waveform.length);

                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            obj.put("topmsg_info", topmsg_voice.toString());
                                            break;
                                    }
                                    long chan_type;
                                    chan_type = chat.type.getConstructor();
                                    switch (chat.type.getConstructor()) {
                                        case TdApi.ChatTypeSupergroup.CONSTRUCTOR:

                                            obj.put("chat_type", "channel");

                                            JSONObject channel_info = new JSONObject();
                                            try {
                                                TdApi.ChatTypeSupergroup channel = (TdApi.ChatTypeSupergroup) chat.type;
                                                channel_info.put("supergroupId", channel.supergroupId);
                                                channel_info.put("isChannel", channel.isChannel);


                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

                                            obj.put("channel_info", channel_info.toString());
                                            break;
                                        case TdApi.ChatTypeBasicGroup.CONSTRUCTOR:
                                            obj.put("chat_type", "group");
                                            JSONObject group_info = new JSONObject();
                                            try {
                                                TdApi.ChatTypeBasicGroup channel = (TdApi.ChatTypeBasicGroup) chat.type;
                                                group_info.put("basicGroupId", channel.basicGroupId);


                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

                                            obj.put("group_info", group_info.toString());

                                            break;
                                        case TdApi.ChatTypePrivate.CONSTRUCTOR:
                                            obj.put("chat_type", "privateChat");

                                            JSONObject privatechat_info = new JSONObject();
                                            try {
                                                TdApi.ChatTypePrivate private_info = (TdApi.ChatTypePrivate) chat.type;

                                                privatechat_info.put("userid", private_info.userId);


                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

                                            obj.put("privatechat_info", privatechat_info.toString());

                                            break;
                                    }

                                    // GetphotoChats(chat.photo.small.id);
                                    arr.put(obj);
                                    //BA.Log("chat : "+obj.toString());


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    continue;

                                }

                                continue;


                            }
                        }
                        BA.Log(arr.toString());
                        testRaiseEvent(arr.toString());


                    }
                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_getchat")) {
                    BA.Log("lib:Raising.. " + EventName + "_getchat " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getchat", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getchat");
                }
            }
        });
    }

    public void OpenChat(long chatid) {
        TdApi.OpenChat getChannel = new TdApi.OpenChat();
        getChannel.chatId = chatid;

        client.send(getChannel, new AuthorizationRequestHandler());
    }

    public void downloadFile(int fileId) {

        BA.Log("idphoto " + fileId);
        client.send(new TdApi.DownloadFile(fileId, 10), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                switch (object.getConstructor()) {
                    case TdApi.UpdateFile.CONSTRUCTOR: {

                        // TdApi.File updatefile = (TdApi.File) object;
                        //BA.Log("addres "+updatefile.local.path);
                        //Bitmap bmp = BitmapFactory.decodeFile(updatefile.local.path);

                        //imgadres(updatefile.local.path);
                        //  testRaiseEvent(bmp);
                        // onSuccess(bmp);
                        TdApi.UpdateFile updateFile = (TdApi.UpdateFile) object;
                        BA.Log("file : " + updateFile.file.id);

                        JSONObject photo_file = new JSONObject();
                        try {
                            photo_file.put("photof_localpath", updateFile.file.local.path);
                            photo_file.put("photof_local_candownload", updateFile.file.local.canBeDownloaded);
                            photo_file.put("photof_local_downloadsize", updateFile.file.local.downloadedSize);
                            photo_file.put("photof_local_isdownload", updateFile.file.local.isDownloadingActive);
                            photo_file.put("photof_local_isdowloaded", updateFile.file.local.isDownloadingCompleted);
                            photo_file.put("photof_local_perfexsize", updateFile.file.local.downloadedPrefixSize);
                            photo_file.put("photof_local_canbedlelte", updateFile.file.local.canBeDeleted);
                            photo_file.put("photof_expectedSize", updateFile.file.expectedSize);
                            photo_file.put("photof_id", updateFile.file.id);
                            photo_file.put("photof_size", updateFile.file.size);
                            photo_file.put("photof_remot_id", updateFile.file.remote.id);
                            photo_file.put("photof_remot_isuploadactive", updateFile.file.remote.isUploadingActive);
                            photo_file.put("photof_remot_isuploadcomplit", updateFile.file.remote.isUploadingCompleted);
                            photo_file.put("photof_remot_uploadsize", updateFile.file.remote.uploadedSize);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        imgadres(photo_file.toString());


                        break;
                    }
                }
            }

            public void imgadres(String Value) {

                if (ba.subExists(EventName + "_imgaddress")) {
                    BA.Log("lib:Raising.. " + EventName + "_imgaddress ");
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_imgaddress", false, new Object[]{Value});
                    //  arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_imgaddress");
                }
            }

            public void onSuccess(Bitmap pBitmap) {
                // String eventName = (EventName+"_imgok");
                if (ba.subExists(EventName + "_imgphoto")) {
                    CanvasWrapper.BitmapWrapper wrapper = new CanvasWrapper.BitmapWrapper();
                    wrapper.setObject(pBitmap);
                    ba.raiseEvent(this, EventName + "_imgphoto", wrapper, mTag);
                    //  ba.raiseEvent(this, EventName+ "_imgphoto", new Object[] { wrapper,mTag});
                }
            }
        });


    }

    public void getChatHistory(long chatid, long fromeMsgId, int offset, int limit, boolean onlylocal) {
        TdApi.GetChatHistory getChatHistory = new TdApi.GetChatHistory(chatid, fromeMsgId, offset, limit, onlylocal);
        client.send(getChatHistory, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                switch (object.getConstructor()) {
                    case TdApi.Error.CONSTRUCTOR:
                        System.err.println("Receive an error for GetChats:" + newLine + object);
                        break;
                    case TdApi.Messages.CONSTRUCTOR: {
                        BA.Log("hi");

                        TdApi.Messages messages = (TdApi.Messages) object;

                        ///BA.Log("chat message : "+messages.messages.toString());

                        final TdApi.Message[] chats2 = messages.messages;
                        for (TdApi.Message chat : messages.messages) {


                            final JSONObject obj = new JSONObject();
                            try {

                                obj.put("meg_id", chat.id);
                                ba.Log("msgid : "+chat.id);
                                obj.put("meg_date", chat.date);
                                obj.put("meg_views", chat.views);
                                obj.put("meg_canBeDeleted", chat.canBeDeletedForAllUsers);
                                obj.put("meg_canBeEdited", chat.canBeEdited);
                                obj.put("meg_chatId", chat.chatId);
                                obj.put("meg_editDate", chat.editDate);
                                obj.put("meg_isPost", chat.isChannelPost);
                                obj.put("meg_replyToMessageId", chat.replyToMessageId);
                                obj.put("meg_senderUserId", chat.senderUserId);
                                obj.put("meg_viaBotUserId", chat.viaBotUserId);
                                obj.put("getConstructor", chat.content.getConstructor());


                                long chat_content;
                                chat_content = chat.content.getConstructor();
                                BA.Log("" + chat_content);
                                switch ((int) chat_content) {

                                    case 1740718156:   //msg photo

                                        obj.put("msg_type", "photo");

                                        JSONObject channel_topmsg = new JSONObject();
                                        try {
                                            BA.Log("photo");
                                            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) chat.content;

                                            channel_topmsg.put("caption_topmsg", messagePhoto.caption.text);
                                            channel_topmsg.put("caption_id", messagePhoto.caption.text);

                                            //  TdApi.PhotoSize photoSize = (TdApi.PhotoSize) messagePhoto.photo.sizes;
                                            // final TdApi.PhotoSize[] userchats2 = messagePhoto.photo.sizes;

                                            for (TdApi.PhotoSize photoSize : messagePhoto.photo.sizes) {
                                                BA.Log("type : " + photoSize);

                                                channel_topmsg.put("photoid_height", photoSize.height);
                                                channel_topmsg.put("photoid_width", photoSize.width);
                                                channel_topmsg.put("photoid_type", photoSize.type);

                                                channel_topmsg.put("photoid_path", photoSize.photo.local.path);
                                                channel_topmsg.put("photoid_canBeDeleted", photoSize.photo.local.canBeDeleted);
                                                channel_topmsg.put("photoid_canBeDownloaded", photoSize.photo.local.canBeDownloaded);
                                                channel_topmsg.put("photoid_downloadedPrefixSize", photoSize.photo.local.downloadedPrefixSize);
                                                channel_topmsg.put("photoid_downloadedSize", photoSize.photo.local.downloadedSize);
                                                channel_topmsg.put("photoid_isDownloadingActive", photoSize.photo.local.isDownloadingActive);
                                                channel_topmsg.put("photoid_isDownloadingCompleted", photoSize.photo.local.isDownloadingCompleted);

                                                channel_topmsg.put("photoid_topmsg", photoSize.photo.id);
                                                channel_topmsg.put("photoid_remote", photoSize.photo.remote);
                                                channel_topmsg.put("photoid_size", photoSize.photo.size);

                                            }


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        obj.put("massege_info", channel_topmsg.toString());


                                        break;

                                    case 596945783:   //msg document

                                        obj.put("msg_type", "document");

                                        JSONObject topmsg_ducument = new JSONObject();
                                        try {
                                            BA.Log("document");
                                            TdApi.MessageDocument top_msgs = (TdApi.MessageDocument) chat.content;
                                            if (top_msgs.caption != null)
                                                topmsg_ducument.put("msgdoc_caption", top_msgs.caption.text);
                                            topmsg_ducument.put("msgdoc_entities", top_msgs.caption.entities);


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        obj.put("massege_info", topmsg_ducument.toString());


                                        break;
                                    case TdApi.MessageVideo.CONSTRUCTOR:   //msg document

                                        obj.put("msg_type", "Video");

                                        JSONObject js_video = new JSONObject();
                                        try {
                                            BA.Log("document");
                                            TdApi.MessageVideo messageVideo = (TdApi.MessageVideo) chat.content;
                                            if (messageVideo.caption != null)
                                                js_video.put("vd_text", messageVideo.caption.text);
                                            js_video.put("vd_entities", messageVideo.caption.entities);
                                            js_video.put("vd_duration", messageVideo.video.duration);
                                            js_video.put("vd_fileName", messageVideo.video.fileName);
                                            js_video.put("vd_hasStickers", messageVideo.video.hasStickers);
                                            js_video.put("vd_height", messageVideo.video.height);
                                            js_video.put("vd_mimeType", messageVideo.video.mimeType);
                                            js_video.put("vd_width", messageVideo.video.width);

                                            js_video.put("vd_f_size", messageVideo.video.video.size);
                                            js_video.put("vd_f_id", messageVideo.video.video.id);
                                            js_video.put("vd_f_expectedSize", messageVideo.video.video.expectedSize);

                                            js_video.put("vd_f_r_uploadedSize", messageVideo.video.video.remote.uploadedSize);
                                            js_video.put("vd_f_r_isUploadingCompleted", messageVideo.video.video.remote.isUploadingCompleted);
                                            js_video.put("vd_f_r_isUploadingActive", messageVideo.video.video.remote.isUploadingActive);
                                            js_video.put("vd_f_r_id", messageVideo.video.video.remote.id);

                                            js_video.put("vd_f_l_canBeDeleted", messageVideo.video.video.local.canBeDeleted);
                                            js_video.put("vd_f_l_downloadedPrefixSize", messageVideo.video.video.local.downloadedPrefixSize);
                                            js_video.put("vd_f_l_isDownloadingCompleted", messageVideo.video.video.local.isDownloadingCompleted);
                                            js_video.put("vd_f_l_isDownloadingActive", messageVideo.video.video.local.isDownloadingActive);
                                            js_video.put("vd_f_l_downloadedSize", messageVideo.video.video.local.downloadedSize);
                                            js_video.put("vd_f_l_canBeDownloaded", messageVideo.video.video.local.canBeDownloaded);
                                            js_video.put("vd_f_l_path", messageVideo.video.video.local.path);

                                            js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                            js_video.put("vd_tb_l_canBeDownloaded", messageVideo.video.thumbnail.photo.local.canBeDownloaded);
                                            js_video.put("vd_tb_l_downloadedSize", messageVideo.video.thumbnail.photo.local.downloadedSize);
                                            js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                            js_video.put("vd_tb_l_isDownloadingActive", messageVideo.video.thumbnail.photo.local.isDownloadingActive);
                                            js_video.put("vd_tb_l_isDownloadingCompleted", messageVideo.video.thumbnail.photo.local.isDownloadingCompleted);
                                            js_video.put("vd_tb_l_canBeDeleted", messageVideo.video.thumbnail.photo.local.canBeDeleted);

                                            js_video.put("vd_tb_id", messageVideo.video.thumbnail.photo.id);
                                            js_video.put("vd_tb_size", messageVideo.video.thumbnail.photo.size);
                                            js_video.put("vd_tb_height", messageVideo.video.thumbnail.height);
                                            js_video.put("vd_tb_width", messageVideo.video.thumbnail.width);
                                            js_video.put("vd_tb_type", messageVideo.video.thumbnail.type);

                                            js_video.put("vd_tb_r_id", messageVideo.video.thumbnail.photo.remote.id);
                                            js_video.put("vd_tb_r_isUploadingActive", messageVideo.video.thumbnail.photo.remote.isUploadingActive);
                                            js_video.put("vd_tb_r_isUploadingCompleted", messageVideo.video.thumbnail.photo.remote.isUploadingCompleted);
                                            js_video.put("vd_tb_r_uploadedSize", messageVideo.video.thumbnail.photo.remote.uploadedSize);


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        obj.put("massege_info", js_video.toString());


                                        break;
                                    case 1989037971:   //msg message

                                        obj.put("msg_type", "message");

                                        JSONObject topmsg_message = new JSONObject();
                                        try {
                                            BA.Log("message");
                                            TdApi.MessageText top_msgs = (TdApi.MessageText) chat.content;
                                            topmsg_message.put("msg_entities", top_msgs.webPage);
                                            topmsg_message.put("msg_text", top_msgs.text.text);


                                            JSONArray inline_msgup = new JSONArray();
                                            TdApi.ReplyMarkupInlineKeyboard rep_mark = (TdApi.ReplyMarkupInlineKeyboard) chat.replyMarkup;
                                            if (chat.replyMarkup != null) {
                                                obj.put("ReplyMarkup", rep_mark.getConstructor());
                                                //  final TdApi.ReplyMarkupShowKeyboard replyMarkupShowKeyboard = (TdApi.ReplyMarkupShowKeyboard) chat.replyMarkup;
                                                for (int i = 0; i < rep_mark.rows.length; i++) {
                                                    int columnsInRow = rep_mark.rows[i].length;
                                                    TableRow row = new TableRow(ba.context);
                                                    for (int j = 0; j < rep_mark.rows[i].length; j++) {
                                                        // replyMarkupShowKeyboard.rows[i][j];
                                                        BA.Log("logs : " + rep_mark.rows[i][j]);
                                                        String a;
                                                        //rep_mark.rows[i][j].text;

                                                        JSONObject ms_inline = new JSONObject();
                                                        try {
                                                            BA.Log("inline");
                                                            //  TdApi.MessagePhoto ms_inline = (TdApi.MessagePhoto) chat.content;
                                                            ms_inline.put("text", rep_mark.rows[i][j].text);
                                                            //  ms_inline.put("type",rep_mark.rows[i][j].type.);
                                                            TdApi.InlineKeyboardButtonTypeCallback rep_callback = (TdApi.InlineKeyboardButtonTypeCallback) rep_mark.rows[i][j].type;

                                                            byte[] bytes = rep_callback.data.clone();
                                                            //TdApi.GetCallbackQueryAnswer getCallbackQueryAnswer_milad = new TdApi.GetCallbackQueryAnswer();
                                                            // TdApi.CallbackQueryData callbackQueryData_milad = new TdApi.CallbackQueryData();
                                                            //callbackQueryData_milad.data = bytes;
                                                            //  getCallbackQueryAnswer_milad.chatId=chat.chatId;
                                                            //  getCallbackQueryAnswer_milad.messageId=chat.id;
                                                            //  getCallbackQueryAnswer_milad.payload = callbackQueryData_milad;
                                                            // TG.getClientInstance().send(getCallbackQueryAnswer_milad, new Client.ResultHandler() {
                                                            //  @Override
                                                            //   public void onResult(TdApi.TLObject object) {

                                                            //        BA.Log("like msg"+object.toString());
                                                            //    }
                                                            //  });

                                                            String s1 = Arrays.toString(bytes);
                                                            String s2 = new String(bytes);


                                                            String base64String = Base64.encodeToString(bytes, rep_callback.data.length);
                                                            //byte[] backToBytes = Base64.decodeBase64(base64String);

                                                            ms_inline.put("data", s2);


                                                        } catch (JSONException e) {
                                                            // TODO Auto-generated catch block
                                                            e.printStackTrace();
                                                        }

                                                        inline_msgup.put(ms_inline);


                                                        //TdApi.InlineKeyboardButtonType inikey =(TdApi.InlineKeyboardButtonType) ;


                                                    }
                                                }
                                            }


                                            topmsg_message.put("massege_inline", inline_msgup.toString());


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        obj.put("massege_info", topmsg_message.toString());
                                        break;

                                    case 527777781:   //msg message

                                        obj.put("msg_type", "voice");

                                        JSONObject topmsg_voice = new JSONObject();
                                        try {
                                            BA.Log("voice");
                                            TdApi.MessageVoiceNote top_msgs = (TdApi.MessageVoiceNote) chat.content;
                                            if (top_msgs.caption != null)
                                                topmsg_voice.put("msgVoice_caption", top_msgs.caption.text);
                                            topmsg_voice.put("msgVoice_islistened", top_msgs.isListened);
                                            topmsg_voice.put("msgVoice_duration", top_msgs.voiceNote.duration);
                                            topmsg_voice.put("msgVoice_mimetype", top_msgs.voiceNote.mimeType);
                                            topmsg_voice.put("msgVoice_voiceId", top_msgs.voiceNote.voice.id);
                                            topmsg_voice.put("msgVoice_persistentId", top_msgs.voiceNote.voice.local.path);
                                            topmsg_voice.put("msgVoice_voiceSize", top_msgs.voiceNote.voice.size);
                                            topmsg_voice.put("msgVoice_waweForm", top_msgs.voiceNote.waveform.length);

                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        obj.put("massege_info", topmsg_voice.toString());
                                        break;

                                    case TdApi.MessageAnimation.CONSTRUCTOR:   //msg message

                                        obj.put("msg_type", "Animation");

                                        JSONObject jsanimation = new JSONObject();
                                        try {
                                            BA.Log("voice");
                                            TdApi.MessageAnimation messageAnimation = (TdApi.MessageAnimation) chat.content;
                                            jsanimation.put("anim_r_uploadedSize", messageAnimation.animation.animation.remote.uploadedSize);
                                            jsanimation.put("anim_r_isUploadingCompleted", messageAnimation.animation.animation.remote.isUploadingCompleted);
                                            jsanimation.put("anim_r_isUploadingActive", messageAnimation.animation.animation.remote.isUploadingActive);
                                            jsanimation.put("anim_r_id", messageAnimation.animation.animation.remote.id);

                                            jsanimation.put("anim_duration", messageAnimation.animation.duration);
                                            jsanimation.put("anim_fileName", messageAnimation.animation.fileName);
                                            jsanimation.put("anim_height", messageAnimation.animation.height);
                                            jsanimation.put("anim_mimeType", messageAnimation.animation.mimeType);
                                            jsanimation.put("anim_width", messageAnimation.animation.width);
                                            jsanimation.put("anim_size", messageAnimation.animation.animation.size);

                                            jsanimation.put("anim_id", messageAnimation.animation.animation.id);
                                            jsanimation.put("anim_expectedSize", messageAnimation.animation.animation.expectedSize);
                                            jsanimation.put("anim_canBeDeleted", messageAnimation.animation.animation.local.canBeDeleted);
                                            jsanimation.put("anim_local", messageAnimation.animation.animation.local);
                                            jsanimation.put("anim_downloadedPrefixSize", messageAnimation.animation.animation.local.downloadedPrefixSize);
                                            jsanimation.put("anim_isDownloadingCompleted", messageAnimation.animation.animation.local.isDownloadingCompleted);
                                            jsanimation.put("anim_isDownloadingActive", messageAnimation.animation.animation.local.isDownloadingActive);
                                            jsanimation.put("anim_downloadedSize", messageAnimation.animation.animation.local.downloadedSize);
                                            jsanimation.put("anim_canBeDownloaded", messageAnimation.animation.animation.local.canBeDownloaded);
                                            jsanimation.put("anim_path", messageAnimation.animation.animation.local.path);

                                            jsanimation.put("anim_tb_type", messageAnimation.animation.thumbnail.type);
                                            jsanimation.put("anim_tb_width", messageAnimation.animation.thumbnail.width);
                                            jsanimation.put("anim_tb_height", messageAnimation.animation.thumbnail.height);
                                            jsanimation.put("anim_tb_path", messageAnimation.animation.thumbnail.photo.local.path);
                                            jsanimation.put("anim_tb_canBeDownloaded", messageAnimation.animation.thumbnail.photo.local.canBeDownloaded);
                                            jsanimation.put("anim_tb_downloadedSize", messageAnimation.animation.thumbnail.photo.local.downloadedSize);
                                            jsanimation.put("anim_tb_isDownloadingActive", messageAnimation.animation.thumbnail.photo.local.isDownloadingActive);
                                            jsanimation.put("anim_tb_isDownloadingCompleted", messageAnimation.animation.thumbnail.photo.local.isDownloadingCompleted);
                                            jsanimation.put("anim_tb_downloadedPrefixSize", messageAnimation.animation.thumbnail.photo.local.downloadedPrefixSize);
                                            jsanimation.put("anim_tb_canBeDeleted", messageAnimation.animation.thumbnail.photo.local.canBeDeleted);
                                            jsanimation.put("anim_tb_expectedSize", messageAnimation.animation.thumbnail.photo.expectedSize);
                                            jsanimation.put("anim_tb_id", messageAnimation.animation.thumbnail.photo.id);
                                            jsanimation.put("anim_tb_size", messageAnimation.animation.thumbnail.photo.size);
                                            jsanimation.put("anim_tb_r_id", messageAnimation.animation.thumbnail.photo.remote.id);
                                            jsanimation.put("anim_tb_r_isUploadingActive", messageAnimation.animation.thumbnail.photo.remote.isUploadingActive);
                                            jsanimation.put("anim_tb_r_isUploadingCompleted", messageAnimation.animation.thumbnail.photo.remote.isUploadingCompleted);
                                            jsanimation.put("anim_tb_r_uploadedSize", messageAnimation.animation.thumbnail.photo.remote.uploadedSize);
                                            if (messageAnimation.caption != null)
                                                jsanimation.put("cp_text", messageAnimation.caption.text);
                                            jsanimation.put("cp_text", messageAnimation.caption.entities);

                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        obj.put("massege_info", jsanimation.toString());
                                        break;

                                    case TdApi.MessageSticker.CONSTRUCTOR:   //msg message

                                        obj.put("msg_type", "Sticker");

                                        JSONObject js_sticker = new JSONObject();
                                        try {
                                            BA.Log("voice");
                                            TdApi.MessageSticker messageSticker = (TdApi.MessageSticker) chat.content;

                                            js_sticker.put("st_emoji", messageSticker.sticker.emoji);
                                            js_sticker.put("st_height", messageSticker.sticker.height);
                                            js_sticker.put("st_isMask", messageSticker.sticker.isMask);
                                            js_sticker.put("st_width", messageSticker.sticker.width);
                                            js_sticker.put("st_setId", messageSticker.sticker.setId);

                                            js_sticker.put("st_mp_", messageSticker.sticker.maskPosition.point);

                                            js_sticker.put("st_mp_scale", messageSticker.sticker.maskPosition.scale);
                                            js_sticker.put("st_mp_xShift", messageSticker.sticker.maskPosition.xShift);
                                            js_sticker.put("st_mp_yShift", messageSticker.sticker.maskPosition.yShift);

                                            js_sticker.put("st_r_uploadedSize", messageSticker.sticker.sticker.remote.uploadedSize);
                                            js_sticker.put("st_r_isUploadingCompleted", messageSticker.sticker.sticker.remote.isUploadingCompleted);
                                            js_sticker.put("st_r_isUploadingActive", messageSticker.sticker.sticker.remote.isUploadingActive);
                                            js_sticker.put("st_r_id", messageSticker.sticker.sticker.remote.id);

                                            js_sticker.put("st_size", messageSticker.sticker.sticker.size);
                                            js_sticker.put("st_id", messageSticker.sticker.sticker.id);
                                            js_sticker.put("st_expectedSize", messageSticker.sticker.sticker.expectedSize);

                                            js_sticker.put("st_l_canBeDeleted", messageSticker.sticker.sticker.local.canBeDeleted);
                                            js_sticker.put("st_l_isDownloadingCompleted", messageSticker.sticker.sticker.local.isDownloadingCompleted);
                                            js_sticker.put("st_l_isDownloadingActive", messageSticker.sticker.sticker.local.isDownloadingActive);
                                            js_sticker.put("st_l_path", messageSticker.sticker.sticker.local.path);
                                            js_sticker.put("st_l_downloadedSize", messageSticker.sticker.sticker.local.downloadedSize);
                                            js_sticker.put("st_l_canBeDownloaded", messageSticker.sticker.sticker.local.canBeDownloaded);
                                            js_sticker.put("st_l_downloadedPrefixSize", messageSticker.sticker.sticker.local.downloadedPrefixSize);

                                            js_sticker.put("st_tb_l_isDownloadingActive", messageSticker.sticker.thumbnail.photo.local.isDownloadingActive);
                                            js_sticker.put("st_tb_l_downloadedPrefixSize", messageSticker.sticker.thumbnail.photo.local.downloadedPrefixSize);
                                            js_sticker.put("st_tb_l_canBeDownloaded", messageSticker.sticker.thumbnail.photo.local.canBeDownloaded);
                                            js_sticker.put("st_tb_l_downloadedSize", messageSticker.sticker.thumbnail.photo.local.downloadedSize);
                                            js_sticker.put("st_tb_l_path", messageSticker.sticker.thumbnail.photo.local.path);
                                            js_sticker.put("st_tb_l_isDownloadingCompleted", messageSticker.sticker.thumbnail.photo.local.isDownloadingCompleted);

                                            js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.id);
                                            js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.size);
                                            js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.expectedSize);

                                            js_sticker.put("st_tb_r_id", messageSticker.sticker.thumbnail.photo.remote.id);
                                            js_sticker.put("st_tb_r_isUploadingActive", messageSticker.sticker.thumbnail.photo.remote.isUploadingActive);
                                            js_sticker.put("st_tb_r_isUploadingCompleted", messageSticker.sticker.thumbnail.photo.remote.isUploadingCompleted);
                                            js_sticker.put("st_tb_r_uploadedSize", messageSticker.sticker.thumbnail.photo.remote.uploadedSize);

                                            js_sticker.put("st_tb_type", messageSticker.sticker.thumbnail.type);
                                            js_sticker.put("st_tb_width", messageSticker.sticker.thumbnail.width);
                                            js_sticker.put("st_tb_height", messageSticker.sticker.thumbnail.height);

                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        obj.put("massege_info", js_sticker.toString());
                                        break;


                                }

                                arrmsg.put(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                continue;

                            }

                        }
                        testRaiseEvent(arrmsg.toString());
                        arrmsg = new JSONArray(new ArrayList<String>());


                    }
                }
            }


            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_getchatshistory")) {
                    BA.Log("lib:Raising.. " + EventName + "_getchatshistory " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getchatshistory", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getchatshistory");
                }
            }

            public void sendbp(Bitmap Value) {

                if (ba.subExists(EventName + "_imgphoto")) {
                    //  BA.Log("lib:Raising.. " + EventName + "_imgphoto " + Value);
                    //  ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_imgphoto", false, new Object[]{Value});
                    ba.raiseEvent(this, EventName + "_imgphoto", Value, mTag);
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_imgphoto");
                }
            }

        });
    }

    public void closeChat(long chatid) {
        TdApi.CloseChat getChannel = new TdApi.CloseChat();
        getChannel.chatId = chatid;

        client.send(getChannel, new AuthorizationRequestHandler());
    }

    public void searchPublicChat(String chatname) {
        TdApi.SearchPublicChat searchPublicChat = new TdApi.SearchPublicChat();
        searchPublicChat.username = chatname;

        client.send(searchPublicChat, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        //BA.Log(object.toString());
                        //System.err.println("Receive an error:" + newLine + object);
                        ///onAuthorizationStateUpdated(null); // repeat last action

                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEventerr(channel_users.toString());

                        break;

                    case TdApi.Chat.CONSTRUCTOR:

                        BA.Log(object.toString());
                        TdApi.Chat chat = (TdApi.Chat) object;

                        JSONObject chat_info = new JSONObject();
                        try {

                            chat_info.put("Chat_clientData", chat.clientData);
                            chat_info.put("Chat_id", chat.id);
                            chat_info.put("Chat_order", chat.order);
                            chat_info.put("Chat_title", chat.title);
                            chat_info.put("Chat_type", chat.type);


                            chat_info.put("photo_expectedSize", chat.photo.small.expectedSize);
                            chat_info.put("photo_id", chat.photo.small.id);
                            chat_info.put("photo_size", chat.photo.small.size);
                            chat_info.put("photo_l_canBeDeleted", chat.photo.small.local.canBeDeleted);
                            chat_info.put("photo_l_canBeDownloaded", chat.photo.small.local.canBeDownloaded);
                            chat_info.put("photo_l_downloadedPrefixSize", chat.photo.small.local.downloadedPrefixSize);
                            chat_info.put("photo_l_downloadedSize", chat.photo.small.local.downloadedSize);
                            chat_info.put("photo_l_isDownloadingActive", chat.photo.small.local.isDownloadingActive);
                            chat_info.put("photo_l_isDownloadingCompleted", chat.photo.small.local.isDownloadingCompleted);
                            chat_info.put("photo_l_path", chat.photo.small.local.path);


                            chat_info.put("photo_r_isUploadingActive", chat.photo.small.remote.isUploadingActive);
                            chat_info.put("photo_r_isUploadingCompleted", chat.photo.small.remote.isUploadingCompleted);
                            chat_info.put("photo_r_uploadedSize", chat.photo.small.remote.uploadedSize);
                            chat_info.put("photo_r_id", chat.photo.small.remote.id);

                            long chan_type;
                            chan_type = chat.type.getConstructor();
                            switch (chat.type.getConstructor()) {
                                case TdApi.ChatTypeSupergroup.CONSTRUCTOR:

                                    chat_info.put("chat_type", "channel");

                                    JSONObject channel_info = new JSONObject();
                                    try {
                                        TdApi.ChatTypeSupergroup channel = (TdApi.ChatTypeSupergroup) chat.type;
                                        channel_info.put("supergroupId", channel.supergroupId);
                                        channel_info.put("isChannel", channel.isChannel);


                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    chat_info.put("channel_info", channel_info.toString());
                                    break;
                                case TdApi.ChatTypeBasicGroup.CONSTRUCTOR:
                                    chat_info.put("chat_type", "group");
                                    JSONObject group_info = new JSONObject();
                                    try {
                                        TdApi.ChatTypeBasicGroup channel = (TdApi.ChatTypeBasicGroup) chat.type;
                                        group_info.put("basicGroupId", channel.basicGroupId);


                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    chat_info.put("group_info", group_info.toString());

                                    break;
                                case TdApi.ChatTypePrivate.CONSTRUCTOR:
                                    chat_info.put("chat_type", "privateChat");

                                    JSONObject privatechat_info = new JSONObject();
                                    try {
                                        TdApi.ChatTypePrivate private_info = (TdApi.ChatTypePrivate) chat.type;

                                        privatechat_info.put("userid", private_info.userId);


                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    chat_info.put("privatechat_info", privatechat_info.toString());

                                    break;
                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        testRaiseEvent(chat_info.toString());
                        break;
                }
            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_searchpublicchat")) {
                    // BA.Log("lib:Raising.. " + EventName + "_getchatshistory " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_searchpublicchat", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_searchpublicchat");
                }
            }

            public void testRaiseEventerr(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }
        });


    }

    public void getRemoteFile(String remoteFile) {
        TdApi.GetRemoteFile getRemoteFile = new TdApi.GetRemoteFile();
        getRemoteFile.remoteFileId = remoteFile;
        getRemoteFile.fileType = new TdApi.FileTypePhoto();

        client.send(getRemoteFile, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                BA.Log(object.toString());
            }
        });
    }

    public void updatefile(int fileid) {
        TdApi.UpdateFile updateFile = new TdApi.UpdateFile();

    }

    public void constats() {
        TdApi.UpdateConnectionState updateConnectionState = new TdApi.UpdateConnectionState(new TdApi.ConnectionState() {
            @Override
            public int getConstructor() {
                return 0;
            }
        });


    }

    public TdApi.ConnectionState getConnectionState() {
        return connectionState;
    }


    /**
     * public void emptyProxy() {
     *
     *         TdApi.ProxyEmpty proxyEmpty = new TdApi.ProxyEmpty();
     *         TdApi.SetProxy setProxy = new TdApi.SetProxy(proxyEmpty);
     *         client.send(setProxy, new Client.ResultHandler() {
     *             @Override
     *             public void onResult(TdApi.Object object) {
     *                 BA.Log("obj : " + object.getConstructor());
     *                 BA.Log("obj : " + object.toString());
     *
     *             }
     *         });
     *     }
     *
     *     public void proxyGrt() {
     *         TdApi.GetProxy getProxy = new TdApi.GetProxy();
     *         client.send(getProxy, new Client.ResultHandler() {
     *             @Override
     *             public void onResult(TdApi.Object object) {
     *                 BA.Log("obj : " + object.getConstructor());
     *                 BA.Log("obj : " + object.toString());
     *
     *             }
     *         });
     *     }
     *      public void setProxy(int prot, String server, String username, String pass) {
     *         TdApi.ProxySocks5 proxySocks5 = new TdApi.ProxySocks5();
     *         proxySocks5.port = prot;
     *         proxySocks5.server = server;
     *         proxySocks5.username = username;
     *         proxySocks5.password = pass;
     *         TdApi.SetProxy setProxy = new TdApi.SetProxy(proxySocks5);
     *         client.send(setProxy, new AuthorizationRequestHandler());
     *     }
     */

    public void settingmedia() {

        TdApi.GetNetworkStatistics getNetworkStatistics = new TdApi.GetNetworkStatistics(true);
        client.send(getNetworkStatistics, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                BA.Log(object.toString());
            }
        });
    }

    private static class DefaultHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            BA.Log(object.toString());
        }
    }

    public void main(String[] args) throws InterruptedException {
        // disable TDLib log
        Client.setLogVerbosityLevel(5);
        //Log.setVerbosityLevel(0);
        //   if (!Log.setFilePath("log")) {
        //      throw new IOError(new IOException("Write access to the current directory is required"));
        //  }

        // create client
        client = Client.create(new UpdatesHandler(), null, null);

        // test Client.execute
        //   defaultHandler.onResult(Client.execute(new TdApi.GetTextEntities("@telegram /test_command https://telegram.org telegram.me @gif @test")));

        // main loop
        /**
         *  while (!quiting) {
         // await authorization
         authorizationLock.lock();
         try {
         while (!haveAuthorization) {
         gotAuthorization.await();
         }
         } finally {
         authorizationLock.unlock();
         }

         while (haveAuthorization) {

         }
         }
         */

    }

    private static String getCacheDir(Context c) {
        return c.getCacheDir().getAbsolutePath();
    }

    private void onAuthorizationStateUpdated(TdApi.AuthorizationState authorizationState) {
        if (authorizationState != null) {
            teleg_warp.authorizationState = authorizationState;
        }
        ba.Log("auth up : " + teleg_warp.authorizationState.toString());
        ba.Log("auth getConstructor : " + teleg_warp.authorizationState.getConstructor());
        if (teleg_warp.authorizationState.getConstructor() == -483510157) {

            TdApi.AuthorizationStateWaitCode users = (TdApi.AuthorizationStateWaitCode) teleg_warp.authorizationState;

            //   TdApi.SearchChats users = (TdApi.SearchChats) object;
            // BA.Log(users.query.toString());


            JSONObject obj = new JSONObject();
            try {
                //TdApi.AuthenticationCodeType authenticationCodeType =(TdApi.AuthenticationCodeType) users.codeInfo.type;
                obj.put("codeType", users.codeInfo.type);
                obj.put("isRegistered", users.isRegistered);
                obj.put("nextCodeType", users.codeInfo.nextType);
                obj.put("timeout", users.codeInfo.timeout);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String Value;
            Value=obj.toString();
            if (ba.subExists(EventName + "_codemsg")) {
                BA.Log("lib:Raising.. " + EventName + "_codemsg " + Value);
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_codemsg", false, new Object[]{Value});
                arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_codemsg");
            }

        }
        if (teleg_warp.authorizationState.getConstructor() == 187548796) {
            BA.Log("password");
            TdApi.AuthorizationStateWaitPassword authStateWaitPassword = (TdApi.AuthorizationStateWaitPassword) teleg_warp.authorizationState;
            BA.Log("password : " + authStateWaitPassword.passwordHint);

            JSONObject channel_pass = new JSONObject();
            ;
            try {
                BA.Log("password");

                channel_pass.put("pass_hint", authStateWaitPassword.passwordHint);
                channel_pass.put("pass_recovery", authStateWaitPassword.hasRecoveryEmailAddress);
                channel_pass.put("pass_email", authStateWaitPassword.recoveryEmailAddressPattern);


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (teleg_warp.authorizationState.getConstructor() == -1834871737) {


            ba.Log("user is login ! ");

            if (ba.subExists(EventName + "_loginok")) {
                BA.Log("lib:Raising.. " + EventName + "_loginok " + "online");
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_loginok", false, new Object[]{"online"});
                arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_loginok");
            }


        }
        switch (teleg_warp.authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                BA.Log("login");
                TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
                parameters.useMessageDatabase = true;
                parameters.useSecretChats = true;
                parameters.apiId = 362503;
                parameters.apiHash = "7722f7ea4e09123a966cc3f38ae0b34c";
                parameters.systemLanguageCode = "en";
                parameters.deviceModel = "aNDROID";
                parameters.systemVersion = "@telegs";
                parameters.applicationVersion = "1.0";
                parameters.enableStorageOptimizer = true;
                parameters.databaseDirectory = getCacheDir(ba.context) + "/" + accname + "/";
                //parameters.databaseDirectory=getCacheDir(ba.context) + "/files/";  accname
                client.send(new TdApi.SetTdlibParameters(parameters), new AuthorizationRequestHandler());


                break;
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                client.send(new TdApi.CheckDatabaseEncryptionKey(), new AuthorizationRequestHandler());
                break;
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR: {
                ////  Console console = System.console();
                ///  String phoneNumber = console.readLine("Please enter phone number: ");
                BA.Log("Please enter phone number: ");
                // client.send(new TdApi.SetAuthenticationPhoneNumber("+989306697481", false, false), new AuthorizationRequestHandler());


                break;
            }
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR: {
                ba.Log("plz insert code !");

                TdApi.AuthorizationStateWaitCode users = (TdApi.AuthorizationStateWaitCode) teleg_warp.authorizationState;

                BA.Log("is sendddddddddddddddddd");
                //   TdApi.SearchChats users = (TdApi.SearchChats) object;
                // BA.Log(users.query.toString());


                JSONObject obj = new JSONObject();
                try {
                    //TdApi.AuthenticationCodeType authenticationCodeType =(TdApi.AuthenticationCodeType) users.codeInfo.type;
                    obj.put("codeType", users.codeInfo.type);
                    obj.put("isRegistered", users.isRegistered);
                    obj.put("nextCodeType", users.codeInfo.nextType);
                    obj.put("timeout", users.codeInfo.timeout);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BA.Log(obj.toString());
                testsms(obj.toString());


                //Console console = System.console();
                //  String code = console.readLine("Please enter authentication code: ");
                //client.send(new TdApi.CheckAuthenticationCode(code, "", ""), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR: {
                // Console console = System.console();
                // String password = console.readLine("Please enter password: ");
                // client.send(new TdApi.CheckAuthenticationPassword(password), new AuthorizationRequestHandler());

                TdApi.AuthorizationStateWaitPassword authStateWaitPassword = (TdApi.AuthorizationStateWaitPassword) teleg_warp.authorizationState;
                BA.Log("password : " + authStateWaitPassword);

                JSONObject channel_pass = new JSONObject();
                ;
                try {
                    BA.Log("photo");

                    channel_pass.put("pass_hint", authStateWaitPassword.passwordHint);
                    channel_pass.put("pass_recovery", authStateWaitPassword.hasRecoveryEmailAddress);
                    channel_pass.put("pass_email", authStateWaitPassword.recoveryEmailAddressPattern);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                if (ba.subExists(EventName + "_accpass")) {
                    BA.Log("lib:Raising.. " + EventName + "_accpass " + channel_pass.toString());
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_accpass", false, new Object[]{channel_pass.toString()});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_accpass");
                }


                break;
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                haveAuthorization = true;
                authorizationLock.lock();
                try {
                    gotAuthorization.signal();
                } finally {
                    authorizationLock.unlock();
                }
                break;
            case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR:
                haveAuthorization = false;
                // print("Logging out");
                break;
            case TdApi.AuthorizationStateClosing.CONSTRUCTOR:
                haveAuthorization = false;
                // print("Closing");
                break;
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR:
                // print("Closed");
                if (!quiting) {
                    client = Client.create(new UpdatesHandler(), null, null); // recreate client after previous has closed
                }
                break;
            default:
                //    System.err.println("Unsupported authorization state:" + newLine + teleg_warp.authorizationState);
        }


    }

    private static int toInt(String arg) {
        int result = 0;
        try {
            result = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
        }
        return result;
    }

    private static class OrderedChat implements Comparable<OrderedChat> {
        final long order;
        final long chatId;

        OrderedChat(long order, long chatId) {
            this.order = order;
            this.chatId = chatId;
        }

        @Override
        public int compareTo(OrderedChat o) {
            if (this.order != o.order) {
                return o.order < this.order ? -1 : 1;
            }
            if (this.chatId != o.chatId) {
                return o.chatId < this.chatId ? -1 : 1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            OrderedChat o = (OrderedChat) obj;
            return this.order == o.order && this.chatId == o.chatId;
        }
    }

    private static void setChatOrder(TdApi.Chat chat, long order) {
        synchronized (chatList) {
            if (chat.order != 0) {
                boolean isRemoved = chatList.remove(new OrderedChat(chat.order, chat.id));
                assert isRemoved;
            }

            chat.order = order;

            if (chat.order != 0) {
                boolean isAdded = chatList.add(new OrderedChat(chat.order, chat.id));
                assert isAdded;
            }
        }
    }

    public void getChatList(final int limit) {
        synchronized (chatList) {
            if (!haveFullChatList && limit > chatList.size()) {
                // have enough chats in the chat list or chat list is too small
                long offsetOrder = Long.MAX_VALUE;
                long offsetChatId = 0;
                if (!chatList.isEmpty()) {
                    OrderedChat last = chatList.last();
                    offsetOrder = last.order;
                    offsetChatId = last.chatId;
                }
                client.send(new TdApi.GetChats(offsetOrder, offsetChatId, limit - chatList.size()), new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        switch (object.getConstructor()) {
                            case TdApi.Error.CONSTRUCTOR:
                                System.err.println("Receive an error for GetChats:" + newLine + object);
                                break;
                            case TdApi.Chats.CONSTRUCTOR:
                                long[] chatIds = ((TdApi.Chats) object).chatIds;
                                if (chatIds.length == 0) {
                                    synchronized (chatList) {
                                        haveFullChatList = true;
                                    }
                                }
                                // chats had already been received through updates, let's retry request
                                getChatList(limit);
                                break;
                            default:
                                System.err.println("Receive wrong response from TDLib:" + newLine + object);
                        }
                    }
                });
                return;
            }

            // have enough chats in the chat list to answer request
            java.util.Iterator<OrderedChat> iter = chatList.iterator();
            System.out.println();
            System.out.println("First " + limit + " chat(s) out of " + chatList.size() + " known chat(s):");
            for (int i = 0; i < limit; i++) {
                long chatId = iter.next().chatId;
                TdApi.Chat chat = chats.get(chatId);
                synchronized (chat) {
                    BA.Log("chats : " + chat.title);
                    System.out.println(chatId + ": " + chat.title);
                    //totalChats++;
                    int role = 0;

                    JSONObject obj = new JSONObject();
                    try {



                        obj.put("chat_id", chat.id);
                        obj.put("title", chat.title);
                        obj.put("lastReadInboxMessageId", chat.lastReadInboxMessageId);
                        obj.put("lastReadOutboxMessageId", chat.lastReadOutboxMessageId);
                        obj.put("replyMarkupMessageId", chat.replyMarkupMessageId);
                        obj.put("unreadCount", chat.unreadCount);
                        obj.put("topdate", chat.lastMessage.date);
                        obj.put("ChatConstructor", chat.type.getConstructor());

                        if (chat.photo != null)   //photo
                            obj.put("photo_expectedSize", chat.photo.small.expectedSize);
                        obj.put("photo_id", chat.photo.small.id);
                        obj.put("photo_size", chat.photo.small.size);
                        obj.put("photo_l_canBeDeleted", chat.photo.small.local.canBeDeleted);
                        obj.put("photo_l_canBeDownloaded", chat.photo.small.local.canBeDownloaded);
                        obj.put("photo_l_downloadedPrefixSize", chat.photo.small.local.downloadedPrefixSize);
                        obj.put("photo_l_downloadedSize", chat.photo.small.local.downloadedSize);
                        obj.put("photo_l_isDownloadingActive", chat.photo.small.local.isDownloadingActive);
                        obj.put("photo_l_isDownloadingCompleted", chat.photo.small.local.isDownloadingCompleted);
                        obj.put("photo_l_path", chat.photo.small.local.path);


                        obj.put("photo_r_isUploadingActive", chat.photo.small.remote.isUploadingActive);
                        obj.put("photo_r_isUploadingCompleted", chat.photo.small.remote.isUploadingCompleted);
                        obj.put("photo_r_uploadedSize", chat.photo.small.remote.uploadedSize);
                        obj.put("photo_r_id", chat.photo.small.remote.id);

                        obj.put("content", chat.lastMessage.content.getConstructor());


                        long chat_content;
                        chat_content = chat.lastMessage.content.getConstructor();
                        switch (chat.lastMessage.content.getConstructor()) {
                            case 1469704153:   //msg photo

                                obj.put("topmasg_type", "photo");

                                JSONObject channel_topmsg = new JSONObject();
                                try {
                                    BA.Log("photo");
                                    TdApi.MessagePhoto top_msgs = (TdApi.MessagePhoto) chat.lastMessage.content;
                                    channel_topmsg.put("caption_topmsg", top_msgs.caption);
                                    channel_topmsg.put("photoid_topmsg", top_msgs.photo.id);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("topmsg_info", channel_topmsg.toString());


                                break;

                            case 1630748077:   //msg document

                                obj.put("topmasg_type", "document");

                                JSONObject topmsg_ducument = new JSONObject();
                                try {
                                    BA.Log("document");
                                    TdApi.MessageDocument top_msgs = (TdApi.MessageDocument) chat.lastMessage.content;
                                    if (top_msgs.caption != null)
                                        topmsg_ducument.put("msgdoc_caption", top_msgs.caption);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("topmsg_info", topmsg_ducument.toString());


                                break;

                            case 1989037971:   //msg message

                                obj.put("topmasg_type", "message");

                                JSONObject topmsg_message = new JSONObject();
                                try {
                                    BA.Log("message");
                                    TdApi.MessageText top_msgs = (TdApi.MessageText) chat.lastMessage.content;
                                    topmsg_message.put("msg_web", top_msgs.webPage);
                                    topmsg_message.put("msg_text", top_msgs.text.text);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("topmsg_info", topmsg_message.toString());
                                break;

                            case -631462405:   //msg message

                                obj.put("topmasg_type", "voice");

                                JSONObject topmsg_voice = new JSONObject();
                                try {
                                    BA.Log("voice");
                                    TdApi.MessageVoiceNote top_msgs = (TdApi.MessageVoiceNote) chat.lastMessage.content;
                                    topmsg_voice.put("msgVoice_caption", top_msgs.caption);
                                    topmsg_voice.put("msgVoice_islistened", top_msgs.isListened);
                                    topmsg_voice.put("msgVoice_duration", top_msgs.voiceNote.duration);
                                    topmsg_voice.put("msgVoice_mimetype", top_msgs.voiceNote.mimeType);
                                    topmsg_voice.put("msgVoice_voiceId", top_msgs.voiceNote.voice.id);
                                    topmsg_voice.put("msgVoice_path", top_msgs.voiceNote.voice.local.path);
                                    topmsg_voice.put("msgVoice_voiceSize", top_msgs.voiceNote.voice.size);
                                    topmsg_voice.put("msgVoice_waweForm", top_msgs.voiceNote.waveform.length);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("topmsg_info", topmsg_voice.toString());
                                break;
                        }
                        long chan_type;
                        chan_type = chat.type.getConstructor();
                        switch (chat.type.getConstructor()) {
                            case TdApi.ChatTypeSupergroup.CONSTRUCTOR:

                                obj.put("chat_type", "channel");

                                JSONObject channel_info = new JSONObject();
                                try {
                                    TdApi.ChatTypeSupergroup channel = (TdApi.ChatTypeSupergroup) chat.type;
                                    channel_info.put("supergroupId", channel.supergroupId);
                                    channel_info.put("isChannel", channel.isChannel);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("channel_info", channel_info.toString());
                                break;
                            case TdApi.ChatTypeBasicGroup.CONSTRUCTOR:
                                obj.put("chat_type", "group");
                                JSONObject group_info = new JSONObject();
                                try {
                                    TdApi.ChatTypeBasicGroup channel = (TdApi.ChatTypeBasicGroup) chat.type;
                                    group_info.put("basicGroupId", channel.basicGroupId);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("group_info", group_info.toString());

                                break;
                            case TdApi.ChatTypePrivate.CONSTRUCTOR:
                                obj.put("chat_type", "privateChat");

                                JSONObject privatechat_info = new JSONObject();
                                try {
                                    TdApi.ChatTypePrivate private_info = (TdApi.ChatTypePrivate) chat.type;

                                    privatechat_info.put("userid", private_info.userId);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("privatechat_info", privatechat_info.toString());

                                break;
                        }

                        // GetphotoChats(chat.photo.small.id);
                        arr.put(obj);
                        //BA.Log("chat : "+obj.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;

                    }

                    continue;


                }
            }
            returnchatlist(arr.toString());

        }


    }

    public void returnchatlist(String Value) {

        if (ba.subExists(EventName + "_getchat")) {
            BA.Log("lib:Raising.. " + EventName + "_getchat " + Value);
            //  Value=Value.replace("[","").replace("]","");
            ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getchat", false, new Object[]{Value});
            arr = new JSONArray(new ArrayList<String>());

        } else {
            BA.Log("lib: NOTFOUND '" + EventName + "_getchat");
        }
    }

    private class UpdatesHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            //BA.Log(("update hand : "+object.toString()));
            switch (object.getConstructor()) {
                case TdApi.UpdateConnectionState.CONSTRUCTOR:
                    BA.Log("UpdateConnectionState");
                    TdApi.ConnectionState connectionState;
                    connectionState = ((TdApi.UpdateConnectionState) object).state;
                    TdApi.UpdateConnectionState updateConnectionState = new TdApi.UpdateConnectionState(connectionState);

                    //BA.Log("conn = "+connectionState);
                    BA.Log("conn = " + connectionState.getConstructor());
                    switch (connectionState.getConstructor()) {
                        case TdApi.ConnectionStateWaitingForNetwork.CONSTRUCTOR:
                            BA.Log("ConnectionStateWaitingForNetwork");
                            connectionstate("ConnectionStateWaitingForNetwork");
                            break;
                        case TdApi.ConnectionStateReady.CONSTRUCTOR:
                            BA.Log("ConnectionStateReady");
                            connectionstate("ConnectionStateReady");

                            break;
                        case TdApi.ConnectionStateConnecting.CONSTRUCTOR:
                            BA.Log("ConnectionStateConnecting");
                            connectionstate("ConnectionStateConnecting");

                            break;
                        case TdApi.ConnectionStateConnectingToProxy.CONSTRUCTOR:
                            connectionstate("ConnectionStateConnectingToProxy");

                            BA.Log("ConnectionStateConnectingToProxy");
                            break;
                        case TdApi.ConnectionStateUpdating.CONSTRUCTOR:
                            connectionstate("ConnectionStateUpdating");

                            BA.Log("ConnectionStateUpdating");
                            break;

                    }
                    break;

                case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                    BA.Log("otttttttttt");
                    onAuthorizationStateUpdated(((TdApi.UpdateAuthorizationState) object).authorizationState);
                    TdApi.UpdateAuthorizationState updateAuthorizationState = (TdApi.UpdateAuthorizationState) object;
                    TdApi.AuthorizationStateWaitCode authorizationStateWaitCode = (TdApi.AuthorizationStateWaitCode) updateAuthorizationState.authorizationState;
                    ba.Log("auth :" + updateAuthorizationState.authorizationState.toString());
                    ba.Log("timeout :" + authorizationStateWaitCode.codeInfo.timeout);

                    break;
                case TdApi.UpdateUser.CONSTRUCTOR:
                    TdApi.UpdateUser updateUser = (TdApi.UpdateUser) object;
                    users.put(updateUser.user.id, updateUser.user);
                    break;
                case TdApi.UpdateUserStatus.CONSTRUCTOR: {
                    TdApi.UpdateUserStatus updateUserStatus = (TdApi.UpdateUserStatus) object;
                    TdApi.User user = users.get(updateUserStatus.userId);
                    synchronized (user) {
                        user.status = updateUserStatus.status;
                    }
                    break;
                }
                case TdApi.UpdateBasicGroup.CONSTRUCTOR:
                    TdApi.UpdateBasicGroup updateBasicGroup = (TdApi.UpdateBasicGroup) object;
                    basicGroups.put(updateBasicGroup.basicGroup.id, updateBasicGroup.basicGroup);
                    break;
                case TdApi.UpdateSupergroup.CONSTRUCTOR:
                    TdApi.UpdateSupergroup updateSupergroup = (TdApi.UpdateSupergroup) object;
                    supergroups.put(updateSupergroup.supergroup.id, updateSupergroup.supergroup);
                    break;
                case TdApi.UpdateSecretChat.CONSTRUCTOR:
                    TdApi.UpdateSecretChat updateSecretChat = (TdApi.UpdateSecretChat) object;
                    secretChats.put(updateSecretChat.secretChat.id, updateSecretChat.secretChat);
                    break;

                case TdApi.UpdateNewChat.CONSTRUCTOR: {
                    TdApi.UpdateNewChat updateNewChat = (TdApi.UpdateNewChat) object;
                    TdApi.Chat chat = updateNewChat.chat;
                    synchronized (chat) {
                        chats.put(chat.id, chat);

                        long order = chat.order;
                        chat.order = 0;
                        setChatOrder(chat, order);
                    }
                    break;
                }
                case TdApi.UpdateChatTitle.CONSTRUCTOR: {
                    TdApi.UpdateChatTitle updateChat = (TdApi.UpdateChatTitle) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.title = updateChat.title;
                    }
                    break;
                }
                case TdApi.UpdateChatPhoto.CONSTRUCTOR: {
                    TdApi.UpdateChatPhoto updateChat = (TdApi.UpdateChatPhoto) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.photo = updateChat.photo;
                    }
                    break;
                }
                case TdApi.UpdateNewMessage.CONSTRUCTOR: {
                    JSONArray arrmsgup = new JSONArray();
                    TdApi.UpdateNewMessage updateChat = (TdApi.UpdateNewMessage) object;
                    BA.Log("new message : ");
                    TdApi.Message chat = (TdApi.Message) updateChat.message;

                    final JSONObject obj = new JSONObject();
                    try {

                        obj.put("meg_id", chat.id);
                        obj.put("meg_date", chat.date);
                        obj.put("meg_views", chat.views);
                        obj.put("meg_canBeDeleted", chat.canBeDeletedForAllUsers);
                        obj.put("meg_canBeEdited", chat.canBeEdited);
                        obj.put("meg_chatId", chat.chatId);
                        obj.put("meg_editDate", chat.editDate);
                        obj.put("meg_isPost", chat.isChannelPost);
                        obj.put("meg_replyToMessageId", chat.replyToMessageId);
                        obj.put("meg_senderUserId", chat.senderUserId);
                        obj.put("meg_viaBotUserId", chat.viaBotUserId);
                        obj.put("getConstructor", chat.content.getConstructor());


                        long chat_content;
                        chat_content = chat.content.getConstructor();
                        BA.Log("" + chat_content);
                        switch ((int) chat_content) {
                            case 1740718156:   //msg photo

                                obj.put("msg_type", "photo");

                                JSONObject channel_topmsg = new JSONObject();
                                try {
                                    BA.Log("photo");
                                    TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) chat.content;

                                    channel_topmsg.put("caption_topmsg", messagePhoto.caption.text);
                                    channel_topmsg.put("caption_id", messagePhoto.caption.text);

                                    //  TdApi.PhotoSize photoSize = (TdApi.PhotoSize) messagePhoto.photo.sizes;
                                    // final TdApi.PhotoSize[] userchats2 = messagePhoto.photo.sizes;

                                    for (TdApi.PhotoSize photoSize : messagePhoto.photo.sizes) {
                                        BA.Log("type : " + photoSize);

                                        channel_topmsg.put("photoid_height", photoSize.height);
                                        channel_topmsg.put("photoid_width", photoSize.width);
                                        channel_topmsg.put("photoid_type", photoSize.type);

                                        channel_topmsg.put("photoid_path", photoSize.photo.local.path);
                                        channel_topmsg.put("photoid_canBeDeleted", photoSize.photo.local.canBeDeleted);
                                        channel_topmsg.put("photoid_canBeDownloaded", photoSize.photo.local.canBeDownloaded);
                                        channel_topmsg.put("photoid_downloadedPrefixSize", photoSize.photo.local.downloadedPrefixSize);
                                        channel_topmsg.put("photoid_downloadedSize", photoSize.photo.local.downloadedSize);
                                        channel_topmsg.put("photoid_isDownloadingActive", photoSize.photo.local.isDownloadingActive);
                                        channel_topmsg.put("photoid_isDownloadingCompleted", photoSize.photo.local.isDownloadingCompleted);

                                        channel_topmsg.put("photoid_topmsg", photoSize.photo.id);
                                        channel_topmsg.put("photoid_remote", photoSize.photo.remote);
                                        channel_topmsg.put("photoid_size", photoSize.photo.size);

                                    }


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("massege_info", channel_topmsg.toString());


                                break;

                            case 596945783:   //msg document

                                obj.put("msg_type", "document");

                                JSONObject topmsg_ducument = new JSONObject();
                                try {
                                    BA.Log("document");
                                    TdApi.MessageDocument top_msgs = (TdApi.MessageDocument) chat.content;
                                    if (top_msgs.caption != null)
                                        topmsg_ducument.put("msgdoc_caption", top_msgs.caption.text);
                                    topmsg_ducument.put("msgdoc_entities", top_msgs.caption.entities);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("massege_info", topmsg_ducument.toString());


                                break;
                            case TdApi.MessageVideo.CONSTRUCTOR:   //msg document

                                obj.put("msg_type", "Video");

                                JSONObject js_video = new JSONObject();
                                try {
                                    BA.Log("document");
                                    TdApi.MessageVideo messageVideo = (TdApi.MessageVideo) chat.content;
                                    if (messageVideo.caption != null)
                                        js_video.put("vd_text", messageVideo.caption.text);
                                    js_video.put("vd_entities", messageVideo.caption.entities);
                                    js_video.put("vd_duration", messageVideo.video.duration);
                                    js_video.put("vd_fileName", messageVideo.video.fileName);
                                    js_video.put("vd_hasStickers", messageVideo.video.hasStickers);
                                    js_video.put("vd_height", messageVideo.video.height);
                                    js_video.put("vd_mimeType", messageVideo.video.mimeType);
                                    js_video.put("vd_width", messageVideo.video.width);

                                    js_video.put("vd_f_size", messageVideo.video.video.size);
                                    js_video.put("vd_f_id", messageVideo.video.video.id);
                                    js_video.put("vd_f_expectedSize", messageVideo.video.video.expectedSize);

                                    js_video.put("vd_f_r_uploadedSize", messageVideo.video.video.remote.uploadedSize);
                                    js_video.put("vd_f_r_isUploadingCompleted", messageVideo.video.video.remote.isUploadingCompleted);
                                    js_video.put("vd_f_r_isUploadingActive", messageVideo.video.video.remote.isUploadingActive);
                                    js_video.put("vd_f_r_id", messageVideo.video.video.remote.id);

                                    js_video.put("vd_f_l_canBeDeleted", messageVideo.video.video.local.canBeDeleted);
                                    js_video.put("vd_f_l_downloadedPrefixSize", messageVideo.video.video.local.downloadedPrefixSize);
                                    js_video.put("vd_f_l_isDownloadingCompleted", messageVideo.video.video.local.isDownloadingCompleted);
                                    js_video.put("vd_f_l_isDownloadingActive", messageVideo.video.video.local.isDownloadingActive);
                                    js_video.put("vd_f_l_downloadedSize", messageVideo.video.video.local.downloadedSize);
                                    js_video.put("vd_f_l_canBeDownloaded", messageVideo.video.video.local.canBeDownloaded);
                                    js_video.put("vd_f_l_path", messageVideo.video.video.local.path);

                                    js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                    js_video.put("vd_tb_l_canBeDownloaded", messageVideo.video.thumbnail.photo.local.canBeDownloaded);
                                    js_video.put("vd_tb_l_downloadedSize", messageVideo.video.thumbnail.photo.local.downloadedSize);
                                    js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                    js_video.put("vd_tb_l_isDownloadingActive", messageVideo.video.thumbnail.photo.local.isDownloadingActive);
                                    js_video.put("vd_tb_l_isDownloadingCompleted", messageVideo.video.thumbnail.photo.local.isDownloadingCompleted);
                                    js_video.put("vd_tb_l_canBeDeleted", messageVideo.video.thumbnail.photo.local.canBeDeleted);

                                    js_video.put("vd_tb_id", messageVideo.video.thumbnail.photo.id);
                                    js_video.put("vd_tb_size", messageVideo.video.thumbnail.photo.size);
                                    js_video.put("vd_tb_height", messageVideo.video.thumbnail.height);
                                    js_video.put("vd_tb_width", messageVideo.video.thumbnail.width);
                                    js_video.put("vd_tb_type", messageVideo.video.thumbnail.type);

                                    js_video.put("vd_tb_r_id", messageVideo.video.thumbnail.photo.remote.id);
                                    js_video.put("vd_tb_r_isUploadingActive", messageVideo.video.thumbnail.photo.remote.isUploadingActive);
                                    js_video.put("vd_tb_r_isUploadingCompleted", messageVideo.video.thumbnail.photo.remote.isUploadingCompleted);
                                    js_video.put("vd_tb_r_uploadedSize", messageVideo.video.thumbnail.photo.remote.uploadedSize);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("massege_info", js_video.toString());


                                break;
                            case 1989037971:   //msg message

                                obj.put("msg_type", "message");

                                JSONObject topmsg_message = new JSONObject();
                                try {
                                    BA.Log("message");
                                    TdApi.MessageText top_msgs = (TdApi.MessageText) chat.content;
                                    topmsg_message.put("msg_entities", top_msgs.webPage);
                                    topmsg_message.put("msg_text", top_msgs.text.text);


                                    JSONArray inline_msgup = new JSONArray();
                                    TdApi.ReplyMarkupInlineKeyboard rep_mark = (TdApi.ReplyMarkupInlineKeyboard) chat.replyMarkup;
                                    if (chat.replyMarkup != null) {
                                        obj.put("ReplyMarkup", rep_mark.getConstructor());
                                        //  final TdApi.ReplyMarkupShowKeyboard replyMarkupShowKeyboard = (TdApi.ReplyMarkupShowKeyboard) chat.replyMarkup;
                                        for (int i = 0; i < rep_mark.rows.length; i++) {
                                            int columnsInRow = rep_mark.rows[i].length;
                                            TableRow row = new TableRow(ba.context);
                                            for (int j = 0; j < rep_mark.rows[i].length; j++) {
                                                // replyMarkupShowKeyboard.rows[i][j];
                                                BA.Log("logs : " + rep_mark.rows[i][j]);
                                                String a;
                                                //rep_mark.rows[i][j].text;

                                                JSONObject ms_inline = new JSONObject();
                                                try {
                                                    BA.Log("inline");
                                                    //  TdApi.MessagePhoto ms_inline = (TdApi.MessagePhoto) chat.content;
                                                    ms_inline.put("text", rep_mark.rows[i][j].text);
                                                    //  ms_inline.put("type",rep_mark.rows[i][j].type.);
                                                    TdApi.InlineKeyboardButtonTypeCallback rep_callback = (TdApi.InlineKeyboardButtonTypeCallback) rep_mark.rows[i][j].type;

                                                    byte[] bytes = rep_callback.data.clone();
                                                    //TdApi.GetCallbackQueryAnswer getCallbackQueryAnswer_milad = new TdApi.GetCallbackQueryAnswer();
                                                    // TdApi.CallbackQueryData callbackQueryData_milad = new TdApi.CallbackQueryData();
                                                    //callbackQueryData_milad.data = bytes;
                                                    //  getCallbackQueryAnswer_milad.chatId=chat.chatId;
                                                    //  getCallbackQueryAnswer_milad.messageId=chat.id;
                                                    //  getCallbackQueryAnswer_milad.payload = callbackQueryData_milad;
                                                    // TG.getClientInstance().send(getCallbackQueryAnswer_milad, new Client.ResultHandler() {
                                                    //  @Override
                                                    //   public void onResult(TdApi.TLObject object) {

                                                    //        BA.Log("like msg"+object.toString());
                                                    //    }
                                                    //  });

                                                    String s1 = Arrays.toString(bytes);
                                                    String s2 = new String(bytes);


                                                    String base64String = Base64.encodeToString(bytes, rep_callback.data.length);
                                                    //byte[] backToBytes = Base64.decodeBase64(base64String);

                                                    ms_inline.put("data", s2);


                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }

                                                inline_msgup.put(ms_inline);


                                                //TdApi.InlineKeyboardButtonType inikey =(TdApi.InlineKeyboardButtonType) ;


                                            }
                                        }
                                    }


                                    topmsg_message.put("massege_inline", inline_msgup.toString());


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("massege_info", topmsg_message.toString());
                                break;

                            case TdApi.MessageVoiceNote.CONSTRUCTOR:   //msg message

                                obj.put("msg_type", "voice");

                                JSONObject topmsg_voice = new JSONObject();
                                try {
                                    BA.Log("voice");
                                    TdApi.MessageVoiceNote top_msgs = (TdApi.MessageVoiceNote) chat.content;
                                    if (top_msgs.caption != null)
                                        topmsg_voice.put("msgVoice_caption", top_msgs.caption.text);
                                    topmsg_voice.put("msgVoice_islistened", top_msgs.isListened);
                                    topmsg_voice.put("msgVoice_duration", top_msgs.voiceNote.duration);
                                    topmsg_voice.put("msgVoice_mimetype", top_msgs.voiceNote.mimeType);
                                    topmsg_voice.put("msgVoice_voiceId", top_msgs.voiceNote.voice.id);
                                    topmsg_voice.put("msgVoice_persistentId", top_msgs.voiceNote.voice.local.path);
                                    topmsg_voice.put("msgVoice_voiceSize", top_msgs.voiceNote.voice.size);
                                    topmsg_voice.put("msgVoice_waweForm", top_msgs.voiceNote.waveform.length);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("massege_info", topmsg_voice.toString());
                                break;

                            case TdApi.MessageAnimation.CONSTRUCTOR:   //msg message

                                obj.put("msg_type", "Animation");

                                JSONObject jsanimation = new JSONObject();
                                try {
                                    BA.Log("voice");
                                    TdApi.MessageAnimation messageAnimation = (TdApi.MessageAnimation) chat.content;
                                    jsanimation.put("anim_r_uploadedSize", messageAnimation.animation.animation.remote.uploadedSize);
                                    jsanimation.put("anim_r_isUploadingCompleted", messageAnimation.animation.animation.remote.isUploadingCompleted);
                                    jsanimation.put("anim_r_isUploadingActive", messageAnimation.animation.animation.remote.isUploadingActive);
                                    jsanimation.put("anim_r_id", messageAnimation.animation.animation.remote.id);

                                    jsanimation.put("anim_duration", messageAnimation.animation.duration);
                                    jsanimation.put("anim_fileName", messageAnimation.animation.fileName);
                                    jsanimation.put("anim_height", messageAnimation.animation.height);
                                    jsanimation.put("anim_mimeType", messageAnimation.animation.mimeType);
                                    jsanimation.put("anim_width", messageAnimation.animation.width);
                                    jsanimation.put("anim_size", messageAnimation.animation.animation.size);

                                    jsanimation.put("anim_id", messageAnimation.animation.animation.id);
                                    jsanimation.put("anim_expectedSize", messageAnimation.animation.animation.expectedSize);
                                    jsanimation.put("anim_canBeDeleted", messageAnimation.animation.animation.local.canBeDeleted);
                                    jsanimation.put("anim_local", messageAnimation.animation.animation.local);
                                    jsanimation.put("anim_downloadedPrefixSize", messageAnimation.animation.animation.local.downloadedPrefixSize);
                                    jsanimation.put("anim_isDownloadingCompleted", messageAnimation.animation.animation.local.isDownloadingCompleted);
                                    jsanimation.put("anim_isDownloadingActive", messageAnimation.animation.animation.local.isDownloadingActive);
                                    jsanimation.put("anim_downloadedSize", messageAnimation.animation.animation.local.downloadedSize);
                                    jsanimation.put("anim_canBeDownloaded", messageAnimation.animation.animation.local.canBeDownloaded);
                                    jsanimation.put("anim_path", messageAnimation.animation.animation.local.path);

                                    jsanimation.put("anim_tb_type", messageAnimation.animation.thumbnail.type);
                                    jsanimation.put("anim_tb_width", messageAnimation.animation.thumbnail.width);
                                    jsanimation.put("anim_tb_height", messageAnimation.animation.thumbnail.height);
                                    jsanimation.put("anim_tb_path", messageAnimation.animation.thumbnail.photo.local.path);
                                    jsanimation.put("anim_tb_canBeDownloaded", messageAnimation.animation.thumbnail.photo.local.canBeDownloaded);
                                    jsanimation.put("anim_tb_downloadedSize", messageAnimation.animation.thumbnail.photo.local.downloadedSize);
                                    jsanimation.put("anim_tb_isDownloadingActive", messageAnimation.animation.thumbnail.photo.local.isDownloadingActive);
                                    jsanimation.put("anim_tb_isDownloadingCompleted", messageAnimation.animation.thumbnail.photo.local.isDownloadingCompleted);
                                    jsanimation.put("anim_tb_downloadedPrefixSize", messageAnimation.animation.thumbnail.photo.local.downloadedPrefixSize);
                                    jsanimation.put("anim_tb_canBeDeleted", messageAnimation.animation.thumbnail.photo.local.canBeDeleted);
                                    jsanimation.put("anim_tb_expectedSize", messageAnimation.animation.thumbnail.photo.expectedSize);
                                    jsanimation.put("anim_tb_id", messageAnimation.animation.thumbnail.photo.id);
                                    jsanimation.put("anim_tb_size", messageAnimation.animation.thumbnail.photo.size);
                                    jsanimation.put("anim_tb_r_id", messageAnimation.animation.thumbnail.photo.remote.id);
                                    jsanimation.put("anim_tb_r_isUploadingActive", messageAnimation.animation.thumbnail.photo.remote.isUploadingActive);
                                    jsanimation.put("anim_tb_r_isUploadingCompleted", messageAnimation.animation.thumbnail.photo.remote.isUploadingCompleted);
                                    jsanimation.put("anim_tb_r_uploadedSize", messageAnimation.animation.thumbnail.photo.remote.uploadedSize);
                                    if (messageAnimation.caption != null)
                                        jsanimation.put("cp_text", messageAnimation.caption.text);
                                    jsanimation.put("cp_text", messageAnimation.caption.entities);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("massege_info", jsanimation.toString());
                                break;

                            case TdApi.MessageSticker.CONSTRUCTOR:   //msg message

                                obj.put("msg_type", "Sticker");

                                JSONObject js_sticker = new JSONObject();
                                try {
                                    BA.Log("sticker");
                                    TdApi.MessageSticker messageSticker = (TdApi.MessageSticker) chat.content;

                                    js_sticker.put("st_emoji", messageSticker.sticker.emoji);
                                    js_sticker.put("st_height", messageSticker.sticker.height);
                                    js_sticker.put("st_isMask", messageSticker.sticker.isMask);
                                    js_sticker.put("st_width", messageSticker.sticker.width);
                                    js_sticker.put("st_setId", messageSticker.sticker.setId);

                                    js_sticker.put("st_mp_", messageSticker.sticker.maskPosition.point);

                                    js_sticker.put("st_mp_scale", messageSticker.sticker.maskPosition.scale);
                                    js_sticker.put("st_mp_xShift", messageSticker.sticker.maskPosition.xShift);
                                    js_sticker.put("st_mp_yShift", messageSticker.sticker.maskPosition.yShift);

                                    js_sticker.put("st_r_uploadedSize", messageSticker.sticker.sticker.remote.uploadedSize);
                                    js_sticker.put("st_r_isUploadingCompleted", messageSticker.sticker.sticker.remote.isUploadingCompleted);
                                    js_sticker.put("st_r_isUploadingActive", messageSticker.sticker.sticker.remote.isUploadingActive);
                                    js_sticker.put("st_r_id", messageSticker.sticker.sticker.remote.id);

                                    js_sticker.put("st_size", messageSticker.sticker.sticker.size);
                                    js_sticker.put("st_id", messageSticker.sticker.sticker.id);
                                    js_sticker.put("st_expectedSize", messageSticker.sticker.sticker.expectedSize);

                                    js_sticker.put("st_l_canBeDeleted", messageSticker.sticker.sticker.local.canBeDeleted);
                                    js_sticker.put("st_l_isDownloadingCompleted", messageSticker.sticker.sticker.local.isDownloadingCompleted);
                                    js_sticker.put("st_l_isDownloadingActive", messageSticker.sticker.sticker.local.isDownloadingActive);
                                    js_sticker.put("st_l_path", messageSticker.sticker.sticker.local.path);
                                    js_sticker.put("st_l_downloadedSize", messageSticker.sticker.sticker.local.downloadedSize);
                                    js_sticker.put("st_l_canBeDownloaded", messageSticker.sticker.sticker.local.canBeDownloaded);
                                    js_sticker.put("st_l_downloadedPrefixSize", messageSticker.sticker.sticker.local.downloadedPrefixSize);

                                    js_sticker.put("st_tb_l_isDownloadingActive", messageSticker.sticker.thumbnail.photo.local.isDownloadingActive);
                                    js_sticker.put("st_tb_l_downloadedPrefixSize", messageSticker.sticker.thumbnail.photo.local.downloadedPrefixSize);
                                    js_sticker.put("st_tb_l_canBeDownloaded", messageSticker.sticker.thumbnail.photo.local.canBeDownloaded);
                                    js_sticker.put("st_tb_l_downloadedSize", messageSticker.sticker.thumbnail.photo.local.downloadedSize);
                                    js_sticker.put("st_tb_l_path", messageSticker.sticker.thumbnail.photo.local.path);
                                    js_sticker.put("st_tb_l_isDownloadingCompleted", messageSticker.sticker.thumbnail.photo.local.isDownloadingCompleted);

                                    js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.id);
                                    js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.size);
                                    js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.expectedSize);

                                    js_sticker.put("st_tb_r_id", messageSticker.sticker.thumbnail.photo.remote.id);
                                    js_sticker.put("st_tb_r_isUploadingActive", messageSticker.sticker.thumbnail.photo.remote.isUploadingActive);
                                    js_sticker.put("st_tb_r_isUploadingCompleted", messageSticker.sticker.thumbnail.photo.remote.isUploadingCompleted);
                                    js_sticker.put("st_tb_r_uploadedSize", messageSticker.sticker.thumbnail.photo.remote.uploadedSize);

                                    js_sticker.put("st_tb_type", messageSticker.sticker.thumbnail.type);
                                    js_sticker.put("st_tb_width", messageSticker.sticker.thumbnail.width);
                                    js_sticker.put("st_tb_height", messageSticker.sticker.thumbnail.height);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("massege_info", js_sticker.toString());
                                break;
                        }

                        arrmsgup.put(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  continue;

                    }
                    update_newmsg(arrmsgup.toString());
                    arrmsgup = new JSONArray(new ArrayList<String>());
                }
                case TdApi.UpdateChatLastMessage.CONSTRUCTOR: {
                    TdApi.UpdateChatLastMessage updateChat = (TdApi.UpdateChatLastMessage) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.lastMessage = updateChat.lastMessage;
                        setChatOrder(chat, updateChat.order);
                        //  BA.Log("imgid:"+chat.photo.small.id);
                        // client.send(new TdApi.DownloadFile(chat.photo.small.id,32), new AuthorizationRequestHandler());
                        //  BA.Log("imgid:"+chat.photo.small.local.downloadedSize);
                    }
                    break;
                }

                case TdApi.UpdateFile.CONSTRUCTOR: {
                    BA.Log("UpdateFile !");

                    //  BA.Log("file : "+object.toString());
                    TdApi.UpdateFile updateFile = (TdApi.UpdateFile) object;
                    //  BA.Log("file : "+updateFile.file.id);

                    JSONObject photo_file = new JSONObject();
                    try {
                        photo_file.put("photof_localpath", updateFile.file.local.path);
                        photo_file.put("photof_local_candownload", updateFile.file.local.canBeDownloaded);
                        photo_file.put("photof_local_downloadsize", updateFile.file.local.downloadedSize);
                        photo_file.put("photof_local_isdownload", updateFile.file.local.isDownloadingActive);
                        photo_file.put("photof_local_isdowloaded", updateFile.file.local.isDownloadingCompleted);
                        photo_file.put("photof_local_perfexsize", updateFile.file.local.downloadedPrefixSize);
                        photo_file.put("photof_local_canbedlelte", updateFile.file.local.canBeDeleted);
                        photo_file.put("photof_expectedSize", updateFile.file.expectedSize);
                        photo_file.put("photof_id", updateFile.file.id);
                        photo_file.put("photof_size", updateFile.file.size);
                        photo_file.put("photof_remot_id", updateFile.file.remote.id);
                        photo_file.put("photof_remot_isuploadactive", updateFile.file.remote.isUploadingActive);
                        photo_file.put("photof_remot_isuploadcomplit", updateFile.file.remote.isUploadingCompleted);
                        photo_file.put("photof_remot_uploadsize", updateFile.file.remote.uploadedSize);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    update_file(photo_file.toString());

                    //   BA.Log("expectedSize "+updateChat.file.remote);

                    //Bitmap bmp= BitmapFactory.decodeFile(updateChat.file.local.path);

                    //testRaiseEvent(bmp);
                    // onSuccess(bmp);
                    //  synchronized (chat) {
                    //  client.send(new TdApi.DownloadFile(chat.photo.small.id,32), new AuthorizationRequestHandler());
                    //    BA.Log("imgid:"+chat.photo.small.id);
                    //       BA.Log("imgid:"+chat.photo.small.local.path);
                    //}
                    break;
                }
                case TdApi.UpdateChatOrder.CONSTRUCTOR: {
                    TdApi.UpdateChatOrder updateChat = (TdApi.UpdateChatOrder) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        setChatOrder(chat, updateChat.order);
                    }
                    break;
                }
                case TdApi.UpdateChatIsPinned.CONSTRUCTOR: {
                    TdApi.UpdateChatIsPinned updateChat = (TdApi.UpdateChatIsPinned) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.isPinned = updateChat.isPinned;
                        setChatOrder(chat, updateChat.order);
                    }
                    break;
                }
                case TdApi.UpdateChatReadInbox.CONSTRUCTOR: {
                    TdApi.UpdateChatReadInbox updateChat = (TdApi.UpdateChatReadInbox) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.lastReadInboxMessageId = updateChat.lastReadInboxMessageId;
                        chat.unreadCount = updateChat.unreadCount;
                    }
                    break;
                }
                case TdApi.UpdateChatReadOutbox.CONSTRUCTOR: {
                    TdApi.UpdateChatReadOutbox updateChat = (TdApi.UpdateChatReadOutbox) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.lastReadOutboxMessageId = updateChat.lastReadOutboxMessageId;
                    }
                    break;
                }
                case TdApi.UpdateChatUnreadMentionCount.CONSTRUCTOR: {
                    TdApi.UpdateChatUnreadMentionCount updateChat = (TdApi.UpdateChatUnreadMentionCount) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.unreadMentionCount = updateChat.unreadMentionCount;
                    }
                    break;
                }

                case TdApi.UpdateMessageMentionRead.CONSTRUCTOR: {
                    TdApi.UpdateMessageMentionRead updateChat = (TdApi.UpdateMessageMentionRead) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.unreadMentionCount = updateChat.unreadMentionCount;
                        //       BA.Log(chat.title);
                    }
                    break;
                }
                case TdApi.UpdateChatReplyMarkup.CONSTRUCTOR: {
                    TdApi.UpdateChatReplyMarkup updateChat = (TdApi.UpdateChatReplyMarkup) object;
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.replyMarkupMessageId = updateChat.replyMarkupMessageId;
                    }
                    break;
                }
                case TdApi.UpdateChatDraftMessage.CONSTRUCTOR: {
                    TdApi.UpdateChatDraftMessage updateChat = (TdApi.UpdateChatDraftMessage) object;
                    //     BA.Log("imgid:"+updateChat);
                    TdApi.Chat chat = chats.get(updateChat.chatId);
                    synchronized (chat) {
                        chat.draftMessage = updateChat.draftMessage;
                        setChatOrder(chat, updateChat.order);
                    }
                    break;
                }
                case TdApi.UpdateScopeNotificationSettings.CONSTRUCTOR: {
                    TdApi.UpdateScopeNotificationSettings update = (TdApi.UpdateScopeNotificationSettings) object;

                }
                case TdApi.UpdateUserFullInfo.CONSTRUCTOR:
                    TdApi.UpdateUserFullInfo updateUserFullInfo = (TdApi.UpdateUserFullInfo) object;
                    usersFullInfo.put(updateUserFullInfo.userId, updateUserFullInfo.userFullInfo);
                    break;
                case TdApi.UpdateBasicGroupFullInfo.CONSTRUCTOR:
                    TdApi.UpdateBasicGroupFullInfo updateBasicGroupFullInfo = (TdApi.UpdateBasicGroupFullInfo) object;
                    basicGroupsFullInfo.put(updateBasicGroupFullInfo.basicGroupId, updateBasicGroupFullInfo.basicGroupFullInfo);
                    break;
                case TdApi.UpdateSupergroupFullInfo.CONSTRUCTOR:
                    TdApi.UpdateSupergroupFullInfo updateSupergroupFullInfo = (TdApi.UpdateSupergroupFullInfo) object;
                    supergroupsFullInfo.put(updateSupergroupFullInfo.supergroupId, updateSupergroupFullInfo.supergroupFullInfo);
                    break;
                default:

            }
        }

        public void update_newmsg(String Value) {

            if (ba.subExists(EventName + "_update_newmsg")) {
                BA.Log("lib:Raising.. " + EventName + "_update_newmsg ");
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_update_newmsg", false, new Object[]{Value});
                //  arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_update_newmsg");
            }
        }

        public void connectionstate(String Value) {

            if (ba.subExists(EventName + "_update_conn")) {
                BA.Log("lib:Raising.. " + EventName + "_update_conn ");
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_update_conn", false, new Object[]{Value});
                //  arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_update_conn");
            }
        }

        public void update_file(String Value) {

            if (ba.subExists(EventName + "_update_file")) {
                BA.Log("lib:Raising.. " + EventName + "_update_file ");
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_update_file", false, new Object[]{Value});
                //  arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_update_file");
            }
        }

        public void testRaiseEvent(Bitmap Value) {

            if (ba.subExists(EventName + "_imgphoto")) {
                BA.Log("lib:Raising.. " + EventName + "_imgphoto " + Value);
                ba.raiseEvent(this, null, 0, EventName + "_imgphoto", false, Value);
            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_imgphoto");
            }
        }

        public void onSuccess(Bitmap pBitmap) {
            // String eventName = (EventName+"_imgok");
            if (ba.subExists(EventName + "_imgphoto")) {
                CanvasWrapper.BitmapWrapper wrapper = new CanvasWrapper.BitmapWrapper();
                wrapper.setObject(pBitmap);
                ba.raiseEvent(this, EventName + "_imgphoto", wrapper, mTag);
                ///  ba.raiseEvent(this, EventName+ "_imgphoto", new Object[] { wrapper,mTag});
            }
        }
    }

    private class AuthorizationRequestHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            // BA.Log(object.toString());
            switch (object.getConstructor()) {

                case TdApi.Error.CONSTRUCTOR:
                    BA.Log(object.toString());
                    System.err.println("Receive an error:" + newLine + object);
                    onAuthorizationStateUpdated(null); // repeat last action
                    break;
                case TdApi.UpdateConnectionState.CONSTRUCTOR:
                    BA.Log("UpdateConnectionState");
                    TdApi.ConnectionState connectionState;
                    connectionState = ((TdApi.UpdateConnectionState) object).state;
                    TdApi.UpdateConnectionState updateConnectionState = new TdApi.UpdateConnectionState(connectionState);

                    //BA.Log("conn = "+connectionState);
                    BA.Log("conn = " + connectionState.getConstructor());
                    switch (connectionState.getConstructor()) {
                        case TdApi.ConnectionStateWaitingForNetwork.CONSTRUCTOR:
                            BA.Log("ConnectionStateWaitingForNetwork");
                            connectionstate("ConnectionStateWaitingForNetwork");
                            break;
                        case TdApi.ConnectionStateReady.CONSTRUCTOR:
                            BA.Log("ConnectionStateReady");
                            connectionstate("ConnectionStateReady");

                            break;
                        case TdApi.ConnectionStateConnecting.CONSTRUCTOR:
                            BA.Log("ConnectionStateConnecting");
                            connectionstate("ConnectionStateConnecting");

                            break;
                        case TdApi.ConnectionStateConnectingToProxy.CONSTRUCTOR:
                            connectionstate("ConnectionStateConnectingToProxy");

                            BA.Log("ConnectionStateConnectingToProxy");
                            break;
                        case TdApi.ConnectionStateUpdating.CONSTRUCTOR:
                            connectionstate("ConnectionStateUpdating");

                            BA.Log("ConnectionStateUpdating");
                            break;

                    }
                    break;
                case TdApi.Ok.CONSTRUCTOR:
                    BA.Log(object.toString());
                    // result is already received through UpdateAuthorizationState, nothing to do
                    break;

                case TdApi.File.CONSTRUCTOR: {

                    TdApi.File updatefile = (TdApi.File) object;
                    // BA.Log("addres "+updatefile.local.path);
                    Bitmap bmp = BitmapFactory.decodeFile(updatefile.local.path);

                    //  testRaiseEvent(bmp);
                    /// onSuccess(bmp);
                    break;
                }
                case TdApi.ChatMembers.CONSTRUCTOR: {
                    BA.Log("const get users");
                    TdApi.ChatMembers chatMembers = (TdApi.ChatMembers) object;

                    final TdApi.ChatMember[] userchats2 = chatMembers.members;

                    for (TdApi.ChatMember chatmem : chatMembers.members) {
                        // BA.Log("user id : "+chatmem.userId);
                        JSONObject channel_users = new JSONObject();
                        try {
                            //  BA.Log("Get user");

                            channel_users.put("user_id", chatmem.userId);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        arrmsg.put(channel_users);

                        // BA.Log("userinfo : "+channel_users.toString());
                        // user_infojs = channel_users;
                        //  BA.Log("userinfo : " + user_infojs.toString());
                        //  usersban(user_infojs.toString());
                    }

                    usersban((arrmsg.toString()));
                    arrmsg = new JSONArray(new ArrayList<String>());

                    break;
                }
                case TdApi.User.CONSTRUCTOR: {

                    JSONObject channel_users = new JSONObject();
                    try {
                        BA.Log("Get user");
                        // TdApi.User userchat = (TdApi.User) object;
                        TdApi.User userchat = (TdApi.User) object;
                        channel_users.put("user_phoneNumber", userchat.phoneNumber);
                        channel_users.put("user_lastName ", userchat.lastName);
                        channel_users.put("user_firstName ", userchat.firstName);
                        channel_users.put("user_id", userchat.id);
                        channel_users.put("user_bot", "");
                        channel_users.put("user_username", userchat.username);
                        channel_users.put("user_photoid", userchat.profilePhoto.small.id);
                        channel_users.put("user_persistentId", userchat.profilePhoto.small.local);


                        // obj.put("msg_info", channel_users.toString());


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // BA.Log("userinfo : "+channel_users.toString());
                    user_infojs = channel_users;
                    //  BA.Log("userinfo : " + user_infojs.toString());
                    userinfos(user_infojs.toString());
                    break;
                }
                case TdApi.AddChatMember.CONSTRUCTOR: {

                }
                default:
                    System.err.println("Receive wrong response from TDLib:" + newLine + object);
            }
        }

        public void testRaiseEvent(Bitmap Value) {

            if (ba.subExists(EventName + "_imgphoto")) {
                //  BA.Log("lib:Raising.. " + EventName + "_imgphoto " + Value);
                //  ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_imgphoto", false, new Object[]{Value});
                ba.raiseEvent(this, EventName + "_imgphoto", Value, mTag);
            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_imgphoto");
            }
        }

        public void userinfos(String Value) {

            if (ba.subExists(EventName + "_getuserinfo")) {
                // BA.Log("lib:Raising.. " + EventName + "_getuserinfo " + Value);
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getuserinfo", false, new Object[]{Value});
                //  arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_getuserinfo");
            }
        }

        public void connectionstate(String Value) {

            if (ba.subExists(EventName + "_update_conn")) {
                BA.Log("lib:Raising.. " + EventName + "_update_conn " + Value);
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_update_conn", false, new Object[]{Value});
                //  arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_update_conn");
            }
        }

        public void usersban(String Value) {

            if (ba.subExists(EventName + "_getusersban")) {
                //  BA.Log("lib:Raising.. " + EventName + "_getusersban " + Value);
                //  Value=Value.replace("[","").replace("]","");
                ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getusersban", false, new Object[]{Value});
                //  arr = new JSONArray(new ArrayList<String>());

            } else {
                BA.Log("lib: NOTFOUND '" + EventName + "_getusersban");
            }
        }

        public void onSuccess(Bitmap pBitmap) {
            // String eventName = (EventName+"_imgok");
            if (ba.subExists(EventName + "_imgphoto")) {
                CanvasWrapper.BitmapWrapper wrapper = new CanvasWrapper.BitmapWrapper();
                wrapper.setObject(pBitmap);
                ba.raiseEvent(this, EventName + "_imgphoto", wrapper, mTag);
                ///  ba.raiseEvent(this, EventName+ "_imgphoto", new Object[] { wrapper,mTag});
            }
        }
    }

    private void errorsend(String value) {

        if (ba.subExists(EventName + "_error")) {
            //  BA.Log("lib:Raising.. " + EventName + "_imgphoto " + Value);
            //  ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_imgphoto", false, new Object[]{Value});
            ba.raiseEvent(this, EventName + "_error", value, mTag);
        } else {
            BA.Log("lib: NOTFOUND '" + EventName + "_error");
        }
    }

    public void testsms(String Value) {

        if (ba.subExists(EventName + "_sendsms")) {
            BA.Log("lib:Raising.. " + EventName + "_sendsms " + Value);
            ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_sendsms", false, new Object[]{Value});
        } else {
            BA.Log("lib: NOTFOUND '" + EventName + "_sendsms");
        }
    }

    private void msgpasssend(String value) {

        if (ba.subExists(EventName + "_msgcode")) {
            //  BA.Log("lib:Raising.. " + EventName + "_imgphoto " + Value);
            //  ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_imgphoto", false, new Object[]{Value});
            ba.raiseEvent(this, EventName + "_msgcode", value, mTag);
        } else {
            BA.Log("lib: NOTFOUND '" + EventName + "_msgcode");
        }
    }

    public void Joinchannel(String chat_id, final int id_user) {
        //
        TdApi.SearchPublicChat searchPublicChat = new TdApi.SearchPublicChat(chat_id);
        client.send(new TdApi.SearchPublicChat(chat_id), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                switch (object.getConstructor()) {
                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        errorsend(channel_users.toString());
                        break;

                    case TdApi.Chat.CONSTRUCTOR:
                        TdApi.Chat chat = (TdApi.Chat) object;

                        final long chatId = chat.id;
                        TdApi.AddChatMember getChata = new TdApi.AddChatMember(chatId, id_user, 0);
                        client.send(getChata, new Client.ResultHandler() {
                            @Override
                            public void onResult(TdApi.Object object) {
                                ba.Log("user join : "+object.toString());
                                ba.Log("const join : "+object.getConstructor());

                                if (object.getConstructor()==-722616727){
                                    okjoin("ok");
                                }

                                switch (object.getConstructor()) {
                                    case TdApi.Error.CONSTRUCTOR:
                                        TdApi.Error error = (TdApi.Error) object;
                                        JSONObject channel_users = new JSONObject();
                                        ;
                                        try {
                                            BA.Log("error");

                                            channel_users.put("Error_code", error.code);
                                            channel_users.put("Error_msg", error.message);
                                            channel_users.put("ErrorConstructor", error.getConstructor());


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        errorsend(channel_users.toString());
                                        break;
                                    case TdApi.AddChatMember.CONSTRUCTOR:
                                        okjoin("ok");
                                        break;

                                }
                            }
                        });




                        break;

                }


            }
            public void okjoin(String Value) {

                if (ba.subExists(EventName + "_joinchannel")) {
                    BA.Log("lib:Raising.. " + EventName + "_joinchannel " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_joinchannel", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_joinchannel");
                }

            }
        });


    }

    public void Viewmsg(long chat_id, final long id_msgs[]) {
        //

        TdApi.ViewMessages searchPublicChat = new TdApi.ViewMessages(chat_id,id_msgs,true);
        client.send(searchPublicChat, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msgview : "+object.toString()+"cons : "+object.getConstructor());

                if(object.getConstructor()==-722616727){
                    testRaiseEvent("ok");
                }

                switch (object.getConstructor()) {
                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        ersend(channel_users.toString());
                        break;
                    case TdApi.ViewMessages.CONSTRUCTOR:
                        testRaiseEvent("ok");
                        break;

                }
            }
            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_viewdon")) {
                    BA.Log("lib:Raising.. " + EventName + "_viewdon " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_viewdon", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_viewdon");
                }
            }

            public void ersend(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }


        });



    }

    public void Getme() {
        //client.send(new TdApi.SetAuthenticationPhoneNumber(phoneN, false, false), new AuthorizationRequestHandler());
        client.send(new TdApi.GetMe(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msg :" + object.toString());
                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.User.CONSTRUCTOR:
                        TdApi.User users = (TdApi.User) object;
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("username", users.username);
                            obj.put("firstName", users.firstName);
                          //  obj.put("foreignLink", users.);
                            obj.put("haveAccess", users.haveAccess);
                            obj.put("id", users.id);
                            obj.put("isVerified", users.isVerified);
                            obj.put("lastName", users.lastName);
                            //obj.put("myLink", users.);
                            obj.put("phoneNumber", users.phoneNumber);
                            obj.put("profilePhoto", users.profilePhoto);
                            obj.put("restrictionReason", users.restrictionReason);
                            obj.put("status", users.status);
                            obj.put("type", users.type);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BA.Log(obj.toString());
                        userinfos((obj.toString()));

                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void userinfos(String Value) {

                if (ba.subExists(EventName + "_getme")) {
                    BA.Log("lib:Raising.. " + EventName + "_getme " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getme", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getme");
                }
            }

        });

    }

    public void Getchannelinfo(int Chanid) {
        //client.send(new TdApi.SetAuthenticationPhoneNumber(phoneN, false, false), new AuthorizationRequestHandler());
        client.send(new TdApi.GetSupergroupFullInfo(Chanid), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msg :" + object.toString());
                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.GetSupergroupFullInfo.CONSTRUCTOR:
                        TdApi.User users = (TdApi.User) object;
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("username", users.username);
                            obj.put("firstName", users.firstName);
                            //  obj.put("foreignLink", users.);
                            obj.put("haveAccess", users.haveAccess);
                            obj.put("id", users.id);
                            obj.put("isVerified", users.isVerified);
                            obj.put("lastName", users.lastName);
                            //obj.put("myLink", users.);
                            obj.put("phoneNumber", users.phoneNumber);
                            obj.put("profilePhoto", users.profilePhoto);
                            obj.put("restrictionReason", users.restrictionReason);
                            obj.put("status", users.status);
                            obj.put("type", users.type);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BA.Log(obj.toString());
                        userinfos((obj.toString()));

                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void userinfos(String Value) {

                if (ba.subExists(EventName + "_getme")) {
                    BA.Log("lib:Raising.. " + EventName + "_getme " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getme", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getme");
                }
            }

        });

    }

    public void Getchanneluser(int Chanid) {
        //client.send(new TdApi.SetAuthenticationPhoneNumber(phoneN, false, false), new AuthorizationRequestHandler());
        client.send(new TdApi.GetSupergroup(Chanid), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msg :" + object.toString());
                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.Supergroup.CONSTRUCTOR:
                        TdApi.Supergroup users = (TdApi.Supergroup) object;
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("anyoneCanInvite", users.anyoneCanInvite);
                            obj.put("date", users.date);
                             obj.put("id", users.id);
                            obj.put("isChannel", users.isChannel);
                            obj.put("isVerified", users.isVerified);
                            obj.put("memberCount", users.memberCount);
                            obj.put("signMessages", users.signMessages);
                            obj.put("restrictionReason", users.restrictionReason);
                            obj.put("status", users.status);
                            obj.put("username", users.username);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BA.Log(obj.toString());
                        userinfos((obj.toString()));

                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void userinfos(String Value) {

                if (ba.subExists(EventName + "_getsupergroup")) {
                    BA.Log("lib:Raising.. " + EventName + "_getsupergroup " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getsupergroup", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getsupergroup");
                }
            }

        });

    }

    public void Getingmsg(final long chatid, final long fromMsg_id[], final int offs, final int lim, String namechannel) {


        client.send(new TdApi.SearchPublicChat(namechannel), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                   //ba.Log("og1 "+object.toString());
                if (object.getConstructor() == TdApi.Chat.CONSTRUCTOR) {
                    TdApi.Chat chat = (TdApi.Chat) object;
                    //TdApi.Message topMessage = chat;
                    long chataId = chat.id;

                    client.send(new TdApi.GetMessages(chataId,fromMsg_id), new Client.ResultHandler() {
                        @Override
                        public void onResult(TdApi.Object object) {

                            ba.Log("msg2 "+object.toString());

                            if (object.getConstructor() == TdApi.Messages.CONSTRUCTOR) {

                                TdApi.Messages messages = (TdApi.Messages) object;

                                ///BA.Log("chat message : "+messages.messages.toString());

                                final TdApi.Message[] chats2 = messages.messages;
                                for (TdApi.Message chat : messages.messages) {


                                    final JSONObject obj = new JSONObject();
                                    try {

                                        obj.put("meg_id", chat.id);
                                        obj.put("meg_date", chat.date);
                                        obj.put("meg_views", chat.views);
                                        obj.put("meg_canBeDeleted", chat.canBeDeletedForAllUsers);
                                        obj.put("meg_canBeEdited", chat.canBeEdited);
                                        obj.put("meg_chatId", chat.chatId);
                                        obj.put("meg_editDate", chat.editDate);
                                        obj.put("meg_isPost", chat.isChannelPost);
                                        obj.put("meg_replyToMessageId", chat.replyToMessageId);
                                        obj.put("meg_senderUserId", chat.senderUserId);
                                        obj.put("meg_viaBotUserId", chat.viaBotUserId);
                                        obj.put("getConstructor", chat.content.getConstructor());


                                        long chat_content;
                                        chat_content = chat.content.getConstructor();
                                        BA.Log("" + chat_content);
                                        switch ((int) chat_content) {

                                            case 1740718156:   //msg photo

                                                obj.put("msg_type", "photo");

                                                JSONObject channel_topmsg = new JSONObject();
                                                try {
                                                    BA.Log("photo");
                                                    TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) chat.content;

                                                    channel_topmsg.put("caption_topmsg", messagePhoto.caption.text);
                                                    channel_topmsg.put("caption_id", messagePhoto.caption.text);

                                                    //  TdApi.PhotoSize photoSize = (TdApi.PhotoSize) messagePhoto.photo.sizes;
                                                    // final TdApi.PhotoSize[] userchats2 = messagePhoto.photo.sizes;

                                                    for (TdApi.PhotoSize photoSize : messagePhoto.photo.sizes) {
                                                        BA.Log("type : " + photoSize);

                                                        channel_topmsg.put("photoid_height", photoSize.height);
                                                        channel_topmsg.put("photoid_width", photoSize.width);
                                                        channel_topmsg.put("photoid_type", photoSize.type);

                                                        channel_topmsg.put("photoid_path", photoSize.photo.local.path);
                                                        channel_topmsg.put("photoid_canBeDeleted", photoSize.photo.local.canBeDeleted);
                                                        channel_topmsg.put("photoid_canBeDownloaded", photoSize.photo.local.canBeDownloaded);
                                                        channel_topmsg.put("photoid_downloadedPrefixSize", photoSize.photo.local.downloadedPrefixSize);
                                                        channel_topmsg.put("photoid_downloadedSize", photoSize.photo.local.downloadedSize);
                                                        channel_topmsg.put("photoid_isDownloadingActive", photoSize.photo.local.isDownloadingActive);
                                                        channel_topmsg.put("photoid_isDownloadingCompleted", photoSize.photo.local.isDownloadingCompleted);

                                                        channel_topmsg.put("photoid_topmsg", photoSize.photo.id);
                                                        channel_topmsg.put("photoid_remote", photoSize.photo.remote);
                                                        channel_topmsg.put("photoid_size", photoSize.photo.size);

                                                    }


                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }

                                                obj.put("massege_info", channel_topmsg.toString());


                                                break;

                                            case 596945783:   //msg document

                                                obj.put("msg_type", "document");

                                                JSONObject topmsg_ducument = new JSONObject();
                                                try {
                                                    BA.Log("document");
                                                    TdApi.MessageDocument top_msgs = (TdApi.MessageDocument) chat.content;
                                                    if (top_msgs.caption != null)
                                                        topmsg_ducument.put("msgdoc_caption", top_msgs.caption.text);
                                                    topmsg_ducument.put("msgdoc_entities", top_msgs.caption.entities);


                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }

                                                obj.put("massege_info", topmsg_ducument.toString());


                                                break;
                                            case TdApi.MessageVideo.CONSTRUCTOR:   //msg document

                                                obj.put("msg_type", "Video");

                                                JSONObject js_video = new JSONObject();
                                                try {
                                                    BA.Log("document");
                                                    TdApi.MessageVideo messageVideo = (TdApi.MessageVideo) chat.content;
                                                    if (messageVideo.caption != null)
                                                        js_video.put("vd_text", messageVideo.caption.text);
                                                    js_video.put("vd_entities", messageVideo.caption.entities);
                                                    js_video.put("vd_duration", messageVideo.video.duration);
                                                    js_video.put("vd_fileName", messageVideo.video.fileName);
                                                    js_video.put("vd_hasStickers", messageVideo.video.hasStickers);
                                                    js_video.put("vd_height", messageVideo.video.height);
                                                    js_video.put("vd_mimeType", messageVideo.video.mimeType);
                                                    js_video.put("vd_width", messageVideo.video.width);

                                                    js_video.put("vd_f_size", messageVideo.video.video.size);
                                                    js_video.put("vd_f_id", messageVideo.video.video.id);
                                                    js_video.put("vd_f_expectedSize", messageVideo.video.video.expectedSize);

                                                    js_video.put("vd_f_r_uploadedSize", messageVideo.video.video.remote.uploadedSize);
                                                    js_video.put("vd_f_r_isUploadingCompleted", messageVideo.video.video.remote.isUploadingCompleted);
                                                    js_video.put("vd_f_r_isUploadingActive", messageVideo.video.video.remote.isUploadingActive);
                                                    js_video.put("vd_f_r_id", messageVideo.video.video.remote.id);

                                                    js_video.put("vd_f_l_canBeDeleted", messageVideo.video.video.local.canBeDeleted);
                                                    js_video.put("vd_f_l_downloadedPrefixSize", messageVideo.video.video.local.downloadedPrefixSize);
                                                    js_video.put("vd_f_l_isDownloadingCompleted", messageVideo.video.video.local.isDownloadingCompleted);
                                                    js_video.put("vd_f_l_isDownloadingActive", messageVideo.video.video.local.isDownloadingActive);
                                                    js_video.put("vd_f_l_downloadedSize", messageVideo.video.video.local.downloadedSize);
                                                    js_video.put("vd_f_l_canBeDownloaded", messageVideo.video.video.local.canBeDownloaded);
                                                    js_video.put("vd_f_l_path", messageVideo.video.video.local.path);

                                                    js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                                    js_video.put("vd_tb_l_canBeDownloaded", messageVideo.video.thumbnail.photo.local.canBeDownloaded);
                                                    js_video.put("vd_tb_l_downloadedSize", messageVideo.video.thumbnail.photo.local.downloadedSize);
                                                    js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                                    js_video.put("vd_tb_l_isDownloadingActive", messageVideo.video.thumbnail.photo.local.isDownloadingActive);
                                                    js_video.put("vd_tb_l_isDownloadingCompleted", messageVideo.video.thumbnail.photo.local.isDownloadingCompleted);
                                                    js_video.put("vd_tb_l_canBeDeleted", messageVideo.video.thumbnail.photo.local.canBeDeleted);

                                                    js_video.put("vd_tb_id", messageVideo.video.thumbnail.photo.id);
                                                    js_video.put("vd_tb_size", messageVideo.video.thumbnail.photo.size);
                                                    js_video.put("vd_tb_height", messageVideo.video.thumbnail.height);
                                                    js_video.put("vd_tb_width", messageVideo.video.thumbnail.width);
                                                    js_video.put("vd_tb_type", messageVideo.video.thumbnail.type);

                                                    js_video.put("vd_tb_r_id", messageVideo.video.thumbnail.photo.remote.id);
                                                    js_video.put("vd_tb_r_isUploadingActive", messageVideo.video.thumbnail.photo.remote.isUploadingActive);
                                                    js_video.put("vd_tb_r_isUploadingCompleted", messageVideo.video.thumbnail.photo.remote.isUploadingCompleted);
                                                    js_video.put("vd_tb_r_uploadedSize", messageVideo.video.thumbnail.photo.remote.uploadedSize);


                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }

                                                obj.put("massege_info", js_video.toString());


                                                break;
                                            case 1989037971:   //msg message

                                                obj.put("msg_type", "message");

                                                JSONObject topmsg_message = new JSONObject();
                                                try {
                                                    BA.Log("message");
                                                    TdApi.MessageText top_msgs = (TdApi.MessageText) chat.content;
                                                    topmsg_message.put("msg_entities", top_msgs.webPage);
                                                    topmsg_message.put("msg_text", top_msgs.text.text);


                                                    JSONArray inline_msgup = new JSONArray();
                                                    TdApi.ReplyMarkupInlineKeyboard rep_mark = (TdApi.ReplyMarkupInlineKeyboard) chat.replyMarkup;
                                                    if (chat.replyMarkup != null) {
                                                        obj.put("ReplyMarkup", rep_mark.getConstructor());
                                                        //  final TdApi.ReplyMarkupShowKeyboard replyMarkupShowKeyboard = (TdApi.ReplyMarkupShowKeyboard) chat.replyMarkup;
                                                        for (int i = 0; i < rep_mark.rows.length; i++) {
                                                            int columnsInRow = rep_mark.rows[i].length;
                                                            TableRow row = new TableRow(ba.context);
                                                            for (int j = 0; j < rep_mark.rows[i].length; j++) {
                                                                // replyMarkupShowKeyboard.rows[i][j];
                                                                BA.Log("logs : " + rep_mark.rows[i][j]);
                                                                String a;
                                                                //rep_mark.rows[i][j].text;

                                                                JSONObject ms_inline = new JSONObject();
                                                                try {
                                                                    BA.Log("inline");
                                                                    //  TdApi.MessagePhoto ms_inline = (TdApi.MessagePhoto) chat.content;
                                                                    ms_inline.put("text", rep_mark.rows[i][j].text);
                                                                    //  ms_inline.put("type",rep_mark.rows[i][j].type.);
                                                                    TdApi.InlineKeyboardButtonTypeCallback rep_callback = (TdApi.InlineKeyboardButtonTypeCallback) rep_mark.rows[i][j].type;

                                                                    byte[] bytes = rep_callback.data.clone();
                                                                    //TdApi.GetCallbackQueryAnswer getCallbackQueryAnswer_milad = new TdApi.GetCallbackQueryAnswer();
                                                                    // TdApi.CallbackQueryData callbackQueryData_milad = new TdApi.CallbackQueryData();
                                                                    //callbackQueryData_milad.data = bytes;
                                                                    //  getCallbackQueryAnswer_milad.chatId=chat.chatId;
                                                                    //  getCallbackQueryAnswer_milad.messageId=chat.id;
                                                                    //  getCallbackQueryAnswer_milad.payload = callbackQueryData_milad;
                                                                    // TG.getClientInstance().send(getCallbackQueryAnswer_milad, new Client.ResultHandler() {
                                                                    //  @Override
                                                                    //   public void onResult(TdApi.TLObject object) {

                                                                    //        BA.Log("like msg"+object.toString());
                                                                    //    }
                                                                    //  });

                                                                    String s1 = Arrays.toString(bytes);
                                                                    String s2 = new String(bytes);


                                                                    String base64String = Base64.encodeToString(bytes, rep_callback.data.length);
                                                                    //byte[] backToBytes = Base64.decodeBase64(base64String);

                                                                    ms_inline.put("data", s2);


                                                                } catch (JSONException e) {
                                                                    // TODO Auto-generated catch block
                                                                    e.printStackTrace();
                                                                }

                                                                inline_msgup.put(ms_inline);


                                                                //TdApi.InlineKeyboardButtonType inikey =(TdApi.InlineKeyboardButtonType) ;


                                                            }
                                                        }
                                                    }


                                                    topmsg_message.put("massege_inline", inline_msgup.toString());


                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                obj.put("massege_info", topmsg_message.toString());
                                                break;

                                            case 527777781:   //msg message

                                                obj.put("msg_type", "voice");

                                                JSONObject topmsg_voice = new JSONObject();
                                                try {
                                                    BA.Log("voice");
                                                    TdApi.MessageVoiceNote top_msgs = (TdApi.MessageVoiceNote) chat.content;
                                                    if (top_msgs.caption != null)
                                                        topmsg_voice.put("msgVoice_caption", top_msgs.caption.text);
                                                    topmsg_voice.put("msgVoice_islistened", top_msgs.isListened);
                                                    topmsg_voice.put("msgVoice_duration", top_msgs.voiceNote.duration);
                                                    topmsg_voice.put("msgVoice_mimetype", top_msgs.voiceNote.mimeType);
                                                    topmsg_voice.put("msgVoice_voiceId", top_msgs.voiceNote.voice.id);
                                                    topmsg_voice.put("msgVoice_persistentId", top_msgs.voiceNote.voice.local.path);
                                                    topmsg_voice.put("msgVoice_voiceSize", top_msgs.voiceNote.voice.size);
                                                    topmsg_voice.put("msgVoice_waweForm", top_msgs.voiceNote.waveform.length);

                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                obj.put("massege_info", topmsg_voice.toString());
                                                break;

                                            case TdApi.MessageAnimation.CONSTRUCTOR:   //msg message

                                                obj.put("msg_type", "Animation");

                                                JSONObject jsanimation = new JSONObject();
                                                try {
                                                    BA.Log("voice");
                                                    TdApi.MessageAnimation messageAnimation = (TdApi.MessageAnimation) chat.content;
                                                    jsanimation.put("anim_r_uploadedSize", messageAnimation.animation.animation.remote.uploadedSize);
                                                    jsanimation.put("anim_r_isUploadingCompleted", messageAnimation.animation.animation.remote.isUploadingCompleted);
                                                    jsanimation.put("anim_r_isUploadingActive", messageAnimation.animation.animation.remote.isUploadingActive);
                                                    jsanimation.put("anim_r_id", messageAnimation.animation.animation.remote.id);

                                                    jsanimation.put("anim_duration", messageAnimation.animation.duration);
                                                    jsanimation.put("anim_fileName", messageAnimation.animation.fileName);
                                                    jsanimation.put("anim_height", messageAnimation.animation.height);
                                                    jsanimation.put("anim_mimeType", messageAnimation.animation.mimeType);
                                                    jsanimation.put("anim_width", messageAnimation.animation.width);
                                                    jsanimation.put("anim_size", messageAnimation.animation.animation.size);

                                                    jsanimation.put("anim_id", messageAnimation.animation.animation.id);
                                                    jsanimation.put("anim_expectedSize", messageAnimation.animation.animation.expectedSize);
                                                    jsanimation.put("anim_canBeDeleted", messageAnimation.animation.animation.local.canBeDeleted);
                                                    jsanimation.put("anim_local", messageAnimation.animation.animation.local);
                                                    jsanimation.put("anim_downloadedPrefixSize", messageAnimation.animation.animation.local.downloadedPrefixSize);
                                                    jsanimation.put("anim_isDownloadingCompleted", messageAnimation.animation.animation.local.isDownloadingCompleted);
                                                    jsanimation.put("anim_isDownloadingActive", messageAnimation.animation.animation.local.isDownloadingActive);
                                                    jsanimation.put("anim_downloadedSize", messageAnimation.animation.animation.local.downloadedSize);
                                                    jsanimation.put("anim_canBeDownloaded", messageAnimation.animation.animation.local.canBeDownloaded);
                                                    jsanimation.put("anim_path", messageAnimation.animation.animation.local.path);

                                                    jsanimation.put("anim_tb_type", messageAnimation.animation.thumbnail.type);
                                                    jsanimation.put("anim_tb_width", messageAnimation.animation.thumbnail.width);
                                                    jsanimation.put("anim_tb_height", messageAnimation.animation.thumbnail.height);
                                                    jsanimation.put("anim_tb_path", messageAnimation.animation.thumbnail.photo.local.path);
                                                    jsanimation.put("anim_tb_canBeDownloaded", messageAnimation.animation.thumbnail.photo.local.canBeDownloaded);
                                                    jsanimation.put("anim_tb_downloadedSize", messageAnimation.animation.thumbnail.photo.local.downloadedSize);
                                                    jsanimation.put("anim_tb_isDownloadingActive", messageAnimation.animation.thumbnail.photo.local.isDownloadingActive);
                                                    jsanimation.put("anim_tb_isDownloadingCompleted", messageAnimation.animation.thumbnail.photo.local.isDownloadingCompleted);
                                                    jsanimation.put("anim_tb_downloadedPrefixSize", messageAnimation.animation.thumbnail.photo.local.downloadedPrefixSize);
                                                    jsanimation.put("anim_tb_canBeDeleted", messageAnimation.animation.thumbnail.photo.local.canBeDeleted);
                                                    jsanimation.put("anim_tb_expectedSize", messageAnimation.animation.thumbnail.photo.expectedSize);
                                                    jsanimation.put("anim_tb_id", messageAnimation.animation.thumbnail.photo.id);
                                                    jsanimation.put("anim_tb_size", messageAnimation.animation.thumbnail.photo.size);
                                                    jsanimation.put("anim_tb_r_id", messageAnimation.animation.thumbnail.photo.remote.id);
                                                    jsanimation.put("anim_tb_r_isUploadingActive", messageAnimation.animation.thumbnail.photo.remote.isUploadingActive);
                                                    jsanimation.put("anim_tb_r_isUploadingCompleted", messageAnimation.animation.thumbnail.photo.remote.isUploadingCompleted);
                                                    jsanimation.put("anim_tb_r_uploadedSize", messageAnimation.animation.thumbnail.photo.remote.uploadedSize);
                                                    if (messageAnimation.caption != null)
                                                        jsanimation.put("cp_text", messageAnimation.caption.text);
                                                    jsanimation.put("cp_text", messageAnimation.caption.entities);

                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                obj.put("massege_info", jsanimation.toString());
                                                break;

                                            case TdApi.MessageSticker.CONSTRUCTOR:   //msg message

                                                obj.put("msg_type", "Sticker");

                                                JSONObject js_sticker = new JSONObject();
                                                try {
                                                    BA.Log("voice");
                                                    TdApi.MessageSticker messageSticker = (TdApi.MessageSticker) chat.content;

                                                    js_sticker.put("st_emoji", messageSticker.sticker.emoji);
                                                    js_sticker.put("st_height", messageSticker.sticker.height);
                                                    js_sticker.put("st_isMask", messageSticker.sticker.isMask);
                                                    js_sticker.put("st_width", messageSticker.sticker.width);
                                                    js_sticker.put("st_setId", messageSticker.sticker.setId);

                                                    js_sticker.put("st_mp_", messageSticker.sticker.maskPosition.point);

                                                    js_sticker.put("st_mp_scale", messageSticker.sticker.maskPosition.scale);
                                                    js_sticker.put("st_mp_xShift", messageSticker.sticker.maskPosition.xShift);
                                                    js_sticker.put("st_mp_yShift", messageSticker.sticker.maskPosition.yShift);

                                                    js_sticker.put("st_r_uploadedSize", messageSticker.sticker.sticker.remote.uploadedSize);
                                                    js_sticker.put("st_r_isUploadingCompleted", messageSticker.sticker.sticker.remote.isUploadingCompleted);
                                                    js_sticker.put("st_r_isUploadingActive", messageSticker.sticker.sticker.remote.isUploadingActive);
                                                    js_sticker.put("st_r_id", messageSticker.sticker.sticker.remote.id);

                                                    js_sticker.put("st_size", messageSticker.sticker.sticker.size);
                                                    js_sticker.put("st_id", messageSticker.sticker.sticker.id);
                                                    js_sticker.put("st_expectedSize", messageSticker.sticker.sticker.expectedSize);

                                                    js_sticker.put("st_l_canBeDeleted", messageSticker.sticker.sticker.local.canBeDeleted);
                                                    js_sticker.put("st_l_isDownloadingCompleted", messageSticker.sticker.sticker.local.isDownloadingCompleted);
                                                    js_sticker.put("st_l_isDownloadingActive", messageSticker.sticker.sticker.local.isDownloadingActive);
                                                    js_sticker.put("st_l_path", messageSticker.sticker.sticker.local.path);
                                                    js_sticker.put("st_l_downloadedSize", messageSticker.sticker.sticker.local.downloadedSize);
                                                    js_sticker.put("st_l_canBeDownloaded", messageSticker.sticker.sticker.local.canBeDownloaded);
                                                    js_sticker.put("st_l_downloadedPrefixSize", messageSticker.sticker.sticker.local.downloadedPrefixSize);

                                                    js_sticker.put("st_tb_l_isDownloadingActive", messageSticker.sticker.thumbnail.photo.local.isDownloadingActive);
                                                    js_sticker.put("st_tb_l_downloadedPrefixSize", messageSticker.sticker.thumbnail.photo.local.downloadedPrefixSize);
                                                    js_sticker.put("st_tb_l_canBeDownloaded", messageSticker.sticker.thumbnail.photo.local.canBeDownloaded);
                                                    js_sticker.put("st_tb_l_downloadedSize", messageSticker.sticker.thumbnail.photo.local.downloadedSize);
                                                    js_sticker.put("st_tb_l_path", messageSticker.sticker.thumbnail.photo.local.path);
                                                    js_sticker.put("st_tb_l_isDownloadingCompleted", messageSticker.sticker.thumbnail.photo.local.isDownloadingCompleted);

                                                    js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.id);
                                                    js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.size);
                                                    js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.expectedSize);

                                                    js_sticker.put("st_tb_r_id", messageSticker.sticker.thumbnail.photo.remote.id);
                                                    js_sticker.put("st_tb_r_isUploadingActive", messageSticker.sticker.thumbnail.photo.remote.isUploadingActive);
                                                    js_sticker.put("st_tb_r_isUploadingCompleted", messageSticker.sticker.thumbnail.photo.remote.isUploadingCompleted);
                                                    js_sticker.put("st_tb_r_uploadedSize", messageSticker.sticker.thumbnail.photo.remote.uploadedSize);

                                                    js_sticker.put("st_tb_type", messageSticker.sticker.thumbnail.type);
                                                    js_sticker.put("st_tb_width", messageSticker.sticker.thumbnail.width);
                                                    js_sticker.put("st_tb_height", messageSticker.sticker.thumbnail.height);

                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                obj.put("massege_info", js_sticker.toString());
                                                break;


                                        }

                                        arrmsg.put(obj);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        continue;

                                    }

                                }


                                userinfos(arrmsg.toString());
                                arrmsg = new JSONArray(new ArrayList<String>());


                            }


                            if (object.getConstructor() == TdApi.Error.CONSTRUCTOR) {

                                TdApi.Error error = (TdApi.Error) object;
                                JSONObject channel_users = new JSONObject();
                                ;
                                try {
                                    BA.Log("error");

                                    channel_users.put("Error_code", error.code);
                                    channel_users.put("Error_msg", error.message);
                                    channel_users.put("ErrorConstructor", error.getConstructor());


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                ersend(channel_users.toString());
                            }

                        }
                    });
                }


                if (object.getConstructor() == TdApi.Error.CONSTRUCTOR) {

                    TdApi.Error error = (TdApi.Error) object;
                    JSONObject channel_users = new JSONObject();
                    ;
                    try {
                        BA.Log("error");

                        channel_users.put("Error_code", error.code);
                        channel_users.put("Error_msg", error.message);
                        channel_users.put("ErrorConstructor", error.getConstructor());


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    ersend(channel_users.toString());
                }



            }

            public void ersend(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void userinfos(String Value) {

                if (ba.subExists(EventName + "_getsmessagess")) {
                    BA.Log("lib:Raising.. " + EventName + "_getsmessagess " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getsmessagess", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getsmessagess");
                }
            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_chatgroup")) {
                    BA.Log("lib:Raising.. " + EventName + "_chatgroup " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_chatgroup", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_chatgroup");
                }
            }

            public String Get_users(int userId){
                TgH.send(new TdApi.GetUser(userId), new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        BA.Log(object.toString());

                        JSONObject channel_users = new JSONObject();
                        try {
                            BA.Log("Get user");
                            TdApi.User userchat = (TdApi.User) object;
                            channel_users.put("user_ ", userchat.phoneNumber);
                            channel_users.put("user_ ", userchat.lastName);
                            channel_users.put("user_ ", userchat.isVerified);
                            channel_users.put("user_ ", userchat.firstName);
                            channel_users.put("user_ ", userchat.id);
                            channel_users.put("user_ ", userchat.username);
                            channel_users.put("user_ ", userchat.profilePhoto.small.id);
                            channel_users.put("user_ ", userchat.profilePhoto.small.expectedSize);




                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        //   user_info=channel_users.toString();
                    }
                });

                return "";
            }


        });
    }

    public void Getmessages(final long chatid,final long msgid[]){
        client.send(new TdApi.GetMessages(chatid,msgid), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msg :" + object.toString());
                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.GetSupergroupFullInfo.CONSTRUCTOR:
                        TdApi.User users = (TdApi.User) object;
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("username", users.username);
                            obj.put("firstName", users.firstName);
                            //  obj.put("foreignLink", users.);
                            obj.put("haveAccess", users.haveAccess);
                            obj.put("id", users.id);
                            obj.put("isVerified", users.isVerified);
                            obj.put("lastName", users.lastName);
                            //obj.put("myLink", users.);
                            obj.put("phoneNumber", users.phoneNumber);
                            obj.put("profilePhoto", users.profilePhoto);
                            obj.put("restrictionReason", users.restrictionReason);
                            obj.put("status", users.status);
                            obj.put("type", users.type);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BA.Log(obj.toString());
                        userinfos((obj.toString()));

                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void userinfos(String Value) {

                if (ba.subExists(EventName + "_getme")) {
                    BA.Log("lib:Raising.. " + EventName + "_getme " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getme", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getme");
                }
            }

        });

    }
    public void Getmessage(final long chatid,final long msgid,final String username){

        client.send(new TdApi.GetMessage(chatid,msgid), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msg :" + object.toString());
                ba.Log("msg :" + object.getConstructor());
                if (object.getConstructor() == TdApi.Message.CONSTRUCTOR){

                    TdApi.Messages messages = (TdApi.Messages) object;

                    ///BA.Log("chat message : "+messages.messages.toString());

                    final TdApi.Message[] chats2 = messages.messages;
                    for (TdApi.Message chat : messages.messages) {


                        final JSONObject obj = new JSONObject();
                        try {

                            obj.put("meg_id", chat.id);
                            obj.put("meg_date", chat.date);
                            obj.put("meg_views", chat.views);
                            obj.put("meg_canBeDeleted", chat.canBeDeletedForAllUsers);
                            obj.put("meg_canBeEdited", chat.canBeEdited);
                            obj.put("meg_chatId", chat.chatId);
                            obj.put("meg_editDate", chat.editDate);
                            obj.put("meg_isPost", chat.isChannelPost);
                            obj.put("meg_replyToMessageId", chat.replyToMessageId);
                            obj.put("meg_senderUserId", chat.senderUserId);
                            obj.put("meg_viaBotUserId", chat.viaBotUserId);
                            obj.put("getConstructor", chat.content.getConstructor());


                            long chat_content;
                            chat_content = chat.content.getConstructor();
                            BA.Log("" + chat_content);
                            switch ((int) chat_content) {

                                case 1740718156:   //msg photo

                                    obj.put("msg_type", "photo");

                                    JSONObject channel_topmsg = new JSONObject();
                                    try {
                                        BA.Log("photo");
                                        TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) chat.content;

                                        channel_topmsg.put("caption_topmsg", messagePhoto.caption.text);
                                        channel_topmsg.put("caption_id", messagePhoto.caption.text);

                                        //  TdApi.PhotoSize photoSize = (TdApi.PhotoSize) messagePhoto.photo.sizes;
                                        // final TdApi.PhotoSize[] userchats2 = messagePhoto.photo.sizes;

                                        for (TdApi.PhotoSize photoSize : messagePhoto.photo.sizes) {
                                            BA.Log("type : " + photoSize);

                                            channel_topmsg.put("photoid_height", photoSize.height);
                                            channel_topmsg.put("photoid_width", photoSize.width);
                                            channel_topmsg.put("photoid_type", photoSize.type);

                                            channel_topmsg.put("photoid_path", photoSize.photo.local.path);
                                            channel_topmsg.put("photoid_canBeDeleted", photoSize.photo.local.canBeDeleted);
                                            channel_topmsg.put("photoid_canBeDownloaded", photoSize.photo.local.canBeDownloaded);
                                            channel_topmsg.put("photoid_downloadedPrefixSize", photoSize.photo.local.downloadedPrefixSize);
                                            channel_topmsg.put("photoid_downloadedSize", photoSize.photo.local.downloadedSize);
                                            channel_topmsg.put("photoid_isDownloadingActive", photoSize.photo.local.isDownloadingActive);
                                            channel_topmsg.put("photoid_isDownloadingCompleted", photoSize.photo.local.isDownloadingCompleted);

                                            channel_topmsg.put("photoid_topmsg", photoSize.photo.id);
                                            channel_topmsg.put("photoid_remote", photoSize.photo.remote);
                                            channel_topmsg.put("photoid_size", photoSize.photo.size);

                                        }


                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    obj.put("massege_info", channel_topmsg.toString());


                                    break;

                                case 596945783:   //msg document

                                    obj.put("msg_type", "document");

                                    JSONObject topmsg_ducument = new JSONObject();
                                    try {
                                        BA.Log("document");
                                        TdApi.MessageDocument top_msgs = (TdApi.MessageDocument) chat.content;
                                        if (top_msgs.caption != null)
                                            topmsg_ducument.put("msgdoc_caption", top_msgs.caption.text);
                                        topmsg_ducument.put("msgdoc_entities", top_msgs.caption.entities);


                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    obj.put("massege_info", topmsg_ducument.toString());


                                    break;
                                case TdApi.MessageVideo.CONSTRUCTOR:   //msg document

                                    obj.put("msg_type", "Video");

                                    JSONObject js_video = new JSONObject();
                                    try {
                                        BA.Log("document");
                                        TdApi.MessageVideo messageVideo = (TdApi.MessageVideo) chat.content;
                                        if (messageVideo.caption != null)
                                            js_video.put("vd_text", messageVideo.caption.text);
                                        js_video.put("vd_entities", messageVideo.caption.entities);
                                        js_video.put("vd_duration", messageVideo.video.duration);
                                        js_video.put("vd_fileName", messageVideo.video.fileName);
                                        js_video.put("vd_hasStickers", messageVideo.video.hasStickers);
                                        js_video.put("vd_height", messageVideo.video.height);
                                        js_video.put("vd_mimeType", messageVideo.video.mimeType);
                                        js_video.put("vd_width", messageVideo.video.width);

                                        js_video.put("vd_f_size", messageVideo.video.video.size);
                                        js_video.put("vd_f_id", messageVideo.video.video.id);
                                        js_video.put("vd_f_expectedSize", messageVideo.video.video.expectedSize);

                                        js_video.put("vd_f_r_uploadedSize", messageVideo.video.video.remote.uploadedSize);
                                        js_video.put("vd_f_r_isUploadingCompleted", messageVideo.video.video.remote.isUploadingCompleted);
                                        js_video.put("vd_f_r_isUploadingActive", messageVideo.video.video.remote.isUploadingActive);
                                        js_video.put("vd_f_r_id", messageVideo.video.video.remote.id);

                                        js_video.put("vd_f_l_canBeDeleted", messageVideo.video.video.local.canBeDeleted);
                                        js_video.put("vd_f_l_downloadedPrefixSize", messageVideo.video.video.local.downloadedPrefixSize);
                                        js_video.put("vd_f_l_isDownloadingCompleted", messageVideo.video.video.local.isDownloadingCompleted);
                                        js_video.put("vd_f_l_isDownloadingActive", messageVideo.video.video.local.isDownloadingActive);
                                        js_video.put("vd_f_l_downloadedSize", messageVideo.video.video.local.downloadedSize);
                                        js_video.put("vd_f_l_canBeDownloaded", messageVideo.video.video.local.canBeDownloaded);
                                        js_video.put("vd_f_l_path", messageVideo.video.video.local.path);

                                        js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                        js_video.put("vd_tb_l_canBeDownloaded", messageVideo.video.thumbnail.photo.local.canBeDownloaded);
                                        js_video.put("vd_tb_l_downloadedSize", messageVideo.video.thumbnail.photo.local.downloadedSize);
                                        js_video.put("vd_tb_l_path", messageVideo.video.thumbnail.photo.local.path);
                                        js_video.put("vd_tb_l_isDownloadingActive", messageVideo.video.thumbnail.photo.local.isDownloadingActive);
                                        js_video.put("vd_tb_l_isDownloadingCompleted", messageVideo.video.thumbnail.photo.local.isDownloadingCompleted);
                                        js_video.put("vd_tb_l_canBeDeleted", messageVideo.video.thumbnail.photo.local.canBeDeleted);

                                        js_video.put("vd_tb_id", messageVideo.video.thumbnail.photo.id);
                                        js_video.put("vd_tb_size", messageVideo.video.thumbnail.photo.size);
                                        js_video.put("vd_tb_height", messageVideo.video.thumbnail.height);
                                        js_video.put("vd_tb_width", messageVideo.video.thumbnail.width);
                                        js_video.put("vd_tb_type", messageVideo.video.thumbnail.type);

                                        js_video.put("vd_tb_r_id", messageVideo.video.thumbnail.photo.remote.id);
                                        js_video.put("vd_tb_r_isUploadingActive", messageVideo.video.thumbnail.photo.remote.isUploadingActive);
                                        js_video.put("vd_tb_r_isUploadingCompleted", messageVideo.video.thumbnail.photo.remote.isUploadingCompleted);
                                        js_video.put("vd_tb_r_uploadedSize", messageVideo.video.thumbnail.photo.remote.uploadedSize);


                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    obj.put("massege_info", js_video.toString());


                                    break;
                                case 1989037971:   //msg message

                                    obj.put("msg_type", "message");

                                    JSONObject topmsg_message = new JSONObject();
                                    try {
                                        BA.Log("message");
                                        TdApi.MessageText top_msgs = (TdApi.MessageText) chat.content;
                                        topmsg_message.put("msg_entities", top_msgs.webPage);
                                        topmsg_message.put("msg_text", top_msgs.text.text);


                                        JSONArray inline_msgup = new JSONArray();
                                        TdApi.ReplyMarkupInlineKeyboard rep_mark = (TdApi.ReplyMarkupInlineKeyboard) chat.replyMarkup;
                                        if (chat.replyMarkup != null) {
                                            obj.put("ReplyMarkup", rep_mark.getConstructor());
                                            //  final TdApi.ReplyMarkupShowKeyboard replyMarkupShowKeyboard = (TdApi.ReplyMarkupShowKeyboard) chat.replyMarkup;
                                            for (int i = 0; i < rep_mark.rows.length; i++) {
                                                int columnsInRow = rep_mark.rows[i].length;
                                                TableRow row = new TableRow(ba.context);
                                                for (int j = 0; j < rep_mark.rows[i].length; j++) {
                                                    // replyMarkupShowKeyboard.rows[i][j];
                                                    BA.Log("logs : " + rep_mark.rows[i][j]);
                                                    String a;
                                                    //rep_mark.rows[i][j].text;

                                                    JSONObject ms_inline = new JSONObject();
                                                    try {
                                                        BA.Log("inline");
                                                        //  TdApi.MessagePhoto ms_inline = (TdApi.MessagePhoto) chat.content;
                                                        ms_inline.put("text", rep_mark.rows[i][j].text);
                                                        //  ms_inline.put("type",rep_mark.rows[i][j].type.);
                                                        TdApi.InlineKeyboardButtonTypeCallback rep_callback = (TdApi.InlineKeyboardButtonTypeCallback) rep_mark.rows[i][j].type;

                                                        byte[] bytes = rep_callback.data.clone();
                                                        //TdApi.GetCallbackQueryAnswer getCallbackQueryAnswer_milad = new TdApi.GetCallbackQueryAnswer();
                                                        // TdApi.CallbackQueryData callbackQueryData_milad = new TdApi.CallbackQueryData();
                                                        //callbackQueryData_milad.data = bytes;
                                                        //  getCallbackQueryAnswer_milad.chatId=chat.chatId;
                                                        //  getCallbackQueryAnswer_milad.messageId=chat.id;
                                                        //  getCallbackQueryAnswer_milad.payload = callbackQueryData_milad;
                                                        // TG.getClientInstance().send(getCallbackQueryAnswer_milad, new Client.ResultHandler() {
                                                        //  @Override
                                                        //   public void onResult(TdApi.TLObject object) {

                                                        //        BA.Log("like msg"+object.toString());
                                                        //    }
                                                        //  });

                                                        String s1 = Arrays.toString(bytes);
                                                        String s2 = new String(bytes);


                                                        String base64String = Base64.encodeToString(bytes, rep_callback.data.length);
                                                        //byte[] backToBytes = Base64.decodeBase64(base64String);

                                                        ms_inline.put("data", s2);


                                                    } catch (JSONException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                    }

                                                    inline_msgup.put(ms_inline);


                                                    //TdApi.InlineKeyboardButtonType inikey =(TdApi.InlineKeyboardButtonType) ;


                                                }
                                            }
                                        }


                                        topmsg_message.put("massege_inline", inline_msgup.toString());


                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    obj.put("massege_info", topmsg_message.toString());
                                    break;

                                case 527777781:   //msg message

                                    obj.put("msg_type", "voice");

                                    JSONObject topmsg_voice = new JSONObject();
                                    try {
                                        BA.Log("voice");
                                        TdApi.MessageVoiceNote top_msgs = (TdApi.MessageVoiceNote) chat.content;
                                        if (top_msgs.caption != null)
                                            topmsg_voice.put("msgVoice_caption", top_msgs.caption.text);
                                        topmsg_voice.put("msgVoice_islistened", top_msgs.isListened);
                                        topmsg_voice.put("msgVoice_duration", top_msgs.voiceNote.duration);
                                        topmsg_voice.put("msgVoice_mimetype", top_msgs.voiceNote.mimeType);
                                        topmsg_voice.put("msgVoice_voiceId", top_msgs.voiceNote.voice.id);
                                        topmsg_voice.put("msgVoice_persistentId", top_msgs.voiceNote.voice.local.path);
                                        topmsg_voice.put("msgVoice_voiceSize", top_msgs.voiceNote.voice.size);
                                        topmsg_voice.put("msgVoice_waweForm", top_msgs.voiceNote.waveform.length);

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    obj.put("massege_info", topmsg_voice.toString());
                                    break;

                                case TdApi.MessageAnimation.CONSTRUCTOR:   //msg message

                                    obj.put("msg_type", "Animation");

                                    JSONObject jsanimation = new JSONObject();
                                    try {
                                        BA.Log("voice");
                                        TdApi.MessageAnimation messageAnimation = (TdApi.MessageAnimation) chat.content;
                                        jsanimation.put("anim_r_uploadedSize", messageAnimation.animation.animation.remote.uploadedSize);
                                        jsanimation.put("anim_r_isUploadingCompleted", messageAnimation.animation.animation.remote.isUploadingCompleted);
                                        jsanimation.put("anim_r_isUploadingActive", messageAnimation.animation.animation.remote.isUploadingActive);
                                        jsanimation.put("anim_r_id", messageAnimation.animation.animation.remote.id);

                                        jsanimation.put("anim_duration", messageAnimation.animation.duration);
                                        jsanimation.put("anim_fileName", messageAnimation.animation.fileName);
                                        jsanimation.put("anim_height", messageAnimation.animation.height);
                                        jsanimation.put("anim_mimeType", messageAnimation.animation.mimeType);
                                        jsanimation.put("anim_width", messageAnimation.animation.width);
                                        jsanimation.put("anim_size", messageAnimation.animation.animation.size);

                                        jsanimation.put("anim_id", messageAnimation.animation.animation.id);
                                        jsanimation.put("anim_expectedSize", messageAnimation.animation.animation.expectedSize);
                                        jsanimation.put("anim_canBeDeleted", messageAnimation.animation.animation.local.canBeDeleted);
                                        jsanimation.put("anim_local", messageAnimation.animation.animation.local);
                                        jsanimation.put("anim_downloadedPrefixSize", messageAnimation.animation.animation.local.downloadedPrefixSize);
                                        jsanimation.put("anim_isDownloadingCompleted", messageAnimation.animation.animation.local.isDownloadingCompleted);
                                        jsanimation.put("anim_isDownloadingActive", messageAnimation.animation.animation.local.isDownloadingActive);
                                        jsanimation.put("anim_downloadedSize", messageAnimation.animation.animation.local.downloadedSize);
                                        jsanimation.put("anim_canBeDownloaded", messageAnimation.animation.animation.local.canBeDownloaded);
                                        jsanimation.put("anim_path", messageAnimation.animation.animation.local.path);

                                        jsanimation.put("anim_tb_type", messageAnimation.animation.thumbnail.type);
                                        jsanimation.put("anim_tb_width", messageAnimation.animation.thumbnail.width);
                                        jsanimation.put("anim_tb_height", messageAnimation.animation.thumbnail.height);
                                        jsanimation.put("anim_tb_path", messageAnimation.animation.thumbnail.photo.local.path);
                                        jsanimation.put("anim_tb_canBeDownloaded", messageAnimation.animation.thumbnail.photo.local.canBeDownloaded);
                                        jsanimation.put("anim_tb_downloadedSize", messageAnimation.animation.thumbnail.photo.local.downloadedSize);
                                        jsanimation.put("anim_tb_isDownloadingActive", messageAnimation.animation.thumbnail.photo.local.isDownloadingActive);
                                        jsanimation.put("anim_tb_isDownloadingCompleted", messageAnimation.animation.thumbnail.photo.local.isDownloadingCompleted);
                                        jsanimation.put("anim_tb_downloadedPrefixSize", messageAnimation.animation.thumbnail.photo.local.downloadedPrefixSize);
                                        jsanimation.put("anim_tb_canBeDeleted", messageAnimation.animation.thumbnail.photo.local.canBeDeleted);
                                        jsanimation.put("anim_tb_expectedSize", messageAnimation.animation.thumbnail.photo.expectedSize);
                                        jsanimation.put("anim_tb_id", messageAnimation.animation.thumbnail.photo.id);
                                        jsanimation.put("anim_tb_size", messageAnimation.animation.thumbnail.photo.size);
                                        jsanimation.put("anim_tb_r_id", messageAnimation.animation.thumbnail.photo.remote.id);
                                        jsanimation.put("anim_tb_r_isUploadingActive", messageAnimation.animation.thumbnail.photo.remote.isUploadingActive);
                                        jsanimation.put("anim_tb_r_isUploadingCompleted", messageAnimation.animation.thumbnail.photo.remote.isUploadingCompleted);
                                        jsanimation.put("anim_tb_r_uploadedSize", messageAnimation.animation.thumbnail.photo.remote.uploadedSize);
                                        if (messageAnimation.caption != null)
                                            jsanimation.put("cp_text", messageAnimation.caption.text);
                                        jsanimation.put("cp_text", messageAnimation.caption.entities);

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    obj.put("massege_info", jsanimation.toString());
                                    break;

                                case TdApi.MessageSticker.CONSTRUCTOR:   //msg message

                                    obj.put("msg_type", "Sticker");

                                    JSONObject js_sticker = new JSONObject();
                                    try {
                                        BA.Log("voice");
                                        TdApi.MessageSticker messageSticker = (TdApi.MessageSticker) chat.content;

                                        js_sticker.put("st_emoji", messageSticker.sticker.emoji);
                                        js_sticker.put("st_height", messageSticker.sticker.height);
                                        js_sticker.put("st_isMask", messageSticker.sticker.isMask);
                                        js_sticker.put("st_width", messageSticker.sticker.width);
                                        js_sticker.put("st_setId", messageSticker.sticker.setId);

                                        js_sticker.put("st_mp_", messageSticker.sticker.maskPosition.point);

                                        js_sticker.put("st_mp_scale", messageSticker.sticker.maskPosition.scale);
                                        js_sticker.put("st_mp_xShift", messageSticker.sticker.maskPosition.xShift);
                                        js_sticker.put("st_mp_yShift", messageSticker.sticker.maskPosition.yShift);

                                        js_sticker.put("st_r_uploadedSize", messageSticker.sticker.sticker.remote.uploadedSize);
                                        js_sticker.put("st_r_isUploadingCompleted", messageSticker.sticker.sticker.remote.isUploadingCompleted);
                                        js_sticker.put("st_r_isUploadingActive", messageSticker.sticker.sticker.remote.isUploadingActive);
                                        js_sticker.put("st_r_id", messageSticker.sticker.sticker.remote.id);

                                        js_sticker.put("st_size", messageSticker.sticker.sticker.size);
                                        js_sticker.put("st_id", messageSticker.sticker.sticker.id);
                                        js_sticker.put("st_expectedSize", messageSticker.sticker.sticker.expectedSize);

                                        js_sticker.put("st_l_canBeDeleted", messageSticker.sticker.sticker.local.canBeDeleted);
                                        js_sticker.put("st_l_isDownloadingCompleted", messageSticker.sticker.sticker.local.isDownloadingCompleted);
                                        js_sticker.put("st_l_isDownloadingActive", messageSticker.sticker.sticker.local.isDownloadingActive);
                                        js_sticker.put("st_l_path", messageSticker.sticker.sticker.local.path);
                                        js_sticker.put("st_l_downloadedSize", messageSticker.sticker.sticker.local.downloadedSize);
                                        js_sticker.put("st_l_canBeDownloaded", messageSticker.sticker.sticker.local.canBeDownloaded);
                                        js_sticker.put("st_l_downloadedPrefixSize", messageSticker.sticker.sticker.local.downloadedPrefixSize);

                                        js_sticker.put("st_tb_l_isDownloadingActive", messageSticker.sticker.thumbnail.photo.local.isDownloadingActive);
                                        js_sticker.put("st_tb_l_downloadedPrefixSize", messageSticker.sticker.thumbnail.photo.local.downloadedPrefixSize);
                                        js_sticker.put("st_tb_l_canBeDownloaded", messageSticker.sticker.thumbnail.photo.local.canBeDownloaded);
                                        js_sticker.put("st_tb_l_downloadedSize", messageSticker.sticker.thumbnail.photo.local.downloadedSize);
                                        js_sticker.put("st_tb_l_path", messageSticker.sticker.thumbnail.photo.local.path);
                                        js_sticker.put("st_tb_l_isDownloadingCompleted", messageSticker.sticker.thumbnail.photo.local.isDownloadingCompleted);

                                        js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.id);
                                        js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.size);
                                        js_sticker.put("st_tb_", messageSticker.sticker.thumbnail.photo.expectedSize);

                                        js_sticker.put("st_tb_r_id", messageSticker.sticker.thumbnail.photo.remote.id);
                                        js_sticker.put("st_tb_r_isUploadingActive", messageSticker.sticker.thumbnail.photo.remote.isUploadingActive);
                                        js_sticker.put("st_tb_r_isUploadingCompleted", messageSticker.sticker.thumbnail.photo.remote.isUploadingCompleted);
                                        js_sticker.put("st_tb_r_uploadedSize", messageSticker.sticker.thumbnail.photo.remote.uploadedSize);

                                        js_sticker.put("st_tb_type", messageSticker.sticker.thumbnail.type);
                                        js_sticker.put("st_tb_width", messageSticker.sticker.thumbnail.width);
                                        js_sticker.put("st_tb_height", messageSticker.sticker.thumbnail.height);

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    obj.put("massege_info", js_sticker.toString());
                                    break;


                            }

                            arrmsg.put(obj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;

                        }

                    }


                    getmessa(arrmsg.toString());
                    arrmsg = new JSONArray(new ArrayList<String>());


                }





                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.GetSupergroupFullInfo.CONSTRUCTOR:
                        TdApi.User users = (TdApi.User) object;
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("username", users.username);
                            obj.put("firstName", users.firstName);
                            //  obj.put("foreignLink", users.);
                            obj.put("haveAccess", users.haveAccess);
                            obj.put("id", users.id);
                            obj.put("isVerified", users.isVerified);
                            obj.put("lastName", users.lastName);
                            //obj.put("myLink", users.);
                            obj.put("phoneNumber", users.phoneNumber);
                            obj.put("profilePhoto", users.profilePhoto);
                            obj.put("restrictionReason", users.restrictionReason);
                            obj.put("status", users.status);
                            obj.put("type", users.type);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BA.Log(obj.toString());
                        //userinfos((obj.toString()));

                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void getmessa(String Value) {

                if (ba.subExists(EventName + "_getmassage")) {
                    BA.Log("lib:Raising.. " + EventName + "_getmassage " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getmassage", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getmassage");
                }
            }

        });

    }

    public void Getpub(final long chatid,final long msgid){
        client.send(new TdApi.GetPublicMessageLink(chatid,msgid,false), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                ba.Log("msg :" + object.toString());
                switch (object.getConstructor()) {

                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.GetSupergroupFullInfo.CONSTRUCTOR:
                        TdApi.User users = (TdApi.User) object;
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("username", users.username);
                            obj.put("firstName", users.firstName);
                            //  obj.put("foreignLink", users.);
                            obj.put("haveAccess", users.haveAccess);
                            obj.put("id", users.id);
                            obj.put("isVerified", users.isVerified);
                            obj.put("lastName", users.lastName);
                            //obj.put("myLink", users.);
                            obj.put("phoneNumber", users.phoneNumber);
                            obj.put("profilePhoto", users.profilePhoto);
                            obj.put("restrictionReason", users.restrictionReason);
                            obj.put("status", users.status);
                            obj.put("type", users.type);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BA.Log(obj.toString());
                        userinfos((obj.toString()));

                        break;
                    case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                        break;

                }

            }

            public void testRaiseEvent(String Value) {

                if (ba.subExists(EventName + "_errormsg")) {
                    BA.Log("lib:Raising.. " + EventName + "_errormsg " + Value);
                    //  Value=Value.replace("[","").replace("]","");
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_errormsg", false, new Object[]{Value});
                    arr = new JSONArray(new ArrayList<String>());

                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_errormsg");
                }
            }

            public void userinfos(String Value) {

                if (ba.subExists(EventName + "_getme")) {
                    BA.Log("lib:Raising.. " + EventName + "_getme " + Value);
                    ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getme", false, new Object[]{Value});
                } else {
                    BA.Log("lib: NOTFOUND '" + EventName + "_getme");
                }
            }

        });

    }

    public  void settypeprox(int select_type,String username,String password,String secret){
        /*
        numproxy : 0 = socks5
        1 : http
        2 : mtproto
         */

        if(select_type==0){
            TdApi.ProxyTypeSocks5 proxyTypeSocks5 =new TdApi.ProxyTypeSocks5(username,password);
            typep=proxyTypeSocks5;
        }
        if(select_type==1){
            TdApi.ProxyTypeHttp proxyTypeHttp =new TdApi.ProxyTypeHttp(username,password,true);
            typep=proxyTypeHttp;

        }
        if(select_type==2){
            TdApi.ProxyTypeMtproto proxyTypeMtproto = new TdApi.ProxyTypeMtproto(secret);
            typep=proxyTypeMtproto;

        }
        if(select_type==3){

        }

    }

    public void AddProxy(String server, int port, boolean enable) {



        TdApi.AddProxy addProxy = new TdApi.AddProxy();
        TdApi.SupergroupMembersFilterBanned filterban = new TdApi.SupergroupMembersFilterBanned();
        addProxy.server=server;
        addProxy.enable=enable;
        addProxy.port=port;
        addProxy.type=typep;


        client.send(addProxy, new AuthorizationRequestHandler());

    }

    public void DisableProxy() {
        TdApi.DisableProxy disableProxy = new TdApi.DisableProxy();
        client.send(disableProxy, new AuthorizationRequestHandler());

    }

    public void EditProxy(int proxyId,String server,
                     int port,
                     boolean enable){

        TdApi.EditProxy editProxy = new TdApi.EditProxy();
        editProxy.enable=enable;
        editProxy.port=port;
        editProxy.type=typep;


        client.send(editProxy, new AuthorizationRequestHandler());


    }

    public void EnableProxy(int proxyId){
        TdApi.EnableProxy enableProxy = new TdApi.EnableProxy();
        enableProxy.proxyId=proxyId;
        client.send(enableProxy, new AuthorizationRequestHandler());
    }

    public void GetProxies(){
        final TdApi.GetProxies getProxies = new TdApi.GetProxies();

        client.send(getProxies, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {

                switch (object.getConstructor()) {





                    case TdApi.Error.CONSTRUCTOR:
                        TdApi.Error error = (TdApi.Error) object;
                        JSONObject channel_users = new JSONObject();
                        ;
                        try {
                            BA.Log("error");

                            channel_users.put("Error_code", error.code);
                            channel_users.put("Error_msg", error.message);
                            channel_users.put("ErrorConstructor", error.getConstructor());


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        testRaiseEvent(channel_users.toString());
                        break;

                    case TdApi.GetSupergroupFullInfo.CONSTRUCTOR:


                        break;
                    case TdApi.Proxies.CONSTRUCTOR:

                        TdApi.Proxies proxies = (TdApi.Proxies) object;

                        for (int i = 0; i < proxies.proxies.length; i++) {
                            TdApi.Proxy proxy = proxies.proxies[i];
                            ba.Log("Proxys server :"+proxy.server );
                        }

                        break;



                }



            }
        });
    }

    public void GetProxyLink(int proxyId){
        TdApi.GetProxyLink getProxyLink = new TdApi.GetProxyLink();
        getProxyLink.proxyId=proxyId;
        client.send(getProxyLink, new AuthorizationRequestHandler());
    }

    public void RemoveProxy(int proxyId){
        TdApi.RemoveProxy removeProxy = new TdApi.RemoveProxy();
        removeProxy.proxyId=proxyId;
        client.send(removeProxy, new AuthorizationRequestHandler());
    }

    public void PingProxy(int proxyId){
        TdApi.PingProxy pingProxy = new TdApi.PingProxy();
        pingProxy.proxyId=proxyId;
        client.send(pingProxy, new AuthorizationRequestHandler());
    }

    public void get_ch(final int limit) {
        synchronized (chatList) {
            if (!haveFullChatList && limit > chatList.size()) {
                // have enough chats in the chat list or chat list is too small
                long offsetOrder = Long.MAX_VALUE;
                long offsetChatId = 0;
                if (!chatList.isEmpty()) {
                    OrderedChat last = chatList.last();
                    offsetOrder = last.order;
                    offsetChatId = last.chatId;
                }
                client.send(new TdApi.GetChats(offsetOrder, offsetChatId, limit - chatList.size()), new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.Object object) {
                        switch (object.getConstructor()) {
                            case TdApi.Error.CONSTRUCTOR:
                                System.err.println("Receive an error for GetChats:" + newLine + object);
                                break;
                            case TdApi.Chats.CONSTRUCTOR:
                                long[] chatIds = ((TdApi.Chats) object).chatIds;
                                if (chatIds.length == 0) {
                                    synchronized (chatList) {
                                        haveFullChatList = true;
                                    }
                                }
                                // chats had already been received through updates, let's retry request
                                get_ch(limit);
                                break;
                            default:
                                System.err.println("Receive wrong response from TDLib:" + newLine + object);
                        }
                    }
                });
                return;
            }

            // have enough chats in the chat list to answer request
            java.util.Iterator<OrderedChat> iter = chatList.iterator();
            System.out.println();
            System.out.println("First " + limit + " chat(s) out of " + chatList.size() + " known chat(s):");
            for (int i = 0; i < limit; i++) {
                long chatId = iter.next().chatId;
                TdApi.Chat chat = chats.get(chatId);
                ba.Log("titell :" +chat.title);

                synchronized (chat) {
                    BA.Log("chats : " + chat.title);
                    System.out.println(chatId + ": " + chat.title);
                    //totalChats++;
                    int role = 0;

                    JSONObject obj = new JSONObject();
                    try {



                        obj.put("chat_id", chat.id);
                        obj.put("title", chat.title);
                        obj.put("lastReadInboxMessageId", chat.lastReadInboxMessageId);
                        obj.put("lastReadOutboxMessageId", chat.lastReadOutboxMessageId);
                        obj.put("replyMarkupMessageId", chat.replyMarkupMessageId);
                        obj.put("unreadCount", chat.unreadCount);
                        obj.put("topdate", chat.lastMessage.date);
                        obj.put("ChatConstructor", chat.type.getConstructor());

                        if (chat.photo != null) {
                            //photo
                            obj.put("photo_expectedSize", chat.photo.small.expectedSize);
                            obj.put("photo_id", chat.photo.small.id);
                            obj.put("photo_size", chat.photo.small.size);
                            obj.put("photo_l_canBeDeleted", chat.photo.small.local.canBeDeleted);
                            obj.put("photo_l_canBeDownloaded", chat.photo.small.local.canBeDownloaded);
                            obj.put("photo_l_downloadedPrefixSize", chat.photo.small.local.downloadedPrefixSize);
                            obj.put("photo_l_downloadedSize", chat.photo.small.local.downloadedSize);
                            obj.put("photo_l_isDownloadingActive", chat.photo.small.local.isDownloadingActive);
                            obj.put("photo_l_isDownloadingCompleted", chat.photo.small.local.isDownloadingCompleted);
                            obj.put("photo_l_path", chat.photo.small.local.path);
                            obj.put("photo_r_isUploadingActive", chat.photo.small.remote.isUploadingActive);
                            obj.put("photo_r_isUploadingCompleted", chat.photo.small.remote.isUploadingCompleted);
                            obj.put("photo_r_uploadedSize", chat.photo.small.remote.uploadedSize);
                            obj.put("photo_r_id", chat.photo.small.remote.id);
                        }


                        obj.put("content", chat.lastMessage.content.getConstructor());


                        long chat_content;
                        chat_content = chat.lastMessage.content.getConstructor();
                        switch (chat.lastMessage.content.getConstructor()) {
                            case 1469704153:   //msg photo

                                obj.put("topmasg_type", "photo");

                                JSONObject channel_topmsg = new JSONObject();
                                try {
                                    BA.Log("photo");
                                    TdApi.MessagePhoto top_msgs = (TdApi.MessagePhoto) chat.lastMessage.content;
                                    channel_topmsg.put("caption_topmsg", top_msgs.caption);
                                    channel_topmsg.put("photoid_topmsg", top_msgs.photo.id);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("topmsg_info", channel_topmsg.toString());


                                break;

                            case 1630748077:   //msg document

                                obj.put("topmasg_type", "document");

                                JSONObject topmsg_ducument = new JSONObject();
                                try {
                                    BA.Log("document");
                                    TdApi.MessageDocument top_msgs = (TdApi.MessageDocument) chat.lastMessage.content;
                                    if (top_msgs.caption != null)
                                        topmsg_ducument.put("msgdoc_caption", top_msgs.caption);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("topmsg_info", topmsg_ducument.toString());


                                break;

                            case 1989037971:   //msg message

                                obj.put("topmasg_type", "message");

                                JSONObject topmsg_message = new JSONObject();
                                try {
                                    BA.Log("message");
                                    TdApi.MessageText top_msgs = (TdApi.MessageText) chat.lastMessage.content;
                                    topmsg_message.put("msg_web", top_msgs.webPage);
                                    topmsg_message.put("msg_text", top_msgs.text.text);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("topmsg_info", topmsg_message.toString());
                                break;

                            case -631462405:   //msg message

                                obj.put("topmasg_type", "voice");

                                JSONObject topmsg_voice = new JSONObject();
                                try {
                                    BA.Log("voice");
                                    TdApi.MessageVoiceNote top_msgs = (TdApi.MessageVoiceNote) chat.lastMessage.content;
                                    topmsg_voice.put("msgVoice_caption", top_msgs.caption);
                                    topmsg_voice.put("msgVoice_islistened", top_msgs.isListened);
                                    topmsg_voice.put("msgVoice_duration", top_msgs.voiceNote.duration);
                                    topmsg_voice.put("msgVoice_mimetype", top_msgs.voiceNote.mimeType);
                                    topmsg_voice.put("msgVoice_voiceId", top_msgs.voiceNote.voice.id);
                                    topmsg_voice.put("msgVoice_path", top_msgs.voiceNote.voice.local.path);
                                    topmsg_voice.put("msgVoice_voiceSize", top_msgs.voiceNote.voice.size);
                                    topmsg_voice.put("msgVoice_waweForm", top_msgs.voiceNote.waveform.length);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                obj.put("topmsg_info", topmsg_voice.toString());
                                break;
                        }
                        long chan_type;
                        chan_type = chat.type.getConstructor();
                        switch (chat.type.getConstructor()) {
                            case TdApi.ChatTypeSupergroup.CONSTRUCTOR:

                                obj.put("chat_type", "channel");

                                JSONObject channel_info = new JSONObject();
                                try {
                                    TdApi.ChatTypeSupergroup channel = (TdApi.ChatTypeSupergroup) chat.type;
                                    channel_info.put("supergroupId", channel.supergroupId);
                                    channel_info.put("isChannel", channel.isChannel);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("channel_info", channel_info.toString());
                                break;
                            case TdApi.ChatTypeBasicGroup.CONSTRUCTOR:
                                obj.put("chat_type", "group");
                                JSONObject group_info = new JSONObject();
                                try {
                                    TdApi.ChatTypeBasicGroup channel = (TdApi.ChatTypeBasicGroup) chat.type;
                                    group_info.put("basicGroupId", channel.basicGroupId);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("group_info", group_info.toString());

                                break;
                            case TdApi.ChatTypePrivate.CONSTRUCTOR:
                                obj.put("chat_type", "privateChat");

                                JSONObject privatechat_info = new JSONObject();
                                try {
                                    TdApi.ChatTypePrivate private_info = (TdApi.ChatTypePrivate) chat.type;

                                    privatechat_info.put("userid", private_info.userId);


                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                obj.put("privatechat_info", privatechat_info.toString());

                                break;
                        }

                        // GetphotoChats(chat.photo.small.id);
                        arr.put(obj);
                        //BA.Log("chat : "+obj.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;

                    }

                    continue;


                }
            }
            BA.Log(arr.toString());
            testRaiseEvent(arr.toString());
        }

    }
    public void testRaiseEvent(String Value) {

        if (ba.subExists(EventName + "_getchat")) {
            BA.Log("lib:Raising.. " + EventName + "_getchat " + Value);
            //  Value=Value.replace("[","").replace("]","");
            ba.raiseEventFromDifferentThread(this, null, 0, EventName + "_getchat", false, new Object[]{Value});
            arr = new JSONArray(new ArrayList<String>());

        } else {
            BA.Log("lib: NOTFOUND '" + EventName + "_getchat");
        }
    }
    }