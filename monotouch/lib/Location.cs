using System;
using MonoTouch.CoreLocation;
using System.Net;
using System.ComponentModel;
using MonoTouch.UIKit;
using MonoTouch.MapKit;

namespace FM
{
	public class Location
	{
		public static CLLocationManager iPhoneLocationManager;

		public Location ()
		{
		}

		/// <summary>
		/// Mileses to latitude degrees.
		/// </summary>
		/// <returns>The to latitude degrees.</returns>
		/// <param name="miles">Miles.</param>
		public static double MilesToLatitudeDegrees(double miles){
			double earthRadius = 3960.0; // in miles
			double radiansToDegrees = 180.0/Math.PI;
			return (miles/earthRadius) * radiansToDegrees;
		}
		
		/// <summary>
		/// Mileses to longitude degrees.
		/// </summary>
		/// <returns>The to longitude degrees.</returns>
		/// <param name="miles">Miles.</param>
		/// <param name="atLatitude">At latitude.</param>
		public static double MilesToLongitudeDegrees(double miles, double atLatitude){
			double earthRadius = 3960.0;
			double degreesToRadians = Math.PI/180.0;
			double radiansToDegrees = 180.0/Math.PI;
			double radiusAtLatitude = earthRadius * Math.Cos(atLatitude * degreesToRadians);
			return (miles / radiusAtLatitude) * radiansToDegrees;
		}

		/// <summary>
		/// get the location of user and update to server
		/// </summary>
		public static void DoLocationAndUpdated(){
			Location.iPhoneLocationManager = new CLLocationManager ();
			Location.iPhoneLocationManager.DesiredAccuracy = 5; // 1000 meters/1 kilometer
			// if this is iOs6
			if (UIDevice.CurrentDevice.CheckSystemVersion (6, 0)) {
				Location.iPhoneLocationManager.LocationsUpdated += (object sender, CLLocationsUpdatedEventArgs e) => {
					CLLocation newLocation = e.Locations [e.Locations.Length - 1];
					Utils.Log("ios6");
					Location.UpdateLocationToServer(newLocation.Coordinate.Latitude.ToString (), newLocation.Coordinate.Longitude.ToString ());
					//iPhoneLocationManager.StopUpdatingLocation();					
				};
			} 
			// if it is iOs 5 or lower
			else {
				Location.iPhoneLocationManager.UpdatedLocation += (object sender, CLLocationUpdatedEventArgs e) => {
					CLLocation newLocation =  e.NewLocation;
					Utils.Log("ios5");
					Location.UpdateLocationToServer(newLocation.Coordinate.Latitude.ToString (), newLocation.Coordinate.Longitude.ToString ());
					//iPhoneLocationManager.StopUpdatingLocation();
				};
			}

			if (CLLocationManager.LocationServicesEnabled) iPhoneLocationManager.StartUpdatingLocation ();
			
		}

		/// <summary>
		/// Updates the location to server.
		/// </summary>
		/// <param name="lat">Lat.</param>
		/// <param name="lng">Lng.</param>
		public static void UpdateLocationToServer(string lat, string lng){
			// url to update the server
			String url = Config.UPDATE_USER_URL + Config.MY_NUMBER + "&lat=" + lat + "&lon=" + lng;
			// create http request
			var request = (HttpWebRequest) WebRequest.Create(url);
			// create background worker to run the request in the backend
			BackgroundWorker bw = new BackgroundWorker();
			bw.DoWork += (sender, e) => {
				Utils.Log("Current location " + lat+"---"+lng);
				// update data to sever
				request.GetResponse();
			};

			bw.RunWorkerCompleted += (sender, args) =>  { };
			bw.RunWorkerAsync();
		}

		/// <summary>
		/// Focus map on a location with miles is region wide.
		/// </summary>
		/// <param name="mapView">Map view.</param>
		/// <param name="lat">Lat.</param>
		/// <param name="lon">Lon.</param>
		/// <param name="miles">Miles.</param>
		public static void MapFocus(MKMapView mapView, double lat, double lon, double miles){
			CLLocationCoordinate2D coords = new CLLocationCoordinate2D(lat, lon);
			MKCoordinateSpan span = new MKCoordinateSpan(Location.MilesToLatitudeDegrees(miles), Location.MilesToLongitudeDegrees(miles, coords.Latitude));
			mapView.Region = new MKCoordinateRegion(coords, span);
		}
	}
}

