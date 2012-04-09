package prompt.yamba.utils;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import winterwell.jtwitter.Twitter.Status;

public class TimelineStoreUtils
{
	public static String COL_ID = "_ID";
	public static String COL_USER = "User_Name";
	public static String COL_TEXT = "Text";
	
	private static String[] _columnNames = {COL_ID, COL_USER, COL_TEXT}; 
	
	public static Cursor adapt(Iterable<Status> list)
	{
		MatrixCursor cursor = new MatrixCursor(_columnNames);
		for (Status s : list)
		{
			RowBuilder newRow = cursor.newRow();
			newRow.add(s.getId());
			newRow.add(s.getUser().getName());
			newRow.add(s.getText());
		}
		return cursor;
	}
}
