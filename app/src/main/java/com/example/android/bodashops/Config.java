package com.example.android.bodashops;

public class Config {
    //configurations class for intent keys and URLs
    public static final String OWNERPHONENOKEY = "com.example.android.bodashops.ownerPhoneNumber";

    private static final String BASE_URL = "https://boda-shops.000webhostapp.com/";

    public static final String URL_PRODUCTS = BASE_URL+"getProducts.php";
    public static final String URL_PRODUCTS_ATTRIBUTES = BASE_URL+"getProductAttributes.php";
    public static final String URL_ORDERS = BASE_URL+"orders.php";


    public static final String URL_UPLOAD_PRODUCT = BASE_URL +"uploadproductdetails.php";
    public static final String URL_UPDATE_PRODUCT = BASE_URL +"updateproductdetails.php";
    public static final String IMG_BASE_URL = BASE_URL+"images/";
    public static final String GET_TYPES_URL = BASE_URL+"getTypes.php";
    public static final String DELETE_PRODUCT_URL = BASE_URL+"deleteProduct.php";
    public static final String OWNERANDSHOPREGISTRATION_URL = BASE_URL+"ownerAccount/android/shopOwnerRegistration.php";
    public static final String OWNERANDLOGIN_URL = BASE_URL+"ownerAccount/android/shopOwnerLogin.php";
    public static final String OWNERLOGOUT_URL = BASE_URL+"ownerAccount/android/shopOwnerLogout.php";

    private static final String KEYPREFIX = "com.example.android.bodashops.";

    //ADD PRODUCT ACTIVITY KEYS
    public static final String PRODNAMEKEY =KEYPREFIX + "PRODNAME";
    public static final String QTYKEY = KEYPREFIX + "QTY";
    public static final String PRICEKEY = KEYPREFIX + "PRICE";
    public static final String TYPEKEY = KEYPREFIX + "TYPE";
    public static final String IMAGEBITMAPSTRING = KEYPREFIX + "IMGBITMAPSTRING";
    public static final String PRODIDKEY = KEYPREFIX + "PRODID";


    public static final String OLDIMGLINK = KEYPREFIX + "OLDIMGLINK";

    //ORDER DETAILS
    public static final String ORDERID = KEYPREFIX + "ORDERID";
    public static final String BUYERNAME = KEYPREFIX + "BUYERNAME";
    public static final String ORDERTOTALPRICE = KEYPREFIX + "ORDERTOTALPRICE";
    public static final String ORDERLOCATION = KEYPREFIX + "ORDERLOCATION";
    public static final String BUYERPHONE = KEYPREFIX + "BUYERPHONE";
    public static final String ORDERTIME = KEYPREFIX + "ORDERTIME";
    public static final String ITEMSCOUNT = KEYPREFIX + "ITEMSCOUNT";
    
    //SHARED PREFERENCES
    public static final String PREF_NAME = KEYPREFIX+"registrationPref";
    public static final String SESSIONPREF_NAME = KEYPREFIX+"sessionPref";

    public static final int PRIVATE_MODE = 0;

    public static final String FNAMEKEY = KEYPREFIX+"f_name";
    public static final String LNAMEKEY = KEYPREFIX+"l_name";
    public static final String EMAILKEY = KEYPREFIX+"email";
    public static final String MOBILEKEY= KEYPREFIX+"mobile";
    public static final String IDKEY = KEYPREFIX+"id";
    public static final String PASSWORDKEY = KEYPREFIX+"password";
    public static final String SHOPNAMEKEY = KEYPREFIX+"shopname";
    public static final String SHOPDESCRIPTIONKEY = KEYPREFIX+"description";
    public static final String SHOPLOCATIONKEY = KEYPREFIX+"location";

    //LOCATION KEYS
    public static final String LONGITUDE_KEY = KEYPREFIX+"LONGITUDE";
    public static final String LATITUDE_KEY = KEYPREFIX+"LATITUDE";
    public static final String COUNTRY_KEY = KEYPREFIX+"COUNTRY";
    public static final String LOCALITY_KEY = KEYPREFIX+"LOCALITY";
    public static final String SUBLOCALITY_KEY = KEYPREFIX+"SUB_LOCALITY";
    public static final String ADMINAREA_KEY = KEYPREFIX+"ADMIN_AREA";
    public static final String FEATURENAME_KEY = KEYPREFIX+"FEATURE_NAME";

    //SESSION KEYS
    public static final String SESSIONEXISTS_KEY = KEYPREFIX+"SESSION_KEY";
}
