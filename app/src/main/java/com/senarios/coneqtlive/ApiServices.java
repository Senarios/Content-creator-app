package com.senarios.coneqtlive;


import com.senarios.coneqtlive.Model.BlockUserSetting;
import com.senarios.coneqtlive.Model.BroadcastBlock;
import com.senarios.coneqtlive.Model.CancelEvent;
import com.senarios.coneqtlive.Model.ChangeSetting;
import com.senarios.coneqtlive.Model.CheckingStripeAccount;
import com.senarios.coneqtlive.Model.CompleteModel;
import com.senarios.coneqtlive.Model.CreateEvent;
import com.senarios.coneqtlive.Model.CreateEventHistory;
import com.senarios.coneqtlive.Model.CreatorFilter;
import com.senarios.coneqtlive.Model.DesktopNotification;
import com.senarios.coneqtlive.Model.DownloadCsv;
import com.senarios.coneqtlive.Model.EventTypeList;
import com.senarios.coneqtlive.Model.ForgetPassword;
import com.senarios.coneqtlive.Model.KickoutUser;
import com.senarios.coneqtlive.Model.Login;
import com.senarios.coneqtlive.Model.Logout;
import com.senarios.coneqtlive.Model.NotificationCountModel;
import com.senarios.coneqtlive.Model.NotificationList;
import com.senarios.coneqtlive.Model.OverViewModel;
import com.senarios.coneqtlive.Model.Payout;
import com.senarios.coneqtlive.Model.PayoutWithdrawAmount;
import com.senarios.coneqtlive.Model.Register;
import com.senarios.coneqtlive.Model.ResetChangedPassword;
import com.senarios.coneqtlive.Model.SocialLogin;
import com.senarios.coneqtlive.Model.StartEventModel;
import com.senarios.coneqtlive.Model.UnBlockUser;
import com.senarios.coneqtlive.Model.UpdateProfile;
import com.senarios.coneqtlive.Model.UpdateStripeAccount;
import com.senarios.coneqtlive.Model.VerificationCode;
import com.senarios.coneqtlive.Model.VerifyPasswordReset;
import com.senarios.coneqtlive.stripe.Model.AccountLinksRes;
import com.senarios.coneqtlive.stripe.Model.AccountsInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServices {

    @FormUrlEncoded
    @POST("accounts")
    Call<AccountsInfo> createUser(@Header("Authorization") String authHeader,
                                  @Field("type") String type,
                                  @Field("country") String country,
                                  @Field("email") String email,
                                  @Field("capabilities[card_payments][requested]") boolean capabilities1,
                                  @Field("capabilities[transfers][requested]") boolean capabilities2);

    @FormUrlEncoded
    @POST("account_links")
    Call<AccountLinksRes> accountLink(@Header("Authorization") String authHeader,
                                      @Field("account") String account,
                                      @Field("type") String type,
                                      @Field("return_url") String returnUrl,
                                      @Field("refresh_url") String refreshUrl);

    @FormUrlEncoded
    @POST("stripe-account-id")
    Call<UpdateStripeAccount> UpdateLink(@Header("Authorization") String authHeader,
                                         @Field("stripe_account_id") String account);

    @FormUrlEncoded
    @POST("signup")
    Call<Register> registerUser(@Field("email") String email,
                                @Field("password") String password,
                                @Field("phone") String number,
                                @Field("first_name") String firstname,
                                @Field("last_name") String last_name,
                                @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("login")
    Call<Login> LoginUser(@Field("email") String email,
                          @Field("password") String password,
                          @Field("device_token") String device_token);

    @Multipart
    @POST("create_event")
    Call<CreateEvent> getCreateEvent(@Header("Authorization") String authorization,
                                     @Part("name") RequestBody name,
                                     @Part("type") RequestBody type,
                                     @Part("description") RequestBody description,
                                     @Part("time") RequestBody time,
                                     @Part("time_duration") RequestBody timeDuration,
                                     @Part("ticket_price") RequestBody ticketPrice,
                                     @Part("stream_interaction") RequestBody streamInteraction,
                                     @Part MultipartBody.Part image1);

    @GET("event_list")
    Call<CreateEventHistory> CreateEventHistoryRes(@Header("Authorization") String authHeader,
                                                   @Query("name") String name,
                                                   @Query("type") String type,
                                                   @Query("description") String description,
                                                   @Query("time") String time,
                                                   @Query("time_duration") String timeDuration,
                                                   @Query("ticket_price") Integer ticketPrice,
                                                   @Query("stream_interaction") String streamInteraction);

    @FormUrlEncoded
    @POST("start_event")
    Call<StartEventModel> getStartEvent(@Header("Authorization") String authHeader,
                                        @Field("event_id") Integer event_id);

    @FormUrlEncoded
    @POST("event_completed")
    Call<CompleteModel> getCompletedEvent(@Header("Authorization") String authHeader,
                                          @Field("event_id") Integer event_id);

    @POST("balance")
    Call<Payout> getPayoutBalance(@Header("Authorization") String authHeader);

    @Multipart
    @POST("settings")
    Call<UpdateProfile> getUpdate(@Header("Authorization") String authorization,
                                  @Part("first_name") RequestBody name,
                                  @Part("last_name") RequestBody type,
                                  @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("change_password_settings")
    Call<ChangeSetting> changeSettings(@Header("Authorization") String authHeader,
                                       @Field("new_password") String newPassword,
                                       @Field("old_password") String oldPassword);

    @POST("check")
    Call<CheckingStripeAccount> getStripeCheck(@Header("Authorization") String authHeader);

    @GET("overview")
    Call<OverViewModel> getOverViewList(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("social_signup_login")
    Call<SocialLogin> getSocialLogin (@Field("first_name") String firstname,
                                      @Field("last_name") String last_name,
                                      @Field("id") String emailId,
                                      @Field("auth_type") String authType,
                                      @Field("image_url") String image,
                                      @Field("device_token") String device_token);
    @FormUrlEncoded
    @POST("user")
    Call<VerificationCode> getVerifyCode  (@Field("userId") int userId,
                                          @Field("otp") int otp);
    @POST("notification")
    Call<NotificationList> getNotification (@Header("Authorization") String authHeader);

    @GET("type")
    Call<EventTypeList> getEventType();

    @FormUrlEncoded
    @POST("refund")
    Call<CancelEvent> getCancelEvent (@Header("Authorization") String authHeader,
                                        @Field("event_id") Integer event_id);

    @GET("blocked_users")
    Call<BlockUserSetting> getBlockList(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("unblock_user")
    Call<UnBlockUser> getUnBlockList (@Header("Authorization") String authHeader,
                                        @Field("content_viewer_id") Integer viewer_id);

    @FormUrlEncoded
    @POST("filter")
    Call<CreatorFilter> getFilterList (@Header("Authorization") String authHeader,
                                        @Field("stat") Integer stat);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgetPassword> getForgetPassword (@Field("email") String email);

    @FormUrlEncoded
    @POST("verify_password_reset_code")
    Call<VerifyPasswordReset> getVerifiedPassword (@Field("email") String email,
                                                  @Field("verification_code") String verify);

    @FormUrlEncoded
    @POST("change_password")
    Call<ResetChangedPassword> getChangedPassword (@Field("email") String email,
                                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("block_user")
    Call<BroadcastBlock> getBroadcastBlock (@Header("Authorization") String authHeader,
                                           @Field("content_viewer_id") Integer viewer_id,
                                           @Field("name") String name);

    @FormUrlEncoded
    @POST("out")
    Call<KickoutUser> getKickOut (@Header("Authorization") String authHeader,
                                         @Field("event_id") Integer eventId,
                                         @Field("content_viewer_id") Integer viewer_id);

    @FormUrlEncoded
    @POST("payout")
    Call<PayoutWithdrawAmount> getWithDrawAmount (@Header("Authorization") String authHeader,
                                                  @Field("amount") Double amount);

    @FormUrlEncoded
    @POST("desktop_notification")
    Call<DesktopNotification> getDesktopNotification (@Header("Authorization") String authHeader,
                                                 @Field("notify") Integer notify);

    @FormUrlEncoded
    @POST("Api")
    Call<DownloadCsv> getDownloadCsv(@Header("Authorization") String authHeader,
                                     @Field("filter") Integer filter,
                                     @Field("stat") Integer stat);

    @GET("count")
    Call<NotificationCountModel> getNotificationCount (@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("logout")
    Call<Logout> getLogout (@Header("Authorization") String authHeader,
                                    @Field("type") String type);
}
