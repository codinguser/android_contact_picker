# Why?

Often in Android development, there is need to support the following workflow:

1. Display list of contacts
2. User selects a contact
3. Display contact details
4. User selects a phone number
5. Perform an action with the number

There is a standard way to call the contact list in Android, but this does not 
always feel well-integrated in your app and does not work well expecially for tabbed
Android applications. 

ContactPicker is an Android library which allows you to easily integrate the above
workflow in your application with minimal effort. It uses the new Fragments API
and asynchronous contact loading introduced in the newer versions of Android.

*This library requires a minimum of Android API level 7 and above since it uses the new contact APIs introduced in Android 2.0*


# Installation

In order to use the library, just clone the project and follow the instructions for
[referencing a library project](http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject)
in order to include it in your project.

If you are using maven, then it is even easier. Add the following to your dependencies:
<pre> <code>
&lt;dependency&gt;
  &lt;groupId&gt;com.codinguser.android&lt;/groupId&gt;
  &lt;artifactId&gt;contactpicker&lt;/artifactId&gt;
  &lt;version&gt;2.2.0&lt;/version&gt;
  &lt;type&gt;apklib&lt;/type&gt;
&lt;/dependency&gt;
</code> </pre>
# Usage

There are two ways to use the library. 

1. If you just want to open a contacts picker from within your app and then get a number, 
   do this:

	```java
	private static final int GET_PHONE_NUMBER = 3007;
	
	public void getContact() {
		startActivityForResult(new Intent(this, ContactsPickerActivity.class), GET_PHONE_NUMBER);
	}
		
	// Listen for results.
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    // See which child activity is calling us back.
	    switch (requestCode) {
	        case GET_PHONE_NUMBER:
	            // This is the standard resultCode that is sent back if the
	            // activity crashed or didn't doesn't supply an explicit result.
	        	if (resultCode == RESULT_CANCELED){
	            	Toast.makeText(this, "No phone number found", Toast.LENGTH_SHORT).show();
	            } 
	            else {
	            	String phoneNumber = (String) data.getExtras().get(ContactsPickerActivity.KEY_PHONE_NUMBER);  
	                //Do what you wish to do with phoneNumber e.g.
	                //Toast.makeText(this, "Phone number found: " + phoneNumber , Toast.LENGTH_SHORT).show();
	            }
	        default:
	            break;
	    }
	}
	
	```

2. If you have a tabbed application with multiple activities in Android and you 
   wish to make a contacts list as one of the tabs, then your contacts activity should 
   subclass the `ContactsPickerActivity` and override the `OnContactNumberSelected` method 
   in order to get notified when a number is selected. The default implementation of
   `OnContactNumberSelected` just sets the number as a result and finishes the activity, 
   so make sure to override it to do what you want. 

# License
The code is provided under an MIT license, so get hacking!!

# Acknowledgements
Thanks to Avi Hayun for feedback used to improve the library

# Contact
For any inquiries, you can reach me at ngewif@codinguser.com

