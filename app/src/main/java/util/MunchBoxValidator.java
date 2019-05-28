package util;

import java.util.regex.Pattern;

import android.widget.EditText;

public class MunchBoxValidator {

	 private static final String PHONE_REGEX = "^((\\+)|(00))[0-9]{6,14}$";
			 //"\\d{3}-\\d{7}";
	 private static final String PHONE_MSG = "###-#######";
	 private static final String REQUIRED_MSG = "required";
	 
	 public static boolean isPhoneNumber(EditText editText, boolean required) {
	        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
	    }


	  public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {
		  
	        String text = editText.getText().toString().trim();
	        // clearing the error, if it was previously set by some other values
	        editText.setError(null);
	 
	        // text required and editText is blank, so return false
	        if ( required && !hasText(editText) ) return false;
	 
	        // pattern doesn't match so returning false
	        if (required && !Pattern.matches(regex, text)) {
	            editText.setError(errMsg);
	            return false;
	        }

		  return true;
	    }
	  public static boolean hasText(EditText editText) {
		  
	        String text = editText.getText().toString().trim();
	        editText.setError(null);
	 
	        // length 0 means there is no text
	        if (text.length() == 0) {
	            editText.setError(REQUIRED_MSG);
	            return false;
	        }
	 
	        return true;
	    }
}
