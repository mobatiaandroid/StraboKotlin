package com.vkc.strabo.constants

import com.vkc.strabo.constants.VKCUrlConstants

interface VKCUrlConstants {
    companion object {
        // Dev URL
        //public String BASE_URL ss= "http://mobicare2.mobatia.com/vkc_strabo/";
        // Live URL
        const val BASE_URL = "https://cms.strabobags.com/vkc_strabo/"

        // public String BASE_URL = "http://ec2-18-211-136-119.compute-1.amazonaws.com/vkc_strabo/";
        // http://ec2-18-211-136-119.compute-1.amazonaws.com/vkc_strabo/apiv2/getstate
        // http://loyalty.strabobags.com/vkc_strabo/apiv2/getstate
        const val GET_GIFTS = BASE_URL + "apiv2/getGifts"
        const val REDEEM_GIFTS = BASE_URL + "apiv2/gifts_redeem"
        const val REDEEM_HISTORY = BASE_URL + "apiv2/redeem_history"
        const val GET_USER_DATA = BASE_URL + "apiv2/getuserdetailswithMobile"
        const val REGISTER_URL = BASE_URL + "apiv2/registration"
        const val OTP_VERIFY_URL = BASE_URL + "apiv2/OTP_verification"
        const val GET_DEALERS = BASE_URL + "apiv2/getDealerswithState"
        const val ASSIGN_DEALERS = BASE_URL + "apiv2/assignDealers"
        const val GET_LOYALTY_POINTS = BASE_URL + "apiv2/getLoyalityPoints"
        const val GET_POINTS_HISTORY = BASE_URL + "apiv2/transaction_history"
        const val GET_PROFILE = BASE_URL + "apiv2/getProfile"
        const val GET_RETAILERS = BASE_URL + "apiv2/getRetailerswithState"
        const val UPLOAD_IMAGE = BASE_URL + "apiv2/upload_shop_images"
        const val SUBMIT_POINTS = BASE_URL + "apiv2/issueLoyalityPoints"
        const val UPLOADED_IMAGE = BASE_URL + "apiv2/last_uploaded_image"
        const val GET_MY_DEALERS = BASE_URL + "apiv2/myDealers"
        const val UPDATE_PROFILE = BASE_URL + "apiv2/profile_updation"
        const val UPDATE_MOBILE = BASE_URL + "apiv2/phoneUpdateOTP"
        const val NEW_REGISTER = BASE_URL + "apiv2/newRegRequest"
        const val GET_STATE = BASE_URL + "apiv2/getstate"
        const val GET_DISTRICT = BASE_URL + "apiv2/getdistrict"
        const val OTP_RESEND_URL = BASE_URL + "apiv2/resend_otp"
        const val GET_APP_VERSION_URL = BASE_URL + "apiv2/loyalty_appversion"
        const val GET_DATA = BASE_URL + "apiv2/fetchUserData"
        const val DEVICE_REGISTRATION_API = BASE_URL + "apiv2/device_registration"
        const val ADD_TO_CART = BASE_URL + "apiv2/addGiftsCartItem"
        const val GET_CART_COUNT = BASE_URL + "apiv2/giftCartCount"
        const val GET_MY_CART = BASE_URL + "apiv2/GiftcartList"
        const val EDIT_MY_CART = BASE_URL + "apiv2/updateGiftCart"
        const val DELETE_MY_CART = BASE_URL + "apiv2/deleteGiftCart"
        const val PLACE_ORDER = BASE_URL + "apiv2/giftsPlaceorder"
        const val REDEEM_LIST_URL = BASE_URL + "apiv2/redeem_history"
        const val GET_NOTIFICATIONS = BASE_URL + "apiv2/NotificationsList"
        const val GET_IMAGE_HISTORY = BASE_URL + "apiv2/uploaded_images_history"
        const val DELETE_IMAGE = BASE_URL + "apiv2/delete_uploaded_images"
        const val GET_DEALERS_SUBDEALER = BASE_URL + "apiv2/myDealers_Subdealers"
        const val GET_SUBDEALER_RETAILERS = BASE_URL + "apiv2/subDealersGiftRedeemedList"
        const val PLACE_ORDER_SUBDEALER = BASE_URL + "apiv2/giftsPlaceorderBySubDealer"
        const val REDEEM_HISTORY_SUBDEALER = BASE_URL + "apiv2/redeem_historyForSubdealer"
        const val GET_REDEEM_LIST = BASE_URL + "apiv2/Redeemed_gifts"
        const val GET_DEALER_POINT = BASE_URL + "apiv2/getLoyalityPoints"
        const val GET_USERS = BASE_URL + "apiv2/getUsers"
        const val GET_CUSTOMERS = BASE_URL + "apiv2/getMyCustomers"
        const val GET_REDEEM_REPORT_APP =
            BASE_URL + "apiv2/RedeemReportForApp" //apiv2/getLoyalityPoints
    }
}