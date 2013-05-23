using System;
using System.IO;

using MonoTouch.Foundation;
using MonoTouch.AddressBook;
using System.Net;
using MonoTouch.UIKit;
using MonoTouch.MessageUI;

namespace FM
{
	public class Utils
	{
		public Utils ()
		{
		}

		/// <summary>
		/// Gets the content from User default with filename is keyword
		/// if there is not data for this
		/// get content form file and save to user defaults
		/// </summary>
		/// <returns>The content from file.</returns>
		/// <param name="fileName">File name.</param>
		public static string GetContentFromFile(string fileName){
			// try to get content from UserDefaults
			String content = NSUserDefaults.StandardUserDefaults.StringForKey(fileName);

			// if no content available
			if (content == null){
				// get content from file
				content = GetContentFromFileAsset(fileName);

				// save to UserDefaults
				NSUserDefaults.StandardUserDefaults.SetString(content, fileName);
				NSUserDefaults.StandardUserDefaults.Synchronize();
			}

			// Return content data
			return content;
		}

		/// <summary>
		/// Gets the content from file.
		/// </summary>
		/// <returns>The content from file asset.</returns>
		/// <param name="fileName">File name.</param>
		public static string GetContentFromFileAsset(string fileName){
			string content = "";
			string filepath = Path.Combine("Asset", fileName);
			if (File.Exists(filepath)) {
				content = File.ReadAllText(filepath);
			} else {
			}

			return content;
		}

		/// <summary>
		/// Standardizes the phone number.
		/// standard phone number will be start with +44 (or any country code) and no space in the number
		/// </summary>
		/// <returns>The phone number.</returns>
		/// <param name="phone">Phone.</param>
		public static string StandardizePhoneNumber(string phone){
			// remove all space
			string standardPhone = phone.Replace(" ", "");
			// remove all brackets
			standardPhone = standardPhone.Replace("(", "");
			standardPhone = standardPhone.Replace(")", "");

			// return if empty string
			if (string.IsNullOrEmpty(standardPhone))
				return standardPhone;
			
			// check if first charator is 0
			if (standardPhone[0] == '0'){
				// remove first charactor
				standardPhone = standardPhone.Substring(1);
				// add +44
				standardPhone = "+44" + standardPhone;
			}
			return standardPhone;
		}

		/// <summary>
		/// Gets all people in contact list.
		/// </summary>
		public static ABPerson[] GetAllPeopleInContactList(){
			ABAddressBook addressBook = new ABAddressBook();
			ABPerson[] contactList = addressBook.GetPeople();
			return contactList;
		}

		/// <summary>
		/// Gets the person from phone number.
		/// </summary>
		/// <returns>The person from number.</returns>
		/// <param name="number">Number.</param>
		public static ABPerson GetPersonFromNumber(string number){
			number = Utils.StandardizePhoneNumber(number);
			ABPerson[] allContact = Utils.GetAllPeopleInContactList();
			ABPerson result = new ABPerson();
			bool hasResult = false;

			// go through all contact in contact list
			foreach(ABPerson person in allContact){
				// get phones property
				ABMultiValue<string> phones = person.GetPhones();
				// go through all phone numbers
				for (int i = 0; i < phones.Count; i++){
					// make the phone number standard
					string phone = Utils.StandardizePhoneNumber(phones[i].Value);
					// compare phone number
					if (phone.Equals(number)){
						// if this is the right one
						result = person;
						hasResult = true;
						// stop searching
						break;
					}
				}
				// stop searching if result found
				if (hasResult) break;
			}

			// return result;
			if (hasResult) return result;
			else return null;
		}
		 
		/// <summary>
		/// Log the specified message.
		/// uncomment the code in this function to disable debugging when deploying
		/// </summary>
		/// <param name="message">Message.</param>
		public static void Log(string message){
			Console.WriteLine(message);
		}

		/// <summary>
		/// Alert the specified title and message.
		/// </summary>
		/// <param name="title">Title.</param>
		/// <param name="message">Message.</param>
		public static void Alert(string title, string message){
			UIAlertView alert = new UIAlertView();
			alert.Title = title;
			alert.Message = message;
			alert.AddButton("Close");
			alert.Show();
		}

		/// <summary>
		/// Composes the SM.
		/// </summary>
		/// <param name="controller">Controller.</param>
		/// <param name="recipients">Recipients.</param>
		/// <param name="message">Message.</param>
		public static void ComposeSMS(UINavigationController controller, string[] recipients, string message){
			MFMessageComposeViewController smsController = new MFMessageComposeViewController();
			smsController.Recipients = recipients;
			smsController.Body = message;
			smsController.Finished += (sender, e) => {
				smsController.DismissViewController(true, null);
			};
			controller.PresentViewController(smsController, true, null);

		}

		/// <summary>
		/// Unixs the time stamp to date time.
		/// </summary>
		/// <returns>The time stamp to date time.</returns>
		/// <param name="unixTimeStamp">Unix time stamp.</param>
		public static DateTime UnixTimeStampToDateTime( double unixTimeStamp )
		{
			// Unix timestamp is seconds past epoch
			System.DateTime dtDateTime = new DateTime(1970,1,1,0,0,0,0);
			dtDateTime = dtDateTime.AddSeconds( unixTimeStamp ).ToLocalTime();
			return dtDateTime;
		}


	}
}

