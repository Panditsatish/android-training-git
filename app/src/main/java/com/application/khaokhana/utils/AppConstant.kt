package com.application.khaokhana.utils

import com.application.khaokhana.model.output.UserProfile


object AppConstant {

  const  val IMAGE_PICK_CODE =0
    const val PERMISSION_CODE =1
    const val REQUEST_CODE=2
    const val couponWalletRecharge = "couponWalletRecharge"
    const val couponWalletExpire = "couponWalletExpire"


    val IMAGE = 1
    const val TIME_FORMAT = "EEE, dd MMM yyyy, HH:mma"

    const val DEVICE_TYPE = 1
    const val SPLASH_DELAY: Long = 2000
    const val MIN_MEMB_COUNT: Int = 2
    const val DETECT_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    var USER: UserProfile? = UserProfile()

    interface BK {
        companion object {
            const val TYPE = "type"
            const val T_PRICE = "T_PRICE"
            const val ITME = "ITME"
            const val FOODS = "FOODS"
            const val MEDIA_DATA = "MEDIA_DATA"
            const val PAYMENT = "PAYMENT"
            const val ODER_ID = "ODER_ID"
            const val ODER_FROM = "ODER_FROM"
        }
    }

    interface PKN {
        companion object {
            const val ACCESS_TOKEN = "access_token"
        }
    }

    interface MT {
        companion object {
            const val UPLOAD_MEDIA = "images"
            const val TEXT_PLAIN = "text/plain"
        }
    }

    interface STATUS {
        companion object {
            const val PENDING = 1
            const val ACCEPTED = 2
            const val COMPLETED = 3
            const val CANCEL_BY_VENDOR = 4
            const val CANCEL_BY_USER = 5
        }
    }

    interface REQUEST_CODES {
        companion object {
            const val APP_SETTING = 1109
            const val CROP_VIDEO = 2200
            const val FOOD_REQ=2201


        }
    }

    interface TRANSACTIONS {
        companion object {
            const val DAILY_COUPON_CREDIT = 1
            const val AMOUNT_CREDIT_BY_ADMIN = 2
            const val ORDER_CREATE_AMOUNT_DEBIT = 3
            const val DAILY_COUPON_DEBIT = 4
            const val AMOUNT_DEBIT_BY_ADMIN = 5
            const val ORDER_CANCEL_AMOUNT_CREDIT = 6
            const val VENDOR_ORDER_CREDIT = 7
        }
    }

    interface AMOUNT_TYPE {
        companion object {
            const val CREDIT = 1
            const val DEBIT = 2
        }
    }

    interface STATUS_PAY {
        companion object {
            const val INITIATED = 0
            const val COMPLETE = 1
            const val FAILED = 2
        }
    }
}