package core;

public class responseType {
    //fixme  define code per case, btw, json format

    //general message
    public static String ERROR_USER_IS_NOT_REGISTERED = "(403) Error! user not registered";
    public static String WARNING_TRAIAL_LIMIT = "(302) Warning! trail finished. you should buy credit.";
    public static String FATAL_INTERNAL_ERROR = "(500) FATAL! Internal error.";
    public static String RESPONSE_SUCCESS_200 = "(200) ok";
    public static String SCORE_MINIMUM_THAN_BASIC_PLAN = "score is minimum than basic plan";
    public static String ERROR_NO_SESSION = "Error, there is not valid session";
    public static String VERSION = "0.1";
    public static String GIFT = "1000";
    public static String SMS_MESSAGE_SEND_CODE = "Your Code is :";


    public static Long IMAGE_ID_START_PINT=200000000L;
    public static Long AUDIO_ID_START_PINT=400000000L;
    public static Long VIDEO_ID_START_PINT=600000000L;




    // Quezz tyoe
    public static String RESPONSE_TEXT = "TEXT";
    public static String RESPONSE_VIDEO = "VIDEO";
    public static String RESPONSE_AUDIO = "AUDIO";
    public static String RESPONSE_QUIZ = "QUIZ";
    public static String RESPONSE_IMAGE = "IMAGE";
    public static String RESPONSE_CHARACTER_FERI = "DASHFERI";
    //Quezz Message
    public static String PUZZLE_DONE_SUCCESS ="Woww! You win!";
    public static String PUZZLE_DONE_FAILED="Ooops! You lose!";




    ///account
    public static String PHONE_ALREADY_REGISTERED = "phone already registered";
    public static String PHONE_IS_NOT_REGISTERED = "phone is not registered";


    // Authentication, Authorization
    public static String SMS_MESSAGE_SEND_CODE_NOT_EXSITS = "first request sms code";
    public static String SMS_MESSAGE_SEND_CODE_INVALID = "Your Code is not valid";
    public static String SMS_MESSAGE_SEND_CODE_SUCCCESS = "code is verified.";

    public static String SESSION_IS_NOT_VALID = "session is not valid or expired.";
    // security

    public static int SESSION_SIZE=60;
    // Credit and account balance
    public static String QUEEZ_NO_CREDIT_MESSAGE = "Hey! charge your account now and get more gifts!";
    public static String ACCOUNT_CREDIT_CHARGED_SUCCESS = "acocunt charged successfully";
    public static String ACCOUNT_CREDIT_CHARGED_ERROR = "error acocunt charging.";



}
