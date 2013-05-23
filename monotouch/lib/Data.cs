using System;
using MonoTouch.Foundation;
using System.Net;
using System.IO;
using System.Threading;

namespace FM
{
	/// <summary>
	/// Class to handle data connection to server and store using User default
	/// </summary>
	public class Data
	{
		public Data ()
		{
		}

		public static NSUserDefaults localStorage = NSUserDefaults.StandardUserDefaults;
		public static string NEW_LINE = Environment.NewLine;

		public static string TRACKING_FILE = "tracking.txt";

		/// <summary>
		/// Check if the Files is exist.
		/// </summary>
		/// <returns><c>true</c>, if exist was filed, <c>false</c> otherwise.</returns>
		/// <param name="filename">Filename.</param>
		public static bool FileExist(string filename){
			return localStorage.StringForKey(filename) != null;
		}
		
		/// <summary>
		/// Gets the content of the file.
		/// </summary>
		/// <returns>The file content.</returns>
		/// <param name="filename">Filename.</param>
		public static string GetFileContent(string filename){
			string str = localStorage.StringForKey(filename);
			if (str == null)
				str = "";
			return str;
		}
		
		/// <summary>
		/// Clear all data in file.
		/// </summary>
		/// <returns><c>true</c>, if file was cleaned, <c>false</c> otherwise.</returns>
		/// <param name="filename">Filename.</param>
		public static bool CleanFile(string filename){
			return Data.InsertDataToFile(filename, "");
		}
		
		/// <summary>
		/// Clear all the file and inserts the data into file.
		/// </summary>
		/// <returns><c>true</c>, if data to file was inserted, <c>false</c> otherwise.</returns>
		/// <param name="filename">Filename.</param>
		/// <param name="data">Data.</param>
		public static bool InsertDataToFile(String filename, String data){
			localStorage.SetString(data, filename);
			return true;
		}
		
		/// <summary>
		/// Appends the data to file.
		/// </summary>
		/// <returns><c>true</c>, if data to file was appended, <c>false</c> otherwise.</returns>
		/// <param name="filename">Filename.</param>
		/// <param name="data">Data.</param>
		public static bool AppendDataToFile(string filename, string data){
			string oldData = Data.GetFileContent(filename);
			string newData = oldData + NEW_LINE + data;
			Data.InsertDataToFile(filename, newData);
			return true;
		}
		
		/// <summary>
		/// Sends the request to server.
		/// </summary>
		/// <returns><c>true</c>, if request to server was success, <c>false</c> otherwise.</returns>
		/// <param name="url">URL.</param>
		public static bool SendRequestToServer(string url){
			try
			{
				Console.WriteLine("Data send: " + url);
				HttpWebRequest req = (HttpWebRequest)WebRequest.Create (url);
				HttpWebResponse resp = (HttpWebResponse)req.GetResponse();
				if(resp.StatusCode == HttpStatusCode.OK)
				{
					return true;
				} else {
					return false;
				}
			}
			catch(Exception ex){
				return false;
			}
		}

		/// <summary>
		/// Sends the URL.
		/// </summary>
		/// <param name="Url">URL.</param>
		public static void SendUrlAsync(string url){
			Thread thread = new Thread(() => {
				Console.WriteLine(url);
				// send data to sever
				if (!Data.SendRequestToServer(url)){
					// if fail	
					// save data into tracking file
					Data.AppendDataToFile(Data.TRACKING_FILE, url);
				}
			});
			thread.Start();
		}

		/// <summary>
		/// Loads the data from server.
		/// </summary>
		/// <returns>The data from server.</returns>
		/// <param name="url">URL.</param>
		public static string LoadDataFromServer(string url){
			Utils.Log("Load Data from server: " + url);
			HttpWebRequest request = (HttpWebRequest) WebRequest.Create (url);
			WebResponse response = request.GetResponse();
			StreamReader reader = new StreamReader(response.GetResponseStream());
			return reader.ReadToEnd();
		}
		

	}
}

