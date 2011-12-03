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
introduced in the newer versions of Android.

*This library is aimed at API level 7 and uses the new contact APIs introduced in Android 2.0*


# Installation

In order to use the library, just clone the project and follow the instructions for
[referencing a library project](http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject)
in order to include it in your project.

Now, to use the library, your activity could just subclass the `ContactsPickerActivity` and then you can
override the `OnNumberSelectedMethod` in order to get notified when a number is selected.
Or if you wish, you can hack directly the source code of the `ContactsPickerActivity` to get the job done.

# License
The code is provided under an MIT license, so get hacking!!

# Contact
For any inquiries, you can reach me at ngewif@codinguser.com

# Known Issues
This contact picker only really works in portrait mode. 
In landscape, the view is not updated properly. This will hopefully be updated in the future.


