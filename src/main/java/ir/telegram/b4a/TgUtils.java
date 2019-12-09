package ir.telegram.b4a;


import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

/**
 * Created by Snake on 27.02.2016.
 */
public class TgUtils {


    public final static Client.ResultHandler emptyResultHandler() {
        return new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.Object object) {

            }
        };
    }




    public static boolean isError(final TdApi.Object object) {
        return object.getConstructor() == TdApi.Error.CONSTRUCTOR;
    }



    public static boolean isOk(final TdApi.Object object) {
        return object.getConstructor() == TdApi.Ok.CONSTRUCTOR;
    }


    public static boolean isAuthorized(TdApi.Object object) {
        if (TgUtils.isError(object))
            return false;
        TdApi.AuthorizationState as = (TdApi.AuthorizationState) object;
        if (as.getConstructor() == TdApi.AuthorizationStateReady.CONSTRUCTOR) {
            return true;
        }
        return false;
    }

    public static TdApi.User getUser(int userId) {
        TdApi.User user = TgH.users.get(userId);
        if(user==null){// create empty user for prevent NullPointerExcepion
            user = new TdApi.User();
            user.id = userId;
            user.firstName=user.lastName="";
            user.username="";
        }
        return user;
    }

    public static TdApi.User getUser(TdApi.ChatMember member) {
        return getUser(member.userId);
    }

}
