using System;
using MonoTouch.UIKit;
using System.Drawing;

namespace FM
{
	public class LoadingView : UIAlertView
	{
		private UIActivityIndicatorView _activityView;

		public void InitActivity(string title)
		{
			Title = title;
			this.Show();
			// Spinner - add after Show() or we have no Bounds.
			_activityView = new UIActivityIndicatorView (UIActivityIndicatorViewStyle.WhiteLarge);
			_activityView.Frame = new RectangleF ((Bounds.Width / 2) - 15, Bounds.Height - 50, 30, 30);
			_activityView.StartAnimating ();
			AddSubview (_activityView);
		}

		public void Hide(){
			this.DismissWithClickedButtonIndex(0, true);
		}
	}
}
