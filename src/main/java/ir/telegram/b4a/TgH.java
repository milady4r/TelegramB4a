package ir.telegram.b4a;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import anywheresoftware.b4a.BA;

/**
 * Created by Snake on 16.01.2016.
 */
public class TgH {

    private static ArrayList<Client.ResultHandler> list = new ArrayList<>(2);
    private static final Object LOCK = new Object();

    public static int selfProfileId;
    //public static String selfProfileUsername;

    public static SparseArray<TdApi.User> users = new SparseArray<>();

    private static String getCacheDir(Context c) {
        return c.getCacheDir().getAbsolutePath();
    }

    public static void init(Context c) {
        TG.setFileLogEnabled(false);
        TG.setLogVerbosity(Log.WARN);
        TG.setDir(c.getFilesDir().getAbsolutePath());
        TG.setFilesDir(getCacheDir(c) + "/files/");

        //if(!list.isEmpty())
        startUpdatesHandler();
    }

    /**
     * Init TDLib, check auth state, load profile info and then call resultCallback
     *
     * @param onGetAuthHandler callback Ok or Error return.
     */
    public static void init(Context c, final Client.ResultHandler onGetAuthHandler) {
        init(c);
        TgH.send(new TdApi.GetAuthorizationState(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                if (TgUtils.isAuthorized(object)) {
                    getProfile(new Client.ResultHandler() {
                        @Override
                        public void onResult(TdApi.Object object) {
                            onGetAuthHandler.onResult(new TdApi.Ok());
                        }


                    });
                } else
                    onGetAuthHandler.onResult(new TdApi.Error());
            }
        });
    }

    public static Client TG() {
        return TG.getClientInstance();
    }

    private final static Client.ResultHandler LoopUpdateHandler = new Client.ResultHandler() {
        @Override
        public void onResult(TdApi.Object object) {
            if (object.getConstructor() == TdApi.UpdateUser.CONSTRUCTOR) {
                updateUser((TdApi.UpdateUser) object);
            }
            // MyLog.log("LoopUpdateHandler");
            synchronized (LOCK) {
                for (Client.ResultHandler r : list)
                    r.onResult(object);
            }
        }
    };

    // public static HashMap<Integer, TdApi.User> users = new HashMap<>();


    private static void updateUser(TdApi.UpdateUser updateUser) {
        // MyLog.log("User: "+updateUser.user.firstName+" "+ updateUser.user.lastName);
        users.put(updateUser.user.id, updateUser.user);
        //TODO memory leak on long usage
        // users.put(updateUser.user.id, updateUser.user);
    }

    public static void startUpdatesHandler() {
        TG.setUpdatesHandler(LoopUpdateHandler);
    }

    public static void setUpdatesHandler(Client.ResultHandler r) {
        list.add(r);
        startUpdatesHandler();
    }

    public static void removeUpdatesHandler(Client.ResultHandler r) {
        synchronized (LOCK) {
            list.remove(r);
        }
    }




    public static void send(final TdApi.Function function) {
        send(function, TgUtils.emptyResultHandler());
    }


    public static void send(final TdApi.Function function, @Nullable final Client.ResultHandler resultHandler) {
        TG().send(function, resultHandler != null ? resultHandler : TgUtils.emptyResultHandler());
    }

    /**
     * Execute {@link TdApi.Function} in current thread. <b>This method cannot be run under TdLib ResultHandler onResult method!</b>
     * @return return result {@link TdApi.Object} or null if thread was interrupted
     */
    public static TdApi.Object execute(final TdApi.Function f) {
        final Semaphore semaphore = new Semaphore(0);

        final TdApi.Object[] resultHandlers = new TdApi.Object[1];
        final long ts = System.currentTimeMillis();
        send(f, new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                resultHandlers[0] = object;
                // Utils.sleep(2000);
                semaphore.release();
            }
        });

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {

        }
  //      MyLog.log(f.getClass().getSimpleName() + " executed in " + (System.currentTimeMillis() - ts));
        return resultHandlers[0];
    }

    public static void sendOnUi(final TdApi.Function f, final Client.ResultHandler resultHandler) {
        final Handler h = new Handler();
        send(f, new Client.ResultHandler() {
            @Override
            public void onResult(final TdApi.Object object) {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        resultHandler.onResult(object);
                    }
                });
            }
        });
    }

    public static void getProfile() { // load profile without callback
        getProfile(null);
    }

    public static void getProfile(final Client.ResultHandler callback) {
        send(new TdApi.GetMe(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {
                if(object.getConstructor()== TdApi.User.CONSTRUCTOR) {
                    TdApi.User me = (TdApi.User) object;
                    selfProfileId = me.id;
                    BA.Log(me.phoneNumber);
                    // selfProfileUsername = me.username;
                    //Sets.set(Const.SETS_PROFILE_ID, selfProfileId); //save last know profile
                    if (callback != null)
                        callback.onResult(object);
                }
            }
        });
    }

    //TODO error 429 Too big total timeout 151.000000 //profile temporary banned. Check with @SpamBot

}
