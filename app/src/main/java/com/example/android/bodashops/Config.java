package com.example.android.bodashops;

public class Config {
    //configurations class for intent keys and URLs
    public static final String OWNERPHONENOKEY = "com.example.android.bodashops.ownerPhoneNumber";

    private static final String BASE_URL = "https://boda-shops.000webhostapp.com/";

    public static final String URL_PRODUCTS = BASE_URL+"getProducts.php";
    public static final String URL_PRODUCTS_ATTRIBUTES = BASE_URL+"getProductAttributes.php";


    public static final String URL_UPLOAD_PRODUCT = BASE_URL +"uploadproductdetails.php";
    public static final String URL_UPDATE_PRODUCT = BASE_URL +"updateproductdetails.php";
    public static final String IMG_BASE_URL = BASE_URL+"images/";
    public static final String GET_TYPES_URL = BASE_URL+"getTypes.php";

    public static final String KEYPREFIX = "com.example.android.bodashops";

    //ADD PRODUCT ACTIVITY KEYS
    public static final String PRODNAMEKEY =KEYPREFIX + "PRODNAME";
    public static final String QTYKEY = KEYPREFIX + "QTY";
    public static final String PRICEKEY = KEYPREFIX + "PRICE";
    public static final String TYPEKEY = KEYPREFIX + "TYPE";
    public static final String IMAGEBITMAPSTRING = KEYPREFIX + "IMGBITMAPSTRING";
    public static final String PRODIDKEY = KEYPREFIX + "PRODID";


    public static final String OLDIMGLINK = KEYPREFIX + "OLDIMGLINK";


}
